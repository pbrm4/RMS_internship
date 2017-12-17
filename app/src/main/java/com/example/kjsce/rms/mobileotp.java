package com.example.kjsce.rms;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class mobileotp extends AppCompatActivity {

    EditText phone,verotp;
    Button otpget,verifyotp;
    JSONObject json;
    JSONParser jParser = new JSONParser();
    String otp;
    String phonenumber;
    private ProgressDialog pDialog;

    private static String url_otp_get = "https://rmsanp.000webhostapp.com/otptxtlcl.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobileotp);
        Toast.makeText(this, "Inside mobileOTP", Toast.LENGTH_SHORT).show();
        String store = getIntent().getStringExtra("store");
        Random rnd = new Random();
       final  String checkrand = "hellohowisitfinenoweretheonesintheworld";

        otpget = (Button)findViewById(R.id.otp);
        phone = (EditText)findViewById(R.id.mobile);
        verifyotp =(Button)findViewById(R.id.checkotp);
        verotp = (EditText)findViewById(R.id.putotp);

        int randomised = ThreadLocalRandom.current().nextInt(0, 31);



        otpget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    phonenumber = phone.getText().toString();
                    phonenumber = "91" +phonenumber;
                otp = Integer.toString((int)checkrand.charAt(ThreadLocalRandom.current().nextInt(0, 31)))
                        +Integer.toString((int)checkrand.charAt(ThreadLocalRandom.current().nextInt(0, 31)));
                Log.d("otp to be sent",otp);
                Log.d("number",phonenumber);

                new getotpasap().execute();



            }
        });


        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typedotp = verotp.getText().toString();
                if(typedotp.equalsIgnoreCase(otp)){
                    Toast.makeText(mobileotp.this, "Phone Verified, details will be messaged", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }



    class getotpasap extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mobileotp.this);
            pDialog.setMessage("Sending request for OTP");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("phone",phonenumber));
                params.add(new BasicNameValuePair("otp",otp));
                Log.d("params",params.toString());

                json = jParser.makeHttpRequest(url_otp_get, "GET", params);

                // check your log for json response
                Log.d("Details", json.toString());

                if (1 == 1) {

                    json.getJSONArray("products");

                }else{
                    // product with pid not found
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();



        }
    }

}
