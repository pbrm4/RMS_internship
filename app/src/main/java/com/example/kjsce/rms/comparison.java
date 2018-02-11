package com.example.kjsce.rms;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.ArrayList;

import static com.example.kjsce.rms.R.id.pid;

public class comparison extends AppCompatActivity {
    private ProgressDialog pDialog;

    //Screen details
    int width = Resources.getSystem().getDisplayMetrics().widthPixels;
    int height = Resources.getSystem().getDisplayMetrics().heightPixels;

    // Creating JSON Parser object and array
    JSONParser jParser = new JSONParser();
    JSONObject json,allstores;
    JSONArray products = null;
    JSONArray stores= null;

    //ArrayStorage for handling items
    ArrayList<String> storename;
    ArrayList<String> searchequery;
    ArrayList<String> fetchList;
    ArrayMap<String,JSONArray> productlist;

    //Location Details
    Double lat;
    Double lon;
    String loc;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PRICE = "price";

    // url to get products list
    private static String url_all_products_prices = "https://rmsanp.000webhostapp.com/getalltypes.php";
    private static String url_all_stores = "https://rmsanp.000webhostapp.com/getallstores.php";

    //Table declaration
    TableLayout tl;
    TableRow tr;
    Button buynow;
    Button delivery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);
        tl = (TableLayout) findViewById(R.id.maintable);

        buynow = new Button(comparison.this);
        delivery = new Button(comparison.this);
        fetchList= new ArrayList<String>();

        fetchList=  getIntent().getStringArrayListExtra("productlist");
        loc = getIntent().getStringExtra("loc");
        String[] parts = loc.split(",");
        lat = Double.parseDouble(parts[0]);
        lon = Double.parseDouble(parts[1]);
        Log.d("Latitude:",lat.toString());
        Log.d("Longitude",lon.toString());

        new GetProductDetails().execute();

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(comparison.this, "Hellooooo", Toast.LENGTH_SHORT).show();
                Intent gotobuy = new Intent(comparison.this,mobileotp1.class);
                gotobuy.putExtra("store","DMart");
                gotobuy.putExtra("choice","Pay");
                startActivity(gotobuy);
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotodelivery = new Intent(comparison.this,mobileotp1.class);
                gotodelivery.putExtra("store","DMart");
                gotodelivery.putExtra("choice","Delivery");
                startActivity(gotodelivery);
            }
        });


    }

    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(comparison.this);
            pDialog.setMessage("Loading comparison details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... param) {

            // updating UI from Background Thread

                    // Check for success tag
                    int success=1;
                    try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("lat",lat.toString()));
                params.add(new BasicNameValuePair("lon",lon.toString()));

                json = jParser.makeHttpRequest(url_all_products_prices, "GET", params1);
                allstores = jParser.makeHttpRequest(url_all_stores,"GET",params);

                // check your log for json response
                Log.d("Single Product Details", json.toString());
                Log.d("ALl retail stores",allstores.toString());

                if (success == 1) {

                    // successfully received product details
                    stores = allstores.getJSONArray("products");
                    storename = new ArrayList<String>();

                    for(int i=0;i<stores.length();i++){

                        JSONObject allstore = stores.getJSONObject(i);
                        String name = allstore.getString("retailname");
                        storename.add(name);
                    }

                    Log.d("All store names:  ",storename.toString());

                    searchequery = new ArrayList<String>();
                    for(int i=0;i<fetchList.size();i++){

                        String productname = fetchList.get(i).toString();
                        for(int j=0;j<storename.size();j++){

                            searchequery.add(productname+storename.get(j));
                        }
                    }

                    Log.d("Search query: ",searchequery.toString());
                    products = json.getJSONArray("products"); // JSON Array
                    Log.d("Size of products..",Integer.toString(products.length()));
                    Log.d("Size of Search query",Integer.toString(searchequery.size()));
                    JSONObject productroot = products.getJSONObject(products.length()-1);
                    Log.d("Productroot: ",productroot.toString());


                    productlist= new ArrayMap<String,JSONArray>();

                    for(int i=0;i<searchequery.size();i++){

                        JSONArray product = productroot.getJSONArray(searchequery.get(i));
                        Log.d("Product root "+i+"  :" ,product.toString());
                        JSONObject productname = product.getJSONObject(1);
                        Log.d("Prdct nm nsd prdct:",productname.toString());
                        productlist.put(searchequery.get(i),product);
                    }

                    Log.d("Product root " ,productlist.toString());

                }else{
                    // product with pid not found
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();

            runOnUiThread(new Runnable() {
                public void run() {
                    /* setting headers for tables
                    */
                    tr = new TableRow(comparison.this);
                    tr.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.FILL_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tr.setPadding(15, 15, 15, 0);
                    for(int i=0;i<storename.size();i++){

                       TextView companyName = new TextView(comparison.this);
                       companyName.setText(storename.get(i));
                       companyName.setTextColor(Color.GRAY);
                       //companyName.setPadding(15, 15, 15, 0);
                       companyName.setTextSize(25);
                       companyName.setMinWidth(width/(storename.size()));
                       companyName.setMinHeight(25);
                       companyName.setGravity(Gravity.CENTER);
                       companyName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                       tr.addView(companyName);
                   }
                    tl.addView(tr, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.FILL_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));


                    double price[] = new double[storename.size()];
                    for(int j=0;j<fetchList.size();j++) {
                        tr = new TableRow(comparison.this);
                        tr.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.FILL_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tr.setPadding(15, 15, 15, 0);


                        for (int i = 0; i < storename.size(); i++) {
                            TextView tv = new TextView(comparison.this);
                            String toprint = fetchList.get(j)+"\n";
                            String quer = fetchList.get(j)+storename.get(i);

                            String textmeikyahai = productlist.get(quer)/*.get(0)*/.toString();
                            Log.d("checkmystring ",textmeikyahai);
                            String textmeikyahai2="";

                            try {
                                textmeikyahai2 = productlist.get(quer).getJSONObject(0).getString("price");
                                price[i] = price[i]+Double.parseDouble(textmeikyahai2);
                                Log.d("checkmystring21212121 ",textmeikyahai2);
                            }
                            catch(JSONException e){
                                Log.d("JSON E","JSON error primtng stack next");
                                e.printStackTrace();
                            }

                            toprint = toprint+textmeikyahai2;
                            tv.setText(toprint);
                            tv.setTextColor(Color.DKGRAY);
                            tv.setTextSize(18);
                            tv.setMinWidth(width/(3));
                            tv.setMinHeight(25);
                            tv.setBackgroundResource(R.drawable.cell_shape);
                            tv.setGravity(Gravity.CENTER);
                            tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                            tr.addView(tv);

                        }

                        tl.addView(tr, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.FILL_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));

                    }
                        tr = new TableRow(comparison.this);
                    TableRow tr1 = new TableRow(comparison.this);
                    tr1.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.FILL_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));


                    for(int i=0;i<storename.size();i++){

                            TextView tv = new TextView(comparison.this);
                            tv.setText(String.format("%.2f", price[i]));
                            tv.setTextColor(getResources().getColor(R.color.price));
                            tv.setTextSize(18);
                            tv.setMinWidth(width/(storename.size()));
                            tv.setMinHeight(25);
                            tv.setBackgroundResource(R.drawable.cell_shape_black);
                            tv.setGravity(Gravity.CENTER);
                            tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                            tr.addView(tv);
                    }
                    tl.addView(tr, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.FILL_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    tr1.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.FILL_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tr1.setPadding(15, 15, 15, 0);
                    tr1.setPadding(15, 15, 15, 0);


                    buynow.setText("BUY");

                    buynow.setTextColor(Color.GRAY);
                    buynow.setGravity(Gravity.CENTER);
                    buynow.setTextSize(20);
                    buynow.setMinWidth(width/(storename.size()));
                    buynow.setMinHeight(25);
                    buynow.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    buynow.setClickable(true);
                    tr1.addView(buynow);
                    tl.addView(tr1,new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.FILL_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    TableRow tr2 = new TableRow(comparison.this);
                    tr2.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.FILL_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tr2.setPadding(15, 15, 15, 0);
                    tr2.setPadding(15, 15, 15, 0);



                    delivery.setText("Delivery");
                    delivery.setTextColor(Color.GRAY);
                    delivery.setGravity(Gravity.CENTER);
                    delivery.setTextSize(20);
                    delivery.setMinWidth(width/(storename.size()));
                    delivery.setMinHeight(25);
                    delivery.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    delivery.setClickable(true);
                    tr2.addView(delivery);
                    tl.addView(tr2,new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.FILL_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                }


            });
        }
    }

}
