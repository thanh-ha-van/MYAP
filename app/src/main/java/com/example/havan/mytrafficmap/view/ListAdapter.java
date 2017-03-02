package com.example.havan.mytrafficmap.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.havan.mytrafficmap.R;

public class ListAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final String[] listItemsName;

    private final Integer[] imageName;

    public ListAdapter(Activity context, String[] content,
                       Integer[] ImageName) {

        super(context, R.layout.list_items, content);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.listItemsName = content;
        this.imageName = ImageName;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewSingle = inflater.inflate(R.layout.list_items, null, true);

        TextView listViewItems = (TextView) listViewSingle.findViewById(R.id.textView1);
        ImageView listViewImage = (ImageView) listViewSingle.findViewById(R.id.imageView1);

        listViewItems.setText(listItemsName[position]);
        listViewImage.setImageResource(imageName[position]);
        return listViewSingle;

    }

    ;

}