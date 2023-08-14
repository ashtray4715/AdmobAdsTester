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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: invoked")

        MobileAds.initialize(this) {
            Log.d(TAG, "onCreate: mobile ads sdk initialization done")
            loadSmallAd()
            loadLargeAd()
        }
    }

    private fun loadSmallAd() {
        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd {
                val background = ColorDrawable(Color.WHITE)
                val styles =
                    NativeTemplateStyle.Builder().withMainBackgroundColor(background).build()
                val template = findViewById<TemplateView>(R.id.small_template_view)
                template.applyStyles(styles)
                template.setNativeAd(it)
            }
            .build()

        adLoader.loadAd(AdRequest.Builder().build());
    }

    private fun loadLargeAd() {
        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd {
                val background = ColorDrawable(Color.WHITE)
                val styles =
                    NativeTemplateStyle.Builder().withMainBackgroundColor(background).build()
                val template = findViewById<TemplateView>(R.id.large_template_view)
                template.applyStyles(styles)
                template.setNativeAd(it)
            }
            .build()

        adLoader.loadAd(AdRequest.Builder().build());
    }

    companion object {
        private const val TAG = "[MG]"
    }
}