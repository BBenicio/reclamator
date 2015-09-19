package com.beno.reclamator;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.beno.reclamator.database.DatabaseHelper;
import com.beno.reclamator.database.DatabaseReader;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
	ArrayAdapter<Entry> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		adapter = new ArrayAdapter<>(getBaseContext(), R.layout.basic_row);

		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			DatabaseReader databaseReader = new DatabaseReader(new DatabaseHelper(this));

			ArrayList<Entry> search = databaseReader.searchEntries(query);

			for (Entry e : search) {
				adapter.add(e);
			}

			databaseReader.close();
		}

		ListView searchList = (ListView)findViewById(R.id.search_list);
		searchList.setAdapter(adapter);

		searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(view.getContext(), EntryDescActivity.class);
				intent.putExtra(EntryDescActivity.ENTRY_EXTRA, adapter.getItem(position));
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		TextView emptySearch = (TextView) findViewById(R.id.empty_search);

		if (adapter.isEmpty()) {
			emptySearch.setText(R.string.no_search_results);
		} else {
			emptySearch.setText(null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}
