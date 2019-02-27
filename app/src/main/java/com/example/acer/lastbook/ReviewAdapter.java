package com.example.acer.lastbook;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by acer on 12/08/2016.
 */
public class ReviewAdapter extends BaseAdapter{

    private Review[] reviews;
    private Context context=null;

    public ReviewAdapter(Context context,Review[] reviews){

        this.reviews=reviews;
        this.context=context;

    }



    @Override
    public int getCount() {
        return this.reviews.length;
    }


    @Override
    public Object getItem(int position) {
        return reviews[position];
    }


    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
        {
            convertView=LayoutInflater.from(context).inflate(R.layout.review_layout,null);
        }

        Review r=(Review) getItem(position);

        TextView comm =(TextView)convertView.findViewById(R.id.vi1);
        comm.setText(r.getCommento());

        TextView a =(TextView)convertView.findViewById(R.id.vi2);
        a.setText("scritto da: "+r.getAutore());

        return convertView;
    }
}
