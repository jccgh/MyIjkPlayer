package com.example.administrator.myijkplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class StandVideoActivity extends AppCompatActivity {

    private StandardGSYVideoPlayer mplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand_video);

        mplayer = (StandardGSYVideoPlayer)findViewById(R.id.standardGSYVideoPlayer);

        String url= "http://ws.acgvideo.com/b/d6/12585371-1.mp4?wsTime=1482836261&wsSecret2=56259685b432533b330a208710e38bfe&oi=3707299117&rate=700";
        mplayer.setUp(url,true,null,"标准播放器");
        mplayer.setNeedLockFull(true);

        mplayer.setIsTouchWiget(true);


    }
}
