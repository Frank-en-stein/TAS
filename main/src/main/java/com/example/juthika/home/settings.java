package com.example.juthika.home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.juthika.home.webconnect.Domain;
import com.example.juthika.home.webconnect.JSONParser;
import com.example.sos.SOSservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Juthika on 10/23/2016.
 */
public class settings extends AppCompatActivity {
    public static final String PREFS_NAME = "TASdata";
    EditText con1,con2, con3, msg;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        con1 = (EditText) findViewById(R.id.contact1);
        con2 = (EditText) findViewById(R.id.contact2);
        con3 = (EditText) findViewById(R.id.contact3);

        msg = (EditText) findViewById(R.id.msgtxt);

        con1.setText(settings.getString("TAScon1", null));
        con2.setText(settings.getString("TAScon2", null));
        con3.setText(settings.getString("TAScon3", null));
        msg.setText(settings.getString("TASmsg", null));

    }

    public void reqlogout(View v)
    {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        if(settings.getBoolean("TASloggedIn", false)==true) {
            editor.remove("TASloggedIn");
            editor.remove("TASuser");
            editor.remove("TASmsg");
            editor.remove("TAScon1");
            editor.remove("TAScon2");
            editor.remove("TAScon3");
            editor.commit();

            stopService(new Intent(settings.this, SOSservice.class));

            Intent i = new Intent(settings.this, login.class);
            startActivity(i);
            finish();
        }
    }

    public void svchng(View v)
    {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        if(settings.getString("TAScon1", null)==con1.getText().toString().trim() && settings.getString("TAScon2", null)==con2.getText().toString().trim()
        && settings.getString("TAScon3", null)==con3.getText().toString().trim() && settings.getString("TASmsg", null)==msg.getText().toString().trim())
        {
            Toast.makeText(this, "No changes to save", Toast.LENGTH_SHORT).show();
            return;
        }

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        final AlertDialog prompt = alert.create();

        input.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);

        alert.setView(input);    //edit text added to alert
        alert.setTitle("Password Required");   //title setted
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();

                new settings.PostAsync().execute("name", input.getText().toString(), "email",
                            settings.getString("TASuser", null), con1.getText().toString().trim(),
                            con2.getText().toString().trim(), con3.getText().toString().trim(), msg.getText().toString().trim());
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                prompt.dismiss();
            }
        });
        alert.show();

    }



    class PostAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private ProgressDialog pDialog;

        private static final String LOGIN_URL = Domain.DOMAIN + "update.php";

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(settings.this);
            pDialog.setMessage("Synchronizing new settings...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("name", args[0]);
                params.put("password", args[1]);
                params.put("email", args[2]);
                params.put("phone", args[3]);
                params.put("contact1", args[4]);
                params.put("contact2", args[5]);
                params.put("contact3", args[6]);
                params.put("msg", args[7]);

                Log.d("request", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());

                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject json) {

            int success = 0;
            String message = "";

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (json != null) {
                //Toast.makeText(settings.this, json.toString(), Toast.LENGTH_LONG).show();

                try {
                    success = json.getInt(TAG_SUCCESS);
                    message = json.getString(TAG_MESSAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (success == 1) {
                Log.d("Success!", message);

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("TASmsg", msg.getText().toString().trim());
                editor.putString("TAScon1", con1.getText().toString().trim());
                editor.putString("TAScon2", con2.getText().toString().trim());
                editor.putString("TAScon3", con3.getText().toString().trim());

                Toast.makeText(settings.this, "Settings Updated", Toast.LENGTH_SHORT).show();
            }
            else if(success==9) {
                Toast.makeText(settings.this, "Wrong Password Entered", Toast.LENGTH_SHORT).show();
            }
            else {
                Log.d("Failure", message);
            }
        }

    }
}
