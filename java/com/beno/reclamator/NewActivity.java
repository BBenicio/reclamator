package com.beno.reclamator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beno.reclamator.database.DatabaseHelper;
import com.beno.reclamator.database.DatabaseWriter;

public class NewActivity extends AppCompatActivity {
    public static final String COMPANY_EXTRA = "company";
    public static final String PROBLEM_EXTRA = "problem";

    public static final String ENTRY_EXTRA = "entry";

    private boolean edit = false;
    private long time = 0;

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
                ((EditText)findViewById(R.id.problem)).setText(entry.problem);
                ((EditText)findViewById(R.id.operator)).setText(entry.operator);
                ((EditText)findViewById(R.id.protocol)).setText(entry.protocol);
                ((EditText)findViewById(R.id.observations)).setText(entry.observations);
                time = entry.getTime();
            }
        }

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    private void save() {
        DatabaseWriter databaseWriter = new DatabaseWriter(new DatabaseHelper(this));
        String company = ((EditText) findViewById(R.id.company)).getText().toString();
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
                MainActivity.reloadEntries();
                close();
            } else {
                Entry entry = new Entry(company, problem, operator, protocol, observations, time);
                databaseWriter.update(entry);
                databaseWriter.close();
                MainActivity.reloadEntries();
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
        if (!(((EditText)findViewById(R.id.company)).getText().toString().isEmpty() ||
              ((EditText)findViewById(R.id.problem)).getText().toString().isEmpty() ||
              ((EditText)findViewById(R.id.operator)).getText().toString().isEmpty() ||
              ((EditText)findViewById(R.id.protocol)).getText().toString().isEmpty() ||
              ((EditText)findViewById(R.id.observations)).getText().toString().isEmpty())) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.cancel);
            builder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        } else
            super.onBackPressed();
    }

    private void close() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
