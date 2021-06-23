package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

const val LOCATION_PERMISSION_REQUEST_CODE = 1

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Add a marker in Sydney and move the camera
        val bairroOuroPreto = LatLng(-19.87291, -43.97814)
        mMap.addMarker(MarkerOptions().position(bairroOuroPreto).title("Marker in Ouro Preto"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bairroOuroPreto, 15f))

        iniciarLocalizacao()
    }

    private fun iniciarLocalizacao() {
        // Checar se temos a permissão FINE ou COARSE concedida
        // Se não tiver, entrará no if
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Assim que entrar no if, solicita as permissões
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_PERMISSION_REQUEST_CODE
            )


            return
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationProvider = LocationManager.GPS_PROVIDER

        val ultimaLocalizacao = locationManager.getLastKnownLocation(locationProvider)

        Toast.makeText(
            this,
            "Lat: ${ultimaLocalizacao?.latitude} Lng: ${ultimaLocalizacao?.longitude}",
            Toast.LENGTH_LONG
        ).show()

        ultimaLocalizacao?.let {
            val latLng = LatLng(it.latitude, it.longitude)

            mMap.addMarker(
                MarkerOptions().position(latLng).title("Minha posição")
            )

            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(latLng, 15f)
            )

            mMap.addCircle(
                CircleOptions()
                    .center(latLng)
                    .radius(50.0)
                    .strokeColor(Color.RED)
                    .fillColor(Color.BLUE)
            )

            // Outras funções para desenhar no mapa (consulte documentação)
//            mMap.addPolygon()
//            mMap.addPolyline()
//            mMap.addMarker()
//            mMap.addTileOverlay()
//            mMap.addGroundOverlay()

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            iniciarLocalizacao()
        }
    }

}

