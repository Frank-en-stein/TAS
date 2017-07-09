package com.example.juthika.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Juthika on 10/19/2016.
 */
public class tutorial extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);
    }
    public void videotutorial(View v) {
        Intent i = new Intent(tutorial.this, videotutorial.class);
        startActivity(i);
    }
    public void imagetutorial(View v) {
        Intent i = new Intent(tutorial.this, imagetutorial.class);
        startActivity(i);
    }
    public void dosanddonts(View v) {
        Intent i = new Intent(tutorial.this, dosanddonts.class);
        startActivity(i);
    }
}







