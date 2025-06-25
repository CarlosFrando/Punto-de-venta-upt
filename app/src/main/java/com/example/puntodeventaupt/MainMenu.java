package com.example.puntodeventaupt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.puntodeventaupt.databinding.FragmentMainMenuBinding;

public class MainMenu extends Fragment {

    private FragmentMainMenuBinding binding;
    public static final String PREFERENCES_NAME = "MiPreferencia";
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentMainMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        binding.btnAgregarProducto.setOnClickListener(v -> {
            guardarPreferencia("última_acción", "Agregar producto");
            startActivity(new Intent(getActivity(), AgregarProductoActivity.class));
        });

        binding.btnVenderProducto.setOnClickListener(v -> {
            guardarPreferencia("última_acción", "Vender producto");
            startActivity(new Intent(getActivity(), VenderProductoActivity.class));
        });

        binding.btnVerInventario.setOnClickListener(v -> {
            guardarPreferencia("última_acción", "Ver inventario");
            startActivity(new Intent(getActivity(), VerInventarioActivity.class));
        });
    }

    private void guardarPreferencia(String clave, String valor) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(clave, valor);
        editor.apply();
        Toast.makeText(getContext(), "Guardado en preferencias: " + valor, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}