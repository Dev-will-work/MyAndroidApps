package com.example.myblescanner

import android.Manifest
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.RequiresApi
import android.content.pm.PackageManager
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    val my_adapter = ListDevicesAdapter(mutableListOf())
    lateinit var start: Button
    lateinit var stop: Button
    private val MY_PERMISSION_lOCATION_REQUEST_NUMBER = 1

    private val bluetoothLeScanner: BluetoothLeScanner
        get() {
            val bluetoothManager =
                applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val bluetoothAdapter = bluetoothManager.adapter
            return bluetoothAdapter.bluetoothLeScanner
        }

    private val bleScanner = object : ScanCallback() {
        @RequiresApi(Build.VERSION_CODES.R)
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            Log.d("MainActivity","onScanResult: ${result?.device?.address} - ${result?.device?.name}")
            val new_device = DeviceInfo(result?.device?.address!!, result.device?.name ?: "unnamed")
            if (!my_adapter.data.contains(new_device)) {
                my_adapter.data.add(new_device)
                my_adapter.notifyItemInserted(my_adapter.itemCount + 1)
            }
        }
        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            Log.d("MainActivity","onBatchScanResults:${results.toString()}")
            Toast.makeText(this@MainActivity, "onBatchScanResults:${results.toString()}", LENGTH_SHORT).show()
        }
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d("MainActivity", "onScanFailed: $errorCode")
            when (errorCode) {
                1 -> Toast.makeText(this@MainActivity, "Error, possibly not enough permissions or scan is already executed", LENGTH_SHORT).show()
                else -> Toast.makeText(this@MainActivity, "Scan failed, error code: $errorCode", LENGTH_SHORT).show()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
           ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) ) {
                Toast.makeText(this@MainActivity, "Access to your location is required for bluetooth LE beacons search, please enable it in phone settings", LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), MY_PERMISSION_lOCATION_REQUEST_NUMBER)
            }
        }

        start = findViewById(R.id.start)
        stop = findViewById(R.id.stop)

        start.setOnClickListener {
            bluetoothLeScanner.startScan(bleScanner)
        }
        stop.setOnClickListener {
            bluetoothLeScanner.stopScan(bleScanner)
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = my_adapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSION_lOCATION_REQUEST_NUMBER -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    Toast.makeText(this@MainActivity, "Permissions granted", LENGTH_SHORT).show()
                } else {
                    // no permissions granted.
                    Toast.makeText(this@MainActivity, "No permissions!", LENGTH_SHORT).show()
                }
                return
            }
        }
    }

}