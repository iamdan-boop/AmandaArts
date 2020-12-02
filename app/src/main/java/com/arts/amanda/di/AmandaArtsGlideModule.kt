package com.arts.amanda.di

import android.content.Context
import com.arts.amanda.R
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey


@GlideModule
class AmandaArtsGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.apply {
            RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .signature(ObjectKey(System.currentTimeMillis().toShort()))
        }
    }
}