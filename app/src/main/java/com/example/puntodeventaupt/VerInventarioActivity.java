package com.example.puntodeventaupt;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VerInventarioActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private LinearLayout layoutTabla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ver_inventario_activity); // Asegúrate que este XML existe

        layoutTabla = findViewById(R.id.layoutTabla);
        dbHelper = new DBHelper(this);

        cargarInventario();
    }

    private void cargarInventario() {
        Cursor cursor = dbHelper.getInventario();

        if (cursor != null && cursor.moveToFirst()) {
            int idxNombre = cursor.getColumnIndex("nombre");
            int idxCantidad = cursor.getColumnIndex("cantidad");
            int idxPrecioVenta = cursor.getColumnIndex("precio_venta");

            if (idxNombre == -1 || idxCantidad == -1 || idxPrecioVenta == -1) {
                String missingCols = "";
                if (idxNombre == -1) missingCols += "nombre ";
                if (idxCantidad == -1) missingCols += "cantidad ";
                if (idxPrecioVenta == -1) missingCols += "precio_venta ";
                Toast.makeText(this, "Faltan columnas en la consulta: " + missingCols.trim(), Toast.LENGTH_LONG).show();
                cursor.close();
                return;
            }

            do {
                int cantidad = cursor.getInt(idxCantidad);

                if (cantidad > 0) {  // Solo mostrar productos con cantidad estrictamente mayor a 0
                    String nombre = cursor.getString(idxNombre);
                    double precioVenta = cursor.getDouble(idxPrecioVenta);

                    TextView textView = new TextView(this);
                    textView.setText("Producto: " + nombre + " | Cantidad: " + cantidad + " | Precio: $" + precioVenta);
                    textView.setPadding(12, 12, 12, 12);
                    textView.setTextSize(16);

                    layoutTabla.addView(textView);
                }
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(this, "Inventario vacío o error al cargar", Toast.LENGTH_SHORT).show();
        }
    }
}
