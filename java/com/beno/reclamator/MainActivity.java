package com.beno.reclamator;

import android.content.Context;
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

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;

    // Used in Entry#toString() for the localized time,
    // in Entry#reloadEntries(), and Company#reloadCompanies for the DatabaseHelper
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

        context = getBaseContext();

        ListView list = (ListView)findViewById(R.id.company_list);
        adapter = new ArrayAdapter<>(this, R.layout.basic_row);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ProblemsActivity.class);
                intent.putExtra(ProblemsActivity.SELECTED_COMPANY_EXTRA, adapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Entry.reloadEntries();
        adapter.clear();

        for (Entry entry : Entry.entries) {
            if (adapter.getPosition(entry.company) == -1)
                adapter.add(entry.company);
        }

        TextView emptyList = (TextView)findViewById(R.id.empty_list_text);
        if (adapter.isEmpty()) {
            emptyList.setText(R.string.empty_list);
        } else {
            emptyList.setText(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_entry_action:
                Intent intent = new Intent(this, NewActivity.class);
                startActivity(intent);
                return true;
            case R.id.search_action:
                onSearchRequested();
                return true;
            case R.id.see_companies_action:
                Intent intent1 = new Intent(this, RegisteredCompaniesActivity.class);
                startActivity(intent1);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
