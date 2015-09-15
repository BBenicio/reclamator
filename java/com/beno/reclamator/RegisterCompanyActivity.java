package com.beno.reclamator;

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
			edit = extras.getParcelable("edit");
		}

		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				save();
			}
		});
	}

	private String getEditText(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}

	private void save() {
		Company company = new Company(getEditText(R.id.name), getEditText(R.id.phone), getEditText(R.id.email),
				                      getEditText(R.id.address), getEditText(R.id.website));

		DatabaseWriter writer = new DatabaseWriter(new DatabaseHelper(this));
		if (edit != null) {
			writer.update(company);
		} else {
			writer.insert(company);
		}
		writer.close();
		Company.reloadCompanies();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_register_company, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
