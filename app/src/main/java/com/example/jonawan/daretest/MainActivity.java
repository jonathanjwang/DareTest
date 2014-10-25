package com.example.jonawan.daretest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView thumbImageView;
    EditText dareText;
    Bitmap thumbBitmap;
    private ParseFile thumbFile, photoFile;
    String mCurrentPhotoPath;
    Dare dareRow;
    int rotate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);
        setContentView(R.layout.activity_main);
        ParseAnalytics.trackAppOpened(getIntent());
        thumbImageView = (ImageView) findViewById(R.id.thumbnail);
        dareText = (EditText) findViewById(R.id.edit_message);
    }

    public void submitDare(View v) {
        setProgressBarIndeterminateVisibility(true);
        dareRow = new Dare();

        String dareMessage = dareText.getText().toString();
        dareRow.setStatement(dareMessage);

        // make thumb saveable in cloud
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] scaledThumbData = stream.toByteArray();
        thumbFile = new ParseFile("dare_thumb.jpg", scaledThumbData);

        File fPhoto = new File(mCurrentPhotoPath);
        byte[] scaledPhotoData = convertFileToByteArray(fPhoto);

        photoFile = new ParseFile("dare_photo.jpg", scaledPhotoData);

        photoFile.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("dare app", "problem saving photo");
                } else {
                    dareRow.setPhotoFile(photoFile);
                    thumbFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                dareRow.setThumbFile(thumbFile);
                                dareRow.put("upvotes", 0);
                                dareRow.put("downvotes", 0);
                                dareRow.put("orientation", rotate);
                                dareRow.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Log.d("dare app", "row saved!");
                                            setProgressBarIndeterminateVisibility(false);
                                            showFeedActivity();
                                        } else {
                                            Log.d("dare app", "problem saving row");
                                        }
                                    }
                                });
                                Log.d("dare app", "thumb saved");
                            } else {
                                Log.d("dare app", "problem saving thumb");
                            }
                        }
                    }

                    );
                    Log.d("dare app", "photo saved");
                }
            }
        });


    }

    public void showFeedActivity() {
        Intent feedListIntent = new Intent(this, FeedActivity.class);
        startActivity(feedListIntent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void takePhoto(View view) {
        dispatchTakePictureIntent();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            rotate = 0;
            try {
                File imageFile = new File(mCurrentPhotoPath);
                ExifInterface exif = new ExifInterface(
                        imageFile.getAbsolutePath());
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Bitmap largeBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            thumbBitmap = ThumbnailUtils.extractThumbnail(largeBitmap, 300, 300);
            thumbImageView.setRotation(rotate);
            thumbImageView.setImageBitmap(thumbBitmap);



        }
    }

    public static byte[] convertFileToByteArray(File f)
    {
        byte[] byteArray = null;
        try
        {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024*8];
            int bytesRead =0;

            while ((bytesRead = inputStream.read(b)) != -1)
            {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return byteArray;
    }

}
