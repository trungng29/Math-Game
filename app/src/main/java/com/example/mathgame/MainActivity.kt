package com.example.mathgame

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mathgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainBinding.btnAdd.setOnClickListener {
            val intent2 = Intent(this@MainActivity, GameActivity::class.java)
            intent2.putExtra("title", "Addition")
            startActivity(intent2)
        }

        mainBinding.btnSub.setOnClickListener {
            val intent3 = Intent(this@MainActivity, GameActivity::class.java)
            intent3.putExtra("title", "Subtraction")
            startActivity(intent3)
        }

        mainBinding.btnMul.setOnClickListener {
            val intent23 = Intent(this@MainActivity, GameActivity::class.java)
            intent23.putExtra("title", "Multiplication")
            startActivity(intent23)
        }

    }
}