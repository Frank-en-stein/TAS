package com.example.juthika.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.juthika.home.webconnect.Domain;
import com.example.juthika.home.webconnect.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Avash on 10/24/2016.
 */
public class login extends AppCompatActivity {
    EditText phn, pass;
    public int success = 0;
    public static final String PREFS_NAME = "TASdata";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if(settings.getBoolean("TASloggedIn", false)==true) {
            Intent i = new Intent(login.this, activity_home.class);
            startActivity(i);
            finish();
        }

        setContentView(R.layout.login);

        phn = (EditText) findViewById(R.id.editText);
        pass = (EditText) findViewById(R.id.editText2);


    }
    public void signup(View v) {
        Intent i = new Intent(login.this, signup.class);
        startActivity(i);
    }

    public void reqLogin(View v)
    {
        String username = phn.getText().toString().trim(); //get this from user input
        String password = pass.getText().toString().trim(); //get this from user input
        new PostAsync().execute(username, password);
    }


    class PostAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private ProgressDialog pDialog;

        private static final String LOGIN_URL = Domain.DOMAIN+"login.php";

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        private static final String TAG_MSG = "msg";
        private static final String TAG_CON1 = "con1";
        private static final String TAG_CON2 = "con2";
        private static final String TAG_CON3 = "con3";

        private String user;
        private String msg;
        private String con1;
        private String con2;
        private String con3;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(login.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("phone", args[0]);
                params.put("password", args[1]);
                user = args[0];

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


            String message = "";

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (json != null) {
                //Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_LONG).show();

                try {
                    success = json.getInt(TAG_SUCCESS);
                    message = json.getString(TAG_MESSAGE);
                    msg = json.getString(TAG_MSG);
                    con1 = json.getString(TAG_CON1);
                    con2 = json.getString(TAG_CON2);
                    con3 = json.getString(TAG_CON3);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (success == 1) {
                Log.d("Success!", message);
            } else {
                Log.d("Failure", message);
            }
            if(success == 1 && message == "1") {
                // We need an Editor object to make preference changes.
                // All objects are from android.context.Context
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("TASloggedIn", true);
                editor.putString("TASuser", user);
                editor.putString("TASmsg", msg);
                editor.putString("TAScon1", con1);
                editor.putString("TAScon2", con2);
                editor.putString("TAScon3", con3);

                // Commit the edits!
                editor.commit();

                Intent i = new Intent(login.this, activity_home.class);
                startActivity(i);
                finish();
            }
            else if(success == 1 && message == "0") {
                Intent i = new Intent(login.this, verify.class);
                i.putExtra("user", user);
                i.putExtra("msg", msg);
                i.putExtra("con1", con1);
                i.putExtra("con2", con2);
                i.putExtra("con3", con3);
                startActivity(i);
                finish();
            }
            else Toast.makeText(getApplicationContext(), "Authentication Error", Toast.LENGTH_LONG).show();
        }

    }

}
