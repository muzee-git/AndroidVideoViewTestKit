package net.muzee.vvt;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoViewTestKitActivity extends Activity {

    String TAG = VideoViewTestKitActivity.class.getSimpleName();
    VideoView mVideoView;
    MediaController mController;
    BroadcastReceiver mCommander;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mVideoView = (VideoView) findViewById(R.id.displayView);
        mController = new MediaController(this);
        mController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mController);
        mCommander = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.containsKey("u")) {
                    final String url = bundle.getString("u");
                    Log.i(TAG, "set-video-path: " + url);
                    mVideoView.setVideoPath(url);
                    if (mVideoView.isActivated()) {
                        mVideoView.stopPlayback();
                    }
                    mVideoView.start();
                }
            }
        };
        registerReceiver(mCommander, new IntentFilter("mpctrl"));
    }

    @Override
    protected void onPause() {
        mVideoView.stopPlayback();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mCommander);
        super.onDestroy();
    }

}