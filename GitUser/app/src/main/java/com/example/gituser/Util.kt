package com.example.gituser

import android.content.Context
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class Util {
    companion object {
        const val TOKEN = "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"

        fun CircleImageView.setImageGlide(context: Context, url: String) {
            Glide
                .with(context)
                .load(url)
                .placeholder(R.drawable.profile_placeholder)
                .into(this)
        }

        fun TextView.setAndVisible(text: String?) {
            if (!text.isNullOrBlank()) {
                this.text = text
                this.visibility = View.VISIBLE
            }
        }
    }
}