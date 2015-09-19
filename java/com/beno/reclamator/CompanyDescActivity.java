package com.beno.reclamator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.beno.reclamator.database.DatabaseHelper;
import com.beno.reclamator.database.DatabaseWriter;

public class CompanyDescActivity extends AppCompatActivity {
	public static final String COMPANY_EXTRA = "company";

	private Company company = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_desc);

		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			company = extra.getParcelable(COMPANY_EXTRA);
			if (company != null) {
				setTitle(company.name);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Company.reloadCompanies();

		if (company != null) {
			((TextView)findViewById(R.id.name_value)).setText(company.name);
			((TextView)findViewById(R.id.phone_value)).setText(company.phoneNumber);
			((TextView)findViewById(R.id.email_value)).setText(company.email);
			((TextView)findViewById(R.id.address_value)).setText(company.address);
			((TextView)findViewById(R.id.website_value)).setText(company.website);
		} else {
			Toast.makeText(this, R.string.company_error, Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_company_desc, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.edit_action:
				Intent intent = new Intent(this, RegisterCompanyActivity.class);
				intent.putExtra(RegisterCompanyActivity.EDIT_EXTRA, company);
				startActivityForResult(intent, 1);
				return true;
			case R.id.delete_action:
				final Activity context = this;

				AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(this);
				deleteBuilder.setTitle(R.string.delete_dialog_title);
				deleteBuilder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DatabaseWriter databaseWriter = new DatabaseWriter(new DatabaseHelper(context));
						databaseWriter.delete(company);
						databaseWriter.close();
						Entry.reloadEntries();
						finish();
					}
				});

				deleteBuilder.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {}
				});

				deleteBuilder.show();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			company = data.getParcelableExtra(COMPANY_EXTRA);
		}
	}
}
