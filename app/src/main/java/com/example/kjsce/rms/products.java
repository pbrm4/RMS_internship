package com.example.kjsce.rms;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class products extends ListActivity {

    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    //ArrayStorage for handling items
    ArrayList<HashMap<String, String>> productsList;
    ArrayList<HashMap<String, String>> productsListprices;
    ArrayList<String>productname;
    ArrayList<String>arr;

    //UI xml elements
    private TextView cart;
    private Button cartNext;
    private EditText searcher;
    private ListView lv;

    //ArrayAdapter to show the listitems
    ArrayAdapter<String> adapter;


    // url to get products list
    private static String url_all_products = "https://rmsanp.000webhostapp.com/getprod.php";
    private static String url_all_products_types = "https://rmsanp.000webhostapp.com/getalltypes.php";


    // JSON Node names
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PNAME = "productname";

    // products JSONArray
    JSONArray products = null;
    JSONArray productprices = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Log.d("Location Details:",getIntent().getStringExtra("placeName"));
        final String loc = getIntent().getStringExtra("placeName");
        productsList = new ArrayList<HashMap<String, String>>();
        productname = new ArrayList<String>();
        cart = (TextView)findViewById(R.id.cartCount);
        cart.setText("0");
        cartNext = (Button)findViewById(R.id.cartButton);
        searcher = (EditText)findViewById(R.id.search_product);


        new LoadAllProducts().execute();

        lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem

                String pid = ((TextView) view.findViewById(R.id.name)).getText().toString();
                Log.d("List just before click", productname.toString());
                if(productname.contains(pid)){
                    productname.remove(pid);
                    ((TextView)view.findViewById(R.id.name)).setBackgroundColor(getResources().getColor(R.color.white));
                }
                else {
                    productname.add(pid);
                    ((TextView)view.findViewById(R.id.name)).setBackgroundColor(getResources().getColor(R.color.navigationBarColor));
                }
                Log.d("List just after click", productname.toString());

                cart.setText(Integer.toString(productname.size()));
            }
        });

        searcher.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                products.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        cartNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(cart.getText().toString())>0) {
                    Intent gotonext = new Intent(products.this, comparison.class);
                    gotonext.putExtra("prices", productsListprices);
                    gotonext.putStringArrayListExtra("productlist", productname);
                    gotonext.putExtra("loc",loc);
                    startActivity(gotonext);
                }
                else{
                    Toast.makeText(products.this, "You have not selected any products, select products to proceed to Cart", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


class LoadAllProducts extends AsyncTask<String, String, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(products.this);
        pDialog.setMessage("Loading products. Please wait.....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    protected String doInBackground(String... args) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
//      JSONObject jsonall = jParser.makeHttpRequest(url_all_products_types,"GET",params);
        Log.d("All Products: ", json.toString());
//      Log.d("All products types",jsonall.toString());

        try {

            int success =1;
            if (success == 1) {
                products = json.getJSONArray(TAG_PRODUCTS);
                productprices =json.getJSONArray(TAG_PRODUCTS);
                for (int i = 0; i < products.length(); i++) {

                    JSONObject c = products.getJSONObject(i);

                    // Storing each json item in variable
                    String name = c.getString(TAG_PNAME);

                    // creating new HashMap
                    HashMap<String,String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_PNAME, name);

                    // adding HashList to ArrayList
                    productsList.add(map);
                }


                /*
                for(int i=0;i<productprices.length();i++){
                    JSONObject c = productprices.getJSONObject(i);
                    // Storing each json item in variable
                    // String id = c.getString(TAG_ID);
                    String name = c.getString(TAG_PNAME);
                    String price = c.getString("price");
                    String retailname = c.getString("retailname");
                    // creating new HashMap

                    HashMap<String,String> map = new HashMap<String, String>();
                    // adding each child node to HashMap key => value
                    // map.put(TAG_ID, id);

                    map.put(TAG_PNAME, name);
                    map.put("price",price);
                    map.put("retailname",retailname);
                    // adding HashList to ArrayList
                    productsListprices.add(map);
                    //arr.add(productsList.get(i).toString());
                }*/


            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(String file_url) {
        // dismiss the dialog after getting all products
        pDialog.dismiss();
        // updating UI from Background Thread
        runOnUiThread(new Runnable() {
            public void run() {
                /**
                 * Updating parsed JSON data into ListView
                 * */
                arr = new ArrayList<String>();
                for(int i=0;i<productsList.size();i++){

                    arr.add(productsList.get(i).get(TAG_PNAME));
                }
                adapter = new ArrayAdapter<String>(products.this,R.layout.list_item,R.id.name,arr);
                setListAdapter(adapter);
            }
        });

    }
  }
}


