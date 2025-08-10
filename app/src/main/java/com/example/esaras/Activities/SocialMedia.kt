package com.example.esaras.Activities
import android.content.Context
import android.content.Intent
import android.net.Uri

object SocialMedia {

    fun openFacebook(context: Context) {
        openSocialMediaUrl(context, "https://www.facebook.com/esarasaajeevika/")
    }

    fun openTwitter(context: Context) {
        openSocialMediaUrl(context, "https://x.com/esarasaajeevika")
    }

    fun openInstagram(context: Context) {
        openSocialMediaUrl(context, "https://www.instagram.com/esarasaajeevika?igsh=MXY4bXhpYjF6dGtwMw==")
    }

    private fun openSocialMediaUrl(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}


