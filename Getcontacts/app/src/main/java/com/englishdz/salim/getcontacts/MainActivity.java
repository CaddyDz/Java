package com.englishdz.salim.getcontacts;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    // The URL used to target the content provider
    static final Uri CONTENT_URL = Uri.parse("content://com.englishdz.salim.contentprovider.ContactProvider/cpcontacts");

    TextView contactsTextView = null;
    EditText deleteIDEditText, idLookupEditText, addNameEditText;

    // Provides access to other applications Content Providers
    ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resolver = getContentResolver();

        contactsTextView = (TextView) findViewById(R.id.contactsTextView);
        deleteIDEditText = (EditText) findViewById(R.id.deleteIDEditText);
        idLookupEditText = (EditText) findViewById(R.id.idLookupEditText);
        addNameEditText = (EditText) findViewById(R.id.addNameEditText);

        getContacts();

    }

    public void getContacts() {

        // Projection contains the columns we want
        String[] projection = new String[]{"id", "name"};

        // Pass the URL, projection and the other option is covered below
        Cursor cursor = resolver.query(CONTENT_URL, projection, null, null, null);

        String contactList = "";

        // Cycle through and display every row of data
        if (cursor.moveToFirst()) {

            do {

                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));

                contactList = contactList + id + " : " + name + "\n";
            } while (cursor.moveToNext());
        }
        contactsTextView.setText(contactList);
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

    public void showContacts(View view) {
        getContacts();
    }

    public void lookupContact(View view) {
        // The id we want to search for
        String idToFind = idLookupEditText.getText().toString();

        // Holds the column data we want to retrieve
        String[] projection = new String[]{"id", "name"};

        // Pass the URL for Content Provider, the projection, the where clause followed by the matches in an array for the ?
        // null is for sort order
        Cursor cursor = resolver.query(CONTENT_URL, projection, "id = ? ", new String[]{idToFind}, null);

        String contact = "";

        // Cycle through our one result or print error
        if (cursor.moveToFirst()) {

            String id = cursor.getString(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));

            contact = contact + id + " : " + name + "\n";
        } else {

            Toast.makeText(this, "Contact Not Found", Toast.LENGTH_SHORT).show();
        }

        contactsTextView.setText(contact);
    }

    public void addContact(View view) {

        // Get the name to add
        String nameToAdd = addNameEditText.getText().toString();

        // Put in the column name and the value
        ContentValues values = new ContentValues();
        values.put("name", nameToAdd);

        // Insert the value into the Content Provider
        resolver.insert(CONTENT_URL, values);

        getContacts();
    }

    public void deleteContact(View view) {
        String idToDelete = deleteIDEditText.getText().toString();

        // Use the resolver to delete ids by passing the content provider url what you're targeting with the where clause and the string that replaces the ? in the where clause
        long idDeleted = resolver.delete(CONTENT_URL, "id = ? ", new String[]{idToDelete});

        getContacts();

    }
}
