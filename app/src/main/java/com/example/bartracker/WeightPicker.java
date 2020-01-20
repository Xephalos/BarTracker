package com.example.bartracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class WeightPicker extends View
{
    Bitmap _originalPicture; // the picture we want to choose where the weights are in
    int _width;
    int _height;
    float _circleCenterX;
    float _circleCenterY;
    int _circleRadius;
    /**
     * constructor that calls the view constructor
     * @param context the context we use it in
     * @param attrs the atttributes
     */
    public WeightPicker(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * draws the view
     * @param canvas the canvas we use to draw
     */
    protected void onDraw(Canvas canvas)
    {
        //first we want to get a resized version of our original bitmap, then we want to paint the whole bitMap to the View
        Bitmap bitmap = Bitmap.createScaledBitmap(_originalPicture, _width, _height, true);
        Rect viewBorder = new Rect(0, 0, _width, _height);
        canvas.drawBitmap(bitmap, null, viewBorder, null);
    }

    /**
     * is called from onTouchEvent and draws a circle in the foreground accordingly
     * @param canvas the canvas wew use to draw
     */
    public void onDrawForeground (Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setARGB(255, 255, 255, 255);
        canvas.drawCircle(_circleCenterX, _circleCenterY, _circleRadius, paint);
    }

    /**
     * recalculates the size of the View
     * @param w the new width
     * @param h the new height
     * @param oldw the old width
     * @param oldh the old height
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
         _width = w;
         _height = h;
    }

    /**
     * resets the _picture we want to find the weigths in
     * @param bitmap the bitmap we want to save
     */
    public void setBitmap(Bitmap bitmap)
    {
        _originalPicture = bitmap;
    }

    /**
     * takes touchscreen movement and draws a circle according to the touch
     * @param event the keytouchevent that occured
     * @return
     */
    public boolean onTouchEvent(MotionEvent event)
    {
        //first we choose whether a key is pressed up or down
        int action = event.getActionMasked();
        switch(action)
        { //if pressed down we set this as the original circle center
            case (MotionEvent.ACTION_DOWN) :
                _circleCenterX = event.getX();
                _circleCenterY = event.getY();
                return true;
            case (MotionEvent.ACTION_UP) : //otherwise we calculate the circle radius and update the view
                _circleRadius = calculateRadius(event.getX(), event.getY());
                invalidate();
                return true;
        }
        return false;
    }

    /**
     * calculates the radius of the circle via pythagoras
     * @param endX the x coordinate of one border point
     * @param endY the y coordinate of one border point
     * @return
     */
    private int calculateRadius(float endX, float endY)
    {
        //first we calculate the width and hightdistance from the center of the circle to the given border point
        //then we use pythagoras to calculate the actual distance
        double widthDistance = Math.abs(_circleCenterX - endX);
        double heightDistance = Math.abs(_circleCenterY - endY);
        int radius = (int) Math.sqrt(widthDistance * widthDistance + heightDistance * heightDistance);
        return radius;
    }
}
