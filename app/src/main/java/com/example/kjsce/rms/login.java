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
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class login extends AppCompatActivity {
    private ProgressDialog pDialog;
    String phone;
    String password;
    JSONParser jParser = new JSONParser();
    JSONObject json;
    Button login;
    int wheretonext;
    String name;
    String choice;
    EditText phonecolumn;
    TextView login_status;
    EditText pass;
    private static String url_login_user = "http://rmsanp.000webhostapp.com/loginuser.php";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phonecolumn = (EditText)findViewById(R.id.log_phone);
        pass= (EditText)findViewById(R.id.log_pwd);
        Bundle extras = getIntent().getExtras();
        phone  = extras.getString("phone");
        choice = extras.getString("choice");
        login = (Button)findViewById(R.id.log_log);
        login_status = (TextView)findViewById(R.id.login_status);
        phonecolumn.setTextColor(getResources().getColor(R.color.black));
        phonecolumn.setEnabled(false);
        phonecolumn.setText(phone);
        password = pass.getText().toString();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    class loginUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(login.this);
            pDialog.setMessage("");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phone",phone));
                params.add(new BasicNameValuePair("password",password));

                json = jParser.makeHttpRequest(url_login_user, "POST", params);
                Log.d("json: ", json.toString());

                Boolean isLoggedIn = json.getBoolean("logged_in");//
                if(isLoggedIn){
                    name = json.getString("name");
                    wheretonext = 1;
                }
                else{
                        wheretonext = 2;
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

            if(wheretonext == 1 ){
                    /*Intent gotoBuy = new Intent(login.this,);
                    gotoBuy.putExtra("name",name);
                    */
                    Log.d("logged in ",choice);
            }
            else if(wheretonext == 2) {
                pass.setText("");
                login_status.setText("Login Credentials not correct, Please try again.");
            }
        }
    }

}
