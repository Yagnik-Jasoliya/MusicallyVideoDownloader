package com.etheriumDeveloper.musicallyvideodownloaded.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.etheriumDeveloper.musicallyvideodownloaded.Activities.UtilityAds;
import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.etheriumDeveloper.musicallyvideodownloaded.Activities.HelpActivity;
import com.etheriumDeveloper.musicallyvideodownloaded.Activities.MainActivity;
import com.etheriumDeveloper.musicallyvideodownloaded.R;
import com.etheriumDeveloper.musicallyvideodownloaded.Utils.CommonMethods;
import com.etheriumDeveloper.musicallyvideodownloaded.autolink.LinkExtractor;
import com.etheriumDeveloper.musicallyvideodownloaded.autolink.LinkSpan;
import com.etheriumDeveloper.musicallyvideodownloaded.autolink.LinkType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

//import com.kyleduo.blurpopupwindow.library.BlurPopupWindow.Builder;
//import org.nibor.autolink.LinkExtractor;
//import org.nibor.autolink.LinkSpan;
//import org.nibor.autolink.LinkType;

public class DashboardFragment extends Fragment {
    public static String FileName = null;
    public static final int PERMISSION_GALLERY = 2;
    public static String html;
    private LinearLayout adView;
    ImageView calc_clear_txt_Prise;
    TextView click_here;
    Activity context;
    public RelativeLayout dashboard;
    String dirPath;
    Document doc;
    Boolean download;
    int downloadId;
    Handler handler;
    RelativeLayout help_layout;
    private InterstitialAd interstitialAd;
    ProgressDialog mSpinner;
    private NativeAd nativeAd;
    private LinearLayout nativeAdContainer;
    EditText postURL;
    View rootview;
    Button search_image;
    SharedPreferences sharedPreferences;
    Dialog show_loader;
    TextView textView;
    String url;
    RelativeLayout url_layout;
    Activity activity;

    @SuppressLint({"StaticFieldLeak"})
    private class DownloadFileFromURL extends AsyncTask<String, String, String> {
        private DownloadFileFromURL() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
//            CommonMethods.mixpanel.track("DashboardFragment - Fetching Data...");
            if (DashboardFragment.this.mSpinner != null) {
                DashboardFragment.this.mSpinner.setMessage("Fetching Data....");
                DashboardFragment.this.mSpinner.show();
                DashboardFragment.this.mSpinner.setCancelable(false);
            }
        }

        protected final String doInBackground(String... params) {
            try {
                DashboardFragment.this.doc = Jsoup.connect(DashboardFragment.html).userAgent("Mozilla/5.0 (Linux; U; Android 4.1.1; en-us; Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800 Build/JRO03S) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30").get();
                System.out.println("doc= " + DashboardFragment.this.doc);
                String[] getvideo = DashboardFragment.this.doc.toString().split("og:video:url\" content=\"");
                System.out.println(" getVideo::1" + getvideo[0]);
                System.out.println(" getVideo::2" + getvideo[1]);
                String[] final_video_url = getvideo[1].split("\">");
                DashboardFragment.this.url = "http:" + final_video_url[0];
                System.out.println(" getVideo::3" + DashboardFragment.this.url);
                if (!DashboardFragment.this.url.contains("api2") && DashboardFragment.this.url.contains("mpak-sinc1")) {
                    String[] final_string = DashboardFragment.this.url.split("\\?");
                    DashboardFragment.this.url = final_string[0];
                    System.out.println(" final url:: " + DashboardFragment.this.url);
                }
                return DashboardFragment.this.url;
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Do not cross size of array sir." + e);
                return "";
            } catch (IOException e2) {
                e2.printStackTrace();
                System.out.println("IOException in Scraping Data::" + e2.toString());
                DashboardFragment.this.setSnackbar6(DashboardFragment.this.dashboard);
                return "";
            }
        }

        protected void onPostExecute(String strings) {
            if (DashboardFragment.this.mSpinner != null && DashboardFragment.this.mSpinner.isShowing()) {
                DashboardFragment.this.mSpinner.dismiss();
            }
            if (strings == "") {
                DashboardFragment.this.setSnackbar6(DashboardFragment.this.dashboard);
            } else if (CommonMethods.isNetworkAvailable(DashboardFragment.this.context)) {
                if (!CommonMethods.is_a_premium_member.booleanValue()) {
                    DashboardFragment.this.interstitialAd = new InterstitialAd(DashboardFragment.this.context, "1692456317493289_1696239023781685");
                    DashboardFragment.this.interstitialAd.loadAd();
                    DashboardFragment.this.interstitialAd.setAdListener(new AbstractAdListener() {
                        public void onError(Ad ad, AdError adError) {
                            super.onError(ad, adError);
                            System.out.println("onError::" + adError.getErrorMessage());
                        }

                        public void onAdLoaded(Ad ad) {
                            super.onAdLoaded(ad);
                            if (DashboardFragment.this.interstitialAd != null) {
                                DashboardFragment.this.interstitialAd.show();
                            }
                        }

                        public void onAdClicked(Ad ad) {
                            super.onAdClicked(ad);
                        }

                        public void onInterstitialDisplayed(Ad ad) {
                            super.onInterstitialDisplayed(ad);
                        }

                        public void onInterstitialDismissed(Ad ad) {
                            super.onInterstitialDismissed(ad);
                        }

                        public void onLoggingImpression(Ad ad) {
                            super.onLoggingImpression(ad);
                        }
                    });
                }
                DashboardFragment.FileName = System.currentTimeMillis() + ".mp4";
                DashboardFragment.this.DownloadFile(DashboardFragment.this.dirPath, DashboardFragment.FileName);
            } else {
                DashboardFragment.this.setSnackbar(DashboardFragment.this.dashboard);
            }
        }
    }

    public static void getOpenFacebookIntent(Context context) {
        Intent i = new Intent("android.intent.action.VIEW");
        i.setData(Uri.parse("https://www.facebook.com/groups/1631631307083443/"));
        context.startActivity(i);
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootview = inflater.inflate(R.layout.dashboard, container, false);
        this.download = Boolean.valueOf(false);
        this.context = getActivity();
        this.handler = new Handler();
        PRDownloader.initialize(getActivity());
        this.mSpinner = new ProgressDialog(this.context);
        this.sharedPreferences = this.context.getSharedPreferences("musically", 0);
        CommonMethods.is_a_premium_member = Boolean.valueOf(this.sharedPreferences.getBoolean("isapremiummember", false));
        System.out.println("is_a_premium_member::" + CommonMethods.is_a_premium_member);
        this.postURL = (EditText) this.rootview.findViewById(R.id.post_url);

//        this.url_layout = (RelativeLayout) this.rootview.findViewById(R.id.url_layout);
//        this.help_layout = (RelativeLayout) this.rootview.findViewById(R.id.help_layout);
        this.dashboard = (RelativeLayout) this.rootview.findViewById(R.id.dashboard);
        this.search_image = (Button) this.rootview.findViewById(R.id.search_image);
        this.calc_clear_txt_Prise = (ImageView) this.rootview.findViewById(R.id.calc_clear_txt_Prise);
        Button help = (Button) this.rootview.findViewById(R.id.help);
//        this.click_here = (TextView) this.rootview.findViewById(R.id.click_here);
//        this.nativeAdContainer = (LinearLayout) this.rootview.findViewById(R.id.native_ad_container);
        this.dirPath = CommonMethods.pathName1;
        if (!CommonMethods.is_a_premium_member.booleanValue()) {
//            showNativeAd();
        }
        if(UtilityAds.isOnline(getActivity())){
            nativeAds();
        }
//        CommonMethods.mixpanel.track("DashboardFragment - onCreateView called");
        help.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if(UtilityAds.isOnline(getActivity())){
                    UtilityAds.fullScreenAdGgl(getActivity());
                }
                Intent intent = new Intent(getActivity(), HelpActivity.class);
                startActivity(intent);
            }
        });


        this.postURL.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("WrongConstant")
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (DashboardFragment.this.postURL.length() > 0) {
                    DashboardFragment.this.calc_clear_txt_Prise.setVisibility(0);
                } else {
                    DashboardFragment.this.calc_clear_txt_Prise.setVisibility(8);
                }
            }
        });
        this.calc_clear_txt_Prise.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(DashboardFragment.this.getActivity(), R.anim.imageclick));
                DashboardFragment.this.postURL.setText("");
            }
        });
        this.search_image.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DashboardFragment.this.scrapDataOnclick(v);
            }
        });
//        this.click_here.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                DashboardFragment.getOpenFacebookIntent(DashboardFragment.this.getActivity());
//            }
//        });
        return this.rootview;
    }


    private void nativeAds() {
        nativeAd = new NativeAd(getActivity(), UtilityAds.Native_FB);
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
                nativeAdContainer = (LinearLayout) rootview.findViewById(R.id.ll_native);
                LayoutInflater inflater = LayoutInflater.from(getActivity());
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
                LinearLayout adChoicesContainer = (LinearLayout) rootview.findViewById(R.id.ad_choices_container);
                AdChoicesView adChoicesView = new AdChoicesView(getActivity(), nativeAd, true);
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
    @SuppressLint("WrongConstant")
    void scrapDataOnclick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.imageclick));
        View view = getActivity().getCurrentFocus();

        if (view != null) {
            FragmentActivity activity = getActivity();
            getActivity();
            ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (CommonMethods.isNetworkAvailable(this.context)) {
            try {
                if (this.postURL.getText().toString().isEmpty()) {
                    setSnackbar9(this.dashboard);
                    return;
                } else if (this.postURL.getText().toString().contains("musical.ly")) {
                    LinkSpan link = (LinkSpan) LinkExtractor.builder().linkTypes(EnumSet.of(LinkType.URL, LinkType.WWW, LinkType.EMAIL)).build().extractLinks(this.postURL.getText().toString()).iterator().next();
                    link.getType();
                    link.getBeginIndex();
                    link.getEndIndex();
                    html = this.postURL.getText().toString().substring(link.getBeginIndex(), link.getEndIndex());
                    new DownloadFileFromURL().execute(new String[0]);
                    System.out.println("Final urlllllllllllllllll::::" + html);
                    return;
                } else {
                    System.out.println("Coming here");
                    setSnackbar8(this.dashboard);
                    return;
                }
            } catch (Exception e) {
                System.out.println("e.getMessage()::::" + e.getMessage());
                return;
            }
        }
        setSnackbar(this.dashboard);
    }

    void setSnackbar(View view) {
        Snackbar snackbar = Snackbar.make(view.findViewById(view.getId()), (CharSequence) "Please connect to Network....", -2);
        if (this.download.booleanValue()) {
            snackbar.setAction((CharSequence) "RETRY", new OnClickListener() {
                public void onClick(View view) {
                    if (CommonMethods.isNetworkAvailable(DashboardFragment.this.context)) {
                        DashboardFragment.FileName = System.currentTimeMillis() + ".mp4";
                        DashboardFragment.this.DownloadFile(DashboardFragment.this.dirPath, DashboardFragment.FileName);
                        return;
                    }
                    DashboardFragment.this.setSnackbar(DashboardFragment.this.dashboard);
                }
            });
        } else {
            snackbar.setAction((CharSequence) "RETRY", new OnClickListener() {
                public void onClick(View view) {
                    if (CommonMethods.isNetworkAvailable(DashboardFragment.this.context)) {
                        DashboardFragment.this.scrapDataOnclick(view);
                    } else {
                        DashboardFragment.this.setSnackbar(DashboardFragment.this.dashboard);
                    }
                }
            });
        }
        snackbar.setActionTextColor(-55215);
        TextView textView = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        textView.setTextColor(-328966);
        textView.setTypeface(Typeface.SERIF);
        snackbar.show();
    }

    void setSnackbar6(View view) {
        Snackbar snackbar = Snackbar.make(view.findViewById(view.getId()), (CharSequence) "Couldn't load, weak network..", -2);
        snackbar.setAction((CharSequence) "RETRY", new OnClickListener() {
            public void onClick(View view) {
                new DownloadFileFromURL().execute(new String[0]);
            }
        });
        snackbar.setActionTextColor(-55215);
        TextView textView = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        textView.setTextColor(-328966);
        textView.setTypeface(Typeface.SERIF);
        snackbar.show();
    }

    void setSnackbar8(View view) {
        Snackbar snackbar = Snackbar.make(view.findViewById(view.getId()), (CharSequence) " Please enter a valid URL... ", 0);
        TextView textView = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        textView.setTextColor(-328966);
        textView.setTypeface(Typeface.SERIF);
        snackbar.show();
    }

    void setSnackbar9(View view) {
        Snackbar snackbar = Snackbar.make(view.findViewById(view.getId()), (CharSequence) " Please enter a URL to download... ", 0);
        ((TextView) snackbar.getView().findViewById(R.id.snackbar_text)).setTextColor(-328966);
        snackbar.show();
    }

    public void DownloadFile(String dirPath, String fileName) {
//        CommonMethods.mixpanel.track("DashboardFragment - Download Video called");
        if (CommonMethods.is_a_premium_member.booleanValue()) {
            try {
            } catch (IllegalArgumentException e) {
                System.out.println(" Illegal Exception caught::" + e.getMessage());
            }
        }
        if (this.show_loader == null) {
            this.show_loader = new Dialog(getActivity());
            this.show_loader.requestWindowFeature(1);
            this.show_loader.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            this.show_loader.setContentView(R.layout.download_via_url);
            this.show_loader.setCancelable(false);
            System.out.println("Inside Download _showLoader::" + this.url);
            this.textView = (TextView) this.show_loader.findViewById(R.id.progress);
        }
        this.show_loader.show();
        System.out.println("Inside Download::" + this.url);
        System.out.println("Inside Download::" + dirPath);
        System.out.println("Inside Download::" + fileName);
        System.out.println("Inside Download::" + this.textView);
        this.downloadId = PRDownloader.download(this.url, dirPath, fileName).build().setOnStartOrResumeListener(new OnStartOrResumeListener() {
            public void onStartOrResume() {
                MainActivity.pager.setCurrentItem(1);
            }
        }).setOnPauseListener(new OnPauseListener() {
            @SuppressLint("WrongConstant")
            public void onPause() {
                PRDownloader.resume(DashboardFragment.this.downloadId);
                Toast.makeText(DashboardFragment.this.context, "Download Paused", 0).show();
            }
        }).setOnCancelListener(new OnCancelListener() {
            public void onCancel() {
                DashboardFragment.this.downloadId = 0;
                MainActivity.pager.setCurrentItem(0);
            }
        }).setOnProgressListener(new OnProgressListener() {
            public void onProgress(Progress progress) {
                long progressPercent = (progress.currentBytes * 100) / progress.totalBytes;
                DashboardFragment.this.textView.setText(progressPercent + "%");
                System.out.println("Progress is::" + ((int) progressPercent));
            }
        }).start(new OnDownloadListener() {
            public void onDownloadComplete() {
                DashboardFragment.this.downloadId = 0;
                DashboardFragment.this.show_loader.dismiss();
                DownloadedFragment.fetchingMediaFromDirectory();
//                new AlertDialog.Builder(DashboardFragment.this.context).setContentView((int) R.layout.dialog).setGravity(17).setBlurRadius(20.0f).setAnimationDuration(300).build().show();
            }

            @SuppressLint("WrongConstant")
            public void onError(Error error) {
                System.out.println("Error is ::" + error);
                DashboardFragment.this.show_loader.dismiss();
                Toast.makeText(DashboardFragment.this.context, "Download Failed", 1).show();
            }
        });
        System.out.println("Status of download is ::" + PRDownloader.getStatus(this.downloadId));
    }


    public static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent("android.intent.action.VIEW", Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException e) {
            context.startActivity(webIntent);
        }
    }

    public void onDestroy() {
        if (this.mSpinner != null && this.mSpinner.isShowing()) {
            this.mSpinner.dismiss();
        }
        super.onDestroy();
    }
}
