package com.example.havan.mytrafficmap.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.havan.mytrafficmap.R;

/**
 * Created by HaVan on 5/14/2017.
 */

public class WaitingDialogManager {


    public Dialog alertDialog;

    public void showWaiting(Activity activity, String title, String message
    ) {

        alertDialog = new Dialog(activity);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setContentView(R.layout.waiting_dialog);
        TextView tvTitle = (TextView) alertDialog.findViewById(R.id.tv_title);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setText(title);
        TextView tvContent = (TextView) alertDialog.findViewById(R.id.tv_content);
        tvContent.setText(message);
        ImageButton titleIcon = (ImageButton) alertDialog.findViewById(R.id.title_icon);

        titleIcon.setImageResource(R.drawable.ic_info);

        alertDialog.show();

    }

    public void DismissDialog() {
        alertDialog.dismiss();
    }
}
