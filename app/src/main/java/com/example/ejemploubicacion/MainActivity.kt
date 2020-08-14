package com.example.ejemploubicacion

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient

class MainActivity : AppCompatActivity() {

    private val permisoFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permisoCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION

    private val CODIGO_DE_SOLICITUD_DE_PERMISO = 100

    // Variable que permite obtener ultima ubicacion
    var fusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = FusedLocationProviderClient(this)

    }

    private fun validarPermisosUbicacion():Boolean{
        val hayUbicacionPrecisa = ActivityCompat.checkSelfPermission(this,permisoFineLocation) == PackageManager.PERMISSION_GRANTED
        val hayUbicacionOrdinaria:Boolean = ActivityCompat.checkSelfPermission(this,permisoCoarseLocation) == PackageManager.PERMISSION_GRANTED

        return hayUbicacionOrdinaria && hayUbicacionPrecisa
    }

    private fun obtenerUbicacion(){

    }

    private fun pedirPermiso(){

    }


    override fun onStart() {
        super.onStart()

        if(validarPermisosUbicacion()){
            obtenerUbicacion()
        } else{
            pedirPermiso()
        }
    }

}