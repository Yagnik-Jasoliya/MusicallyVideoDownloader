package com.etheriumDeveloper.musicallyvideodownloaded.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.etheriumDeveloper.musicallyvideodownloaded.Activities.DashboardActivity;
import com.etheriumDeveloper.musicallyvideodownloaded.Activities.UtilityAds;
import com.google.android.exoplayer2.util.MimeTypes;
import com.etheriumDeveloper.musicallyvideodownloaded.Activities.MainActivity;
import com.etheriumDeveloper.musicallyvideodownloaded.R;
import com.etheriumDeveloper.musicallyvideodownloaded.Utils.CommonMethods;

import java.io.File;
import java.util.ArrayList;

public class MediaAdapter extends BaseAdapter {
    public static Context context;
    public static String filename;
    public static ArrayList<String> imgid;

    class ViewHolder {
        ImageView Imaage,delete;
        RelativeLayout delete_check_layout;
        ImageView delte_video;
        ImageView image_video;
        RelativeLayout selected_layout;
//        ImageView share_video;
        ImageButton share_video_layout;

        ViewHolder() {
        }
    }

    public MediaAdapter(Context context, ArrayList<String> imgid) {
        this.context = context;
        this.imgid = imgid;
    }

    public int getCount() {
        return imgid.size();
    }

    public Object getItem(int position) {
        return imgid.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    @SuppressLint("WrongConstant")
    private void playVideo(String filePath) {
        filename = filePath;
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setFlags(402653184);
            System.out.println("videopath" + filePath);
            intent.setDataAndType(Uri.parse(filePath), MimeTypes.VIDEO_MP4);
            context.startActivity(Intent.createChooser(intent, "Play with"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Please install any video player to play the video !", 0).show();
        }
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.media_grid_list_item, parent, false);
            viewHolder.Imaage = (ImageView) convertView.findViewById(R.id.Imaage);
            viewHolder.image_video = (ImageView) convertView.findViewById(R.id.image_video);
//            viewHolder.share_video = (ImageView) convertView.findViewById(R.id.share_video);
            viewHolder.delte_video = (ImageView) convertView.findViewById(R.id.delte_video);
            viewHolder.selected_layout = (RelativeLayout) convertView.findViewById(R.id.selected_layout);
            viewHolder.share_video_layout = (ImageButton) convertView.findViewById(R.id.share_layout);
            viewHolder.delete = (ImageButton) convertView.findViewById(R.id.imb_collection_delete);
            viewHolder.delete_check_layout = (RelativeLayout) convertView.findViewById(R.id.delete_check_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (((String) imgid.get(position)).contains("mp4")) {
            viewHolder.image_video.setVisibility(0);
            Glide.with(context).load(Uri.fromFile(new File((String) imgid.get(position)))).into(viewHolder.Imaage);
        } else {
            viewHolder.image_video.setVisibility(8);
            Glide.with(context).load(Uri.fromFile(new File((String) imgid.get(position)))).into(viewHolder.Imaage);
        }
        if (CommonMethods.selectedPosition.size() > 0) {
//            viewHolder.share_video_layout.setVisibility(8);
            viewHolder.image_video.setVisibility(8);
            int localIndex = CommonMethods.selectedPosition.indexOf(Integer.valueOf(position));
            if (localIndex == -1) {
                viewHolder.delete_check_layout.setVisibility(8);
                viewHolder.selected_layout.setVisibility(8);
            } else if (((Integer) CommonMethods.selectedPosition.get(localIndex)).intValue() == position) {
                viewHolder.delete_check_layout.setVisibility(0);
                viewHolder.selected_layout.setVisibility(0);
            } else {
                viewHolder.delete_check_layout.setVisibility(8);
                viewHolder.selected_layout.setVisibility(8);
            }
        } else {
            viewHolder.delete_check_layout.setVisibility(8);
//            viewHolder.share_video_layout.setVisibility(0);
            viewHolder.image_video.setVisibility(0);
            viewHolder.selected_layout.setVisibility(8);
        }
        viewHolder.image_video.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (((String) MediaAdapter.imgid.get(position)).contains("mp4")) {
                    MediaAdapter.this.playVideo((String) MediaAdapter.imgid.get(position));
                }
            }
        });
        viewHolder.delete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.activity);
                alert.setTitle("Deleting...");
                alert.setMessage("Do You Really Want to Delete This Image ?.");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        File file = new File(MediaAdapter.imgid.get(position));
                        if (file.exists()) {
                            file.delete();
                            MainActivity.activity.startActivity(new Intent(MainActivity.activity, MainActivity.class));
                            MainActivity.activity.finish();
                            return;
                        }
                        Toast.makeText(MainActivity.activity, "No Files Found", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.show();
            }
        });
        viewHolder.share_video_layout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Uri myUri = FileProvider.getUriForFile(MediaAdapter.context, MediaAdapter.context.getApplicationContext().getPackageName() + ".my.package.name.provider", new File((String) MediaAdapter.imgid.get(position)));
                try {
                    Intent share = new Intent("android.intent.action.SEND");
                    share.putExtra("android.intent.extra.STREAM", myUri);
                    share.addFlags(1);
                    share.setType("image/*");
                    MediaAdapter.context.startActivity(Intent.createChooser(share, "Share with"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }
}
