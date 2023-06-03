package com.practicum.playlistmaker.settings.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor

class SettingsInteractorImpl(
    private val context: Context,
    private val shareLink: String,
    private val agreementLink: String,
    private val email: String,
    private val subject: String,
    private val text: String,
) : SettingsInteractor {
    override fun shareBtn() {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plane"
        share.putExtra(Intent.EXTRA_TEXT, shareLink)
        context.startActivity(share)
    }

    override fun supportBtn() {
        val emails = arrayOf<String>(email)
        val emailSupport = Intent(Intent.ACTION_SENDTO)
        emailSupport.data = Uri.parse("mailto:")
        emailSupport.putExtra(Intent.EXTRA_EMAIL, emails)
        emailSupport.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailSupport.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(emailSupport)
    }

    override fun agreementBtn() {
        val agreement =
            Intent(Intent.ACTION_VIEW, Uri.parse(agreementLink))
        context.startActivity(agreement)
    }
}