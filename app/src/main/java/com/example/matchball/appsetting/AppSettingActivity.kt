package com.example.matchball.appsetting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matchball.R
import com.example.matchball.appsetting.language.ChangeLanguageActivity
import com.example.matchball.databinding.ActivityAppSettingBinding
import com.example.matchball.home.MainActivity

class AppSettingActivity : AppCompatActivity() {

    private lateinit var appSettingBinding: ActivityAppSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appSettingBinding = ActivityAppSettingBinding.inflate(layoutInflater)
        setContentView(appSettingBinding.root)

        initEvents()
    }

    private fun initEvents() {
        changeLanguage()
        back()
    }

    private fun changeLanguage() {
        appSettingBinding.language.setOnClickListener{
            startActivity(Intent(this, ChangeLanguageActivity::class.java))
        }
    }

    private fun back() {
        appSettingBinding.back.setOnClickListener {
            finish()
        }
    }
}