package com.englishdz.salim.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase contactsDB = null;

    Button createDBButton, addContactButton, deleteContactButton, getContactsButton, deleteDBButton;
    EditText nameEditText, emailEditText, contactListEditText, idEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDBButton = (Button) findViewById(R.id.createDBButton);
        addContactButton = (Button) findViewById(R.id.addContactButton);
        deleteContactButton = (Button) findViewById(R.id.deleteContactButton);
        getContactsButton = (Button) findViewById(R.id.getContactsButton);
        deleteDBButton = (Button) findViewById(R.id.deleteDBButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        contactListEditText = (EditText) findViewById(R.id.contactListEditText);
        idEditText = (EditText) findViewById(R.id.idEditText);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createDatabase(View view) {

        try {

            // Opens a current database or create it
            // Pass the database name, designate that only this app can use it and a DatabaseErrorHandler in the case of database corruption
            contactsDB = this.openOrCreateDatabase("MyContacts", MODE_PRIVATE, null);

            // Execute an SQL statement that isn't select
            contactsDB.execSQL("CREATE TABLE IF NOT EXISTS contacts " + "(id integer primary key, name VARCHAR, email VARCHAR);");

            // The database on the file system
            File database = getApplicationContext().getDatabasePath("MyContacts.db");

            // Check if the database exists
            if (!database.exists()) {
                Toast.makeText(this, "Database Created", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Database Missing", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Log.e("CONTACTS ERROR", "Error Creating Database");
        }

        // Make buttons clickable since the database was created
        addContactButton.setClickable(true);
        deleteContactButton.setClickable(true);
        getContactsButton.setClickable(true);
        deleteDBButton.setClickable(true);
    }

    public void addContact(View view) {

        // Get the contact name and email entered
        String contactName = nameEditText.getText().toString();
        String contactEmail = emailEditText.getText().toString();
        // Execute SQL statement to insert new data
        contactsDB.execSQL("INSERT INTO contacts (name, email) VALUES ('" + contactName + "', '" + contactEmail + "');");
    }

    public void getContacts(View view) {

        // A Cursor provides read and write access to database results
        Cursor cursor = contactsDB.rawQuery("SELECT * FROM contacts", null);

        // Get the index for the column name provided
        int idColumn = cursor.getColumnIndex("id");
        int nameColumn = cursor.getColumnIndex("name");
        int emailColumn = cursor.getColumnIndex("email");

        // Move to the first row of results
        cursor.moveToFirst();

        String contactList = "";

        // Verify that we have results
        if (cursor != null && (cursor.getCount() > 0)) {
            do {
                // Get the results and store them in a String
                String id = cursor.getString(idColumn);
                String name = cursor.getString(nameColumn);
                String email = cursor.getString(emailColumn);

                contactList = contactList + id + " : " + name + " : " + email + "\n";

                // Keep getting results as long as they exist
            } while(cursor.moveToNext());

            contactListEditText.setText(contactList);
        } else {

            Toast.makeText(this, "No Results to Show", Toast.LENGTH_SHORT).show();
            contactListEditText.setText("");
        }
    }

    public void deleteContact(View view) {

        // Get the id to delete
        String id = idEditText.getText().toString();

        // Delete matching id in database
        contactsDB.execSQL("DELETE FROM contacts WHERE id = " + id + ";");
    }

    public void deleteDatabase(View view) {

        // Delete database
        this.deleteDatabase("MyContacts");
    }

    @Override
    protected void onDestroy() {
        contactsDB.close();
        super.onDestroy();
    }
}
