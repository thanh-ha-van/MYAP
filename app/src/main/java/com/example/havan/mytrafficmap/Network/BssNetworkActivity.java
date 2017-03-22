package com.example.havan.mytrafficmap.Network;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.havan.mytrafficmap.Network.Object.Account;
import com.example.havan.mytrafficmap.Network.Object.Data;
import com.example.havan.mytrafficmap.Network.Object.FullData;
import com.example.havan.mytrafficmap.Network.Service.DataService;
import com.example.havan.mytrafficmap.Network.Service.LoadSuccessListener;
import com.example.havan.mytrafficmap.Network.Service.LoginSuccessListener;
import com.example.havan.mytrafficmap.Network.Service.RestService;
import com.example.havan.mytrafficmap.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import bolts.Continuation;
import bolts.Task;

@Fullscreen
@EActivity(R.layout.activity_bss_network)
public class BssNetworkActivity extends AppCompatActivity {

    @ViewById(R.id.et_user)
    EditText etUser;

    @ViewById(R.id.et_pass)
    EditText etPass;

    @ViewById(R.id.tv_data)
    TextView tvData;

    @ViewById(R.id.btn_login)
    Button btnLogin;

    @Bean
    protected DataService dataService;

    @Bean
    protected DataService dataServiceLoad;

    @AfterViews
    protected void initViews() {

        final String user = etUser.getText().toString();
        final String pass = etPass.getText().toString();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataService
                        .loadData("password", user, pass)
                        .continueWith(new Continuation<FullData, Void>() {
                    @Override
                    public Void then(Task<FullData> task) throws Exception {
                        uiThread(task.getResult());
                        return null;
                    }
                });
            }
        });
    }

    @UiThread
    protected void uiThread(FullData listData) {

        for (Data data : listData.getData()) {
            tvData.append(
                    "\n"
                            + "ID: "
                            + data.getId()
                            + "\n"
                            + "CreatorId:"
                            + data.getCreatorId()
                            + "\n"

                    // The FullData Object content multiple variables. ID AND CreatorID
                    // is the 2 first variables.
                    // Wanna show more? just add more definition to Data and getter and setter.

            );
        }
    }

}
