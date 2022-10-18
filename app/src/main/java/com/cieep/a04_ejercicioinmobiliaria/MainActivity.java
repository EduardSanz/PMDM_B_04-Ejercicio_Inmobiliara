package com.cieep.a04_ejercicioinmobiliaria;

import android.content.Intent;
import android.os.Bundle;

import com.cieep.a04_ejercicioinmobiliaria.configuraciones.Constantes;
import com.cieep.a04_ejercicioinmobiliaria.modelos.Inmueble;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;


import com.cieep.a04_ejercicioinmobiliaria.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Inmueble> inmueblesList;
    private ActivityMainBinding binding;
    private ActivityResultLauncher<Intent> addInmuebleLauncher;
    private ActivityResultLauncher<Intent> editInmuebleLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inmueblesList = new ArrayList<>();

        setSupportActionBar(binding.toolbar);

        inicializaLaunchers();


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddInmuebleActivity.class);
               addInmuebleLauncher.launch(intent);
            }
        });
    }

    private void inicializaLaunchers() {
        addInmuebleLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null) {
                                if (result.getData().getExtras() != null) {
                                    if (result.getData().getExtras().getSerializable(Constantes.INMUEBLE) != null) {
                                        // Estraigo el inmueble
                                        Inmueble inmueble = (Inmueble) result.getData().getExtras().getSerializable(Constantes.INMUEBLE);
                                        // Agrego el inmueble
                                        inmueblesList.add(inmueble);
                                        // Muestro los inmuebles
                                        muestraInmueblesContenido();
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "NO HAY DATOS", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "NO HAY BUNDLE EN EL INTENT", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(MainActivity.this, "NO HAY INTENT", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(MainActivity.this, "VENTANA CANCELADA", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        editInmuebleLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null) {
                                // Si existe INMUEBLE -> ACTUALIZO
                                if (result.getData().getExtras() != null && result.getData().getExtras().getSerializable(Constantes.INMUEBLE) != null){
                                    Inmueble inmueble = (Inmueble) result.getData().getExtras().getSerializable(Constantes.INMUEBLE);
                                    int posicion = result.getData().getExtras().getInt(Constantes.POSICION);
                                    inmueblesList.set(posicion, inmueble);
                                    muestraInmueblesContenido();
                                }
                                else {
                                    // SI NO EXISTE INMUEBLE -> ELIMINO
                                    if (result.getData().getExtras() != null) {
                                        int posicion = result.getData().getExtras().getInt(Constantes.POSICION);
                                        inmueblesList.remove(posicion);
                                        muestraInmueblesContenido();
                                    }
                                }
                            }
                            else {

                            }
                        }
                    }
                }
        );
    }

    private void muestraInmueblesContenido() {
        binding.contentMain.contenedor.removeAllViews();

        for (int i = 0; i < inmueblesList.size(); i++) {
            Inmueble inmueble = inmueblesList.get(i);

            View inmuebleView = LayoutInflater.from(MainActivity.this).inflate(R.layout.inmueble_view_model,null);
            TextView lblDireccion = inmuebleView.findViewById(R.id.lblDireccionInmuebleModel);
            TextView lblNumero = inmuebleView.findViewById(R.id.lblNumeroInmuebleModel);
            TextView lblCiudad = inmuebleView.findViewById(R.id.lblCiudadInmuebleModel);
            TextView lblProvincia = inmuebleView.findViewById(R.id.lblProvinciaInmuebleModel);
            RatingBar rbValoracion = inmuebleView.findViewById(R.id.rbValoracionInmuebleModel);

            int finalI = i;
            inmuebleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, EditInmuebleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constantes.INMUEBLE, inmueble);
                    bundle.putInt(Constantes.POSICION, finalI);
                    intent.putExtras(bundle);
                    editInmuebleLauncher.launch(intent);
                }
            });

            lblDireccion.setText(inmueble.getDireccion());
            lblNumero.setText(String.valueOf(inmueble.getNumero()));
            lblCiudad.setText(inmueble.getCiudad());
            lblProvincia.setText(inmueble.getProvincia());
            rbValoracion.setRating(inmueble.getValoracion());

            binding.contentMain.contenedor.addView(inmuebleView);
        }

    }


}