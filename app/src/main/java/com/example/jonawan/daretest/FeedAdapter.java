package com.example.jonawan.daretest;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.Arrays;

/**
 * Created by jonathanjwang on 10/22/14.
 */
public class FeedAdapter extends ParseQueryAdapter<Dare> {

    public FeedAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Dare>() {
            public ParseQuery<Dare> create() {
                // Here we can configure a ParseQuery to display
                // only top-rated meals.
                ParseQuery query = new ParseQuery("Dare");

                query.whereContainedIn("upvotes", Arrays.asList(0));
                query.orderByDescending("createdAt");
                return query;
            }
        });
    }

    @Override
    public View getItemView(Dare dare, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.dares_list, null);
        }

        super.getItemView(dare, v, parent);

        ParseImageView dareImage = (ParseImageView) v.findViewById(R.id.photo);
        ParseFile photoFile = dare.getParseFile("thumb");
        if (photoFile != null) {
            dareImage.setRotation(dare.getOrientation());
            dareImage.setParseFile(photoFile);
            dareImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    // nothing to do
                }
            });
        }

        Resources res = v.getResources();
        TextView titleTextView = (TextView) v.findViewById(R.id.text_statement);
        titleTextView.setText(dare.getStatement());

        TextView upvotesTextView = (TextView) v.findViewById(R.id.text_upvotes);
        upvotesTextView.setText(""+dare.getUpvotes()+res.getString(R.string.upvote_text_glyph));

        TextView downvotesTextView = (TextView) v.findViewById(R.id.text_downvotes);
        downvotesTextView.setText(""+dare.getDownvotes()+res.getString(R.string.downvote_text_glyph));
        return v;
    }


}
