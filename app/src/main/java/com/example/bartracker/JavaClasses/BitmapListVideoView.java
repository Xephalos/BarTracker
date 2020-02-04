package com.example.bartracker.JavaClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * takes a list of bitmaps as an input and displays them like a media player
 */
public class BitmapListVideoView extends View {
    List<Bitmap> _frameList;
    int _frameCounter;
    Bitmap _currentBitmap;

    public BitmapListVideoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    /**
     * sets the bitmaplist for this view
     * @param frameList the bitmpaList you want to display
     */
    public void setBitmapList(List<Bitmap> frameList)
    {
        _frameList = frameList;
    }

    /**
     * draws the current bitmap to the view
     * @param canvas
     */
    public void onDraw(Canvas canvas)
    {
        //Todo display each second

    }

    /**
     * increases the counter and resets the current Bitmap
     */
    private void increaseCOunter()
    {
        if(_frameCounter > _frameList.size() - 1)
        {
            return;
        }
        else
        {
            _frameCounter++;
            _currentBitmap = _frameList.get(_frameCounter);
        }
    }
}
