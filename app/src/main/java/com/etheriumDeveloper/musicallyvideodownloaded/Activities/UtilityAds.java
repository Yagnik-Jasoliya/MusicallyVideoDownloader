package com.etheriumDeveloper.musicallyvideodownloaded.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by om on 2/22/2018.
 */

public class UtilityAds {
    public static String fb_banner="575993676135226_576225966111997";
    public static String str_banner_google="";
    //public static String bannerAdsUnits="";
    public static String google_full_screen_id="ca-app-pub-2088193644957545/7276921659";
    public static String Interstitial_FB="575993676135226_576225609445366";
    public static String Native_FB="575993676135226_576222666112327";

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static void loaddBannerAd(Context context, RelativeLayout relativeLayout) {
        AdView adView=new AdView(context);
        adView.setAdUnitId(str_banner_google);
        adView.setAdSize(AdSize.BANNER);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        relativeLayout.addView(adView);
    }
    InterstitialAd interstitial;
    public void AdMobInterstitial(final Context c, final Class destination) {
        if (isOnline(c)) {
            this.interstitial = new InterstitialAd(c);
            this.interstitial.setAdUnitId(this.google_full_screen_id);
            final ProgressDialog pd = new ProgressDialog(c);
            pd.setProgressStyle(0);
            pd.setMessage("Please wait...");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
            this.interstitial.loadAd(new AdRequest.Builder().build());
            this.interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    super.onAdLoaded();
                    pd.dismiss();
                    UtilityAds.this.interstitial.show();
                }

                public void onAdClosed() {
                    super.onAdClosed();
                    pd.dismiss();
                    c.startActivity(new Intent(c, destination));
                }

                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    pd.dismiss();
                    c.startActivity(new Intent(c, destination));
                }
            });
            return;
        }
        c.startActivity(new Intent(c, destination));
    }




    public static com.facebook.ads.InterstitialAd interstitialAd;
    public static void FbFullAd(Context context)
    {
        interstitialAd = new com.facebook.ads.InterstitialAd(context, Interstitial_FB);
        interstitialAd.loadAd();
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }
            @Override
            public void onInterstitialDismissed(Ad ad) {
            }
            @Override
            public void onError(Ad ad, AdError adError) {
            }
            @Override
            public void onAdLoaded(Ad ad) {
                // Show the ad when it's done loading.

                if (interstitialAd==null||!interstitialAd.isAdLoaded())
                {

                }else {
                    interstitialAd.show();
                }
            }
            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
    }



    public static NativeAd nativeAd;
    public static void nativeAds100(final Context context, final LinearLayout linearLayout) {
        nativeAd = new NativeAd(context, Native_FB);
        nativeAd.setAdListener(new com.facebook.ads.AdListener()
        {
            @Override
            public void onError(Ad ad, AdError adError)
            {
            }
            @Override
            public void onAdLoaded(Ad ad) {
                View adView = NativeAdView.render(context, nativeAd, NativeAdView.Type.HEIGHT_100);
                // LinearLayout nativeAdContainer = (LinearLayout) findViewById(R.id.linear_native_ad_fb);
                linearLayout.addView(adView);
            }
            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

        });
        nativeAd.loadAd();
    }
    public static void nativeAds300(final Context context, final LinearLayout linearLayout) {
        nativeAd = new NativeAd(context, Native_FB);
        nativeAd.setAdListener(new com.facebook.ads.AdListener()
        {
            @Override
            public void onError(Ad ad, AdError adError)
            {
            }
            @Override
            public void onAdLoaded(Ad ad) {
                View adView = NativeAdView.render(context, nativeAd, NativeAdView.Type.HEIGHT_300);
                // LinearLayout nativeAdContainer = (LinearLayout) findViewById(R.id.linear_native_ad_fb);
                linearLayout.addView(adView);
            }
            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

        });
        nativeAd.loadAd();
    }
    public static void nativeAds120(final Context context, final LinearLayout linearLayout) {
        nativeAd = new NativeAd(context, Native_FB);
        nativeAd.setAdListener(new com.facebook.ads.AdListener()
        {
            @Override
            public void onError(Ad ad, AdError adError)
            {
            }
            @Override
            public void onAdLoaded(Ad ad) {
                View adView = NativeAdView.render(context, nativeAd, NativeAdView.Type.HEIGHT_120);
                // LinearLayout nativeAdContainer = (LinearLayout) findViewById(R.id.linear_native_ad_fb);
                linearLayout.addView(adView);
            }
            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

        });
        nativeAd.loadAd();
    }


    public static InterstitialAd mInterstitialAd;
    public static void fullScreenAdGgl(Context context)
    {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(google_full_screen_id);
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }
    public static void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


}
