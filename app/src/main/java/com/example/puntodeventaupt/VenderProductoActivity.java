package com.example.puntodeventaupt;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.puntodeventaupt.databinding.FragmentVenderProductoActivityBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

class FragmentVenderProductoActivity extends Fragment {

    private List<ItemVenta> carrito = new ArrayList<>();
    private ActivityResultLauncher<Intent> scanLauncher;
    private FragmentVenderProductoActivityBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVenderProductoActivityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scanLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    IntentResult intentResult = IntentIntegrator.parseActivityResult(
                            result.getResultCode(), result.getData()
                    );
                    if (intentResult != null && intentResult.getContents() != null) {
                        String folio = intentResult.getContents();
                        binding.textViewFolio.setText("Folio: " + folio);
                        buscarProductoPorFolio(folio);
                    } else {
                        Toast.makeText(getContext(), "Escaneo cancelado", Toast.LENGTH_SHORT).show();
                    }
                });

        binding.btnEscanearCodigo.setOnClickListener(v -> {
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
            integrator.setPrompt("Escanea el código del producto");
            integrator.setBeepEnabled(true);
            integrator.setOrientationLocked(true);
            scanLauncher.launch(integrator.createScanIntent());
        });

        binding.btnAgregar.setOnClickListener(v -> {
            String folio = binding.textViewFolio.getText().toString().replace("Folio: ", "").trim();
            String nombre = binding.textViewNombre.getText().toString().replace("Nombre: ", "").trim();
            String cantidadStr = binding.editTextCantidad.getText().toString().trim();
            String precioStr = binding.textViewPrecioVenta.getText().toString().replace("Precio: $", "").trim();

            if (folio.isEmpty() || nombre.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int cantidad = Integer.parseInt(cantidadStr);
            double precioUnitario = Double.parseDouble(precioStr);

            carrito.add(new ItemVenta(folio, nombre, cantidad, precioUnitario));
            Toast.makeText(getContext(), "Producto agregado al carrito", Toast.LENGTH_SHORT).show();

            binding.editTextCantidad.setText("");
        });

        binding.btnCobrar.setOnClickListener(v ->
                Toast.makeText(getContext(), "Proceso de cobro (simulado)", Toast.LENGTH_SHORT).show()
        );
    }

    private void buscarProductoPorFolio(String folio) {
        DBHelper dbHelper = new DBHelper(getContext());
        Cursor cursor = dbHelper.getProductoPorFolio(folio);

        if (cursor != null && cursor.moveToFirst()) {
            int colNombre = cursor.getColumnIndex("nombre");
            int colPrecioVenta = cursor.getColumnIndex("precio_venta");

            if (colNombre == -1 || colPrecioVenta == -1) {
                Toast.makeText(getContext(), "Error: columnas no encontradas en la base de datos", Toast.LENGTH_SHORT).show();
                cursor.close();
                return;
            }

            String nombre = cursor.getString(colNombre);
            double precioVenta = cursor.getDouble(colPrecioVenta);

            binding.textViewNombre.setText("Nombre: " + nombre);
            binding.textViewPrecioVenta.setText("Precio: $" + precioVenta);

            cursor.close();
        } else {
            Toast.makeText(getContext(), "Producto no encontrado", Toast.LENGTH_SHORT).show();
            binding.textViewNombre.setText("Nombre: -");
            binding.textViewPrecioVenta.setText("Precio: $0.00");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
