package com.example.juthika.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.juthika.home.webconnect.Domain;
import com.example.juthika.home.webconnect.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class verify extends AppCompatActivity {
    EditText ver;
    public static final String PREFS_NAME = "TASdata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        ver = (EditText)findViewById(R.id.editText);
    }
    public void reqVer(View v){
        String vertxt = ver.getText().toString().trim();
        new verify.PostAsync().execute(vertxt);
    }

    class PostAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private ProgressDialog pDialog;

        private static final String LOGIN_URL = Domain.DOMAIN+"verify.php";

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";

        public int success = 0;


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(verify.this);
            pDialog.setMessage("Attempting to verify...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("vertxt", args[0]);
                params.put("phone", getIntent().getStringExtra("user"));

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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (success == 1) {
                Log.d("Success!", message);
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("TASloggedIn", true);
                editor.putString("TASuser", getIntent().getStringExtra("user"));
                editor.putString("TASmsg", getIntent().getStringExtra("msg"));
                editor.putString("TAScon1", getIntent().getStringExtra("con1"));
                editor.putString("TAScon2", getIntent().getStringExtra("con2"));
                editor.putString("TAScon3", getIntent().getStringExtra("con3"));


                // Commit the edits!
                editor.commit();

                Intent i = new Intent(verify.this, activity_home.class);
                startActivity(i);
                finish();
            } else {
                Log.d("Failure", message);
                Toast.makeText(getApplicationContext(), "Wrong Verification Code", Toast.LENGTH_LONG).show();
            }

        }

    }
}
