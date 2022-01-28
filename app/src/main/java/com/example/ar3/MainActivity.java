package com.example.ar3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ar3.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnlogin(View view) {
        setContentView(R.layout.login);
    }

    public void btnregister(View view) {
        setContentView(R.layout.register);
    }

    public void btnbackhome(View view) {
        setContentView(R.layout.activity_main);
    }

    public void btnview(View view) {
        setContentView(R.layout.view);
    }

    public void btngoback(View view) {
        setContentView(R.layout.login);
    }

    public void btnlogout(View view) {
        setContentView(R.layout.activity_main);
    }

    public void btnMeasure(View view) {
        Intent intent= new Intent(MainActivity.this,Measure.class);
        startActivity(intent);
    }
}
