package com.example.havan.mytrafficmap.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.havan.mytrafficmap.R;

import org.androidannotations.annotations.Click;

/**
 * Created by NTT on 3/17/2017.
 */

public class YNDialogManager {

    boolean re = false;
    public boolean showNYDialog(Activity activity, String title, String message) {


        final Dialog alertDialog = new Dialog(activity);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(
                                android
                                        .graphics
                                        .Color
                                        .TRANSPARENT
                        )
                );
        alertDialog.setCancelable(false);
        alertDialog.setContentView(R.layout.yes_no_dialog_layout);

        TextView tvTitle = (TextView) alertDialog.findViewById(R.id.tv_title);
        tvTitle.setText(title);

        TextView tvContent = (TextView) alertDialog.findViewById(R.id.tv_content);
        tvContent.setText(message);

        ImageButton dialogButton = (ImageButton) alertDialog.findViewById(R.id.btn_ok_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re = true;
                alertDialog.dismiss();

            }
        });

        ImageButton cancelButton = (ImageButton) alertDialog.findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re = false;
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        return re;
    }
}
