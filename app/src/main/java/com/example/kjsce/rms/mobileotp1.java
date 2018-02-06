package com.example.kjsce.rms;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class mobileotp1 extends AppCompatActivity {

    EditText phone,verotp;
    Button otpget,verifyotp;
    JSONObject json1;
    JSONParser jParser = new JSONParser();
    String otp;
    String phonenumber;
    String phone_tosend;
    String name;
    String store;
    String choice;
    int wheretonext=-1;
    private ProgressDialog pDialog;

    //private static String url_otp_get = "https://rmsanp.000webhostapp.com/otptxtlcl.php";4
    private static String url_otp_get = "https://rmsanp.000webhostapp.com/newuser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobileotp);
        Toast.makeText(this, "Inside mobileOTP", Toast.LENGTH_SHORT).show();
        Bundle extras = getIntent().getExtras();
        store  = extras.getString("store");
        choice = extras.getString("choice");
        Random rnd = new Random();
        final  String checkrand = "hellohowisitfinenoweretheonesintheworld";

        otpget = (Button)findViewById(R.id.otp);
        phone = (EditText)findViewById(R.id.mobile);
        verifyotp =(Button)findViewById(R.id.checkotp);
        verotp = (EditText)findViewById(R.id.putotp);

       // int randomised = ThreadLocalRandom.current().nextInt(0, 31);



        otpget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber = phone.getText().toString();
               // phonenumber = "91" +phonenumber;
                otp = Integer.toString((int)checkrand.charAt(ThreadLocalRandom.current().nextInt(0, 31)))
                        +Integer.toString((int)checkrand.charAt(ThreadLocalRandom.current().nextInt(0, 31)));
            //    otp = "123456";
                Log.d("otp to be sent",otp);
                Log.d("number",phonenumber);

                new getotpasap().execute();



            }
        });


        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* String typedotp = verotp.getText().toString();
                if(typedotp.equalsIgnoreCase(otp)){
                    Toast.makeText(mobileotp1.this, "Phone Verified, details will be messaged", Toast.LENGTH_SHORT).show();
                }*/

               Intent gotoRegister = new Intent(mobileotp1.this,register.class);
               startActivity(gotoRegister);

               /* Intent launchIntent = getPackageManager().getLaunchIntentForPackage("net.one97.paytm");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }*/
            }
        });
    }

    class getotpasap extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mobileotp1.this);
            pDialog.setMessage("Pinging DB for user");
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

                json1 = jParser.makeHttpRequest(url_otp_get, "GET", params);
                Log.d("json: ", json1.toString());

                int status = json1.getInt("status");//      getJSONObject("status");
                if(status == 1){
                            wheretonext = 1;
                            phone_tosend = json1.getString("phone");
                            Log.d("phone",phone_tosend);

                }
                else if(status ==2){
                            wheretonext = 2;
                            phone_tosend = json1.getString("phone");
                            name = json1.getString("name");

                }

                else{
                            wheretonext = 0;
                }
                // check your log for json response

            } catch (JSONException e) {
                e.printStackTrace();
            }



            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if(wheretonext == 1){
                Intent gotoRegister = new Intent(mobileotp1.this,register.class);
                gotoRegister.putExtra("phone",phone_tosend);
                gotoRegister.putExtra("choice",choice);
                startActivity(gotoRegister);
            }
            else if(wheretonext == 2 /*&& choice.equalsIgnoreCase("Pay")*/){
                Intent gotoRegister = new Intent(mobileotp1.this,login.class);
                gotoRegister.putExtra("phone",phone_tosend);
                gotoRegister.putExtra("choice",choice);
                startActivity(gotoRegister);
            }
           /* else if(wheretonext == 2 && choice.equalsIgnoreCase("Delivery")){
                Intent gotoRegister = new Intent(mobileotp1.this,login.class);
                gotoRegister.putExtra("phone",phone_tosend);
                startActivity(gotoRegister);
            } */
        }
    }

}
