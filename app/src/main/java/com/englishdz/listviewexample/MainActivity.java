package com.englishdz.listviewexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] favoriteYTChannels = {"Thomas Frank", "Poly Matter", "exurb1a", "Domics", "The School of life", "Veritasium", "Crash Course", "Kurzgesagt", "Smarter Every Day", "Rationality Rules", "Cosmic Skeptic", "College Humor", "DarkMatter2525", "Vsauce", "CGP Grey"};

        // Create List adapter by the array adapter of the created array of values.
        ListAdapter theAdapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.textView1, favoriteYTChannels);

        // Create a list view out of the UI component
        ListViewCompat theListView = (ListViewCompat) findViewById(R.id.theListView);

        // Populate the list view with the list adapter
        theListView.setAdapter(theAdapter);


        // Add click event listener to list view rows
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Create a message string that holds the value of the clicked or selected list view row
                String channelPicked = "You have subscribed to " + String.valueOf(adapterView.getItemAtPosition(position));

                // Create a toast with the created message string
                Toast.makeText(MainActivity.this, channelPicked, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
