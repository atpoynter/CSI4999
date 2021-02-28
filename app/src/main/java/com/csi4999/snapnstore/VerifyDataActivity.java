package com.csi4999.snapnstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class VerifyDataActivity extends AppCompatActivity {
    TextView receiver_msg1, receiver_msg2;
    Button btnSubmit, btnBarcode;
    String ocrDataHolder, ocrDataHolder2;
    String finalResult ;
    String HttpURL = "http://35.223.15.126/AndroidUserSubmit.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_data);
        btnSubmit = (Button)findViewById(R.id.submit2);
        receiver_msg1 = (TextView)findViewById(R.id.ocr_data);
        receiver_msg2 = (TextView)findViewById(R.id.ocr_data2);
        btnBarcode = (Button) findViewById(R.id.btn_barcode);

        // create the get Intent object
        Intent intent = getIntent();

        // receive the value by getStringExtra() method
        // and key must be same which is send by first activity
        String str = intent.getStringExtra("message_key");
        String str2 = intent.getStringExtra("message_key2");

        // display the string into textView
        receiver_msg1.setText(str);
        receiver_msg2.setText(str2);


        btnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                // get the value which input by user in EditText
                // and convert it to string

                // Create the Intent object of this class Context() to Second_activity class
                Intent intent = new Intent(getApplicationContext(), VerifyDataActivity.class);

                // now by putExtra method put the value in key, value pair
                // key is message_key by this key we will receive the value, and put the string

                intent.putExtra("message_key", str);
                intent.putExtra("message_key2", str2);

                // start the Intent
                startActivity(intent);
            }
        });

        //Adding Click Listener on button.
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    // If EditText is not empty and CheckEditText = True then this block will execute.

                    VerifyDataFunction(ocrDataHolder, ocrDataHolder2);

                }
                else {

                    // If EditText is empty then this block will execute .
                    Toast.makeText(VerifyDataActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }


            }
        });

    }



    public void CheckEditTextIsEmptyOrNot(){

        ocrDataHolder = receiver_msg1.getText().toString();
        ocrDataHolder2 = receiver_msg2.getText().toString();


        if(TextUtils.isEmpty(ocrDataHolder) || TextUtils.isEmpty(ocrDataHolder2))
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }

    public void VerifyDataFunction(final String ocrData, final String ocrData2){
        //ocrData3^^

        class VerifyDataFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(VerifyDataActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(VerifyDataActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("ocrData",params[0]);

                hashMap.put("ocrData2",params[1]);
/*
                hashMap.put("userName",params[2]);

                hashMap.put("email",params[3]);

                hashMap.put("password",params[4]);*/

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        VerifyDataFunctionClass verifyDataFunctionClass = new VerifyDataFunctionClass();

        verifyDataFunctionClass.execute(ocrData, ocrData2);
    }

}