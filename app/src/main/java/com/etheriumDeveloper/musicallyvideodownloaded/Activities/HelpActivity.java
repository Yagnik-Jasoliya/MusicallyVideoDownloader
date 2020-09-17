package com.etheriumDeveloper.musicallyvideodownloaded.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etheriumDeveloper.musicallyvideodownloaded.R;

public class HelpActivity extends AppCompatActivity {
    private Button btnNext;
//    private Button btnSkip;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;

    private ViewPager viewPager;
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        public void onPageSelected(int position) {
            HelpActivity.this.addBottomDots(position);
            if (position == HelpActivity.this.layouts.length - 1) {
                HelpActivity.this.btnNext.setText(HelpActivity.this.getString(R.string.start));
//                HelpActivity.this.btnSkip.setVisibility(View.GONE);
                return;
            }
            HelpActivity.this.btnNext.setText(HelpActivity.this.getString(R.string.next));
//            HelpActivity.this.btnSkip.setVisibility(View.VISIBLE);
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public Object instantiateItem(ViewGroup container, int position) {
            this.layoutInflater = (LayoutInflater) HelpActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = this.layoutInflater.inflate(HelpActivity.this.layouts[position], container, false);
            container.addView(view);
            return view;
        }

        public int getCount() {
            return HelpActivity.this.layouts.length;
        }

        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(1280);
        }

        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(1024, 1024);
        this.viewPager = (ViewPager) findViewById(R.id.view_pager);
        this.dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
//        this.btnSkip = (Button) findViewById(R.id.btn_skip);
        this.btnNext = (Button) findViewById(R.id.btn_next);
        this.layouts = new int[]{R.layout.welcome_slide1, R.layout.welcome_slide2, R.layout.welcome_slide3, R.layout.welcome_slide4};
        addBottomDots(0);
        changeStatusBarColor();
        this.myViewPagerAdapter = new MyViewPagerAdapter();
        this.viewPager.setAdapter(this.myViewPagerAdapter);
        this.viewPager.addOnPageChangeListener(this.viewPagerPageChangeListener);
//        this.btnSkip.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
////                if(UtilityAds.isOnline(getApplicationContext())){
////                    UtilityAds.FbFullAd(getApplicationContext());
////                }
//                HelpActivity.this.launchHomeScreen();
//            }
//        });
        this.btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                if(UtilityAds.isOnline(getApplicationContext())){
//                    UtilityAds.fullScreenAdGgl(getApplicationContext());
//                }
                int current = HelpActivity.this.getItem(1);
                if (current < HelpActivity.this.layouts.length) {
                    HelpActivity.this.viewPager.setCurrentItem(current);
                } else {
                    if(UtilityAds.isOnline(getApplicationContext())){
                        UtilityAds.FbFullAd(getApplicationContext());
                    }
                    HelpActivity.this.launchHomeScreen();
                }
            }
        });
    }
    private void addBottomDots(int currentPage) {
        this.dots = new TextView[this.layouts.length];
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
        this.dotsLayout.removeAllViews();
        for (int i = 0; i < this.dots.length; i++) {
            this.dots[i] = new TextView(this);
            this.dots[i].setText(Html.fromHtml("&#8226;"));
            this.dots[i].setTextSize(35.0f);
            this.dots[i].setTextColor(colorsInactive[currentPage]);
            this.dotsLayout.addView(this.dots[i]);
        }
        if (this.dots.length > 0) {
            this.dots[currentPage].setTextColor(colorsActive[currentPage]);
        }
    }

    private int getItem(int i) {
        return this.viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        }
    }
}
