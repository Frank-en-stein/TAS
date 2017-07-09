package com.example.juthika.home;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class sosreceiver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sosreceiver);

        TextView from = (TextView) findViewById(R.id.from);
        TextView sms = (TextView) findViewById(R.id.SMS);

        from.setText("From: " + getIntent().getExtras().getString("src"));
        sms.setText(getIntent().getExtras().getString("msg").replace("TASsosAlert\n", ""));
    }

    public void mapinit(View V) {
        String all[] = getIntent().getExtras().getString("msg").split("\n");
        String temp[] = all[3].split(":");
        Toast.makeText(this, all[3], Toast.LENGTH_SHORT).show();
        double lat= Double.parseDouble(temp[1].trim());
        temp = all[4].split(":");
        double longi = Double.parseDouble(temp[1].trim());
        Toast.makeText(this, lat+" "+longi, Toast.LENGTH_SHORT).show();
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f(Victim Location)", lat, longi, lat, longi);
        showMap(Uri.parse(uri));
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
