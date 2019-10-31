/*Created on: 10/29/2019
This is a custom ArrayAdapter that can be user to populate a list view.
This Adapter takes the context that it was created in, A list of Note objects, and an SQL database
handler.
 */
package com.c323proj6.CodyRidener;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class NoteAdapter extends ArrayAdapter<Note> {
    private Context mContext;
    private sqLiteHandler sqlH;
    private ArrayList<Note> notesList;
    // A constructor that instantiates all of the items declared above.
    public NoteAdapter(@NonNull Context context, ArrayList<Note> list, sqLiteHandler sql){
        super(context,0,list);
        mContext = context;
        notesList = list;
        sqlH = sql;

    }
    @NonNull
    @Override
    // This function takes the current position of the adapter, the view, and the view group so
    // that it can populate all of the views inside of the list.
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup par){
        // Sets the view we wish to convert as the item.
        View item = convertView;
        // If the view we're converting is null we will inflate it using the current view group and
        // the custom list_item layout that will determine how the list will look.
        if(item == null){
            item = LayoutInflater.from(mContext).inflate(R.layout.list_item, par,false);
        }
        // Uses the position provided to get the current note we're using from the note list that
        // we got when the adapter was constructed.
        final Note current = notesList.get(pos);
        // Instantiates all the views inside of the layout and gives them values based on the user
        // defined notes that we have.
        TextView addressDate = item.findViewById(R.id.dateAddressText);
        TextView content = item.findViewById(R.id.Content);
        ImageButton but = item.findViewById(R.id.deleteBtn);
        TextView id = item.findViewById(R.id.sqlID);
        View layout = item.findViewById(R.id.itemLayout);
        addressDate.setText(current.getAddress()+ " , " + current.getDate());
        content.setText(current.getContent());
        id.setText(Integer.toString(current.getId()));
        // Sets the on click listener for the button that we will use to delete the current list item
        // from the database.
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This line removes the current list item from the database using the note's unique
                // ID
                sqlH.removeRow(Integer.toString(current.getId()));
                // This will call a function in the listActivity class that repopulates the list after
                // we delete an item from it so that the user sees it immediately disappear from the
                // list.
                ((listActivity) mContext).populateNotes(((listActivity) mContext).search.getText().toString());

            }
        });
        // Sets the layout's background color to the note color the user selected before adding the
        // note to their list.
        layout.setBackgroundColor(Color.parseColor(current.getColor()));
        return item;
    }
}
