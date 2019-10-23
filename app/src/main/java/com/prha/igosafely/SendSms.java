package com.prha.igosafely;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.net.Uri;
import android.os.Bundle;
//import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SendSms extends AppCompatActivity implements LocationListener {

    Button getLocationBtn;
    TextView locationText;
    String add, Msg;
    List<String> EmContacts = new ArrayList<>();

    LocationManager locationManager;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_send_sms);

        getLocationBtn = (Button)findViewById(R.id.sendSms);
        locationText = (TextView)findViewById(R.id.address);

        SQLiteDatabase db;
        db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
        try{
            c=db.rawQuery("SELECT * FROM details", null);
            if(c.getCount()==0)
            {

                return;
            }
//            StringBuffer buffer=new StringBuffer();

            if (c.moveToFirst()) {
                do {

                    EmContacts.add(c.getString(1));
                } while (c.moveToNext());
            }
            Log.d("contact", EmContacts.get(0));

        }catch (Exception e){
            Log.d("db",e.toString());


        }


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }


        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                if (EmContacts.size() != 0) {
                    try {
//                    SmsManager smgr = SmsManager.getDefault();
//                    smgr.sendTextMessage(EmContacts.get(0),null,Msg,null,null);
//                    Toast.makeText(SendSms.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
//                    if(EmContacts.get(1)!=null) {
//                        SmsManager smgr1 = SmsManager.getDefault();
//                        smgr1.sendTextMessage(EmContacts.get(1), null, Msg, null, null);
//                        Toast.makeText(SendSms.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
//                    }
                        if (EmContacts.size() == 2) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse("smsto:"));
                            i.setType("vnd.android-dir/mms-sms");
                            i.putExtra("address", EmContacts.get(0) + ";" + EmContacts.get(1));
                            i.putExtra("sms_body", Msg);
                            startActivity(Intent.createChooser(i, "Send sms via:"));
                        }
                        if (EmContacts.size() == 1) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse("smsto:"));
                            i.setType("vnd.android-dir/mms-sms");
                            i.putExtra("address", EmContacts.get(0));
                            i.putExtra("sms_body", Msg);
                            startActivity(Intent.createChooser(i, "Send sms via:"));

                        }
                    } catch (Exception e) {
                        Toast.makeText(SendSms.this, "SMS Failed to Send, Please try again" + e.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("msg", e.toString());
                    }
                }
                else{
                    try{
                      AlertDialog.Builder detect = new AlertDialog.Builder(SendSms.this);
                        detect.setMessage("Add atleast one emergency contact to continue.").setPositiveButton("OK", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                Intent in = new Intent(SendSms.this,Register.class);
                                startActivity(in);
                            }
                        });
                        detect.setCancelable(false);
                        AlertDialog alert=detect.create();
                        alert.show();
                            return;

                    }catch (Exception e){
                        Log.d("db",e.toString());
                    }

                }
            }

        });
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        add = "http://maps.google.com/maps?q="+location.getLatitude()+","+location.getLongitude();

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));

            Msg = "Please Help Me I'm in Danger!! My address is "+"\n"+(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2))+"\n "+" You Can also locate me on this link address"+ add;
        }catch(Exception e)
        {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(SendSms.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


}