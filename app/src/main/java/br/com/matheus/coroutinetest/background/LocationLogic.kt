package br.com.matheus.coroutinetest.background

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.core.content.ContextCompat
import br.com.matheus.coroutinetest.model.Parada
import com.google.android.gms.location.*
import java.io.Serializable

class LocationLogic(val ctx: Context, val parada: Parada,val onFinish: () -> Unit ): Serializable {

    private val fusedLocationClient: FusedLocationProviderClient
    private val locationRequest: LocationRequest

    private val locationCallback: LocationCallback

    init {
        //Location Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)

        //Location Request
        locationRequest = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(result: LocationResult?) {
                Log.i("LocationLogic", "Distancia dois pontos -> onLocationResult()")
                result?.let {
                    for (location in it.locations){
                        var distance = FloatArray(1)

                        Location.distanceBetween(location.latitude,
                            location.longitude,
                            parada?.latitude ?: 0.0,
                            parada?.longitude ?: 0.0,
                            distance)

                        if (avisarParada(distance[0])){
                            fusedLocationClient.removeLocationUpdates(this)
                            onFinish()
                        }

                    }
                }
            }
        }
    }


    fun distanciaDoisPontos(looper: Looper){
        Log.i("LocationLogic", "Distancia dois pontos")
        val permissionCheck = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionCheck == PackageManager.PERMISSION_GRANTED){
            Log.i("LocationLogic", "Distancia dois pontos -> permissao ok")

            //Calcula a distância continuamente
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, looper)
        }
    }


    private fun avisarParada(distancia: Float): Boolean{
        val vibrator = ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (distancia <= 150f){
            Log.i("LocationLogic", "Distância < 900m")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                //deprecated in API 26
                vibrator.vibrate(500)
                vibrator.vibrate(500)
            }

            return true
        }else{
            Log.i("LocationLogic", "Distância > 900m")
            return false
        }
    }



}