package com.example.havan.mytrafficmap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Window;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

import java.lang.ref.WeakReference;

@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {

    //Private declarations
    private InternalHandler mHandler;
    private long mStartTime;

    //Constants
    private static final int GO_AHEAD = 1;
    private static final long MAX_TIME = 2000L;


    @AfterViews
    public void afterViews() {

        mStartTime = SystemClock.uptimeMillis();
        mHandler = new InternalHandler(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final Message goAHeadMessage = mHandler.obtainMessage(GO_AHEAD);
        mHandler.sendMessageAtTime(goAHeadMessage, mStartTime + MAX_TIME);

    }

    private void endSplash() {
        Intent main = new Intent(this, MainActivity_.class);
        startActivity(main);
        finish();

    }


    private static class InternalHandler extends Handler {

        private WeakReference<SplashActivity> mSAWeakRef;

        public InternalHandler(SplashActivity mSARef) {
            this.mSAWeakRef = new WeakReference<SplashActivity>(mSARef);
        }

        @Override
        public void handleMessage(Message msg) {
            final SplashActivity mActivity = mSAWeakRef.get();
            if (mActivity == null)
                return;
            switch (msg.what) {
                case GO_AHEAD:
                    long elapsedTime = SystemClock.uptimeMillis() - mActivity.mStartTime;
                    if (elapsedTime >= MAX_TIME) {
                        mActivity.endSplash();
                    }
                    break;
            }
        }
    }
}