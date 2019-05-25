package ittepic.com.mx.tpdm_u5_practica1_ibaez_loreto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MyReceiver extends BroadcastReceiver {

    BaseDatos dbms;

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadCastReceiver";

    String msg, numeroTel = "";


    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.i(TAG, ">>>> Intent Received" + intent.getAction());
        dbms = new BaseDatos(context, "PRACTICA1", null, 1);
        if (intent.getAction() == SMS_RECEIVED) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                Object[] mypdu = (Object[]) bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[mypdu.length];

                for (int i = 0; i < mypdu.length; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = bundle.getString("format");

                        messages[i] = SmsMessage.createFromPdu((byte[]) mypdu[i], format);
                    } else {
                        messages[i] = SmsMessage.createFromPdu((byte[]) mypdu[i]);
                    }

                    msg = messages[i].getMessageBody();
                    numeroTel = messages[i].getOriginatingAddress();
                }

                metodo(msg, numeroTel, context);

                //Toast.makeText(context, "Message: " + msg + " " + numeroTel, Toast.LENGTH_LONG).show();

            }
        }
    }// onReceive

    private void metodo(String mensaje, String telefono, Context context) {
        String[] val;
        if (mensaje.toLowerCase().startsWith("suerte")) {
            val = mensaje.split(" ");
            //consultar();
            consultar(val[0].replace(" ", ""), val[1].replace(" ", ""), context, telefono);

            System.out.println("ENTRO SUERTE");

        } else if (mensaje.toLowerCase().startsWith("amor")) {
            val = mensaje.split(" ");
            consultar(val[0].replace(" ", ""), val[1].replace(" ", ""), context, telefono);
            //consultar();
            System.out.println("ENTRO AMOR");

        } else if (mensaje.toLowerCase().startsWith("resultados")) {
            val = mensaje.split(" ");

            consultarResultados(val[0].replace(" ", ""), val[1].replace(" ", ""), context, telefono);
            System.out.println("ENTRO RESULTADOS");

        } else if (mensaje.toLowerCase().startsWith("chisme")) {
            String id = "" + (int) ((Math.random() * 6) + 1);
            System.out.println("ID: " + id);
            consultarChisme(mensaje, id, context, telefono);
            System.out.println("ENTRO CHISME");

        }
    }

//    private void consultar() {
//        try {
//            SQLiteDatabase selec = dbms.getReadableDatabase();
//            Cursor c = selec.rawQuery("SELECT * FROM CHISME", null);
//
//            if (c.moveToFirst()) {
//                String cadena = "";
//                do {
//
//                    String id = c.getString(0);
//                    String nom = c.getString(1);
////                    String dom = c.getString(2);
//
//                    cadena += id + ", " + nom + ", " + "\n";
//                } while (c.moveToNext());
//
//                System.out.println(">>>> " + cadena);
//                selec.close();
//
//            } else {
//                // mensaje("ERROR", "AÃºn no hay datos para mostrar!");
//            }
//
//        } catch (SQLException e) {
//            //mensaje("Error de consulta!", e.getMessage());
//        }
//    }// consultar


    private void consultar(String tabla, String signo, Context context, String telefono) {
        try {
            SQLiteDatabase selec = dbms.getReadableDatabase();
            Cursor c = selec.rawQuery("SELECT * FROM " + tabla.toUpperCase() + " WHERE SIGNO COLLATE SQL_Latin1_General_Cp1_CI_AI LIKE '%" + signo + "%'", null);

            if (c.moveToFirst()) {
                String cadena = "";
                do {

                    String des = c.getString(2);

                    cadena += des;
                } while (c.moveToNext());

                System.out.println(">>>> " + cadena);
                enviarMensaje(telefono, "" + cadena, context, tabla, signo);
                selec.close();

            } else {
                System.out.println("NO HAY DATOS");
            }

        } catch (SQLException e) {
            System.out.println("ERROR CONSULTA " + e.getMessage());
        }
    }// consultar

    private void consultarChisme(String tabla, String id, Context context, String telefono) {
        try {
            SQLiteDatabase selec = dbms.getReadableDatabase();
            Cursor c = selec.rawQuery("SELECT * FROM " + tabla.toUpperCase() + " WHERE IDCHISME =" + id, null);

            if (c.moveToFirst()) {
                String cadena = "";
                do {

                    String des = c.getString(1);

                    cadena += des;
                } while (c.moveToNext());

                System.out.println(">>>> " + cadena);
                enviarMensaje(telefono, "" + cadena, context, tabla, id);
                selec.close();

            } else {
                System.out.println("NO HAY DATOS");
            }

        } catch (SQLException e) {
            System.out.println("ERROR CONSULTA " + e.getMessage());
        }
    }// consultar

    private void consultarResultados(String tabla, String signo, Context context, String telefono) {
        try {
            SQLiteDatabase selec = dbms.getReadableDatabase();
            Cursor c = selec.rawQuery("SELECT * FROM " + tabla.toUpperCase() + " WHERE EQUIPO COLLATE SQL_Latin1_General_Cp1_CI_AI LIKE '%" + signo + "%'", null);

            if (c.moveToFirst()) {
                String cadena = "";
                do {

                    String des = c.getString(2);

                    cadena += des;
                } while (c.moveToNext());

                System.out.println(">>>> " + cadena);
                enviarMensaje(telefono, "" + cadena, context, tabla, signo);
                selec.close();

            } else {
                System.out.println("NO HAY DATOS");
            }

        } catch (SQLException e) {
            System.out.println("ERROR CONSULTA " + e.getMessage());
        }
    }// consultar


    private void enviarMensaje(String numero, String mensaje, Context context, String tabla, String signo) {

        SmsManager sms = SmsManager.getDefault();

        ArrayList<String> parts = sms.divideMessage(tabla.toUpperCase() + " " + signo.toUpperCase() + ":\n" + mensaje + "\n\nBy: OscarILo");
        sms.sendMultipartTextMessage(numero, null, parts, null, null);
        System.out.println("NUMERO " + numero);

        Toast.makeText(context, "Mensaje " + tabla + " Enviado.", Toast.LENGTH_LONG).show();

    }


}// class
