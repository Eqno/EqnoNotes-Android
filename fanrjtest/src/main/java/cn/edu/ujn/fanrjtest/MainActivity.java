package cn.edu.ujn.fanrjtest;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final Uri URI = Uri.parse("content://cn.edu.ujn.SQLite");
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.b1);
        button.setOnClickListener(view -> {
            String[] column = new String[]{"_id", "date","task","note"};
            Cursor query = this.getContentResolver().query(URI, column,
                    null, null, null, null);
            query.moveToFirst();
            ((ListView) findViewById(R.id.listView1)).setAdapter(new SimpleCursorAdapter(
                    MainActivity.this,
                    R.layout.line_test, query, column,
                    new int[]{
                            R.id.tv1_line_test,
                            R.id.tv2_line_test,
                            R.id.tv3_line_test,
                            R.id.tv4_line_test
                    }, 0));
            Toast.makeText(MainActivity.this, "查询列表成功！", Toast.LENGTH_LONG).show();
        });
    }
}