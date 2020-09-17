package com.etheriumDeveloper.musicallyvideodownloaded.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.etheriumDeveloper.musicallyvideodownloaded.R;

//import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
//import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings.Builder;

public class Process extends Activity implements RewardedVideoAdListener {
    Button getfans;
    TextView info;
//    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
    ProgressDialog progressdialog;
    Button rate;
    TextView ratemessage;
    private boolean showRating = false;
    TextView text_message;
    AdView adViewbanner;

    @SuppressLint("WrongConstant")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process);
        getWindow().setFlags(1024, 1024);

        this.rate = (Button) findViewById(R.id.button_rating);
        this.getfans = (Button) findViewById(R.id.button_process);
        this.text_message = (TextView) findViewById(R.id.message);
        this.ratemessage = (TextView) findViewById(R.id.ratemessage);
        this.info = (TextView) findViewById(R.id.info);
        if(UtilityAds.isOnline(getApplicationContext())){
            UtilityAds.FbFullAd(getApplicationContext());
        }
        adViewbanner = new AdView(this, UtilityAds.fb_banner, AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        adContainer.addView(adViewbanner);
        adViewbanner.loadAd();

        this.mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        this.mRewardedVideoAd.setRewardedVideoAdListener(this);
//        this.mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        this.mFirebaseRemoteConfig.setConfigSettings(new Builder().setDeveloperModeEnabled(false).build());
//        this.mFirebaseRemoteConfig.setDefaults((int) R.xml.remote_config_defaults);
//        this.showRating = this.mFirebaseRemoteConfig.getBoolean("show_rating");
        this.rate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Process.this.launchMarket();
            }
        });
        this.getfans.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Process.this);
                builder.setTitle((CharSequence) "Watch a video to process your fans orders");
                builder.setMessage((CharSequence) "Are you sure?");
                builder.setPositiveButton((CharSequence) "YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Process.this.loadRewardedVideoAd();
                        Process.this.progressdialog = new ProgressDialog(Process.this);
                        Process.this.progressdialog.setMessage("Please Wait video is loading....");
                        Process.this.progressdialog.show();
                    }
                });
                builder.setNegativeButton((CharSequence) "NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        if (this.showRating) {
            this.ratemessage.setVisibility(0);
            this.rate.setVisibility(0);
        } else {
            this.ratemessage.setVisibility(4);
            this.rate.setVisibility(4);
        }
//        this.info.setText(this.mFirebaseRemoteConfig.getString("info"));
        long cacheExpiration = 1;
//        if (this.mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
//            cacheExpiration = 0;
//        }
//        this.mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener((Activity) this, new OnCompleteListener<Void>() {
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Process.this.mFirebaseRemoteConfig.activateFetched();
//                }
//                if (Process.this.showRating) {
//                    Process.this.ratemessage.setVisibility(0);
//                    Process.this.rate.setVisibility(0);
//                } else {
//                    Process.this.ratemessage.setVisibility(4);
//                    Process.this.rate.setVisibility(4);
//                }
//                Process.this.info.setText(Process.this.mFirebaseRemoteConfig.getString("info"));
//            }
//        });
    }

    private void launchMarket() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " Unable to find Playstore", Toast.LENGTH_LONG).show();
        }
    }


    public void onRewardedVideoAdLoaded() {
        this.progressdialog.dismiss();
        this.mRewardedVideoAd.show();
    }

    public void onRewardedVideoAdOpened() {
    }

    public void onRewardedVideoStarted() {
    }

    public void onRewardedVideoAdClosed() {
    }

    public void onRewarded(RewardItem rewardItem) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Processing request.. Please wait ");
        pd.show();
        new Handler() {
            @SuppressLint("WrongConstant")
            public void handleMessage(Message message) {
                pd.dismiss();
                Process.this.getfans.setVisibility(4);
                Process.this.rate.setVisibility(4);
                Process.this.text_message.setVisibility(0);
                info.setVisibility(View.GONE);
            }
        }.sendMessageDelayed(new Message(), 10000);
    }

    public void onRewardedVideoAdLeftApplication() {
    }

    public void onRewardedVideoAdFailedToLoad(int i) {
        this.progressdialog.dismiss();
        Log.d("video failed", "ads failed no fill");
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Processing request.. Please wait ");
        pd.show();
        new Handler() {
            @SuppressLint("WrongConstant")
            public void handleMessage(Message message) {
                pd.dismiss();
                Process.this.getfans.setVisibility(4);
                Process.this.rate.setVisibility(4);
                Process.this.text_message.setVisibility(0);
                info.setVisibility(View.GONE);

            }
        }.sendMessageDelayed(new Message(), 10000);
    }

    public void onRewardedVideoCompleted() {
    }

    private void loadRewardedVideoAd() {
        this.mRewardedVideoAd.loadAd(getString(R.string.process_reward), new AdRequest.Builder().build());
    }
}
