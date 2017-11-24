package nl.lennartklein.lennartkleinpset4;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class TodoAdapter extends ResourceCursorAdapter {

    TodoAdapter(Context context, int layoutId, Cursor cursor) {
        super(context, layoutId, cursor, true);
    }

    @Override
    public void bindView(View view, Context context, Cursor cur) {

        // Fetch data from row
        String title = cur.getString(cur.getColumnIndex("title"));
        int completedInt = cur.getInt(cur.getColumnIndex("completed"));

        // Create a boolean from 0 or 1
        boolean completed = completedInt == 1;

        // Set the data in the views
        TextView titleTv = view.findViewById(R.id.todo_title);
        titleTv.setText(title);
        CheckBox completedCb = view.findViewById(R.id.todo_completed);
        completedCb.setChecked(completed);
    }
}
