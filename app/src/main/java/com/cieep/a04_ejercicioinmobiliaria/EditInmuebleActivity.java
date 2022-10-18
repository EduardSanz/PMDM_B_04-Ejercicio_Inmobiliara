package com.cieep.a04_ejercicioinmobiliaria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cieep.a04_ejercicioinmobiliaria.configuraciones.Constantes;
import com.cieep.a04_ejercicioinmobiliaria.databinding.ActivityEditInmuebleBinding;
import com.cieep.a04_ejercicioinmobiliaria.modelos.Inmueble;

import java.lang.invoke.ConstantCallSite;

public class EditInmuebleActivity extends AppCompatActivity {

    private ActivityEditInmuebleBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditInmuebleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // FASE 1 -> Obtener el Objeto a Editar
        Intent intentMain = getIntent();
        Bundle bundleMain = intentMain.getExtras();
        Inmueble inmuebleEdit = (Inmueble) bundleMain.getSerializable(Constantes.INMUEBLE);
        Log.d("OBJETO", inmuebleEdit.toString());

        // FASE 2 -> Mostrar los datos del Objeto en la interfaz
        binding.txtDireccionEditInmueble.setText(inmuebleEdit.getDireccion());
        binding.txtNumeroEditInmueble.setText(String.valueOf(inmuebleEdit.getNumero()));
        binding.txtCPEditInmueble.setText(inmuebleEdit.getCp());
        binding.txtCiudadEditInmueble.setText(inmuebleEdit.getCiudad());
        binding.txtProvinciaEditInmueble.setText(inmuebleEdit.getProvincia());
        binding.rbValoracionEditInmueble.setRating(inmuebleEdit.getValoracion());

        binding.btnUpdateEditInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Inmueble inmuebleUpdated = crearInmueble();
                if (inmuebleUpdated != null) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    int posicion = bundleMain.getInt(Constantes.POSICION);
                    bundle.putInt(Constantes.POSICION, posicion);
                    bundle.putSerializable(Constantes.INMUEBLE, inmuebleUpdated);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        binding.btnDeleteEditInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int posicion = bundleMain.getInt(Constantes.POSICION);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(Constantes.POSICION, posicion);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private Inmueble crearInmueble() {
        if (
                binding.txtDireccionEditInmueble.getText().toString().isEmpty() ||
                        binding.txtNumeroEditInmueble.getText().toString().isEmpty() ||
                        binding.txtCPEditInmueble.getText().toString().isEmpty() ||
                        binding.txtProvinciaEditInmueble.getText().toString().isEmpty() ||
                        binding.txtCiudadEditInmueble.getText().toString().isEmpty()
        )
            return null;

        return new Inmueble(
                binding.txtDireccionEditInmueble.getText().toString(),
                Integer.parseInt(binding.txtNumeroEditInmueble.getText().toString()),
                binding.txtCiudadEditInmueble.getText().toString(),
                binding.txtProvinciaEditInmueble.getText().toString(),
                binding.txtCPEditInmueble.getText().toString(),
                binding.rbValoracionEditInmueble.getRating()
        );
    }
}