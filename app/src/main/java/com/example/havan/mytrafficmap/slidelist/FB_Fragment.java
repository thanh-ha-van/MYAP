package com.example.havan.mytrafficmap.slidelist;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.havan.mytrafficmap.R;


@SuppressLint("NewApi")
public class FB_Fragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(R.layout.fb_fragment, container, false);

		return rootView;
	}

}
