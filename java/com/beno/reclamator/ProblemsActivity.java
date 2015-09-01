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


public class ProblemsActivity extends AppCompatActivity {
    static ArrayAdapter<String> adapter;
    public static final String SELECTED_COMPANY_EXTRA = "selectedCompany";
    String selectedCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            selectedCompany = extras.getString(SELECTED_COMPANY_EXTRA);

        adapter = new ArrayAdapter<>(this, R.layout.basic_row);

        ListView list = (ListView) findViewById(R.id.problemList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), EntriesActivity.class);
                intent.putExtra(EntriesActivity.SELECTED_COMPANY_EXTRA, selectedCompany);
                intent.putExtra(EntriesActivity.SELECTED_PROBLEM_EXTRA, adapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();

        for (Entry entry : MainActivity.entries) {
            if (entry.company.equals(selectedCompany) && adapter.getPosition(entry.problem) == -1)
                adapter.add(entry.problem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_problems, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newEntryAction:
                Intent intent = new Intent(this, NewActivity.class);
                intent.putExtra(NewActivity.COMPANY_EXTRA, selectedCompany);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
