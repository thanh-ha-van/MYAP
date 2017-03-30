package com.example.havan.mytrafficmap.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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

    public String getName(int position) {
        String my_new_str = navSpinner.get(position).getTitle().replaceAll(" ", "_").toLowerCase();
        return my_new_str;
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

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/SVN-Aguda Bold.otf");
        txtTitle.setTypeface(tf);

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
        navSpinner.add(new SpinnerItem("airport", R.drawable.airport));
        navSpinner.add(new SpinnerItem("atm", R.drawable.atm));
        navSpinner.add(new SpinnerItem("bank", R.drawable.bank));
        navSpinner.add(new SpinnerItem("bar", R.drawable.bar));
        navSpinner.add(new SpinnerItem("bus stop", R.drawable.bus));
        navSpinner.add(new SpinnerItem("cafe", R.drawable.cafe));
        navSpinner.add(new SpinnerItem("car repair",R.drawable.car));
        navSpinner.add(new SpinnerItem("car wash", R.drawable.car_wash));
        navSpinner.add(new SpinnerItem("church", R.drawable.church));
        navSpinner.add(new SpinnerItem("gas station", R.drawable.ic_local_gas_station));
        navSpinner.add(new SpinnerItem("hospital", R.drawable.hospital));
        navSpinner.add(new SpinnerItem("hotel", R.drawable.hotel));
        navSpinner.add(new SpinnerItem("library", R.drawable.library));
        navSpinner.add(new SpinnerItem("movie theater", R.drawable.ic_movie));
        navSpinner.add(new SpinnerItem("parking", R.drawable.parking));
        navSpinner.add(new SpinnerItem("police", R.drawable.police));
        navSpinner.add(new SpinnerItem("store", R.drawable.ic_store));
        navSpinner.add(new SpinnerItem("supermarket", R.drawable.supermarket));
        navSpinner.add(new SpinnerItem("university", R.drawable.university));
    }
}