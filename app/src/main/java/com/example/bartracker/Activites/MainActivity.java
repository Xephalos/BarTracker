package com.example.bartracker.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.bartracker.R;
import com.example.bartracker.JavaClasses.WeightPicker;

/**
 * this class is the mainactivity and is called when the app starts
 */
public class MainActivity extends AppCompatActivity
{
    private Uri _video;
    private static int VIDEOCODE = 1000;
    static String XPOINT = "XPOINT";
    static String YPOINT = "YPOINT";
    static String RADIUS = "RADIUS";
    static String VIDEO = "VIDEO";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pickVideo();
    }

    /**
     * asks the user to pick a video
     */
    private void pickVideo()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, VIDEOCODE );

    }

    /**
     * saves the video and changes the image of the view to that
     * @param requestCode the requestcode
     * @param resultCode the resultcode
     * @param data the video that was picked
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data); //needs to be called but doesnt do anything
        if(resultCode == Activity.RESULT_OK)
        {
            //first we get the frist frame
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(this, data.getData());
            Bitmap picture = mediaMetadataRetriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST);
            //now we set the frame and save the uri
            WeightPicker weightPicker = findViewById(R.id.Mainactivity_WeightPicker);
            weightPicker.setBitmap(picture);
            _video = data.getData();
        }
    }

    /**
     * finishes this activity and passes the circle next to the AnalysedLiftDisplay Activity
     * @param view the button we choose to do the function on
     */
    public void chooseCircle(View view)
    {
       //first we get the values
        WeightPicker weightPicker = findViewById(R.id.Mainactivity_WeightPicker);
        float x = weightPicker.getcircleX();
        float y = weightPicker.getcircleY();
        float radius = weightPicker.getcircleRadius();

        //then we start the AnalysedLiftDisplayActivity
        Intent intent = new Intent(this, AnalysedLiftDisplayActivity.class);
        intent.putExtra(XPOINT, x);
        intent.putExtra(YPOINT, y);
        intent.putExtra(RADIUS, radius);
        intent.putExtra(VIDEO, _video);
        startActivity(intent);
    }
}
