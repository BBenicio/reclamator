package com.beno.reclamator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beno.reclamator.database.DatabaseHelper;
import com.beno.reclamator.database.DatabaseWriter;

public class RegisterCompanyActivity extends AppCompatActivity {
	public static final String EDIT_EXTRA = "edit";

	Company edit = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_company);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			edit = extras.getParcelable(EDIT_EXTRA);
		}

		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				save();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (edit != null) {
			setEditText(R.id.name, edit.name);
			setEditText(R.id.phone, edit.phoneNumber);
			setEditText(R.id.email, edit.email);
			setEditText(R.id.address, edit.address);
			setEditText(R.id.website, edit.website);
		}
	}

	private String getEditText(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}
	private void setEditText(int id, String text) {
		((EditText) findViewById(id)).setText(text);
	}

	private void save() {
		if (getEditText(R.id.name).isEmpty()) {
			Toast.makeText(this, R.string.empty_field, Toast.LENGTH_SHORT).show();
			return;
		}

		Company company = new Company(getEditText(R.id.name), getEditText(R.id.phone), getEditText(R.id.email),
				                      getEditText(R.id.address), getEditText(R.id.website));

		if (!company.website.isEmpty() && !company.website.startsWith("http://") &&
			!company.website.startsWith("https://")) {
			company.website = "http://" + company.website;
		}

		DatabaseWriter writer = new DatabaseWriter(new DatabaseHelper(this));
		if (edit != null) {
			writer.update(company);
			writer.close();
			Intent intent = new Intent();
			intent.putExtra(CompanyDescActivity.COMPANY_EXTRA, company);
			setResult(RESULT_OK, intent);
		} else if (Company.getFromName(company.name) == null) {
			writer.insert(company);
			writer.close();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.name_already_registered);
			builder.setNeutralButton("Ok", null);
			builder.show();
			return;
		}

		Company.reloadCompanies();
		finish();
	}

	@Override
	public void onBackPressed() {
		if (getEditText(R.id.name).isEmpty() && getEditText(R.id.phone).isEmpty() &&
			getEditText(R.id.email).isEmpty() && getEditText(R.id.address).isEmpty() &&
			getEditText(R.id.website).isEmpty()) {

			super.onBackPressed();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.cancel);
			builder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});

			builder.setNegativeButton(R.string.no_button, null);
			builder.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_register_company, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}
