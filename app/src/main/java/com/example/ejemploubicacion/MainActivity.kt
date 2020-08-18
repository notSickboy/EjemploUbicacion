package com.example.ejemploubicacion

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.tasks.OnSuccessListener

class MainActivity : AppCompatActivity() {

    private val permisoFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permisoCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION

    private val CODIGO_DE_SOLICITUD_DE_PERMISO = 100

    // Variable que permite obtener ultima ubicacion
    var fusedLocationClient: FusedLocationProviderClient? = null

    // Variable que hace una petición de ubicación
    var locationRequest:LocationRequest? = null

    var callback: LocationCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = FusedLocationProviderClient(this)
        iniciar_el_location_request()
    }

    private fun iniciar_el_location_request(){
        locationRequest = LocationRequest()
        locationRequest?.interval = 10000
        locationRequest?.fastestInterval = 50000
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun validarPermisosUbicacion():Boolean{
        val hayUbicacionPrecisa = ActivityCompat.checkSelfPermission(this,permisoFineLocation) == PackageManager.PERMISSION_GRANTED
        val hayUbicacionOrdinaria:Boolean = ActivityCompat.checkSelfPermission(this,permisoCoarseLocation) == PackageManager.PERMISSION_GRANTED

        return hayUbicacionOrdinaria && hayUbicacionPrecisa
    }

    @SuppressLint("MissingPermission")
    private fun obtenerUbicacion(){
        validarPermisosUbicacion()

        /* fusedLocationClient?.lastLocation?.addOnSuccessListener(this,object: OnSuccessListener<Location>{
            override fun onSuccess(location: Location?) {
            if(location != null){
                Toast.makeText(applicationContext, location?.latitude.toString() + "-" + location?.longitude.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }) */

        callback = object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                for(ubicacion in locationResult?.locations!!){
                    Toast.makeText(applicationContext, ubicacion.latitude.toString() + "," + ubicacion.longitude.toString(), Toast.LENGTH_LONG).show()
                }

            }
        }

        fusedLocationClient?.requestLocationUpdates(locationRequest,callback, null)
    }

    private fun pedirPermiso(){
        val mostrar_contexto_al_usuario = ActivityCompat.shouldShowRequestPermissionRationale(this,permisoFineLocation)
        if (mostrar_contexto_al_usuario){
            solicitarPermiso()
        } else {
            solicitarPermiso()
        }

    }

    fun solicitarPermiso(){
        requestPermissions(arrayOf(permisoCoarseLocation,permisoFineLocation),CODIGO_DE_SOLICITUD_DE_PERMISO)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CODIGO_DE_SOLICITUD_DE_PERMISO ->{
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Obtener ubicación
                    obtenerUbicacion()
                } else{
                    Toast.makeText(this,"No diste permisos para acceder a la ubicación", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun detener_actualizacion_de_ubicacion(){
        fusedLocationClient?.removeLocationUpdates(callback)
    }

    override fun onStart() {
        super.onStart()

        if(validarPermisosUbicacion()){
            obtenerUbicacion()
        } else{
            pedirPermiso()
        }
    }

    override fun onPause() {
        super.onPause()
        detener_actualizacion_de_ubicacion()
    }

}