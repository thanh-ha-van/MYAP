package com.example.havan.mytrafficmap.Route;

/**
 * Created by HaVan on 3/9/2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.havan.mytrafficmap.R;
import com.example.havan.mytrafficmap.Route.RouteModel;


import java.util.List;

public class CustomRouteAdapter extends ArrayAdapter<RouteModel> {

    private LayoutInflater inflater;
    public List<RouteModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtAddress;
        ImageView imgChoice;
    }

    public CustomRouteAdapter(List<RouteModel> data, Context context) {
        super(context, R.layout.list_route_single_item, data);
        this.dataSet = data;
        this.mContext = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RouteModel routeModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(
                    R.layout.list_route_single_item,
                    parent,
                    false
            );

            viewHolder.txtName = (TextView)
                    convertView.findViewById(R.id.name);
            viewHolder.txtAddress = (TextView)
                    convertView.findViewById(R.id.address);
            viewHolder.imgChoice = (ImageView)
                    convertView.findViewById(R.id.chossing);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setTextColor(
                routeModel.
                        isChecked() ?
                        Color.parseColor("#5A9B97") :
                        Color.parseColor("#3F4B53"));

        viewHolder.txtName.setTypeface(null, Typeface.BOLD );

        viewHolder.imgChoice.setVisibility(
                routeModel.isChecked() ?
                        View.VISIBLE :
                        View.GONE);

        viewHolder.txtName.setText(routeModel.getName());
        viewHolder.txtAddress.setText(routeModel.getAddress());
        // Return the completed view to render on screen
        return convertView;
    }

    public void setCheck(int position) {
        for (int i = 0; i < getCount(); i++) {
            getItem(i).setChecked(false);

        }
        getItem(position).setChecked(true);
        notifyDataSetChanged();
    }

    @Override
    public RouteModel getItem(int position) {
        return super.getItem(getCount() - position - 1);
    }

}