/* Created on 10/29/2019
This is a custom handler used by this app to add, read, and erase information from the SQLite
database associated with this app.
This is the main handler for passing information between activities.
 */
package com.c323proj6.CodyRidener;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class sqLiteHandler  extends SQLiteOpenHelper {
    SQLiteDatabase read;
    SQLiteDatabase write;
    // Creates a database with the name NoteKeeper, if the database already exists then the handler
    // will automatically use the pre-existing database instead.
    public sqLiteHandler(Context context){ super(context, "NoteKeeper", null, 1);
    // Creates an instance to write to the database than another one to read from the database.
        // These will be use in later function of the handler.
       write = this.getWritableDatabase();
       read = this.getReadableDatabase();
        onCreate(write);
    }
    @Override
    //When the handler is created it attempts to create a notes table within the notekeeper database
    // If an exception is thrown that means the table exists and we don't need to take an action.
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table notes (content text, place text,date text,color varchar(12), id integer primary key autoincrement)");
        }catch(SQLException e){

        }

    }
    // A function that can be used to get all of the rows from the Notes table. It also accept a
    // parameter that will be used to refine the results retrieved from the database.
    public Cursor getAll(@Nullable String conds){
        if(conds == null) {
            // Selects all rows from the database if the conds input is null.
            return read.query("notes", new String[]{"content", "place", "date", "color", "id"}, null, null, null, null, "id", null);
        }else {
            // If the conds input is not null it will look at the database and check if there are
            // any rows that have matching parts at the beginning of the place or date strings and returns
            // those if they exist.
            return read.query("notes", new String[]{"content", "place", "date", "color", "id"}, "date like ? or place like ?", new String[] {conds+"%",conds+"%"}, null, null, "id", null);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void record(String content, String place, String date, String color){
        write.execSQL("insert into notes(content, place, date,color) values('"+content+"','" + place + "'" + ",'" + date+"','" + color +"')");
    }
    public void removeRow(String id){
        write.execSQL("delete from notes where id ="+id);
    }

}
