package nl.lennartklein.lennartkleinpset4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Context context;
    TodoDatabase db;
    ListView lv;
    TodoAdapter ta;
    EditText input_todo;
    Button button_todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set globals
        context = this;
        lv = findViewById(R.id.list_todo);
        button_todo = findViewById(R.id.submit_todo);

        // Set input field
        input_todo = findViewById(R.id.input_todo);
        input_todo.setImeOptions(EditorInfo.IME_ACTION_DONE);
        input_todo.setRawInputType(InputType.TYPE_CLASS_TEXT  | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        input_todo.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    button_todo.performClick();
                    return true;
                }
                return false;
            }
        });

        // Get instance of the database
        db = TodoDatabase.getInstance(getApplicationContext());

        // Fill the list with the database contents
        Cursor data = db.selectAll();
        ta = new TodoAdapter(getApplicationContext(), R.layout.row_todo, data);
        lv.setAdapter(ta);

        // Make list clickable
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox box = view.findViewById(R.id.todo_completed);
                boolean status = box.isChecked();
                status = !status;
                db.update(l, status);
                updateData();
            }
        });

        // Make item deletable
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, final long l) {
                // Show dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle(R.string.delete_todo);
                alert.setPositiveButton(R.string.label_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.delete(l);
                        updateData();
                    }
                });
                alert.setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                alert.show();
                return true;
            }
        });
    }


    // Back button on device
    @Override
    public void onBackPressed() {
        input_todo.setText("");
    }

    // Insert the title from input in the database
    public void insertTodo(View view) {
        String title = input_todo.getText().toString();
        if (!Objects.equals(title, "")) {
            db.insert(title);
            input_todo.setText("");
            updateData();
        }
    }

    private void updateData() {
        ta.swapCursor(db.selectAll());
        ta.notifyDataSetChanged();
    }

}
