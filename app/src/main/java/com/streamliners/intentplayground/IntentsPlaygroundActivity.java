package com.streamliners.intentplayground;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.streamliners.intentplayground.databinding.ActivityIntentsPlaygroundBinding;

import java.net.URI;

public class IntentsPlaygroundActivity extends AppCompatActivity {
    private static final int REQUEST_COUNT = 0;
    ActivityIntentsPlaygroundBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout();
        setupHideErrorsForEditText();
    }


    //    Initial Setups---------------------------------------------------------------
    private void setupLayout() {
        b = ActivityIntentsPlaygroundBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        setTitle("Intents Playground");
    }

    private void setupHideErrorsForEditText() {
        TextWatcher myTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideErrors();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        b.data.getEditText().addTextChangedListener(myTextWatcher);
        b.initialCounterET.getEditText().addTextChangedListener(myTextWatcher);
    }


//    Event handlers---------------------------------------------------------------

    public void openMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void sendImplicitIntent(View view) {

//      to get the data for text editor and .trim() for removing all the white spaces between them
//      Validate data input
        String input = b.data.getEditText().getText().toString().trim();

        if (input.isEmpty()) {
            b.data.setError("Please Enter Something!");
            return;
        }

//        Validate intent type
//        int is for the checked radio button id

        int type = b.radioGroup.getCheckedRadioButtonId();

//        Now handling implicit intent cases according to the selected radio button

        if (type == R.id.webpage) {
            openWebpage(input);

        } else if (type == R.id.dialNumber) {
            dialNumber(input);
        } else if (type == R.id.shareText) {
            shareText(input);
        } else {
            Toast.makeText(this, "Please select an intent type!", Toast.LENGTH_SHORT).show();
        }

        /*

        switch (type){

            case R.id.webpage:
                openWebpage(input);
                break;

            case R.id.dialNumber:
                dialNumber(input);
                break;

            case R.id.shareText:
                shareText(input);
                break;

            default:
                Toast.makeText(this,"Please select an intent type!",Toast.LENGTH_SHORT).show();
        }
    }*/

    }

    public void sendData(View view) {
        //      Validate data input
        String input = b.initialCounterET.getEditText().getText().toString().trim();

        if (input.isEmpty()) {
            b.initialCounterET.setError("Please Enter Something!");
            return;
        }

//        Get count
        int initialCounter = Integer.parseInt(input);

//        Create Intent
        Intent intent = new Intent(this, MainActivity.class);

//        Create Bundle
        Bundle bundle = new Bundle();

        bundle.putInt(Constants.INITIAL_COUNT_KEY,initialCounter);
        bundle.putInt(Constants.MIN_VALUE, -100);
        bundle.putInt(Constants.MAX_VALUE, 100);

//        Pass Bundle
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_COUNT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_COUNT && resultCode == RESULT_OK){
//            Get Data
            int count = data.getIntExtra(Constants.FINAL_VALUE, Integer.MIN_VALUE);

//            Show Data
            b.result.setText("Final count recieved : " + count);
            b.result.setVisibility(View.VISIBLE);
        }

    }

    //    Implicit intent senders------------------------------------------------------

    private void openWebpage(String url) {
//        Checking if user input is url or not
        if (!url.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~|!:,.;]*[-a-zA-Z0-9+&@#/%=~|]")) {
            b.data.setError("Invalid URL!");
            return;
        }
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

        hideErrors();
    }

    private void dialNumber(String number) {
        //        Checking if user input is valid number or not
        if (!number.matches("^\\d{10}$")) {
            b.data.setError("Invalid Mobile Number!");
            return;
        }
//        Append tell: for the number
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);

        hideErrors();
    }

    private void shareText(String text) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, "Share text via"));

    }


    //    Error Hiding-----------------------------------------------------------------
    private void hideErrors() {
        b.data.setError(null);
    }


}