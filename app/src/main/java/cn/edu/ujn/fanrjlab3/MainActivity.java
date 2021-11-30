package cn.edu.ujn.fanrjlab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tv1, tv2;
    private EditText et1, et2, et3, et4;
    private Button button1, button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.rbtn);
        button3 = findViewById(R.id.button3);
        // TODO: 2021/11/25 add modify username
        button1.setOnClickListener(view -> {
            SharedPreferences sp = getSharedPreferences("fanruojun",  MODE_PRIVATE);

            SharedPreferences.Editor edit = sp.edit();
            edit.putString("name", et1.getText().toString());
            edit.commit();

            et1.setText("");
            tv1.setText(sp.getString("name", null));
        });
        // TODO: 2021/11/25 add modify motto
        button2.setOnClickListener(view -> {
            SharedPreferences sp = getSharedPreferences("fanruojun",  MODE_PRIVATE);

            SharedPreferences.Editor edit = sp.edit();
            edit.putString("motto", et1.getText().toString());
            edit.commit();

            et1.setText("");
            tv2.setText(sp.getString("motto", null));
        });
        // TODO: 2021/11/25 add learning task
        button3.setOnClickListener(view -> {
            String date = et2.getText().toString();
            String task = et3.getText().toString();
            String note = et4.getText().toString();
            SQLiteDatabase wdb = new MyDBHelper(MainActivity.this).getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("date", date);
            cv.put("task", task);
            cv.put("note", note);
            wdb.insert("notepad", null, cv);
            Toast.makeText(MainActivity.this,"插入成功",Toast.LENGTH_LONG).show();

            String[] column = new String[]{"_id", "date","task","note"};
            Cursor query = new MyDBHelper(MainActivity.this)
                    .getReadableDatabase()
                    .query("notepad", column,
                            null, null, null, null, null);
            query.moveToFirst();
            ((ListView) findViewById(R.id.lv1)).setAdapter(new SimpleCursorAdapter(
                    MainActivity.this,
                    R.layout.line_app, query, column,
                    new int[]{
                            R.id.tv1_line_app,
                            R.id.tv2_line_app,
                            R.id.tv3_line_app,
                            R.id.tv4_line_app
                    }, 0));
        });
    }
}