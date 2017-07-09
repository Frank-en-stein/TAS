package locationManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.smshandler.SmsSender;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnGeofencingTransitionListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.geofencing.model.GeofenceModel;
import io.nlopez.smartlocation.geofencing.utils.TransitionGeofence;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

/**
 * Created by home on 11/3/2016.
 */

public class SmartLocation implements OnLocationUpdatedListener, OnActivityUpdatedListener{
    private LocationGooglePlayServicesProvider provider;

    private static final int LOCATION_PERMISSION_ID = 1001;

    private Activity activity;
    private Context context;

    public Location currLocation;
    public DetectedActivity currLocationActivity;
    public Geofence currGeo;
    public int geoTransitionType;
    public SimpleDateFormat sdf;
    public String currDateTime;

    public boolean updateNeeded;

    public SmartLocation(Activity act, Context con) {
        activity = act;
        context = con;
        sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        updateNeeded = false;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
            return;
        }
        startLocation();

        currLocation = io.nlopez.smartlocation.SmartLocation.with(context).location().getLastLocation();
        currLocationActivity = io.nlopez.smartlocation.SmartLocation.with(context).activity().getLastActivity();
        currDateTime = sdf.format(new Date());

    }

    @Override
    public void onActivityUpdated(DetectedActivity detectedActivity) {
        currLocationActivity = detectedActivity;
    }


    @Override
    public void onLocationUpdated(Location location) {
        currLocation = location;
        currDateTime = sdf.format(new Date());

        if(updateNeeded){
            new SmsSender(context, String.format("lat:%.8f long:%.8f\nTime:%s", location.getLatitude(),
                    location.getLongitude(), currDateTime));
            updateNeeded = false;
        }
    }


    public void startLocation() {

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        io.nlopez.smartlocation.SmartLocation smartLocation = new io.nlopez.smartlocation.SmartLocation.Builder(context).logging(true).build();

        smartLocation.location(provider).start(this);
        smartLocation.activity().start(this);
    }

    public void stopLocation() {
        io.nlopez.smartlocation.SmartLocation.with(context).location().stop();

        io.nlopez.smartlocation.SmartLocation.with(context).activity().stop();

        io.nlopez.smartlocation.SmartLocation.with(context).geofencing().stop();
    }

    public String getNameFromType(DetectedActivity activityType) {
        switch (activityType.getType()) {
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.TILTING:
                return "tilting";
            default:
                return "unknown";
        }
    }

    public void turnGPSOn()
    {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        context.sendBroadcast(intent);

        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);


        }
    }

}
