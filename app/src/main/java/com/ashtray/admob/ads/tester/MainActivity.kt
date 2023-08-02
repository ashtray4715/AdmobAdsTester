package com.ashtray.admob.ads.tester

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ashtray.admob.ads.library.customized.NativeTemplateStyle
import com.ashtray.admob.ads.library.customized.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd

class MainActivity : AppCompatActivity() {

    private val nativeAdListener = NativeAd.OnNativeAdLoadedListener {
        val background = ColorDrawable(Color.WHITE)
        val styles = NativeTemplateStyle.Builder().withMainBackgroundColor(background).build()
        val template = findViewById<TemplateView>(R.id.template_view)
        template.setStyles(styles)
        template.setNativeAd(it)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: invoked")

        MobileAds.initialize(this) {
            Log.d(TAG, "onCreate: mobile ads sdk initialization done")
        }

        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd(nativeAdListener)
            .build()

        adLoader.loadAd(AdRequest.Builder().build());
    }

    companion object {
        private const val TAG = "[MG]"
    }
}