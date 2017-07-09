package com.example.juthika.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Juthika on 10/22/2016.
 */
public class archieve extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archieve);
    }
    public  void preview(View v)
    {
        Intent i = new Intent(this, preview.class);
        startActivity(i);
    }
}


