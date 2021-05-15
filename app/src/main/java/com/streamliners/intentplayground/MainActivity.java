package com.streamliners.intentplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.streamliners.intentplayground.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private int qty = 0;
    private ActivityMainBinding b;
    private int minVal, maxVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show();

        setupEventHandlers();
        getInitialCount();
    }

    private void getInitialCount() {
       /*
       qty = getIntent().getIntExtra(Constants.INITIAL_COUNT_KEY,0);
        b.qty.setText(String.valueOf(qty)); //to show the text acc to the counter
        */
        Bundle bundle = getIntent().getExtras();
        qty = bundle.getInt(Constants.INITIAL_COUNT_KEY,0);
        minVal = bundle.getInt(Constants.MIN_VALUE,Integer.MIN_VALUE);
        maxVal = bundle.getInt(Constants.MAX_VALUE,Integer.MAX_VALUE);

        b.qty.setText(String.valueOf(qty));

        if(qty != 0){
            b.SendBack.setVisibility(View.VISIBLE);
        }
    }

    private void setupEventHandlers() {
        b.decBtn.setOnClickListener(v -> decqty());
        b.incBtn.setOnClickListener(v -> incQty());
    }


    public void decqty() {
        b.qty.setText(" " + --qty);

    }

    public void incQty() {
        b.qty.setText(" " + ++qty);
    }

    public void sendBack(View view){
//        Validate count
        if(qty >= minVal && qty <= maxVal){
            Intent intent = new Intent();
            intent.putExtra(Constants.FINAL_VALUE, qty);
            setResult(RESULT_OK, intent);

//            To close the activity
            finish();
        }
        else{
            Toast.makeText(this,"Not in Range!",Toast.LENGTH_SHORT).show();

        }
    }
}