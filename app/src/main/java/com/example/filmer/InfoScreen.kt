package com.example.filmer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.sax.Element
import mehdi.sakout.aboutpage.AboutPage

class InfoScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_screen)
        val versionElement = mehdi.sakout.aboutpage.Element()
        versionElement.title = "Version 1.0"
        val adsElement = mehdi.sakout.aboutpage.Element()
        adsElement.title = "Advertise with us"

        val aboutPage = AboutPage(this, R.style.WidgetAppAboutPage)
            .isRTL(false)
            .setDescription("Bla - Bla - Bla")
            .setImage(R.drawable.logo)
            .addItem(versionElement)
            .addItem(adsElement)
            .addGroup("Connect with us")
            .addEmail("example@gmail.com")
            .addFacebook("example")
            .addTwitter("example")
            .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
            .addPlayStore("com.ideashower.readitlater.pro")
            .addInstagram("example")
            .addGitHub("example")
            .create()
        setContentView(aboutPage)
    }
}