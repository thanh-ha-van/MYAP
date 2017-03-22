package com.example.havan.mytrafficmap.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.havan.mytrafficmap.R;

public class AlertDialogManager {

    public void showAlertDialog(Activity activity, String title, String message,
                                int status) {

        final Dialog alertDialog = new Dialog(activity);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setContentView(R.layout.alert_dialog_layout);

        TextView tvTitle = (TextView) alertDialog.findViewById(R.id.tv_title);
        tvTitle.setText(title);

        TextView tvContent = (TextView) alertDialog.findViewById(R.id.tv_content);
        tvContent.setText(message);

        ImageButton titleIcon = (ImageButton) alertDialog.findViewById(R.id.title_icon);

        if (status == 1)
            titleIcon.setImageResource(R.drawable.success);
        if (status == 2)

            titleIcon.setImageResource(R.drawable.failed);
        if (status == 3)

            titleIcon.setImageResource(R.drawable.ic_info);

        ImageButton dialogButton = (ImageButton) alertDialog.findViewById(R.id.btn_ok_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();


    }
}

