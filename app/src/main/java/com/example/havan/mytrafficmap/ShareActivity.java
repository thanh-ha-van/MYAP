package com.example.havan.mytrafficmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

import java.util.Arrays;
import java.util.List;

@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_share)
public class ShareActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginManager manager;

    @AfterViews
    public void afterViews() {

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("publish_actions");

        manager = LoginManager.getInstance();

        manager.logInWithPublishPermissions(this, permissionNeeds);

        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                        .setContentTitle("My Traffic Map")
                        .setContentDescription("Demo sharing with facebook API")
                        .setContentUrl(Uri.parse("http://hd.wallpaperswide.com/thumbs/ha_long_bay_vietnam-t2.jpg"))
                        .setImageUrl(Uri.parse("http://hd.wallpaperswide.com/thumbs/ha_long_bay_vietnam-t2.jpg"))
                        .build();
                ShareDialog.show(ShareActivity.this,shareLinkContent);
                onBackPressed();

            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

}

