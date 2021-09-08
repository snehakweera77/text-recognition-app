package com.example.textrecognition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var captureBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        captureBtn.setOnClickListener {
            var intent = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
        }

    }
}