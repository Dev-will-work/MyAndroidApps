package com.example.volleysample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val queue = Volley.newRequestQueue(applicationContext)
        val url = "https://www.google.com/search?q=Russia"
        val btnOk = findViewById<Button>(R.id.bOk)
        val textView = findViewById<TextView>(R.id.view)

        btnOk.setOnClickListener {
            val StringRequest = StringRequest (
                Request.Method.GET, url, Response.Listener<String> {
                response -> textView.text = "Response is: ${response.substring(0, 500)}"
            },
                Response.ErrorListener {
                    textView.text = "That didn't work"
                }
            )
            queue.add(StringRequest)
        }
    }



}