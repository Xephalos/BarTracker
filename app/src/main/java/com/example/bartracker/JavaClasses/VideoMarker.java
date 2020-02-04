package com.example.bartracker.JavaClasses;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;

import org.jcodec.api.SequenceEncoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * this class take a video of a lift  and a circle of the weights and marks wherevever the weight is
 */
public class VideoMarker
{
    private float _circleX;
    private float _circleY;
    private float _radius;
    private int _intRadius;
    private ArrayList<ArrayList<Color>> _circleColor;
    private ArrayList<Bitmap> _frameList = new ArrayList<Bitmap>();


    /**
     * takes a video and a circle of the weights and tracks the weigths. only works correctly if the video has 60fps
     * @param video the video u want to mark as an uri
     * @param circleX the initial x position of the circle
     * @param circleY the initial y positon of the circle
     * @param radius the radius of the circle
     * @return all the images from the original video marked in a list
     */
    public ArrayList<Bitmap> markLift(Uri video, float circleX, float circleY, float radius) throws IOException
    {
        //first we want to save the first circle and also get the radius as an int
        _circleX = circleX;
        _circleY = circleY;
        _radius = radius;
        _intRadius = (int) radius;

        //then we add the circle for each frame of the video
        //but first we have to get the mediaplayer and the mediametaretriever
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(video.getPath()); //to get each frame
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(video.getPath());
        int length = mediaPlayer.getDuration(); //for the length of the video
        int amountFrame = length*60; //since we are dealing with 60fps
        workFirstFrame(mediaMetadataRetriever);
        for(int counter = 1; counter < amountFrame; counter++)
        {
            Bitmap frame = mediaMetadataRetriever.getFrameAtTime(counter*1666 , MediaMetadataRetriever.OPTION_CLOSEST); //since the video is 60fps, there is a new frame every 0.016666 seconds so every 1666 mikroseconds
            setNewCirclePositon(frame); // we need to use unedited bitmap in here so we calculate the video without the circle
            setColorAtCircle(frame);
            drawCircle(frame);
            _frameList.add(frame);
        }
        return _frameList;
    }



    /**
     * draws the circle on the first frame since that cant be done in a foor loop
     * @param mediaMetadataRetriever the mediametadata retreiver that is used for the other frames
     */
    private void workFirstFrame(MediaMetadataRetriever mediaMetadataRetriever)
    {
        Bitmap frame = mediaMetadataRetriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST);
        setColorAtCircle(frame);
        drawCircle(frame);
        _frameList.add(frame);
    }

    /**
     * resets the circleposition for a new frame
     * @param frame we want to reset the circleposition for
     */
    private void setNewCirclePositon(Bitmap frame)
    {
        ArrayList<ArrayList<Integer>> colorDifference = new ArrayList<ArrayList<Integer>>();
        for(int counter = 0; counter < 22; counter++) //size 22 since we check 10 pixels in each direction
        {
            colorDifference.add(new ArrayList<Integer>());
        }


        //for every possible 10 pixels we want to check where we have to set the new middlepoint of the circle
        //a pixel is possible if it is less than 100 pixles far away from the x and y position of the current circle
        //we calculate the best new middlepoint by the sum of the difference of the colors of each pixel
        for(int xcounter = 0; xcounter < 200; xcounter+= 10)
        {
            for (int ycounter = 0; ycounter < 200; ycounter+= 10 )
            {
               colorDifference.get(xcounter).set(ycounter, calculateColorDifference(frame, (int) (xcounter + _circleX - 100), (int) (ycounter + _circleY -100)));
               // +_circleX -100 to get the actual point we want to focus on
            }
        }

        //now we take the smalles difference and the set that at the new circle middlepoint
        int newX = 0;
        int newY = 0;
        int minimumDifference = colorDifference.get(0).get(0);
        for(int xcounter = 0; xcounter < colorDifference.size(); xcounter++)
        {
            for(int ycounter = 0; ycounter < colorDifference.get(xcounter).size(); ycounter++)
            {
                if(colorDifference.get(xcounter).get(ycounter) < minimumDifference)
                {
                    minimumDifference = colorDifference.get(xcounter).get(ycounter);
                    newX = xcounter;
                    newY = ycounter;
                }
            }
        }

        _circleX = newX;
        _circleY = newY;
    }

    /**
     * caluclates the color Difference for one pixel of a Bitmap according to the current _circleColor
     * @param frame the frame we want to calculate the new circle for
     * @param xPosition the x position of the pixel
     * @param yPosition the y position of a pixel
     * @return
     */
    private Integer calculateColorDifference(Bitmap frame, int xPosition, int yPosition)
    {
        int result = 0;
        //we take each pixel around the PositonPixel and caluclate the difference in RGB
        for(int xcounter = 0; xcounter < 200; xcounter++)
        {
            for(int ycounter = 0; ycounter < 200; ycounter++)
            {
                if(distance(xcounter + xPosition -100, ycounter + yPosition -100) < _radius)
                {
                    int newColor = frame.getPixel(xcounter + xPosition -100, ycounter + yPosition -100);
                    int newRed = Color.red(newColor);
                    int newBlue = Color.red(newColor);
                    int newGreen = Color.green(newColor);

                    int originalColor = _circleColor.get((int) (xcounter + _circleX -100)).get((int) (ycounter + yPosition -100)).toArgb();
                    int originalRed = Color.red(originalColor);
                    int originalBlue = Color.red(originalColor);
                    int originalGreen = Color.green(originalColor);

                    result = newRed + newBlue + newGreen - originalRed - originalBlue - originalGreen;
                }
            }
        }
        return result;
    }

    /**
     * sets the _circleColor to the  color of the circle at the position _circleX, _circleY, and _radius
     * @param frame the current frame we want to
     */
    private void setColorAtCircle(Bitmap frame)
    {
        ArrayList<ArrayList<Color>> result = new ArrayList<ArrayList<Color>>();
        for(int counter = 0; counter < _intRadius + 1; counter++)
        {
            result.add(new ArrayList<Color>());
        }
        //we want to get all the colors of the pixels that are in the circle
        //to do that we take all the square with the  diameter of the square and only add all points which distance are less than radius units away from the origin point
        for(int xcounter = 0; xcounter < 2*_radius; xcounter++)
        {
            for( int ycounter = 0; ycounter < 2+ _radius; ycounter++)
            {
                if(distance(xcounter + _circleX - _radius, ycounter + _circleY -_radius) < _radius)
                {
                    int intColor  = frame.getPixel(xcounter, ycounter);
                    Color color = Color.valueOf(intColor);
                    result.get(xcounter).set(ycounter, color);
                }
            }
        }

        _circleColor = result;
    }

    /**
     * calculates the difference between a point and the current middle point of the circle
     * @param x the x value of the point
     * @param y the y value of the point
     * @return the distance as a float
     */
    private float distance(float x, float y)
    {
        //we just calcualte the width and heightdifference and then use pythagoras
        float widthDifference = Math.abs( x - _circleX);
        float heightDiffernece = Math.abs(y - _circleY);
        return (float) Math.sqrt(widthDifference*widthDifference + heightDiffernece*heightDiffernece);
    }

    /**
     * takes a bitmap and draws the current circle on it
     * @param frame the Bitmap you want to draw on
     */
    private void drawCircle(Bitmap frame)
    {
        Canvas canvas = new Canvas(frame);
        Paint paint = new Paint();
        paint.setARGB(255, 0,0,0);
        paint.setStrokeWidth(20);
        canvas.drawCircle(_circleX, _circleY, _radius, paint);
    }

}
