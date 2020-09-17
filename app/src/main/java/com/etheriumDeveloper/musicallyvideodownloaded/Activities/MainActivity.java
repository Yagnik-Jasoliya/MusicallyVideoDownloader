package com.etheriumDeveloper.musicallyvideodownloaded.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.etheriumDeveloper.musicallyvideodownloaded.Adapters.ViewPagerAdapter;
import com.etheriumDeveloper.musicallyvideodownloaded.Fragments.DashboardFragment;
import com.etheriumDeveloper.musicallyvideodownloaded.Fragments.DownloadedFragment;
import com.etheriumDeveloper.musicallyvideodownloaded.R;
import com.etheriumDeveloper.musicallyvideodownloaded.Utils.CommonMethods;

//import com.kobakei.ratethisapp.RateThisApp;
//import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class MainActivity extends AppCompatActivity {
    public static ViewPager pager;
    public static TabLayout tabLayout;
    Dialog dialog;
    public static Activity activity;
    Editor editor;
    ImageView music,adlayout;
    private Boolean exit = Boolean.valueOf(false);
    SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        getWindow().setFlags(1024, 1024);
        activity=this;
        this.sharedPreferences = getSharedPreferences("musically", 0);
        this.editor = this.sharedPreferences.edit();
//        RateThisApp.onStart(this);
//        RateThisApp.showRateDialogIfNeeded(this);
//        CommonMethods.mixpanel = MixpanelAPI.getInstance(this, CommonMethods.projectToken);
//        CommonMethods.mixpanel.track("MainActivity - onCreate called");
        if (ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") != 0) {
            askPermission_gallery();
        } else {
            CommonMethods.createDirectory();
            System.out.println(" Yahan nahi aaraha hai");
        }
//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        music = (ImageView) findViewById(R.id.musically);
        adlayout = (ImageView) findViewById(R.id.adlayout);
        this.music.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textCollage();

            }
        });
        this.adlayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
             if(UtilityAds.isOnline(getApplicationContext())){
                 UtilityAds.FbFullAd(getApplicationContext());
             }
            }
        });
        pager = (ViewPager) findViewById(R.id.mainPager);
        tabLayout = (TabLayout) findViewById(R.id.mainTabs);
        setupViewPager(pager);
        tabLayout.setupWithViewPager(pager);
        pager.setOnPageChangeListener(new OnPageChangeListener() {
            @SuppressLint("WrongConstant")
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                MainActivity.this.invalidateOptionsMenu();
                View view = MainActivity.this.getCurrentFocus();
                if (view != null) {
                    ((InputMethodManager) MainActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (MainActivity.pager.getCurrentItem() == 0 || MainActivity.pager.getCurrentItem() != 1) {
                }
            }

            public void onPageSelected(int position) {
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
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
        boolean isAppInstalled = appInstalledOrNot("com.zhiliaoapp.musically");
        if (isAppInstalled) {
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically");
            startActivity(LaunchIntent);
            Log.i("Tag", "Application is already installed.");
        } else {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.zhiliaoapp.musically");
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(myAppLinkToMarket);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "You don't have Google Play installed", Toast.LENGTH_LONG).show();
            }
            Log.i("Tag", "Application is not currently installed.");
        }
    }

    public void onDestroy() {

        CommonMethods.selectedPosition.clear();
        Glide.with(getApplicationContext()).pauseRequests();
        super.onDestroy();
    }

    private void shareTextUrl() {
        Intent share = new Intent("android.intent.action.SEND");
        share.setType("text/plain");
        share.addFlags(524288);
        share.putExtra("android.intent.extra.SUBJECT", "I love this App..  ");
        share.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=com.etheriumDeveloper.musicallyvideodownloaded");
        startActivity(Intent.createChooser(share, "https://play.google.com/store/apps/details?id=com.etheriumDeveloper.musicallyvideodownloaded"));
    }

    void askPermission_gallery() {
        if (VERSION.SDK_INT < 23) {
            CommonMethods.createDirectory();
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            CommonMethods.createDirectory();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DashboardFragment(), "Download");
        adapter.addFrag(new DownloadedFragment(), "History");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

//    @SuppressLint("WrongConstant")
//    public void onBackPressed() {
//        try {
//            if (this.exit.booleanValue()) {
//                finish();
//                return;
//            }
//            Toast.makeText(this, "Press twice to Exit.", 0).show();
//            this.exit = Boolean.valueOf(true);
//            new Handler().postDelayed(new Runnable() {
//                public void run() {
//                    MainActivity.this.exit = Boolean.valueOf(false);
//                }
//            }, 3000);
//        } catch (Exception e) {
//            System.out.println("hahahah BUG::;" + e.getMessage());
//        }
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CommonMethods.createDirectory();
    }
}
