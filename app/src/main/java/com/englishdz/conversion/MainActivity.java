package com.englishdz.conversion;

import android.icu.util.Currency;
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

        amountTextView = (EditText) findViewById(R.id.amount_text_view);
        // Fills the spinner with the unit options
        addItemsToUnitTypeSpinner();
        // Add listener to the spinner
        addListenerToUnitTypeSpinner();
        // Get a reference to the edit text view to retrieve the amount of the unit type
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
        // Get a reference to the spinner
        unitTypeSpinner = (Spinner) findViewById(R.id.unit_type_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> unitTypeSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.conversion_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        unitTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        unitTypeSpinner.setAdapter(unitTypeSpinnerAdapter);
    }

    public void addListenerToUnitTypeSpinner() {
        unitTypeSpinner = (Spinner) findViewById(R.id.unit_type_spinner);

        unitTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long arg3) {
                // Get the item selected in the Spinner
                String itemSelectedInSpinner = adapterView.getItemAtPosition(position).toString();
                // Verify if I'm converting from teaspoon so that I use the right conversion algorithm
                checkIfConvertingFromTsp(itemSelectedInSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO: figure out what to do here later on
            }
        });
    }

    public void checkIfConvertingFromTsp(String currentUnit) {
        if (currentUnit.equals("teaspoon")) {
            updateUnitTypesUsingTsp(Quantity.Unit.tsp);
        } else {
            if (currentUnit.equals("tablespoon")) {
                updateUnitTypesUsingOther(Quantity.Unit.tbs);
            } else if (currentUnit.equals("cup")) {
                updateUnitTypesUsingOther(Quantity.Unit.cup);
            } else if (currentUnit.equals("ounce")) {
                updateUnitTypesUsingOther(Quantity.Unit.oz);
            } else if (currentUnit.equals("pint")) {
                updateUnitTypesUsingOther(Quantity.Unit.pint);
            } else if (currentUnit.equals("quart")) {
                updateUnitTypesUsingOther(Quantity.Unit.quart);
            } else if (currentUnit.equals("gallon")) {
                updateUnitTypesUsingOther(Quantity.Unit.gallon);
            } else if (currentUnit.equals("pound")) {
                updateUnitTypesUsingOther(Quantity.Unit.pound);
            } else if (currentUnit.equals("milliliter")) {
                updateUnitTypesUsingOther(Quantity.Unit.ml);
            } else if (currentUnit.equals("liter")) {
                updateUnitTypesUsingOther(Quantity.Unit.liter);
            } else if (currentUnit.equals("milligram")) {
                updateUnitTypesUsingOther(Quantity.Unit.mg);
            } else {
                updateUnitTypesUsingOther(Quantity.Unit.kg);
            }
        }
    }

    public void updateUnitTypesUsingTsp(Quantity.Unit currentUnit) {
        // Convert the value in the EditText box to a double
        double doubleToConvert = Double.parseDouble(amountTextView.getText().toString());
        // Combine value to unit
        String teaspoonValueAndUnit = doubleToConvert + " tsp";
        // Change the value for the teaspoon TextView
        tspUnit.setText(teaspoonValueAndUnit);
        // Update all the Unit Text Fields
        updateUnitTextFieldsUsingTsp(doubleToConvert, Quantity.Unit.tbs, tbsUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, Quantity.Unit.cup, cupUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, Quantity.Unit.oz, ozUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, Quantity.Unit.pint, ptUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, Quantity.Unit.quart, qtUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, Quantity.Unit.gallon, glUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, Quantity.Unit.pound, pdUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, Quantity.Unit.ml, mlUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, Quantity.Unit.liter, ltUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, Quantity.Unit.mg, mgUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, Quantity.Unit.kg, kgUnit);
    }

    public void updateUnitTextFieldsUsingTsp(double doubleToConvert, Quantity.Unit unitConvertingTo, TextView theTextView) {
        Quantity unitQuantity = new Quantity(doubleToConvert, Quantity.Unit.tsp);
        String tempUnit = unitQuantity.to(unitConvertingTo).toString();
        theTextView.setText(tempUnit);
    }

    public void updateUnitTypesUsingOther(Quantity.Unit currentUnit) {
        // Convert the value in the EditText box to a double
        double doubleToConvert = Double.parseDouble(amountTextView.getText().toString());
        // Create a Quantity using the teaspoon unit
        Quantity currentQuantitySelected = new Quantity(doubleToConvert, currentUnit);
        // Create the String for the teaspoon TextView
        String valueInTeaspoons = currentQuantitySelected.to(Quantity.Unit.tsp).toString();
        // Set the text for the teaspoon TextView
        tspUnit.setText(valueInTeaspoons);
        updateUnitTextFieldsUsingTsp(doubleToConvert, currentUnit, Quantity.Unit.tbs, tbsUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, currentUnit, Quantity.Unit.cup, cupUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, currentUnit, Quantity.Unit.oz, ozUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, currentUnit, Quantity.Unit.pint, ptUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, currentUnit, Quantity.Unit.quart, qtUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, currentUnit, Quantity.Unit.gallon, glUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, currentUnit, Quantity.Unit.pound, pdUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, currentUnit, Quantity.Unit.ml, mlUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, currentUnit, Quantity.Unit.liter, ltUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, currentUnit, Quantity.Unit.mg, mgUnit);
        updateUnitTextFieldsUsingTsp(doubleToConvert, currentUnit, Quantity.Unit.kg, kgUnit);
        // Set the currently selected unit to the number in the EditText
        if (currentUnit.name().equals(currentQuantitySelected.unit.name())) {
            // Create the TextView text by taking the value in EditText and adding on the currently selected unit in the spinner
            String currentUnitTextViewText = doubleToConvert + " " + currentQuantitySelected.unit.name();
            // Create the TextView name to change by getting the currently selected quantities unit name and tacking on _text_view
            String currentTextViewName = currentQuantitySelected.unit.name() + "_text_view";
            // Get the resource id needed for the textView to use in findViewById
            int currentId = getResources().getIdentifier(currentTextViewName, "id", MainActivity.this.getPackageName());
            // Create an instance of the TextView we want to change
            TextView currentTextView = (TextView) findViewById(currentId);
            // Put the right data in the TextView
            currentTextView.setText(currentUnitTextViewText);

        }
    }

    public void updateUnitTextFieldsUsingTsp(double doubleToConvert, Quantity.Unit currentUnit, Quantity.Unit preferredUnit, TextView targetTextView) {
        Quantity currentQuantitySelected = new Quantity(doubleToConvert, currentUnit);
        // Algorithm used quantityInTbs.to(Unit.tsp).to(Unit.ounce)
        String tempTextViewText = currentQuantitySelected.to(Quantity.Unit.tsp).to(preferredUnit).toString();
        targetTextView.setText(tempTextViewText);
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
