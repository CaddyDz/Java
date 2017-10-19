package com.englishdz.conversion;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView, tspUnit, cupUnit, tbsUnit, ozUnit, kgUnit, qtUnit, glUnit, pdUnit, mlUnit, ltUnit, mgUnit, ptUnit;

    private EditText amountTextView;

    private Spinner unitTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        amountTextView = (EditText) findViewById(R.id.amount_text_view);

        addItemsToUnitTypeSpinner();

        addListenerToUnitTypeSpinner();

        initializeTextViews();

    }

    public void initializeTextViews() {
        tspUnit = (TextView) findViewById(R.id.tsp_unit);
        cupUnit = (TextView) findViewById(R.id.cup_unit);
        tbsUnit = (TextView) findViewById(R.id.tbs_unit);
        ozUnit = (TextView) findViewById(R.id.oz_unit);
        kgUnit = (TextView) findViewById(R.id.kg_unit);
        qtUnit = (TextView) findViewById(R.id.qt_unit);
        glUnit = (TextView) findViewById(R.id.gl_unit);
        pdUnit = (TextView) findViewById(R.id.pd_unit);
        mlUnit = (TextView) findViewById(R.id.ml_unit);
        ltUnit = (TextView) findViewById(R.id.lt_unit);
        mgUnit = (TextView) findViewById(R.id.mg_unit);
        ptUnit = (TextView) findViewById(R.id.pt_unit);
    }

    public void addItemsToUnitTypeSpinner() {
        unitTypeSpinner = (Spinner) findViewById(R.id.unit_type_spinner);

        ArrayAdapter<CharSequence> unitTypeSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.conversion_types, android.R.layout.simple_spinner_item);
        unitTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        unitTypeSpinner.setAdapter(unitTypeSpinnerAdapter);
    }

    public void addListenerToUnitTypeSpinner() {
        unitTypeSpinner = (Spinner) findViewById(R.id.unit_type_spinner);

        unitTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String itemSelected = adapterView.getItemAtPosition(position).toString();
                checkIfConvertingFromTsp(itemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO: figure out what to do here later on
            }
        });
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
}
