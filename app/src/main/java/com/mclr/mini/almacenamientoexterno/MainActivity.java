package com.mclr.mini.almacenamientoexterno;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private final String FILENAME="archivoPrueba.txt";
    EditText mEditText;

    // Storage Permissions
    private static final int SOLICITAR_ALMACENAMIENTO_EXTERNO = 1;
    private static String[] PERMISOS_ALMACENAMIENTO = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mEditText = (EditText)findViewById(R.id.editText);
        verifyStoragePermissions(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean esAlmacenamientoExternoExcritura() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    public boolean esAlmacenamientoExternoLectura() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    public void escribirArchivo(View view) {
        if (esAlmacenamientoExternoExcritura()) {
            try {
                File textFile = new File(Environment.getExternalStorageDirectory(), FILENAME);
                FileOutputStream fileOutputStream = new FileOutputStream(textFile);
                fileOutputStream.write(mEditText.getText().toString().getBytes());
                fileOutputStream.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al escribir archivo",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No se puede escribir en el almacenamiento externo", Toast.LENGTH_LONG).show();
        }
    }

    public void leerArchivo(View view) {
        if (esAlmacenamientoExternoLectura()) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                //Utilizar esta llamada para obtener uno de los directorios estándar
                //File textFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), FILENAME);
                File textFile = new File(Environment.getExternalStorageDirectory(), FILENAME);

                //Esto muestra como utilizar el método getFreeSpace()
                //Toast.makeText(this, "Available Space: " + Environment.getExternalStorageDirectory().getFreeSpace(),Toast.LENGTH_SHORT).show();

                //Aquí esta un ejemplo práctico de la utilización del método getFreeSpace().  Se necesitará definir: ESPACIO_REQUERIDO_ARCHIVO
                //if (Environment.getExternalStorageDirectory().getFreeSpace() < ESPACIO_REQUERIDO_ARCHIVO) {
                //    //No hay suficiente espacio
                //} else {
                //    //Existe suficiente espacio
                //}
                FileInputStream fileInputStream = new FileInputStream(textFile);
                if (fileInputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String nuevaLinea = null;
                    while ( (nuevaLinea = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(nuevaLinea+"\n");
                    }
                    fileInputStream.close();
                }
                mEditText.setText(stringBuilder);
            } catch (java.io.IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al leer el archivo",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No se puede leer del almacenamiento externo", Toast.LENGTH_LONG).show();
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Verificar si se cuenta con permisos de escritura
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // Si no se cuenta con permiso, solicitarlo al usuario
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISOS_ALMACENAMIENTO,
                    SOLICITAR_ALMACENAMIENTO_EXTERNO
            );
        }
    }
}
