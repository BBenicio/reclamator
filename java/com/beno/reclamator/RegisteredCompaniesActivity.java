package com.beno.reclamator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RegisteredCompaniesActivity extends AppCompatActivity {
	ArrayAdapter<String> names;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registered_companies);

		names = new ArrayAdapter<>(this, R.layout.basic_row);

		ListView list = (ListView)findViewById(R.id.companyList);
		list.setAdapter(names);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				Intent intent = new Intent(view.getContext(), CompanyDescActivity.class);
				intent.putExtra(CompanyDescActivity.COMPANY_EXTRA, Company.getFromName(names.getItem(position)));
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Company.reloadCompanies();

		names.clear();
		for (Company c : Company.companies) {
			names.add(c.name);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_registered_companies, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.newCompanyAction) {
			Intent intent = new Intent(this, RegisterCompanyActivity.class);
			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
