package com.example.jonawan.daretest;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.parse.ParseQueryAdapter;


public class FeedActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private ParseQueryAdapter<Dare> mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getListView().setClickable(false);

     //   setContentView(R.layout.activity_feed); // ?

/*
        mainAdapter = new ParseQueryAdapter<Dare>(this, Dare.class);
        mainAdapter.setTextKey("statement");
        mainAdapter.setImageKey("thumb");

        // Default view is all meals
        setListAdapter(mainAdapter);
        */

        mainAdapter = new FeedAdapter(this);
        mainAdapter.loadObjects();
        getListView().setOnItemClickListener(this);
        setListAdapter(mainAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed, menu);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Dare dare = mainAdapter.getItem(position);

        Intent viewDareDetails = new Intent(this, DareViewActivity.class);
        viewDareDetails.putExtra("dareObjectId", dare.getObjectId());
        viewDareDetails.putExtra("dareStatement", dare.getStatement());
        viewDareDetails.putExtra("orientation", dare.getOrientation());
        startActivity(viewDareDetails);
    }
}
