package com.d42gmail.cavar.bugnews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Enigma on 19.11.2015..
 */
public class BugAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<Bug> list1;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    public BugAdapter(Context ctx, ArrayList<Bug> list1) {
        this.ctx = ctx;
        this.list1 = list1;
    }

    @Override
    public int getCount() {
        return list1.size();
    }

    @Override
    public Bug getItem(int position) {
        return list1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
           convertView=View.inflate(ctx,R.layout.itemlayout,null);
        }
        Bug current= list1.get(position);
        //Setup the ImageLoader, we'll use this to display our images
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ctx).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        //Setup options for ImageLoader so it will handle caching for us.
        options = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();
        ImageView image = (ImageView) convertView.findViewById(R.id.imagelist);
        imageLoader.displayImage(current.getImageurl(),image,options);

        TextView title = (TextView) convertView.findViewById(R.id.titlelist);
        TextView descripton= (TextView) convertView.findViewById(R.id.descriptionlist);




        title.setText("" + current.getTitle());
        descripton.setText(""+current.getDescription());
        Log.i("aa","aasd:"+current.getDescription());

        return convertView;
    }
}
