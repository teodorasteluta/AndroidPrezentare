package com.example.navigationdrawer.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.navigationdrawer.MainActivity;
import com.example.navigationdrawer.R;

import java.io.File;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        mVideoView = findViewById(R.id.video_view);

        Intent intent = getIntent();
        String selectedMovie = intent.getStringExtra("motivational_video_1");

        // Load appropriate video based on selected movie
        String videoPath = getVideoPathForMovie(selectedMovie);

        mVideoView.setVideoPath(videoPath);
        mVideoView.start();
    }

    private String getVideoPathForMovie(String movie) {
        String filename = movie + ".mp4"; // Assuming the video file has the same name as the movie with a .mp4 extension
        String videoPath = "android.resource://" + getPackageName() + "/raw/" + filename;
        return videoPath;
    }

}

