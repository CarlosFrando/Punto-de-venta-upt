package com.example.puntodeventaupt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.content.ContentValues;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etUsuario, etContrasena;
    Button btnEnviar;

    SharedPreferences sharedPreferences;
    public static final String PREF_NAME = "MisPreferencias";

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btnEnviar = findViewById(R.id.btnEnviar);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        dbHelper = new DBHelper(this);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = etUsuario.getText().toString().trim();
                String contrasena = etContrasena.getText().toString().trim();

                if (usuario.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Guardar en SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("usuario_guardado", usuario);
                editor.apply();

                // Guardar en SQLite
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("usuario", usuario);
                values.put("contrasena", contrasena);

                long resultado = db.insert("usuarios", null, values);
                db.close();

                if (resultado != -1) {
                    Toast.makeText(MainActivity.this, "Inicio de sesión guardado correctamente", Toast.LENGTH_SHORT).show();
                    etUsuario.setText("");
                    etContrasena.setText("");
                    Intent intent = new Intent(MainActivity.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Error al guardar en SQLite", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
