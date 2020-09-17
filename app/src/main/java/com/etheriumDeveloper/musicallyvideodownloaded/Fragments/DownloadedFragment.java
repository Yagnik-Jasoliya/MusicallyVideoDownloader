package com.etheriumDeveloper.musicallyvideodownloaded.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.etheriumDeveloper.musicallyvideodownloaded.Activities.UtilityAds;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.etheriumDeveloper.musicallyvideodownloaded.Adapters.MediaAdapter;
import com.etheriumDeveloper.musicallyvideodownloaded.R;
import com.etheriumDeveloper.musicallyvideodownloaded.Utils.CommonMethods;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class DownloadedFragment extends Fragment {
    private static Activity activity;
    public static ImageView delete_btn;
    public static MediaAdapter mediaAdapter;
    public static RelativeLayout noMedia;
    public static GridView photos_grid;
    public static RelativeLayout rel_delete;
    public static ArrayList<String> results = new ArrayList();
    public static final String root = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Musical.ly Video Downloader/");
    private static boolean setMultipleSelection = false;
    private AdView adView;
    View rootview;
    SharedPreferences sharedPreferences;
    AdView adViewbanner;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("test().create");
        this.rootview = inflater.inflate(R.layout.downloaded, container, false);
        activity = getActivity();
        this.sharedPreferences = activity.getSharedPreferences("musically", 0);
        CommonMethods.is_a_premium_member = Boolean.valueOf(this.sharedPreferences.getBoolean("isapremiummember", false));
        setMultipleSelection = false;
        rel_delete = (RelativeLayout) this.rootview.findViewById(R.id.rel_delete);
        noMedia = (RelativeLayout) this.rootview.findViewById(R.id.noMedia);
        delete_btn = (ImageView) this.rootview.findViewById(R.id.delete_btn);
        photos_grid = (GridView) this.rootview.findViewById(R.id.photos_grid);

        mediaAdapter = new MediaAdapter(activity, results);

        photos_grid.setAdapter(mediaAdapter);
        fetchingMediaFromDirectory();

        adViewbanner = new AdView(getActivity(), UtilityAds.fb_banner, AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = (LinearLayout) rootview.findViewById(R.id.banner_container);
        adContainer.addView(adViewbanner);
        adViewbanner.loadAd();

        final Animation lefttoright = AnimationUtils.loadAnimation(activity, R.anim.lefttoright);
        lefttoright.setFillAfter(true);
        lefttoright.setDuration(1500);
        final Animation slide_up = AnimationUtils.loadAnimation(activity, R.anim.slide_up);
        slide_up.setFillAfter(true);
        slide_up.setDuration(100);
        photos_grid.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                CommonMethods.selectedPosition.clear();
                DownloadedFragment.setMultipleSelection = true;
                CommonMethods.selectedPosition.add(Integer.valueOf(i));
                DownloadedFragment.rel_delete.startAnimation(lefttoright);
                DownloadedFragment.rel_delete.setVisibility(0);
                return true;
            }
        });
        photos_grid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (DownloadedFragment.setMultipleSelection) {
                    int localIndex = CommonMethods.selectedPosition.indexOf(Integer.valueOf(i));
                    if (localIndex == -1) {
                        CommonMethods.selectedPosition.add(Integer.valueOf(i));
                    } else {
                        CommonMethods.selectedPosition.remove(localIndex);
                    }
                    if (CommonMethods.selectedPosition.size() <= 0) {
                        DownloadedFragment.setMultipleSelection = false;
                        DownloadedFragment.rel_delete.setVisibility(8);
                        DownloadedFragment.rel_delete.startAnimation(slide_up);
                        CommonMethods.selectedPosition.clear();
                    }
                    DownloadedFragment.mediaAdapter.notifyDataSetChanged();
                }
            }
        });
        delete_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DownloadedFragment.setMultipleSelection = false;
                DownloadedFragment.rel_delete.setVisibility(8);
                DownloadedFragment.this.deleteFiles();
            }
        });

        return this.rootview;
    }

    private void deleteFiles() {
        while (CommonMethods.selectedPosition.size() > 0) {
            File myDirectory = new File((String) results.get(((Integer) CommonMethods.selectedPosition.get(0)).intValue()));
            CommonMethods.selectedPosition.remove(0);
            if (myDirectory.exists()) {
                myDirectory.delete();
            }
        }
        fetchingMediaFromDirectory();
    }

    public static void fetchingMediaFromDirectory() {
        results.clear();
        File[] VideoFiles = new File(root).listFiles();
        if (VideoFiles != null) {
            for (File file : VideoFiles) {
                if (file.isFile()) {
                    results.add(root + file.getName());
                }
            }
        }
        if (results.size() == 0) {
            noMedia.setVisibility(0);
            return;
        }
        Collections.sort(results, Collections.reverseOrder());

//        Collections.sort(results.subList(1, results.size()));
        noMedia.setVisibility(8);
        mediaAdapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        if (this.adView != null) {
            this.adView.destroy();
        }
        super.onDestroy();
    }
}
