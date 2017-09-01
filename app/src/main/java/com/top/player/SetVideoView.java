package com.top.player;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.VideoView;

/**
 * Created by zym on 2017/5/19.
 */
public class SetVideoView extends VideoView {
    int defaultWidth = 1920;
    int defaultHeight = 1080;

    public SetVideoView(Context context) {
        super(context);
    }

    public SetVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SetVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(defaultWidth, widthMeasureSpec);
        int height = getDefaultSize(defaultHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
