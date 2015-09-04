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
			DatabaseReader databaseReader = new DatabaseReader(new DatabaseHelper(MainActivity.context));

			ArrayList<Entry> search = databaseReader.search(query);

			for (Entry e : search) {
				adapter.add(e);
			}

			databaseReader.close();
		}

		ListView searchList = (ListView)findViewById(R.id.searchList);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		/*int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}*/

		return super.onOptionsItemSelected(item);
	}
}
