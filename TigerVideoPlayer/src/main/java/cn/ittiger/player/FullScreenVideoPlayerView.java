package cn.ittiger.player;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import cn.ittiger.player.state.ScreenState;
import cn.ittiger.player.util.Utils;

/**
 * 当要通过一个Activity直接全屏播放某个视频，请使用此PlayerView
 * 播放结束或者点击顶部的返回按钮均会直接退出播放
 *
 * @author: ylhu
 * @time: 2017/12/5
 */
public class FullScreenVideoPlayerView extends VideoPlayerView {

    public FullScreenVideoPlayerView(Context context) {

        super(context);
    }

    public FullScreenVideoPlayerView(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    public FullScreenVideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
    }

    public FullScreenVideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void startPlayVideo() {

        if(canPlay() == false) {
            Toast.makeText(getContext(), R.string.vp_no_network, Toast.LENGTH_SHORT).show();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((Activity)getContext()).finish();
                }
            }, 500);
        } else {
            fullScreenCanPlay();
        }
    }

    private void fullScreenCanPlay() {

        forceFullScreen();
        viewStubFullScreenGestureView();
        Utils.hideViewIfNeed(mVideoFullScreenView);
        Utils.hideViewIfNeed(mVideoFullScreenBackView);
        PlayerManager.getInstance().setScreenState(mCurrentScreenState = ScreenState.SCREEN_STATE_FULLSCREEN);
        play();
    }

    private void forceFullScreen() {

        if(Utils.getActivity(getContext()).getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {//不是横屏
            Utils.getActivity(getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        Utils.getActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
    }

    @Override
    protected void onFullScreenHeaderBackClick(View view) {

        Utils.getActivity(getContext()).finish();
    }

    @Override
    protected void onChangeLogicCompleteState() {

        stopVideoProgressUpdate();
        Toast.makeText(getContext(), "Play complete", Toast.LENGTH_SHORT).show();
        Utils.getActivity(getContext()).finish();
    }
}
