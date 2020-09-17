package com.etheriumDeveloper.musicallyvideodownloaded.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.InterstitialAd;
import com.etheriumDeveloper.musicallyvideodownloaded.R;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    Button next;
    EditText username;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.home);
        getWindow().setFlags(1024, 1024);
        this.next = (Button) findViewById(R.id.button_next);
        this.username = (EditText) findViewById(R.id.username);
        if(UtilityAds.isOnline(getApplicationContext())){
            UtilityAds.fullScreenAdGgl(getApplicationContext());
        }
        this.next.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (HomeScreen.this.username.getText().toString().length() > 1) {
                    final ProgressDialog pd = new ProgressDialog(HomeScreen.this);
                    pd.setMessage("Checking username: " + HomeScreen.this.username.getText().toString() + "\n Please wait.");
                    pd.show();
                    new Handler() {
                        public void handleMessage(Message message) {
                            pd.dismiss();
                            HomeScreen.this.startActivity(new Intent(HomeScreen.this, Process.class));
                        }
                    }.sendMessageDelayed(new Message(), 5000);
                    return;
                }
                HomeScreen.this.username.setError("Enter username");
            }
        });

        if(UtilityAds.isOnline(getApplicationContext())){
            nativeAds();
        }
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
                nativeAdContainer = (LinearLayout) findViewById(R.id.ll_native);
                LayoutInflater inflater = LayoutInflater.from(HomeScreen.this);
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
                AdChoicesView adChoicesView = new AdChoicesView(HomeScreen.this, nativeAd, true);
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

}
