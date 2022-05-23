package com.example.mapsapplication

import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mapsapplication.databinding.ActivityMapsBinding
import android.location.Geocoder
import android.widget.AutoCompleteTextView
import java.util.*
import android.widget.ArrayAdapter
import android.widget.Toast


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var input: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        input = findViewById(R.id.inputField)
        val hseAddresses = resources.getStringArray(R.array.hse_campuses)
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hseAddresses)
        input.setAdapter(adapter)
        input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchAddress = input.text.toString().replace("\\s".toRegex(), "")
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address> = geocoder.getFromLocationName(searchAddress, 1)
                if (addresses.isNotEmpty()) {
                    val latitude: Double = addresses[0].latitude
                    val longitude: Double = addresses[0].longitude
                    val hse = LatLng(latitude, longitude)

                    val validatedAddress = hseAddresses.filter { str -> str.replace("\\s".toRegex(), "").contains(searchAddress, ignoreCase = true) }
                    if (validatedAddress.isNotEmpty()) {
                    mMap.addMarker(MarkerOptions().position(hse).title(getString(R.string.HSE_marker, validatedAddress[0])))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hse, 17f))
                    } else {
                        Toast.makeText(this, getString(R.string.HSE_marker_error), Toast.LENGTH_LONG).show()
                    }
                }

            }
            return@setOnEditorActionListener true
        }
        
        

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

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title(getString(R.string.Sydney_marker)))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}