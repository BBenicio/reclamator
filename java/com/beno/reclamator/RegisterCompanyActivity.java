package com.beno.reclamator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
		Company company = new Company(getEditText(R.id.name), getEditText(R.id.phone), getEditText(R.id.email),
				                      getEditText(R.id.address), getEditText(R.id.website));

		if (!company.website.startsWith("http://") && !company.website.startsWith("https://")) {
			company.website = "http://" + company.website;
		}

		DatabaseWriter writer = new DatabaseWriter(new DatabaseHelper(this));
		if (edit != null) {
			writer.update(company);
			writer.close();
			Intent intent = new Intent();
			intent.putExtra(CompanyDescActivity.COMPANY_EXTRA, company);
			setResult(RESULT_OK, intent);
		} else {
			writer.insert(company);
			writer.close();
		}
		Company.reloadCompanies();
		finish();
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
