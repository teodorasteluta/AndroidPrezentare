package com.example.navigationdrawer.Navigation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.navigationdrawer.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SettingsFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 102;

    private ImageView mImageView;
    private Bitmap mBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mImageView = view.findViewById(R.id.image_view);


        // Initialize ImageView
        if (mImageView != null) {
            SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            String encodedImage = sharedPreferences.getString("image", "");
            if (!encodedImage.equals("")) {
                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                mImageView.setImageBitmap(decodedBitmap);
            }
        }

        // Button to select image
        Button selectImageButton = view.findViewById(R.id.select_image_button);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for permission to access external storage
                if (ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission to access external storage
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    // Launch image picker
                    launchImagePicker();
                }
            }
        });

        return view;
    }

    private void launchImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, launch image picker
                launchImagePicker();
            } else {
                // Permission denied
                Toast.makeText(getContext(), "Permission denied to access external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK && data != null) {
            // Get selected image
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                mBitmap = BitmapFactory.decodeStream(inputStream);
                if (mImageView != null) {
                    mImageView.setImageBitmap(mBitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        editor.putString("image", encodedImage);
        editor.apply();


    }


}
