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

import com.beno.reclamator.database.DatabaseHelper;
import com.beno.reclamator.database.DatabaseReader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    public static final ArrayList<Entry> entries = new ArrayList<>();
    private static DatabaseReader databaseReader;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

        context = getBaseContext();

        databaseReader = new DatabaseReader(new DatabaseHelper(this));

        ListView list = (ListView)findViewById(R.id.companyList);
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
        databaseReader.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadEntries();
        adapter.clear();

        for (Entry entry : entries) {
            if (adapter.getPosition(entry.company) == -1)
                adapter.add(entry.company);
        }
    }

    public static void reloadEntries() {
        databaseReader.open();
        entries.clear();
        entries.addAll(databaseReader.query());
        databaseReader.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newEntryAction:
                Intent intent = new Intent(this, NewActivity.class);
                startActivity(intent);
                break;
            case R.id.searchAction:
                onSearchRequested();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
