package com.ashtray.admob.ads.library.customized

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
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
    private var templateType = 0
    private var styles: NativeTemplateStyle? = null
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

    fun setStyles(styles: NativeTemplateStyle?) {
        this.styles = styles
        applyStyles()
    }

    fun getNativeAdView(): NativeAdView? {
        return nativeAdView
    }

    private fun applyStyles() {
        val mainBackground: Drawable? = styles!!.mainBackgroundColor
        if (mainBackground != null) {
            background!!.background = mainBackground
            if (primaryView != null) {
                primaryView!!.background = mainBackground
            }
            if (secondaryView != null) {
                secondaryView!!.background = mainBackground
            }
            if (tertiaryView != null) {
                tertiaryView!!.background = mainBackground
            }
        }
        val primary = styles!!.primaryTextTypeface
        if (primary != null && primaryView != null) {
            primaryView!!.typeface = primary
        }
        val secondary = styles!!.secondaryTextTypeface
        if (secondary != null && secondaryView != null) {
            secondaryView!!.typeface = secondary
        }
        val tertiary = styles!!.tertiaryTextTypeface
        if (tertiary != null && tertiaryView != null) {
            tertiaryView!!.typeface = tertiary
        }
        val ctaTypeface = styles!!.callToActionTextTypeface
        if (ctaTypeface != null && callToActionView != null) {
            callToActionView!!.typeface = ctaTypeface
        }
        if (styles!!.primaryTextTypefaceColor != null && primaryView != null) {
            primaryView!!.setTextColor(styles!!.primaryTextTypefaceColor!!)
        }
        if (styles!!.secondaryTextTypefaceColor != null && secondaryView != null) {
            secondaryView!!.setTextColor(styles!!.secondaryTextTypefaceColor!!)
        }
        if (styles!!.tertiaryTextTypefaceColor != null && tertiaryView != null) {
            tertiaryView!!.setTextColor(styles!!.tertiaryTextTypefaceColor!!)
        }
        if (styles!!.callToActionTypefaceColor != null && callToActionView != null) {
            callToActionView!!.setTextColor(styles!!.callToActionTypefaceColor!!)
        }
        val ctaTextSize = styles!!.callToActionTextSize
        if (ctaTextSize > 0 && callToActionView != null) {
            callToActionView!!.textSize = ctaTextSize
        }
        val primaryTextSize = styles!!.primaryTextSize
        if (primaryTextSize > 0 && primaryView != null) {
            primaryView!!.textSize = primaryTextSize
        }
        val secondaryTextSize = styles!!.secondaryTextSize
        if (secondaryTextSize > 0 && secondaryView != null) {
            secondaryView!!.textSize = secondaryTextSize
        }
        val tertiaryTextSize = styles!!.tertiaryTextSize
        if (tertiaryTextSize > 0 && tertiaryView != null) {
            tertiaryView!!.textSize = tertiaryTextSize
        }
        val ctaBackground: Drawable? = styles!!.callToActionBackgroundColor
        if (ctaBackground != null && callToActionView != null) {
            callToActionView!!.background = ctaBackground
        }
        val primaryBackground: Drawable? = styles!!.primaryTextBackgroundColor
        if (primaryBackground != null && primaryView != null) {
            primaryView!!.background = primaryBackground
        }
        val secondaryBackground: Drawable? = styles!!.secondaryTextBackgroundColor
        if (secondaryBackground != null && secondaryView != null) {
            secondaryView!!.background = secondaryBackground
        }
        val tertiaryBackground: Drawable? = styles!!.tertiaryTextBackgroundColor
        if (tertiaryBackground != null && tertiaryView != null) {
            tertiaryView!!.background = tertiaryBackground
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
        secondaryView!!.visibility = VISIBLE
        val secondaryText: String? = if (adHasOnlyStore(nativeAd)) {
            nativeAdView?.storeView = secondaryView
            store
        } else if (!TextUtils.isEmpty(advertiser)) {
            nativeAdView?.advertiserView = secondaryView
            advertiser
        } else {
            ""
        }
        primaryView!!.text = headline
        callToActionView!!.text = cta

        //  Set the secondary view to be the star rating if available.
        if (starRating != null && starRating > 0) {
            secondaryView!!.visibility = GONE
            ratingBar!!.visibility = VISIBLE
            ratingBar!!.rating = starRating.toFloat()
            nativeAdView?.starRatingView = ratingBar
        } else {
            secondaryView!!.text = secondaryText
            secondaryView!!.visibility = VISIBLE
            ratingBar!!.visibility = GONE
        }
        if (icon != null) {
            iconView!!.visibility = VISIBLE
            iconView!!.setImageDrawable(icon.drawable)
        } else {
            iconView!!.visibility = GONE
        }
        if (tertiaryView != null) {
            tertiaryView!!.text = body
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

    val templateTypeName: String
        get() {
            if (templateType == R.layout.gnt_medium_template_view) {
                return MEDIUM_TEMPLATE
            } else if (templateType == R.layout.gnt_small_template_view) {
                return SMALL_TEMPLATE
            }
            return ""
        }

    private fun initView(context: Context, attributeSet: AttributeSet?) {
        val attributes =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.TemplateView, 0, 0)
        templateType = try {
            attributes.getResourceId(
                R.styleable.TemplateView_gnt_template_type, R.layout.gnt_medium_template_view
            )
        } finally {
            attributes.recycle()
        }
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(templateType, this)
    }

    public override fun onFinishInflate() {
        super.onFinishInflate()
        nativeAdView = findViewById<View>(R.id.native_ad_view) as NativeAdView?
        primaryView = findViewById<View>(R.id.primary) as TextView?
        secondaryView = findViewById<View>(R.id.secondary) as TextView?
        tertiaryView = findViewById<View>(R.id.body) as TextView?
        ratingBar = findViewById<View>(R.id.rating_bar) as RatingBar?
        ratingBar!!.isEnabled = false
        callToActionView = findViewById<View>(R.id.cta) as Button?
        iconView = findViewById<View>(R.id.icon) as ImageView?
        mediaView = findViewById<View>(R.id.media_view) as MediaView?
        background = findViewById<View>(R.id.background) as ConstraintLayout?
    }

    companion object {
        private const val MEDIUM_TEMPLATE = "medium_template"
        private const val SMALL_TEMPLATE = "small_template"
    }
}
