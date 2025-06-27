package com.example.puntodeventaupt;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class VerInventarioActivity extends Fragment {

    private DBHelper dbHelper;
    private LinearLayout layoutTabla;


    public VerInventarioActivity() {
        // Constructor vacío
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_inventario_activity, container, false);
        layoutTabla = view.findViewById(R.id.layoutTabla);
        dbHelper = new DBHelper(requireContext());
        cargarInventario();
        return view;
    }

    private void cargarInventario() {
        Cursor cursor = dbHelper.getInventario(); // Método que debe traer todos los productos

        if (cursor != null && cursor.moveToFirst()) {
            // Obtener índices de columnas, validar que existan
            int idxNombre = cursor.getColumnIndex("nombre");
            int idxCantidad = cursor.getColumnIndex("cantidad");
            int idxPrecioVenta = cursor.getColumnIndex("precio_venta");

            if (idxNombre == -1 || idxCantidad == -1 || idxPrecioVenta == -1) {
                String missingCols = "";
                if (idxNombre == -1) missingCols += "nombre ";
                if (idxCantidad == -1) missingCols += "cantidad ";
                if (idxPrecioVenta == -1) missingCols += "precio_venta ";
                Toast.makeText(requireContext(), "Faltan columnas en la consulta: " + missingCols.trim(), Toast.LENGTH_LONG).show();
                cursor.close();
                return;
            }

            do {
                int cantidad = cursor.getInt(idxCantidad);
                if (cantidad <= 0) continue;

                String nombre = cursor.getString(idxNombre);
                double precioVenta = cursor.getDouble(idxPrecioVenta);

                TextView textView = new TextView(requireContext());
                textView.setText("Producto: " + nombre + " | Cantidad: " + cantidad + " | Precio: $" + precioVenta);
                textView.setPadding(12, 12, 12, 12);
                textView.setTextSize(16);

                layoutTabla.addView(textView);

            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(requireContext(), "Inventario vacío o error al cargar", Toast.LENGTH_SHORT).show();
        }
    }

}
