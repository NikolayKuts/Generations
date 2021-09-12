package com.example.generations.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.generations.R
import com.example.generations.presentation.fragments.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, MainFragment())
                .commit()
        }
    }
}