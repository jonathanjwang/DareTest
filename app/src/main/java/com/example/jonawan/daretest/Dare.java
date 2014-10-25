package com.example.jonawan.daretest;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by jonathanjwang on 10/20/14.
 */
@ParseClassName("Dare")
public class Dare extends ParseObject {


    public Dare() {
        // A default constructor is required.
    }

    public String getStatement() {
        return getString("statement");
    }

    public void setStatement(String statement) {
        put("statement", statement);
    }

    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setAuthor(ParseUser user) {
        put("author", user);
    }
    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }

    public void setPhotoFile(ParseFile file) {
        put("photo", file);
    }

    public ParseFile getThumbFile() {
        return getParseFile("thumb");
    }

    public void setThumbFile(ParseFile file) {
        put("thumb", file);
    }
    public int getUpvotes() {
        return getInt("upvotes");
    }
    public int getDownvotes() {
        return getInt("downvotes");
    }

    public void setOrientation(int orientation) {
        put("orientation", orientation);
    }
    public int getOrientation() {
        return getInt("orientation");
    }

}
