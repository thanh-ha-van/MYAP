package com.example.havan.mytrafficmap.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.havan.mytrafficmap.R;


public class TitleNavigationAdapter extends BaseAdapter {

    private ImageView imgIcon;
    private ArrayList<SpinnerItem> navSpinner;

    private TextView txtTitle;

    private ArrayList<SpinnerItem> spinnerNavItem;

    private Context context;

    public TitleNavigationAdapter(Context context) {

        addBar();
        this.spinnerNavItem = navSpinner;
        this.context = context;
    }

    @Override
    public int getCount() {
        return spinnerNavItem.size();
    }

    @Override
    public Object getItem(int index) {
        return spinnerNavItem.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.
                            getSystemService(
                                    Activity.LAYOUT_INFLATER_SERVICE
                            );
            convertView = mInflater.inflate(R.layout.list_item_title_navigation, null);
        }

        imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
        txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);

        imgIcon.setImageResource(spinnerNavItem.get(position).getIcon());
        imgIcon.setVisibility(View.VISIBLE);
        txtTitle.setText(spinnerNavItem.get(position).getTitle());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item_title_navigation, null);

        }

        imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
        txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);

        imgIcon.setImageResource(spinnerNavItem.get(position).getIcon());
        txtTitle.setText(spinnerNavItem.get(position).getTitle());
        return convertView;
    }

    private void addBar() {
        // Spinner title navigation data
        navSpinner = new ArrayList<SpinnerItem>();
        navSpinner.add(new SpinnerItem("Airport", R.drawable.airport));
        navSpinner.add(new SpinnerItem("Atm", R.drawable.atm));
        navSpinner.add(new SpinnerItem("Bank", R.drawable.bank));
        navSpinner.add(new SpinnerItem("Bar", R.drawable.bar));
        navSpinner.add(new SpinnerItem("Cafe", R.drawable.cafe));
        navSpinner.add(new SpinnerItem("Church", R.drawable.church));
        navSpinner.add(new SpinnerItem("Food", R.drawable.food));
        navSpinner.add(new SpinnerItem("Hospital", R.drawable.hospital));
        navSpinner.add(new SpinnerItem("Hotel", R.drawable.hotel));
        navSpinner.add(new SpinnerItem("Library", R.drawable.library));
        navSpinner.add(new SpinnerItem("Police", R.drawable.police));
        navSpinner.add(new SpinnerItem("Supermarket", R.drawable.supermarket));
        navSpinner.add(new SpinnerItem("Theatre", R.drawable.ic_movie));
    }
}