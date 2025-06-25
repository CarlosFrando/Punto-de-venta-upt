package com.example.puntodeventaupt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.puntodeventaupt.databinding.FragmentAgregarProductoActivityBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AgregarProductoActivity extends Fragment {

    private FragmentAgregarProductoActivityBinding binding;
    private ActivityResultLauncher<Intent> scanLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAgregarProductoActivityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lanzador del escáner de código de barras
        scanLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    IntentResult intentResult = IntentIntegrator.parseActivityResult(
                            result.getResultCode(), result.getData()
                    );
                    if (intentResult != null && intentResult.getContents() != null) {
                        binding.editTextFolio.setText(intentResult.getContents());
                    } else {
                        Toast.makeText(getContext(), "Escaneo cancelado", Toast.LENGTH_SHORT).show();
                    }
                });

        // Botón para iniciar escaneo
        binding.btnEscanearCodigo.setOnClickListener(v -> {
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
            integrator.setPrompt("Escanea el código de barras");
            integrator.setBeepEnabled(true);
            integrator.setOrientationLocked(true);
            scanLauncher.launch(integrator.createScanIntent());
        });

        // Botón para guardar el producto
        binding.btnGuardar.setOnClickListener(v -> {
            String folio = binding.editTextFolio.getText().toString().trim();
            String nombre = binding.editTextNombre.getText().toString().trim();
            String cantidadStr = binding.editTextCantidad.getText().toString().trim();
            String precioCompraStr = binding.editTextPrecioCompra.getText().toString().trim();
            String precioVentaStr = binding.editTextPrecioVenta.getText().toString().trim();

            // Validación de campos vacíos
            if (folio.isEmpty() || nombre.isEmpty() || cantidadStr.isEmpty()
                    || precioCompraStr.isEmpty() || precioVentaStr.isEmpty()) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int cantidad = Integer.parseInt(cantidadStr);
                double precioCompra = Double.parseDouble(precioCompraStr);
                double precioVenta = Double.parseDouble(precioVentaStr);

                if (cantidad <= 0 || precioCompra < 0 || precioVenta < 0) {
                    Toast.makeText(getContext(), "Valores inválidos: asegúrate de ingresar números positivos", Toast.LENGTH_SHORT).show();
                    return;
                }

                Producto producto = new Producto(folio, nombre, cantidad, precioCompra, precioVenta);
                DBHelper dbHelper = new DBHelper(getContext());

                boolean insertado = dbHelper.insertarProducto(producto);
                if (insertado) {
                    Toast.makeText(getContext(), "Producto guardado exitosamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                } else {
                    Toast.makeText(getContext(), "Error al guardar el producto", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Error: asegúrate de ingresar números válidos", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón para cancelar
        binding.btnCancelar.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    // Limpia todos los campos después de guardar
    private void limpiarCampos() {
        binding.editTextFolio.setText("");
        binding.editTextNombre.setText("");
        binding.editTextCantidad.setText("");
        binding.editTextPrecioCompra.setText("");
        binding.editTextPrecioVenta.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}