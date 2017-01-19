package com.example.administrator.myijkplayer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.shuyu.gsyvideoplayer.GSYPreViewManager;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.CustomGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    private CustomGSYVideoPlayer mGSYVideoPlayer;
    private OrientationUtils mOrientationUtils;
    private boolean isplay;
    private boolean ispause;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGSYVideoPlayer = (CustomGSYVideoPlayer)findViewById(R.id.customvideo);

        //视屏播放地址，可以是本地的，也可以是网上的

        String filepath = getExternalFilesDir(null).getAbsolutePath()+ File.separator+"qwe"+File.separator;
        File file = new File(filepath);
        file.mkdirs();
        String url ="http://ws.acgvideo.com/0/d5/12538658-1.mp4?wsTime=1482759915&wsSecret2=f4e6fb54cb7a393a551364eeedcc2448&oi=2067324200&rate=6";
        mGSYVideoPlayer.setUp(url,true,file,"阿依哟额额");

        mOrientationUtils = new OrientationUtils(this,mGSYVideoPlayer);
        mOrientationUtils.setEnable(false);

        //设置触摸左半屏可以调节亮度，触摸右边可以调节音量
        mGSYVideoPlayer.setIsTouchWiget(true);
        //打开实时预览
        mGSYVideoPlayer.setOpenPreView(true);

        mGSYVideoPlayer.setRotateViewAuto(false);
        mGSYVideoPlayer.setLockLand(false);
        //是否需要设置全屏时的动画效果
        mGSYVideoPlayer.setShowFullAnimation(false);
        //设置全屏时锁住屏幕
        mGSYVideoPlayer.setNeedLockFull(true);

        mGSYVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                mOrientationUtils.resolveByClick();
                //这个方法表示设置为横向全屏
                //第二个参数设置为true隐藏actionbar
                //第三个参数设置为true表示隐藏statusbar(statusbar是显示信号的状态栏)
                mGSYVideoPlayer.startWindowFullscreen(MainActivity.this,true,true);
            }
        });

        mGSYVideoPlayer.setStandardVideoAllCallBack(new Samlistener(){

            @Override
            public void onPrepared(String s, Object... objects) {
                super.onPrepared(s, objects);
                mOrientationUtils.setEnable(true);
                isplay = true;

            }

            @Override
            public void onAutoComplete(String s, Object... objects) {
                super.onAutoComplete(s, objects);
                Toast.makeText(MainActivity.this,"播放完成",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickStartError(String s, Object... objects) {
                super.onClickStartError(s, objects);
            }

            @Override
            public void onQuitFullscreen(String s, Object... objects) {
                super.onQuitFullscreen(s, objects);
                if(mOrientationUtils!=null){
                    mOrientationUtils.backToProtVideo();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoPlayer.releaseAllVideos();
        GSYPreViewManager.instance().releaseMediaPlayer();
        if(mOrientationUtils!=null){
            mOrientationUtils.releaseListener();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ispause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ispause=false;
    }

    @Override
    public void onBackPressed() {
        if(mOrientationUtils!=null){
            mOrientationUtils.backToProtVideo();
        }
        if(StandardGSYVideoPlayer.backFromWindowFull(this)){
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isplay && !ispause) {
            if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
                if (!mGSYVideoPlayer.isIfCurrentIsFullscreen()) {
                    mGSYVideoPlayer.startWindowFullscreen(MainActivity.this, true, true);
                }
            } else {
                //新版本isIfCurrentIsFullscreen的标志位内部提前设置了，所以不会和手动点击冲突
                if (mGSYVideoPlayer.isIfCurrentIsFullscreen()) {
                    StandardGSYVideoPlayer.backFromWindowFull(this);
                }
                if (mOrientationUtils != null) {
                    mOrientationUtils.setEnable(true);
                }
            }
        }
    }

    public void doStandClick(View view) {
        startActivity(new Intent(MainActivity.this,StandVideoActivity.class));
    }
}
