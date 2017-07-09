package com.example.juthika.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Juthika on 10/23/2016.
 */
public class imagetutorial extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagetutorial);
    }

    public void nexttwo(View v)
    {
        Intent i = new Intent(this, imagetwo.class);
        startActivity(i);
    }
}
