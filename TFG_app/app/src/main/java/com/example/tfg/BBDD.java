package com.example.tfg;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class BBDD extends SQLiteOpenHelper {
    String create = "CREATE TABLE Registros(FechaHora varchar(30) PRIMARY KEY, Temperatura varchar(5), Humedad varchar(5));";
    String drop = "Drop table Registros";


    public BBDD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(drop);
        db.execSQL(create);


    }

    public int insert(String temp, String hum) {

        SQLiteDatabase descp_ins;
        SQLiteDatabase descp_busc;
        Calendar c = Calendar.getInstance();
        System.out.println(c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        System.out.println(formattedDate);

        descp_ins = this.getWritableDatabase();
        descp_busc = this.getReadableDatabase();
        String cad = "INSERT INTO Registros(FechaHora,Temperatura,Humedad) VALUES ('" + formattedDate + "',+'" + temp + "' , '" + hum + "')"; //insertar en la tabla los datos introducidos en la         aplicacion guardado en variables
        boolean unico = true;

        try {
            descp_ins.execSQL(cad);
        } catch (Exception e) {
            e.getStackTrace();
            unico = false;
        }

        return 1;


    }

    public ArrayList<String> readAll() {
        ArrayList<String> registros = new ArrayList<String>();
        SQLiteDatabase db;
        db = this.getReadableDatabase();
        String[] campos = new String[]{"Fecha", "Temperatura", "Humedad"};

        Cursor cursor = db.rawQuery("select * from Registros", null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String reg = "Fecha Hora: " + cursor.getString(cursor.getColumnIndex("FechaHora"));
                reg += " Temp: " + cursor.getString(cursor.getColumnIndex("Temperatura")) + "º";
                reg += " Humedad: " + cursor.getString(cursor.getColumnIndex("Humedad")) + "%\n";
                registros.add(reg);
                cursor.moveToNext();
            }
        }


        return registros;
    }


}







