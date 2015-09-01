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

public class EntriesActivity extends AppCompatActivity {
    public static final String SELECTED_COMPANY_EXTRA = "selectedCompany";
    public static final String SELECTED_PROBLEM_EXTRA = "selectedProblem";
    String selectedCompany;
    String selectedProblem;
    ArrayAdapter<Entry> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);

        adapter = new ArrayAdapter<>(this, R.layout.basic_row);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedCompany = extras.getString(SELECTED_COMPANY_EXTRA);
            selectedProblem = extras.getString(SELECTED_PROBLEM_EXTRA);
        }

        ListView list = (ListView) findViewById(R.id.entryList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        adapter.clear();
        for (Entry entry : MainActivity.entries) {
            if (entry.company.equals(selectedCompany) && entry.problem.equals(selectedProblem))
                adapter.add(entry);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entries, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newEntryAction:
                Intent intent = new Intent(this, NewActivity.class);
                intent.putExtra(NewActivity.COMPANY_EXTRA, selectedCompany);
                intent.putExtra(NewActivity.PROBLEM_EXTRA, selectedProblem);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
