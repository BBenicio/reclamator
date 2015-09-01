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
        if (extras != null)
            selected = extras.getParcelable(ENTRY_EXTRA);
        else
            selected = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (selected != null) {
            ((TextView) findViewById(R.id.companyValue)).setText(selected.company);
            ((TextView) findViewById(R.id.problemValue)).setText(selected.problem);
            ((TextView) findViewById(R.id.operatorValue)).setText(selected.operator);
            ((TextView) findViewById(R.id.protocolValue)).setText(selected.protocol);
            ((TextView) findViewById(R.id.observationsValue)).setText(selected.observations);
            ((TextView) findViewById(R.id.timeValue)).setText(selected.toString());
        } else {
            Toast.makeText(this, R.string.entry_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entry_desc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_edit:
                Intent intent = new Intent(this, NewActivity.class);
                intent.putExtra(NewActivity.ENTRY_EXTRA, selected);
                startActivityForResult(intent, 1);
                return true;
            case R.id.action_delete:
                final Activity context = this;

                AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(this);
                deleteBuilder.setTitle(R.string.delete_dialog_title);
                deleteBuilder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseWriter databaseWriter = new DatabaseWriter(new DatabaseHelper(context));
                        databaseWriter.delete(selected);
                        databaseWriter.close();
                        ProblemsActivity.adapter.remove(selected.problem);
                        MainActivity.reloadEntries();
                        finish();
                    }
                });

                deleteBuilder.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                deleteBuilder.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK)
            selected = data.getParcelableExtra(ENTRY_EXTRA);
    }
}
