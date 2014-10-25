package com.example.jonawan.daretest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by jonathanjwang on 10/24/14.
 */
public class DareViewActivity extends Activity {

    ParseImageView photoImageView;
    TextView statement;
    Dare dare;
    int orientation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dare_view);

        photoImageView = (ParseImageView)findViewById(R.id.photo);
     //   statement = (TextView)findViewById(R.id.text_statement);


        Bundle extras = getIntent().getExtras();
        String objectId ="";
        if (extras != null) {
            objectId = extras.getString("dareObjectId");
            String dareStatement = extras.getString("dareStatement");
            orientation = extras.getInt("orientation");
            Log.d("dare app", "dare statement: " + dareStatement);
//            statement.setText(dareStatement);
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Dare");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            dare = (Dare)object;
                            ParseFile pf = object.getParseFile("photo");
                            Log.d("dare app", "pf url: " + pf.getUrl());
                            photoImageView.setParseFile(pf);
                            photoImageView.setRotation(orientation);
                            photoImageView.loadInBackground();
                        } else {
                            Log.d("dare app", "dare object retrieval failed");
                        }
                    }
                });



    }
}
