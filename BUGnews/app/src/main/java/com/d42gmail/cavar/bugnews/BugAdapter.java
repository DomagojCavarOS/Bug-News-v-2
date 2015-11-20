package com.d42gmail.cavar.bugnews;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Enigma on 19.11.2015..
 */
public class BugAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<Bug> list1;

    public BugAdapter(Context ctx, ArrayList<Bug> list1) {
        this.ctx = ctx;
        this.list1 = list1;
    }

    @Override
    public int getCount() {
        return list1.size();
    }

    @Override
    public Object getItem(int position) {
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

        ImageView image = (ImageView) convertView.findViewById(R.id.imagelist);
        TextView title = (TextView) convertView.findViewById(R.id.titlelist);
        TextView descripton= (TextView) convertView.findViewById(R.id.descriptionlist);

        image.setImageURI(Uri.parse(current.getImageurl()));
        title.setText("" + current.getTitle());
        descripton.setText(""+current.getDescription());
        Log.i("aa","aasd:"+current.getDescription());

        return convertView;
    }
}
