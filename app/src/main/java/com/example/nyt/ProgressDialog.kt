package com.example.nyt

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.progressdialog.view.*


class ProgressDialog(context: Context, message: String?) {

    private val view = LayoutInflater.from(context).inflate(R.layout.progressdialog, null)
    private var alertDialog: AlertDialog

    init {

        if (!message.isNullOrBlank())
            view.progress_message.text = message

        alertDialog = AlertDialog
            .Builder(context)
            .setCancelable(false)
            .setView(view)
            .create()
    }

    fun showDialog() {
        alertDialog.show()
    }

    fun dismissDialog() {
        alertDialog.dismiss()
    }
}