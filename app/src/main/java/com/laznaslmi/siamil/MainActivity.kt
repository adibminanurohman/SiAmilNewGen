package com.laznaslmi.siamil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ambil data yang dikirim dari LoginActivity
        val nip = intent.getStringExtra("nip")
        val nama = intent.getStringExtra("nama")
        val kotaLayanan = intent.getStringExtra("kotaLayanan")
        val dep = intent.getStringExtra("dep")

    }
}
