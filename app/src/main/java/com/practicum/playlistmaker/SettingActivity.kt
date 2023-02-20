package com.practicum.playlistmaker

import android.content.Intent
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val goBackBtn = findViewById<ImageButton>(R.id.goBackBtn)

        goBackBtn.setOnClickListener {
            val goBackIntent = Intent(this, MainActivity::class.java)
            startActivity(goBackIntent)
            finish()
        }
        val nightModeSwitch =findViewById<SwitchCompat>(R.id.NightModeSwitch)
        nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val shareBtn = findViewById<Button>(R.id.shareBtn)
        shareBtn.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plane"
            share.putExtra(Intent.EXTRA_TEXT, getString(R.string.AndroidDevLink))
            startActivity(share)
        }
        val supportBtn = findViewById<Button>(R.id.supportBtn)
        var emails = arrayOf<String>(getString(R.string.recipientEmail))
        supportBtn.setOnClickListener {
            val emailSupport = Intent(Intent.ACTION_SENDTO)
            emailSupport.data = Uri.parse("mailto:")
            emailSupport.putExtra(Intent.EXTRA_EMAIL,emails)
            emailSupport.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject))
            emailSupport.putExtra(Intent.EXTRA_TEXT, getString(R.string.message))
            startActivity(emailSupport)
        }
        val agreementBtn = findViewById<Button>(R.id.agreementBtn)
        agreementBtn.setOnClickListener {
            val agreement = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.userAgreementLink)))
            startActivity(agreement)
        }
    }
}