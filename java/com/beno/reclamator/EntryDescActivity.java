package com.beno.reclamator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.beno.reclamator.database.DatabaseHelper;
import com.beno.reclamator.database.DatabaseWriter;


public class EntryDescActivity extends AppCompatActivity {
    public static final String ENTRY_EXTRA = "entry";

    private Entry selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_desc);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selected = extras.getParcelable(ENTRY_EXTRA);
        } else {
            // Toast#makeText cannot be shouldn't be called here
            // So selected gets set to null for the user to be warned
            selected = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (selected != null) {
            ((TextView) findViewById(R.id.company_value)).setText(selected.company);
            ((TextView) findViewById(R.id.problem_value)).setText(selected.problem);
            ((TextView) findViewById(R.id.operator_value)).setText(selected.operator);
            ((TextView) findViewById(R.id.protocol_value)).setText(selected.protocol);
            ((TextView) findViewById(R.id.observations_value)).setText(selected.observations);
            ((TextView) findViewById(R.id.time_value)).setText(selected.toString());
        } else {
            Toast.makeText(this, R.string.entry_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entry_desc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_action:
                Intent intent = new Intent(this, NewActivity.class);
                intent.putExtra(NewActivity.ENTRY_EXTRA, selected);
                startActivityForResult(intent, 1);
                return true;
            case R.id.delete_action:
                final Activity context = this;

                AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(this);
                deleteBuilder.setTitle(R.string.delete_dialog_title);
                deleteBuilder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseWriter databaseWriter = new DatabaseWriter(new DatabaseHelper(context));
                        databaseWriter.delete(selected);
                        databaseWriter.close();
                        Entry.reloadEntries();
                        finish();
                    }
                });

                deleteBuilder.setNegativeButton(R.string.no_button, null);

                deleteBuilder.show();
                return true;
            case R.id.company_info_action:
                Intent intent1 = new Intent(this, CompanyDescActivity.class);
                intent1.putExtra(CompanyDescActivity.COMPANY_EXTRA, Company.getFromName(selected.company));
                startActivity(intent1);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // When the entry gets edited, reload it
        if (requestCode == 1 && resultCode == RESULT_OK) {
            selected = data.getParcelableExtra(ENTRY_EXTRA);
        }
    }
}
