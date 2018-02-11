package com.example.kjsce.rms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class register extends AppCompatActivity {
    private ProgressDialog pDialog;
    EditText inputName;
    EditText inputPE;
    EditText inputpwd;
    String name;
    JSONObject json;
    int wheretonext;
    JSONParser jParser = new JSONParser();
    String password;
    String phone;

    private static String url_add_user = "http://rmsanp.000webhostapp.com/adduser.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button login;
        Button register;
        Bundle extras = getIntent().getExtras();
        phone  = extras.getString("phone");
        Log.d("phone recvd. ",phone);

        login = (Button) findViewById(R.id.reg_log);
        inputName = (EditText) findViewById(R.id.reg_name);
        inputPE = (EditText) findViewById(R.id.reg_pe);
        inputPE.setTextColor(getResources().getColor(R.color.black));
        inputPE.setText(phone);
        inputPE.setEnabled(false);
        inputpwd = (EditText) findViewById(R.id.reg_pwd);

        /*login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regpage = new Intent(register.this, login.class);
                startActivity(regpage);
            }

        });*/
        register = (Button) findViewById(R.id.reg_reg);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = inputName.getText().toString();
                password = inputpwd.getText().toString();
                new registeruser().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new product
     * */


    class registeruser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(register.this);
            pDialog.setMessage("Adding "+name+" to the Database");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name",name));
                params.add(new BasicNameValuePair("phone",phone));
                params.add(new BasicNameValuePair("password",password));

                json = jParser.makeHttpRequest(url_add_user, "POST", params);
                Log.d("json: ", json.toString());

                int status = json.getInt("status");//      getJSONObject("status");
                if(status == 1){
                    wheretonext = 1;

                }
                else if(status ==2){
                    wheretonext = 2;

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
                Log.d("Testing1","111");
            }
            else if(wheretonext == 2){
                    Log.d("Testing2","222");


            }
        }
    }

}

