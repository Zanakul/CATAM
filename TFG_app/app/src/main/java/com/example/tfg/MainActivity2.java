package com.example.tfg;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity {


    BBDD bd = new BBDD(this, "bbdd", null, 10);
    private SensorManager sm;
    private Sensor gs;
    private SensorEventListener gsEvent;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
    static BluetoothSocket btSocket = null;
    float val = 0;
    boolean bacc = false, bfre = false;
    BluetoothDevice hc06 = ba.getRemoteDevice("20:15:12:04:35:41");
    float initVal = 75;
    float valores[] = new float[40];
    int cs = 0;
    static String humidity, temperature;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        gs = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SeekBar sk = findViewById(R.id.seekBarSpeed);
        sk.setProgress(50);


        int count = 0;
        do {
            try {
                btSocket = hc06.createRfcommSocketToServiceRecord(mUUID);
                btSocket.connect();
                System.out.println(btSocket.isConnected());


            } catch (IOException e) {
                System.out.println("not conected");
            }
            count++;
        } while (!btSocket.isConnected() && count < 3);

        if (gs == null) {
            Toast.makeText(this, "The divice has no gyroscope", Toast.LENGTH_SHORT).show();
        }
        int finalCount = count;
        gsEvent = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (bacc || bfre) {
                    if (cs < 40) {
                        valores[cs] = event.values[1];
                        cs++;
                    } else {
                        for (int i = 0; i < 40; i++) {
                            val += valores[i];
                        }
                        val /= 40;
                        cs = 0;
                        refresh(val);
                    }
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        ImageButton acc = findViewById(R.id.imageButtonA);
        acc.setBackgroundColor(Color.rgb(102, 179, 255));
        ImageButton dacc = findViewById(R.id.imageButtonF);
        dacc.setBackgroundColor(Color.rgb(102, 179, 255));

        acc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bacc = true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    bacc = false;
                    reset();

                }


                return false;
            }
        });

        dacc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bfre = true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    bfre = false;
                    reset();

                }

                return false;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        sm.registerListener(gsEvent, gs, SensorManager.SENSOR_DELAY_FASTEST);
    }


    @Override
    protected void onPause() {
        super.onPause();

        sm.unregisterListener(gsEvent);
    }

    private void refresh(float val) {
        try {

            OutputStream os = btSocket.getOutputStream();
            String s = "";
            if (val < -9) {
                val = -9;
            }
            if (val >= 0) {
                val *= 10;
                float escal = (130 - initVal) / 90;
                val *= escal;
                val += initVal;
            }
            if (val < 0) {
                val *= 10;
                float escal = initVal / 90;
                val *= escal;
                val += initVal;
            }
            SeekBar sk = findViewById(R.id.seekBarSpeed);
            int speed = 210 - (sk.getProgress() / 2);
            if (bacc) {
                s = (int) val + ";" + speed + "#" + 1 + ":" + 2 + "_";
            }
            if (bfre) {
                s = (int) val + ";" + speed + "#" + 0 + ":" + 2 + "_";
            }
            System.out.println(s);
            byte out[] = s.getBytes();
            os.write(out);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void desc(View v) {
        try {

            reset();
            btSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.finish();


    }

    public void reset() {
        try {
            OutputStream os = btSocket.getOutputStream();
            String s = initVal + ";" + 255 + "#" + 0 + ":" + 2 + "_";
            byte out[] = s.getBytes();

            os.write(out);
        } catch (IOException e) {

        }

    }

    public void temp(View v) {
        int tempe = 0, hum = 0;
        double temper = 0;

        try {
            OutputStream os = btSocket.getOutputStream();
            InputStream is = btSocket.getInputStream();
            String s = initVal + ";" + 255 + "#" + 0 + ":" + 1 + "_";
            byte out[] = s.getBytes();

            os.write(out);
            os.write(out);


            tempe = is.read();
            hum = is.read();
            temper = tempe / 5.0;

        } catch (IOException e) {

        }


        temperature = temper + "";
        humidity = hum + "";
        MainActivity.temp = temperature;
        MainActivity.hum = humidity;
        TextView tx = findViewById(R.id.textViewTemp);
        TextView tx2 = findViewById(R.id.textViewHume);
        tx.setText(temperature + "º");
        tx2.setText(humidity + "%");

        bd.insert(temperature, humidity);

    }


}