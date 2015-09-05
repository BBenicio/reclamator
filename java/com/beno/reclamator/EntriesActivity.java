package com.beno.reclamator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EntriesActivity extends AppCompatActivity {
    private static final String TAG = "EntriesActivity";

    public static final String SELECTED_COMPANY_EXTRA = "selectedCompany";
    public static final String SELECTED_PROBLEM_EXTRA = "selectedProblem";

    String selectedCompany;
    String selectedProblem;

    ArrayAdapter<Entry> adapter;

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(selectedCompany);
            actionBar.setSubtitle(selectedProblem);
        } else {
            setTitle(selectedProblem);
        }
    }

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

        setupActionBar();

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

        TextView emptyList = (TextView)findViewById(R.id.emptyListText);
        if (adapter.isEmpty()) {
            emptyList.setText(R.string.empty_list);
        } else {
            emptyList.setText(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
