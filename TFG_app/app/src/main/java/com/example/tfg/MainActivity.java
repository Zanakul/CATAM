package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    BBDD bd = new BBDD(this, "bbdd", null, 10);
    static String temp, hum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout ly = findViewById(R.id.LL1);
        ly.setBackgroundColor(Color.rgb(102, 179, 255));


    }


    public void buttonStart(View v) {

        startActivity(new Intent(MainActivity.this, MainActivity2.class));
    }


    public void buttonRegistros(View v) {
        startActivity(new Intent(MainActivity.this, MainActivity3.class));


    }


}