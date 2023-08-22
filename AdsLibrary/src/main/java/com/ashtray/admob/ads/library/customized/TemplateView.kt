package com.ashtray.admob.ads.library.customized

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ashtray.admob.ads.library.R
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

/**
 * Base class for a template view. *
 */
class TemplateView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private var nativeAd: NativeAd? = null
    private var nativeAdView: NativeAdView? = null
    private var primaryView: TextView? = null
    private var secondaryView: TextView? = null
    private var ratingBar: RatingBar? = null
    private var tertiaryView: TextView? = null
    private var iconView: ImageView? = null
    private var mediaView: MediaView? = null
    private var callToActionView: Button? = null
    private var background: ConstraintLayout? = null

    init {
        initView(context, attrs)
    }

    fun getNativeAdView(): NativeAdView? {
        return nativeAdView
    }

    fun applyStyles(styles: NativeTemplateStyle) {
        styles.mainBackgroundColor?.let { mainBackground ->
            background?.setBackgroundColor(mainBackground.color)
            primaryView?.background = mainBackground
            secondaryView?.background = mainBackground
            tertiaryView?.background = mainBackground
        }

        styles.primaryTextTypeface?.let { primary ->
            primaryView?.typeface = primary
        }

        styles.secondaryTextTypeface?.let { secondary ->
            secondaryView?.typeface = secondary
        }

        styles.tertiaryTextTypeface?.let { tertiary ->
            tertiaryView?.typeface = tertiary
        }

        styles.callToActionTextTypeface?.let { ctaTypeface ->
            callToActionView?.typeface = ctaTypeface
        }

        styles.primaryTextTypefaceColor?.let { primary ->
            primaryView?.setTextColor(primary)
        }

        styles.secondaryTextTypefaceColor?.let { secondary ->
            secondaryView?.setTextColor(secondary)
        }

        styles.tertiaryTextTypefaceColor?.let { tertiary ->
            tertiaryView?.setTextColor(tertiary)
        }

        styles.callToActionTypefaceColor?.let { callToAction ->
            callToActionView?.setTextColor(callToAction)
        }

        styles.callToActionTextSize?.let { ctaTextSize ->
            callToActionView?.textSize = ctaTextSize
        }

        styles.primaryTextSize?.let { primaryTextSize ->
            primaryView?.textSize = primaryTextSize
        }

        styles.secondaryTextSize?.let { secondaryTextSize ->
            secondaryView?.textSize = secondaryTextSize
        }

        styles.tertiaryTextSize?.let { tertiaryTextSize ->
            tertiaryView?.textSize = tertiaryTextSize
        }

        styles.callToActionBackgroundColor?.let { ctaBackground ->
            callToActionView?.background = ctaBackground
        }

        styles.primaryTextBackgroundColor?.let { primaryBackground ->
            primaryView?.background = primaryBackground
        }

        styles.secondaryTextBackgroundColor?.let { secondaryBackground ->
            secondaryView?.background = secondaryBackground
        }

        styles.tertiaryTextBackgroundColor?.let { tertiaryBackground ->
            tertiaryView?.background = tertiaryBackground
        }

        invalidate()
        requestLayout()
    }

    private fun adHasOnlyStore(nativeAd: NativeAd): Boolean {
        val store: String? = nativeAd.store
        val advertiser: String? = nativeAd.advertiser
        return !TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser)
    }

    fun setNativeAd(nativeAd: NativeAd) {
        this.nativeAd = nativeAd
        val store: String? = nativeAd.store
        val advertiser: String? = nativeAd.advertiser
        val headline: String? = nativeAd.headline
        val body: String? = nativeAd.body
        val cta: String? = nativeAd.callToAction
        val starRating: Double? = nativeAd.starRating
        val icon: NativeAd.Image? = nativeAd.icon
        nativeAdView?.callToActionView = callToActionView
        nativeAdView?.headlineView = primaryView
        nativeAdView?.mediaView = mediaView
        secondaryView?.visibility = VISIBLE
        val secondaryText: String? = if (adHasOnlyStore(nativeAd)) {
            nativeAdView?.storeView = secondaryView
            store
        } else if (!TextUtils.isEmpty(advertiser)) {
            nativeAdView?.advertiserView = secondaryView
            advertiser
        } else {
            ""
        }
        primaryView?.text = headline
        callToActionView?.text = cta

        //  Set the secondary view to be the star rating if available.
        if (starRating != null && starRating > 0) {
            secondaryView?.visibility = GONE
            ratingBar?.visibility = VISIBLE
            ratingBar?.rating = starRating.toFloat()
            nativeAdView?.starRatingView = ratingBar
        } else {
            secondaryView?.text = secondaryText
            secondaryView?.visibility = VISIBLE
            ratingBar?.visibility = GONE
        }
        if (icon != null) {
            iconView?.visibility = VISIBLE
            iconView?.setImageDrawable(icon.drawable)
        } else {
            iconView?.visibility = GONE
        }
        if (tertiaryView != null) {
            tertiaryView?.text = body
            nativeAdView?.bodyView = tertiaryView
        }
        nativeAdView?.setNativeAd(nativeAd)
    }

    /**
     * To prevent memory leaks, make sure to destroy your ad when you don't need it anymore. This
     * method does not destroy the template view.
     * https://developers.google.com/admob/android/native-unified#destroy_ad
     */
    fun destroyNativeAd() {
        nativeAd?.destroy()
    }

    private fun initView(context: Context, attributeSet: AttributeSet?) {
        val attributes = context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.TemplateView, 0, 0
        )
        val templateType = try {
            attributes.getResourceId(
                R.styleable.TemplateView_gnt_template_type, R.layout.gnt_medium_template_view
            )
        } finally {
            attributes.recycle()
        }
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        inflater?.inflate(templateType, this)
    }

    public override fun onFinishInflate() {
        super.onFinishInflate()
        nativeAdView = findViewById(R.id.native_ad_view)
        primaryView = findViewById(R.id.primary)
        secondaryView = findViewById(R.id.secondary)
        tertiaryView = findViewById(R.id.body)
        ratingBar = findViewById(R.id.rating_bar)
        ratingBar?.isEnabled = false
        callToActionView = findViewById(R.id.cta)
        iconView = findViewById(R.id.icon)
        mediaView = findViewById(R.id.media_view)
        background = findViewById(R.id.background)
    }
}
