/*
This is the activity containing the user's full list of notes. It will populate the list as well
as allowing the user to search through their existing notes using the date, or the location the note
was taken at.
 */
package com.c323proj6.CodyRidener;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


public class listActivity extends AppCompatActivity implements TextWatcher {
    private ArrayList<Note> notes = new ArrayList<>();
    private sqLiteHandler sql;
    private ListView list;
    public TextView search;
    @Override
    // Instantiates the variables above as well as adds the text changed listener to the search bar
    // that will repopulate the list based on what is inputted into the search bar.
    public void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.note_list_layout);
        sql = new sqLiteHandler(this);
        list = findViewById(R.id.noteList);
        search = findViewById(R.id.textSearch);
        search.addTextChangedListener(this);
        // Populates the list of notes using the SQL database created above.
        populateNotes(null);

    }

    public void populateNotes(@Nullable String specs) {
            // Creates an empty notes list so that when the user searches the notes in the list
            // before don't stay in the list making the search useless.
            notes = new ArrayList<>();
            // Calls the function to get all of the notes based on the 'specs' or the stuff the
            // the user inputted into the search bar.
            Cursor c = sql.getAll(specs);
            // Uses the cursor move to next to iterate through all of the available rows within the
            //function.
            while (c.moveToNext()) {
                String[] vals = new String[c.getColumnCount()];
                // A for loop to iterate every column of the cursor to log the information into
                // the 'vals' array which will temporaryily store all of the row's data before we
                // put it into a note object within the notes array.
                for (int i = 0; i < c.getColumnCount() - 1; i++) {
                    vals[i] = c.getString(i);
                }
                Note temp = new Note(vals[2], vals[0], c.getInt(c.getColumnCount() - 1), vals[1], vals[3]);
                notes.add(temp);

            }
            // Displays that there are no notes if the notes list is empty.
            if(notes.isEmpty()){
                    list.setAdapter(new ArrayAdapter<String>(this, R.layout.empty_list,R.id.emptyView, new String[]{"No Notes"}));
            }else {
                // Populates the list with all the notes from the notes list if notes isn't empty.
                populateList();
            }
    }

    private void populateList() {
        // Creates an instance of our adapter class then set it as the adapter for our list view.
        NoteAdapter adapt = new NoteAdapter(this,notes,sql);
        list.setAdapter(adapt);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    // Updates the results of the list when the user changes the search text field.
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        populateNotes(search.getText().toString());

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
