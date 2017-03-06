package com.example.basius.cameraapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    //Variable per a el path de la foto
    String mCurrentPhotoPath;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Button camera = (Button) view.findViewById(R.id.button_camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        Button video = (Button) view.findViewById(R.id.button_video);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakeVideoIntent();
            }
    });
        return view;
    }


    private File createImageFile(String extencio, File storageDir) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                extencio,         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.d("DEBUG",mCurrentPhotoPath);
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                photoFile = createImageFile(".jpg",storageDir);
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

        // start the Video Capture Intent
        startActivityForResult(takeVideoIntent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File videoFile = null;
            try {
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                videoFile = createImageFile(".mp4",storageDir);
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (videoFile != null) {
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(videoFile));
                startActivityForResult(takeVideoIntent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
            }
        }
    }

}
