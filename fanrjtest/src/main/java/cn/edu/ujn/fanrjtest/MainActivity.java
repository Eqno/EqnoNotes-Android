package cn.edu.ujn.fanrjtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final Uri URI = Uri.parse("content://cn.edu.ujn.SQLite");
    private Button queryTask;
    private ListView taskList;

    private void instantiate()
    {
        taskList = findViewById(R.id.task_list);
        queryTask = findViewById(R.id.query_task);
    }

    private void updateListView()
    {
        String[] column = new String[]{"_id", "date","task","note"};
        Cursor query = this.getContentResolver().query(URI, column,
                null, null, null, null);
        query.moveToFirst();
        taskList.setAdapter(new SimpleCursorAdapter(
                MainActivity.this,
                R.layout.line_test, query, column,
                new int[]{
                        R.id.tv1_line_test,
                        R.id.tv2_line_test,
                        R.id.tv3_line_test,
                        R.id.tv4_line_test
                }, 0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: 12/21/2021 instantiate
        instantiate();
        // TODO: 12/21/2021 add clear task
        queryTask.setOnClickListener(view -> { updateListView(); });
    }
}