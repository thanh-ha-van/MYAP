package com.example.havan.mytrafficmap.view;

/**
 * Created by HaVan on 3/9/2017.
 */
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.havan.mytrafficmap.R;
import com.example.havan.mytrafficmap.SQLite.DataModel;

import java.util.List;


/**
 * Created by NTT on 3/7/2017.
 */

public class CustomAdapter extends ArrayAdapter<DataModel> {

    public List<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtAddress;
        ImageView info;
    }

    public CustomAdapter(List<DataModel> data, Context context) {
        super(context, R.layout.fav_list_single_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.fav_list_single_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtAddress = (TextView) convertView.findViewById(R.id.address);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtAddress.setText(dataModel.getAddress());
        // Return the completed view to render on screen
        return convertView;
    }
}