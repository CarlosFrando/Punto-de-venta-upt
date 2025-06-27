package com.example.puntodeventaupt;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.ContentValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class LoginMain extends Fragment {

    EditText etUsuario, etContrasena;
    Button btnEnviar, btnRegistrar;
    CheckBox checkGuardarSesion;

    SharedPreferences sharedPreferences;
    DBHelper dbHelper;

    public static final String PREF_NAME = "MisPreferencias";

    public LoginMain() {
        // Constructor obligatorio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_main, container, false);

        etUsuario = view.findViewById(R.id.etUsuario);
        etContrasena = view.findViewById(R.id.etContrasena);
        btnEnviar = view.findViewById(R.id.btnEnviar);
        btnRegistrar = view.findViewById(R.id.btnRegistrar);
        checkGuardarSesion = view.findViewById(R.id.checkGuardarSesion);

        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        dbHelper = new DBHelper(requireContext());

        btnEnviar.setOnClickListener(v -> {
            String usuario = etUsuario.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario=? AND contrasena=?", new String[]{usuario, contrasena});

            if (cursor.moveToFirst()) {
                if (checkGuardarSesion.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("usuario_guardado", usuario);
                    editor.putBoolean("sesion_activa", true);
                    editor.apply();
                }

                Toast.makeText(requireContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                etUsuario.setText("");
                etContrasena.setText("");

                // Ir al menú principal
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flcontenedor, new MainMenu())
                        .commit();
            } else {
                Toast.makeText(requireContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
            db.close();
        });

        btnRegistrar.setOnClickListener(v -> {
            String usuario = etUsuario.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("usuario", usuario);
            values.put("contrasena", contrasena);

            long resultado = db.insert("usuarios", null, values);
            db.close();

            if (resultado != -1) {
                Toast.makeText(requireContext(), "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                etUsuario.setText("");
                etContrasena.setText("");
            } else {
                Toast.makeText(requireContext(), "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
