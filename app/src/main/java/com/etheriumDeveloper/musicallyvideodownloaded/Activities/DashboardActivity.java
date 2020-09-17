package com.etheriumDeveloper.musicallyvideodownloaded.Activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etheriumDeveloper.musicallyvideodownloaded.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    ImageView download,followers,album,rate,pipad;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getWindow().setFlags(1024, 1024);
       activity=this;

        if(UtilityAds.isOnline(getApplicationContext())){
            nativeAds();
        }
        download=findViewById(R.id.download);
        followers=findViewById(R.id.followers);
        album=findViewById(R.id.album);
        rate=findViewById(R.id.ic_rate);
        pipad=findViewById(R.id.pipad);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UtilityAds.isOnline(getApplicationContext())){
                    UtilityAds.fullScreenAdGgl(getApplicationContext());
                }
                Intent intent=new Intent(DashboardActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UtilityAds.isOnline(getApplicationContext())){
                    UtilityAds.fullScreenAdGgl(getApplicationContext());
                }
                Intent intent=new Intent(DashboardActivity.this,MainActivity2.class);
                startActivity(intent);
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(DashboardActivity.this, " unable to find market app", Toast.LENGTH_LONG).show();
                }
            }
        });
        pipad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textCollage();
            }
        });
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UtilityAds.isOnline(getApplicationContext())){
                    UtilityAds.FbFullAd(getApplicationContext());
                }
                Intent intent=new Intent(DashboardActivity.this,HomeScreen.class);
                startActivity(intent);
            }
        });

    }
    private NativeAd nativeAd;
    private LinearLayout nativeAdContainer;
    private LinearLayout adView;
    private void nativeAds() {
        nativeAd = new NativeAd(this, UtilityAds.Native_FB);
        nativeAd.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
//                gif.setVisibility(View.VISIBLE);
                if (nativeAd != null) {
                    nativeAd.unregisterView();
                }

                // Add the Ad view into the ad container.
                nativeAdContainer = (LinearLayout) findViewById(R.id.native_ad_container);
                LayoutInflater inflater = LayoutInflater.from(DashboardActivity.this);
                // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdContainer, false);
                nativeAdContainer.addView(adView);

                // Create native UI using the ad metadata.
                ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.native_ad_icon);
                TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
                MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
                TextView nativeAdSocialContext = (TextView) adView.findViewById(R.id.native_ad_social_context);
                TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
                Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

                // Set the Text.
                nativeAdTitle.setText(nativeAd.getAdTitle());
                nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                nativeAdBody.setText(nativeAd.getAdBody());
                nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

                // Download and display the ad icon.
                NativeAd.Image adIcon = nativeAd.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

                // Download and display the cover image.
                nativeAdMedia.setNativeAd(nativeAd);

                // Add the AdChoices icon
                LinearLayout adChoicesContainer = (LinearLayout) findViewById(R.id.ad_choices_container);
                AdChoicesView adChoicesView = new AdChoicesView(DashboardActivity.this, nativeAd, true);
                adChoicesContainer.addView(adChoicesView);

                // Register the Title and CTA button to listen for clicks.
                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdTitle);
                clickableViews.add(nativeAdCallToAction);
                nativeAd.registerViewForInteraction(nativeAdContainer,clickableViews);

            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        });
        // Initiate a request to load an ad.
        nativeAd.loadAd();
    }
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    private void textCollage() {
        boolean isAppInstalled = appInstalledOrNot("com.terabyte.pipcamera");
        if (isAppInstalled) {
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.terabyte.pipcamera");
            startActivity(LaunchIntent);
            Log.i("Tag", "Application is already installed.");
        } else {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.terabyte.pipcamera");
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(myAppLinkToMarket);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "You don't have Google Play installed", Toast.LENGTH_LONG).show();
            }
            Log.i("Tag", "Application is not currently installed.");
        }
    }
    boolean doubleBackToExitPressedOnce = false;

    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
