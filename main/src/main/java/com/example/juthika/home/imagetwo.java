package com.example.juthika.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by home on 11/9/2016.
 */

public class imagetwo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagetwo);
    }

    public void nexthree(View v)
    {
        Intent i = new Intent(this, imagetwo.class);
        startActivity(i);
    }
    public void prevone(View v)
    {
        finish();
    }

}
