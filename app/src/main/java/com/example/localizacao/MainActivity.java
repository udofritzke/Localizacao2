package com.example.localizacao;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/*
GitHub: https://github.com/udofritzke/Localizacao2
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Localizacao";
    private FusedLocationProviderClient clienteFusedLocation;
    static TextView texto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button busca = (Button) findViewById(R.id.botao_busca);
        busca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscaCoordenadas();
            }
        });
        texto = findViewById(R.id.texto);

        // verificação das permissões concedidas
        Log.d(TAG, "Testando permissões concedidas ...");
        TextView texto = findViewById(R.id.texto);
        texto.setText("Testando permissões concedidas");
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                texto.setText("Permissão localização precisa concedida!");
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                texto.setText("Permissão localização aproximada concedida!");
                            } else {
                                texto.setText("Sem permissão de localização!");
                            }
                        }
                );
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

        void buscaCoordenadas() {

        // Obten provedor de localização combinada
        clienteFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        Log.d(TAG, "iniciando localização");
        TextView texto = findViewById(R.id.texto);
        texto.setText("iniciando localização");

        GoogleApiAvailability disponibilidadeAPI = GoogleApiAvailability.getInstance();
        int codErro = disponibilidadeAPI.isGooglePlayServicesAvailable(this);
        if (codErro != ConnectionResult.SUCCESS) {
            Log.d(TAG, "Serviço não disponível");
        } else Log.d(TAG, "Serviço disponível");

        try {
            clienteFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    TextView texto = findViewById(R.id.texto);
                    texto.setText("sucesso");
                    // Recuperou ultima localização conhecida
                    if (location != null) {
                        // Logic to handle location object
                        Log.d(TAG, "Pegou localização");

                        String txt = "Longitude: " + location.getLongitude() + "\n" + "Latitude: " + location.getLatitude();
                        texto.setText(txt);

                    } else Log.d(TAG, "sucesso mas não pegou localização");
                }
            });
        } catch (SecurityException e) {
            Log.d(TAG, e.getMessage());
        }
    }
}