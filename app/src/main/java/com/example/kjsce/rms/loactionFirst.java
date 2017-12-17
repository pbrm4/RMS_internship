package com.example.kjsce.rms;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import static android.R.attr.data;


public class loactionFirst extends AppCompatActivity {


    //Global Declarations
    Button manual_loc;
    Button aut_loc;
    int PLACE_PICKER_REQUEST = 1;
    private PlaceDetectionClient mPlaceDetectionClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaction_first);

        manual_loc = (Button)findViewById(R.id.loc_man);
        aut_loc=(Button)findViewById(R.id.auto_loc);

        aut_loc.setOnClickListener(new View.OnClickListener()
        {
            @Override
             public void onClick(View v)
            {
                    String locsend ="19.0730847,72.8998221";
                Toast.makeText(loactionFirst.this,"Place: K.J. Somaiya College Of Engineering" , Toast.LENGTH_LONG).show();
                    Intent gotoProduct = new Intent(loactionFirst.this,products.class);
                    gotoProduct.putExtra("placeName",locsend);

                    startActivity(gotoProduct);
            }
        });


        manual_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    startActivityForResult(builder.build(loactionFirst.this), PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.

                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

            }


        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {

            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                LatLng loc= place.getLatLng();
                String locsend = loc.latitude+","+loc.longitude;
                Log.d("location to be sent",locsend);
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                Intent gotoProduct = new Intent(this,products.class);
                gotoProduct.putExtra("placeName",locsend);
                startActivity(gotoProduct);

            }

            else if (resultCode == PlacePicker.RESULT_ERROR) {

                Status status = PlacePicker.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("STATUS", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {

                Log.i("STATUS:  ","CANCELLED BY USER");
            }
        }
    }
}
