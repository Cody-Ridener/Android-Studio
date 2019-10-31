/*Created on 10/29/2019
This is a class used to store information for each of the different notes that the user has saved.
This information includes
The date that the note was created on.
The content of the note.
The database ID for the note (This will be useful when removing notes from the database.)
The address the user was at when the note was created
The background color for the note.
 */



package com.c323proj6.CodyRidener;
import android.util.Log;
public class Note {
    private String date;
    private String content;
    private int id;
    private String address;
    private String color;
    // A basic constructor for the note that instantiates all of the above variables.
    public Note(String d, String c, int i, String a,String col){
        date = d;
        content = c;
        id = i;
        address = a;
        color = col;
        Log.d("coke",Integer.toString(id));
    }
    // A list of get methods that can be used to retrieve each of the elements of the note.
    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getColor() {
        return color;
    }
}
