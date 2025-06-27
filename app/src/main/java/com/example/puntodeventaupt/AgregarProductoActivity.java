package com.example.puntodeventaupt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AgregarProductoActivity extends AppCompatActivity {

    EditText editTextFolio, editTextNombre, editTextCantidad, editTextPrecioCompra, editTextPrecioVenta;
    Button btnGuardar, btnCancelar, btnEscanearCodigo;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_agregar_producto_activity);

        editTextFolio = findViewById(R.id.editTextFolio);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextCantidad = findViewById(R.id.editTextCantidad);
        editTextPrecioCompra = findViewById(R.id.editTextPrecioCompra);
        editTextPrecioVenta = findViewById(R.id.editTextPrecioVenta);
        btnEscanearCodigo = findViewById(R.id.btnEscanearCodigo);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        dbHelper = new DBHelper(this);

        btnEscanearCodigo.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Escanea el código de barras");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        });

        btnGuardar.setOnClickListener(v -> {
            String folio = editTextFolio.getText().toString().trim();
            String nombre = editTextNombre.getText().toString().trim();
            String cantidadStr = editTextCantidad.getText().toString().trim();
            String precioCompraStr = editTextPrecioCompra.getText().toString().trim();
            String precioVentaStr = editTextPrecioVenta.getText().toString().trim();

            if (nombre.isEmpty() || cantidadStr.isEmpty() || precioCompraStr.isEmpty() || precioVentaStr.isEmpty()) {
                Toast.makeText(AgregarProductoActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int cantidad = Integer.parseInt(cantidadStr);
                double precioCompra = Double.parseDouble(precioCompraStr);
                double precioVenta = Double.parseDouble(precioVentaStr);

                Producto producto = new Producto(folio, nombre, cantidad, precioCompra, precioVenta);

                boolean exito = dbHelper.insertarProducto(producto);

                if (exito) {
                    Toast.makeText(AgregarProductoActivity.this, "Producto guardado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                } else {
                    Toast.makeText(AgregarProductoActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(AgregarProductoActivity.this, "Datos numéricos inválidos", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelar.setOnClickListener(v -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show();
            } else {
                editTextFolio.setText(result.getContents());
                Toast.makeText(this, "Código escaneado: " + result.getContents(), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void limpiarCampos() {
        editTextFolio.setText("");
        editTextNombre.setText("");
        editTextCantidad.setText("");
        editTextPrecioCompra.setText("");
        editTextPrecioVenta.setText("");
    }
}
