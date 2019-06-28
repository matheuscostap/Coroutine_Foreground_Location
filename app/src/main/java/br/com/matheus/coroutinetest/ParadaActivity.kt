package br.com.matheus.coroutinetest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.matheus.coroutinetest.background.LocationService
import br.com.matheus.coroutinetest.model.Parada
import br.com.matheus.coroutinetest.network.GeocodingRepositoryImpl
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_parada.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ParadaActivity : AppCompatActivity() {

    private val repository = GeocodingRepositoryImpl()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private val parada: Parada? by lazy {
        intent.extras.getSerializable("parada") as Parada
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parada)

            //Location Client
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            //Location Request
            locationRequest = LocationRequest.create().apply {
                interval = 5000
                fastestInterval = 1000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

        progress.visibility = View.VISIBLE
        tvDistancia.visibility = View.GONE

        parada?.let {
            chamarGeocoding(it)

            tvCodigo.text = it.codigo
            tvLat.text = it.latitude.toString()
            tvLon.text = it.longitude.toString()

            val adapter = LinhaAdapter(this, it.linhas)
            rvLinhas.layoutManager = LinearLayoutManager(this)
            rvLinhas.adapter = adapter
        }

        btnGeoface.setOnClickListener {
            iniciarServico()
        }

    }


    /*private fun distanciaDoisPontos(){
        val permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionCheck == PackageManager.PERMISSION_GRANTED){

            //Calcula a distância continuamente
            fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback(){
                override fun onLocationResult(result: LocationResult?) {
                    result?.let {
                        for (location in it.locations){
                            var distance = FloatArray(1)

                            Location.distanceBetween(location.latitude,
                                location.longitude,
                                parada?.latitude ?: 0.0,
                                parada?.longitude ?: 0.0,
                                distance)

                            tvDistancia.visibility = View.VISIBLE
                            tvDistancia.text = distance[0].toString()
                        }
                    }
                }
            }, null)

            //Calcula distância uma única vez

            /*fusedLocationClient.lastLocation
                .addOnSuccessListener {location ->
                    var distance = FloatArray(1)

                    Location.distanceBetween(location.latitude,
                                             location.longitude,
                                  parada?.latitude ?: 0.0,
                                 parada?.longitude ?: 0.0,
                                             distance)

                    Toast.makeText(this, "Distância parada: ${distance[0]}",Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {error ->
                    Toast.makeText(this, "Erro",Toast.LENGTH_LONG).show()
                    Log.e("ParadaActivity","Erro -> ${error.message}")
                    error.printStackTrace()
                }*/
        }
    }*/


    private fun iniciarServico(){
        parada?.let {
            val intent = Intent(this@ParadaActivity, LocationService::class.java)
            intent.putExtra("parada",parada)
            startService(intent)
        }
    }


    private fun chamarGeocoding(parada: Parada){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val geoResult = repository.retornarEndereco("jsonv2",parada.latitude.toString(),parada.longitude.toString())

                withContext(Dispatchers.Main){
                    geoResult?.let {
                        tvEndereco.text = it.display_name
                    }

                    progress.visibility = View.GONE

                    pedirPermissao()
                }
            }catch (e: Throwable){
                e.printStackTrace()
                runOnUiThread {
                    progress.visibility = View.GONE
                    Toast.makeText(this@ParadaActivity,"Erro!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun pedirPermissao(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
    }
}
