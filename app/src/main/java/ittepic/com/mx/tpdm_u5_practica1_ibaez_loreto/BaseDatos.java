package ittepic.com.mx.tpdm_u5_practica1_ibaez_loreto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {

    public BaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE SUERTE (IDSUERTE INTEGER PRIMARY KEY AUTOINCREMENT," +
                "SIGNO VARCHAR(200), " +
                "DESCRIPCION VARCHAR(200))");

        db.execSQL("CREATE TABLE AMOR (IDAMOR INTEGER PRIMARY KEY AUTOINCREMENT," +
                "SIGNO VARCHAR(200), " +
                "DESCRIPCION VARCHAR(200))");

        db.execSQL("CREATE TABLE RESULTADOS (IDRESULTADOS INTEGER PRIMARY KEY AUTOINCREMENT," +
                "EQUIPO VARCHAR(200), " +
                "DESCRIPCION VARCHAR(200))");

        db.execSQL("CREATE TABLE CHISME (IDCHISME INTEGER PRIMARY KEY AUTOINCREMENT," +
                "CHISME VARCHAR(200) )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
