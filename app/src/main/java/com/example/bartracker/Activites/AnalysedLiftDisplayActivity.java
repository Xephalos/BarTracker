package com.example.bartracker.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bartracker.Activites.MainActivity;
import com.example.bartracker.R;

/**
 * this activtiy displays the anyalsed lift and other relevant data like velocity
 */
public class AnalysedLiftDisplayActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysed_lift_display);
        initalize();
    }

    /**
     * requests the data from prevoius activity and manages all other classes
     */
    private void initalize()
    {
        //first we get the circle  and the video
        Intent intent = getIntent();
        float circleX = intent.getFloatExtra(MainActivity.XPOINT, 0);
        float circleY = intent.getFloatExtra(MainActivity.YPOINT, 0);
        float radius = intent.getFloatExtra(MainActivity.RADIUS, 0);
    }
}
