package com.example.juthika.home;

import android.os.Bundle;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.juthika.home.webconnect.Domain;
import com.example.juthika.home.webconnect.JSONParser;

/**
 * Created by Juthika on 10/24/2016.
 */
public class signup extends AppCompatActivity {

    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+[.]+[a-z]+";
    private final String namePattern = "[a-zA-Z .-]+";
    private final String phnPattern = "[0-9]{11}";

    public String nametxt, emailtxt, phntxt, passtxt, cpasstxt, msgtxt;
    public EditText name, email, phn, pass, cpass, c1, c2, c3, msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        name = (EditText) findViewById(R.id.editText9);
        email = (EditText) findViewById(R.id.editText8);
        phn = (EditText) findViewById(R.id.editText3);
        pass = (EditText) findViewById(R.id.editText4);
        cpass = (EditText) findViewById(R.id.editText5);
        c1 = (EditText) findViewById(R.id.editText101);
        c2 = (EditText) findViewById(R.id.editText102);
        c3 = (EditText) findViewById(R.id.editText103);
        msg = (EditText) findViewById(R.id.editText500);
    }

    public void reqSignup(View v) {
        nametxt = name.getText().toString().trim();
        emailtxt = email.getText().toString().trim();
        phntxt = phn.getText().toString().trim();
        passtxt = pass.getText().toString().trim();
        cpasstxt = cpass.getText().toString().trim();
        msgtxt = msg.getText().toString().trim();

        if (!nametxt.matches(namePattern) || nametxt.length() == 0) {
            Toast.makeText(getApplicationContext(), "Invalid name", Toast.LENGTH_SHORT).show();
        } else if (!emailtxt.matches(emailPattern) || emailtxt.length() == 0) {
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
        } else if (!phntxt.matches(phnPattern) || phntxt.length() == 0) {
            Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
        } else if (passtxt.length() < 8) {
            Toast.makeText(getApplicationContext(), "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
        } else if (!passtxt.equals(cpasstxt)) {
            Toast.makeText(getApplicationContext(), "Passwords dont match", Toast.LENGTH_SHORT).show();
            pass.setText("");
            cpass.setText("");
        }
        else if (!c1.getText().toString().trim().matches(phnPattern) || phntxt.length() == 0) {
            Toast.makeText(getApplicationContext(), "Invalid emergency number1", Toast.LENGTH_SHORT).show();
        }
        else if (!c2.getText().toString().trim().matches(phnPattern) || phntxt.length() == 0) {
            Toast.makeText(getApplicationContext(), "Invalid emergency number2", Toast.LENGTH_SHORT).show();
        }
        else if (!c3.getText().toString().trim().matches(phnPattern) || phntxt.length() == 0) {
            Toast.makeText(getApplicationContext(), "Invalid emergency number3", Toast.LENGTH_SHORT).show();
        }
        else{
            if(msgtxt == "Enter emergency message") msgtxt = "I am in Danger, Please Help!";
            new signup.PostAsync().execute(nametxt, passtxt, emailtxt, phntxt, c1.getText().toString().trim(),
                    c2.getText().toString().trim(), c3.getText().toString().trim(), msgtxt);
        }


    }


    class PostAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private ProgressDialog pDialog;

        private static final String LOGIN_URL = Domain.DOMAIN + "signup.php";

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(signup.this);
            pDialog.setMessage("Attempting to signup...");
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
                Toast.makeText(signup.this, json.toString(), Toast.LENGTH_LONG).show();

                try {
                    success = json.getInt(TAG_SUCCESS);
                    message = json.getString(TAG_MESSAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (success == 1) {
                Log.d("Success!", message);
            } else {
                Log.d("Failure", message);
            }
        }

    }

}