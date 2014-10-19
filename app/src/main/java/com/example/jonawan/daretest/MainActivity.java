package com.example.jonawan.daretest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParsePush;

import java.util.LinkedList;


public class MainActivity extends Activity implements View.OnClickListener {

    Button cameraButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView mImageView, thumbImageView;
    EditText dareText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraButton = (Button) findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(this);

        thumbImageView = (ImageView) findViewById(R.id.thumbnail);

        Button btn = (Button) findViewById(R.id.submit_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFancyMethod(v);
            }
        });

        dareText = (EditText) findViewById(R.id.edit_message);


    }

    public void myFancyMethod(View v) {

        String dareMessage = dareText.getText().toString();

        LinkedList<String> channels = new LinkedList<String>();
        channels.add("DareChannel");

        ParsePush push = new ParsePush();
        push.setChannels(channels);
        push.setMessage(dareMessage);
        push.sendInBackground();
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


    @Override
    public void onClick(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            thumbImageView.setImageBitmap(imageBitmap);
        }
    }

}
