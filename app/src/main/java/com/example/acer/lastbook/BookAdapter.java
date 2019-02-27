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


public class BookAdapter extends BaseAdapter{

    private Book[] books=null;
    private Context context=null;
    WindowManager manager;

    public BookAdapter(Context context, Book[] books,WindowManager m){

        this.books=books;
        this.context=context;
        this.manager=m;
    }



    @Override
    public int getCount() {
       return this.books.length;
    }


    @Override
    public Object getItem(int position) {
        return books[position];
    }


    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
        {
            convertView=LayoutInflater.from(context).inflate(R.layout.book_layout,null);
        }
        Book bk=(Book) getItem(position);

        TextView v =(TextView)convertView.findViewById(R.id.textView);
        v.setText(bk.getTitolo());

        ImageView img =(ImageView)convertView.findViewById(R.id.imageView);

        DisplayMetrics metrics = new DisplayMetrics();
        this.manager.getDefaultDisplay().getMetrics(metrics);
        double h= metrics.heightPixels*0.2;

        img.getLayoutParams().height= (int) h;

        img.setImageDrawable(bk.getImmagine());
        img.requestLayout();

        return convertView;
    }
}
