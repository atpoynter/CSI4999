package com.csi4999.snapnstore;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mlkittest.R;

import java.util.HashMap;

public class VerifyDataActivity extends AppCompatActivity {
    TextView receiver_msg1, receiver_msg2;
    Button Submit;
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
        Submit = (Button)findViewById(R.id.submit2);
        receiver_msg1 = (TextView)findViewById(R.id.ocr_data);
        receiver_msg2 = (TextView)findViewById(R.id.ocr_data2);        // create the get Intent object
        Intent intent = getIntent();

        // receive the value by getStringExtra() method
        // and key must be same which is send by first activity
        String str = intent.getStringExtra("message_key");

        // display the string into textView
        receiver_msg1.setText(str);

        //Adding Click Listener on button.
        Submit.setOnClickListener(new View.OnClickListener() {
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