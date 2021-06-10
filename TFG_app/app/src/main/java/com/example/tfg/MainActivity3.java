package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity3 extends AppCompatActivity {
    BBDD bd = new BBDD(this, "bbdd", null, 10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        TextView tx = findViewById(R.id.textViewReg);
        ArrayList<String> registros = new ArrayList<>();
        String aux = "";
        registros = bd.readAll();
        for (int i = 0; i < registros.size(); i++) {

            aux += registros.get(i);

        }
        tx.setText(aux);

    }

    public void butFinish(View v) {
        this.finish();
    }
}