package com.beno.reclamator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beno.reclamator.database.DatabaseHelper;
import com.beno.reclamator.database.DatabaseWriter;

public class NewActivity extends AppCompatActivity {
    public static final String COMPANY_EXTRA = "company";
    public static final String PROBLEM_EXTRA = "problem";

    public static final String ENTRY_EXTRA = "entry";

    // When the user clicks save, should we update an existing entry?
    private boolean edit = false;
    private long time = 0;

    private ArrayAdapter<String> companyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            EditText company = (EditText) findViewById(R.id.company);
            if (extras.getString(COMPANY_EXTRA) != null)
                company.setText(extras.getString(COMPANY_EXTRA));

            if (extras.getString(PROBLEM_EXTRA) != null) {
                EditText problem = (EditText) findViewById(R.id.problem);
                problem.setText(extras.getString(PROBLEM_EXTRA));
            }

            if (extras.getParcelable(ENTRY_EXTRA) != null) {
                Entry entry = extras.getParcelable(ENTRY_EXTRA);
                edit = true;
                company.setText(entry.company);
                setEditText(R.id.problem, entry.problem);
                setEditText(R.id.operator, entry.operator);
                setEditText(R.id.protocol, entry.protocol);
                setEditText(R.id.observations, entry.observations);
                time = entry.getTime();
            }
        }

        companyAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);

        AutoCompleteTextView company = (AutoCompleteTextView)findViewById(R.id.company);
        company.setThreshold(1);
        company.setAdapter(companyAdapter);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Company.reloadCompanies();
        for (Company c : Company.companies) {
            companyAdapter.add(c.name);
        }
    }

    private String getEditText(int id) {
        return ((EditText)findViewById(id)).getText().toString();
    }

    private void setEditText(int id, String text) {
        ((EditText)findViewById(id)).setText(text);
    }

    private void save() {
        DatabaseWriter databaseWriter = new DatabaseWriter(new DatabaseHelper(this));
        String company = ((AutoCompleteTextView) findViewById(R.id.company)).getText().toString();
        String problem = ((EditText) findViewById(R.id.problem)).getText().toString();
        String operator = ((EditText) findViewById(R.id.operator)).getText().toString();
        String protocol = ((EditText) findViewById(R.id.protocol)).getText().toString();
        String observations = ((EditText) findViewById(R.id.observations)).getText().toString();

        if (company.isEmpty() || problem.isEmpty() || protocol.isEmpty())
            Toast.makeText(this, R.string.empty_field, Toast.LENGTH_SHORT).show();
        else {
            if (!edit) {
                databaseWriter.insert(new Entry(company, problem, operator, protocol, observations));
                databaseWriter.close();
                Entry.reloadEntries();
                close();
            } else {
                Entry entry = new Entry(company, problem, operator, protocol, observations, time);
                databaseWriter.update(entry);
                databaseWriter.close();
                Entry.reloadEntries();
                Intent intent = new Intent();
                intent.putExtra(EntryDescActivity.ENTRY_EXTRA, entry);
                setResult(RESULT_OK, intent);
                close();
            }
        }
        databaseWriter.close();
    }

    @Override
    public void onBackPressed() {
        if (getEditText(R.id.company).isEmpty() && getEditText(R.id.problem).isEmpty() &&
            getEditText(R.id.operator).isEmpty() && getEditText(R.id.protocol).isEmpty() &&
            getEditText(R.id.observations).isEmpty()) {

            super.onBackPressed();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.cancel);
            builder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setNegativeButton(R.string.no_button, null);
            builder.show();
        }
    }

    private void close() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
