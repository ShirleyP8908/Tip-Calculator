package com.example.tipcalculator;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,TextWatcher,SeekBar.OnSeekBarChangeListener{
    //declare your variables for the widgets
    private EditText editTextBillAmount;
    private TextView textViewPercent;
    RadioGroup radioGroup;
    RadioButton radioButton;
    private TextView textViewTip;

    private TextView personTotal;

    RadioButton option_no;

    private RadioButton option_tip;

    private RadioButton option_total;

    private TextView textViewTotal;
    private SeekBar seekBar;

    private Spinner spinner;

    private SeekBar party_seekBar;

    private int party = 0;

    //declare the variables for the calculations
    private double billAmount = 0;
    private double percent = .15;

    //set the number formats to be used for the $ amounts , and % amounts
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat = NumberFormat.getPercentInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        setContentView(R.layout.activity_main);
        //add Listeners to Widgets
        radioGroup = findViewById(R.id.radioGroup);
        option_no = findViewById(R.id.one_person);
        option_tip = findViewById(R.id.two_persons);
        option_total = findViewById(R.id.three_persons);
        editTextBillAmount = findViewById(R.id.editText_BillAmount);
        textViewTip = findViewById(R.id.textViewTip);
        textViewPercent = findViewById(R.id.textViewPercent);
        textViewTotal = findViewById(R.id.textViewTotal);
        personTotal = findViewById(R.id.per_person_total);
        seekBar = findViewById(R.id.seekBar);
        spinner = findViewById(R.id.party_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this
        , R.array.numberos, android.R.layout.simple_spinner_item); //This sets up the spinner and drops down the items//
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        /*  Note: each View that will be retrieved using findViewById needs to map to a View with the matching id
Example: editTextBillAmount
Needs to map to a View with the following: android:id="@+id/editText_BillAmount    */
        editTextBillAmount.addTextChangedListener(this);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //This adds the options menu/layer onto the app screen//
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    /* Note:   int i, int i1, and int i2
        represent start, before, count respectively
        The charSequence is converted to a String and parsed to a double for you  */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d("MainActivity", "inside onTextChanged method: charSequence= "+charSequence);
        //surround risky calculations with try catch (what if billAmount is 0 ?
        //charSequence is converted to a String and parsed to a double for you

        Log.d("MainActivity", "Bill Amount = "+billAmount);
        //setText on the textView
        //perform tip and total calculation and update UI by calling calculate
        try {
            billAmount = Double.parseDouble(charSequence.toString());
        }
        catch (NumberFormatException e){
            billAmount = 0;
        }
        calculate();

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        percent = progress / 100.0;
        textViewPercent.setText(percent +"%");//calculate percent based on seeker value
        calculate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    // calculate and display tip and total amounts
    private void calculate() {
        Log.d("MainActivity", "inside calculate method");
        //uncomment below
       // format percent and display in percentTextView
       // calculate the tip and total
       double tip = billAmount * percent;
      //use the tip example to do the same for the Total
        double total = billAmount + tip;
        double perPersonTotal;
        perPersonTotal = total / party;
        if(option_no.isChecked()) { //If the no button is checked, the per person total is set regularly//
            personTotal.setText(currencyFormat.format(perPersonTotal));
        } else if (option_tip.isChecked()) { //If tip is checked (AKA pressed), the tip gets rounded up//
            tip = billAmount * percent;
            tip = Math.ceil(tip); //Directions stated to round up the tip first and Match.ceil() does the rounding//
            total = billAmount + tip;
            perPersonTotal = total / party;
            personTotal.setText(currencyFormat.format(perPersonTotal));
        } else if (option_total.isChecked()) {
            total = Math.ceil(total);//Now the total needs to be rounded out, hence why it happens here//
            perPersonTotal = total / party;
            personTotal.setText(currencyFormat.format(perPersonTotal));
        }
       // display tip and total formatted as currency
       //user currencyFormat instead of percentFormat to set the textViewTip
       textViewTip.setText(currencyFormat.format(tip));
       //use the tip example to do the same for the Total
        textViewTotal.setText(currencyFormat.format(total));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    //This is to select from the spinner//
        String text = parent.getItemAtPosition(position).toString();
        party = Integer.parseInt(text); //Party is initialized to zero but in here, it starts at 1//
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        calculate(); //Calculate method gets called so that calculations are made based on how many are in the party//
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void checkButton(View v){ //This is for the radio buttons, notice the calculate method being shown//
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(getBaseContext(), radioButton.getText(), Toast.LENGTH_LONG).show();
        calculate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //This is for the menu options "info" and "share" respectively
        int info_id = item.getItemId();
        int share_button = item.getItemId();
        if (info_id == R.id.information_button) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Info"); //Sets the title name to Info//
            builder.setMessage("This app helps you count up a percentage of the bill depending on how many" +
                    " people you have in your party. A certain percentage is counted so that the bill" +
                    " is split properly amongst each other. If you want to share to each other how much" +
                    " the other owe's, you can simply press the share icon.");
            builder.show();
            return true;
        }else if (share_button == R.id.share_button) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "The total that you need to pay is " + personTotal.getText().toString());
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}