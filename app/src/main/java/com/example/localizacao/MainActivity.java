package com.example.localizacao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

/*
GitHub:
 */

public class MainActivity extends AppCompatActivity {
    private static String TAG = "Localizacao";
    private FusedLocationProviderClient clienteFusedLocation;
    private GoogleApiClient clienteApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clienteFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        Log.d(TAG, "iniciando localização");
        TextView texto = findViewById(R.id.texto);
        texto.setText("iniciando localização");

        GoogleApiAvailability disponibilidadeAPI = GoogleApiAvailability.getInstance();
        int codErro = disponibilidadeAPI.isGooglePlayServicesAvailable(this);
        if (codErro != ConnectionResult.SUCCESS){
            Log.d(TAG, "Serviço não disponível");
        }else Log.d(TAG, "Serviço disponível");

        try {
            clienteFusedLocation.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            TextView texto = findViewById(R.id.texto);
                            texto.setText("sucesso");
                            // Recuperou ultima localização conhecida
                            if (location != null) {
                                // Logic to handle location object
                                Log.d(TAG, "Pegou localização");

                                String txt = "Longitude: "+location.getLongitude()+"\n"
                                             +"Latitude: "+location.getLatitude();
                                texto.setText(txt);

                            } else Log.d(TAG, "sucesso mas não pegou localização");
                        }
                    });
        } catch (SecurityException e){
            Log.d(TAG, e.getMessage());
        }
    }
}