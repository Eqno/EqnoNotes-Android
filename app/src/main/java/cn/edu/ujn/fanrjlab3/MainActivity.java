package cn.edu.ujn.fanrjlab3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ListView taskList;
    private SharedPreferences sharedPr;
    private EditText userName, userMotto;
    private Button modifyName, modifyMotto, addTask, clearTask;

    private void instantiate()
    {
        taskList = findViewById(R.id.task_list);
        userName = findViewById(R.id.user_name);
        addTask = findViewById(R.id.swith_to_add);
        clearTask = findViewById(R.id.clear_task);
        userMotto = findViewById(R.id.user_motto);
        modifyName = findViewById(R.id.modify_name);
        modifyMotto = findViewById(R.id.modify_motto);
    }

    private void initUserInfo()
    {
        sharedPr = getSharedPreferences("eqnoxx", MODE_PRIVATE);
        userName.setText(sharedPr.getString("name", null));
        userMotto.setText(sharedPr.getString("motto", null));
    }

    private void modifyUserInfo(String key, EditText text)
    {
        SharedPreferences.Editor editor = sharedPr.edit();
        editor.putString(key, text.getText().toString()).apply();
        text.setText(sharedPr.getString(key, null));
    }

    private void updateListView()
    {
        String[] column = new String[]{"_id", "date", "task", "note"};
        Cursor query = new MyDBHelper(MainActivity.this).getReadableDatabase()
                .query("notepad", column, null, null, null, null, null);
        query.moveToFirst();
        taskList.setAdapter(new SimpleCursorAdapter(
                MainActivity.this,
                R.layout.line_app, query, column,
                new int[]{
                        R.id.tv1_line_app,
                        R.id.tv2_line_app,
                        R.id.tv3_line_app,
                        R.id.tv4_line_app
                }, 0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: 12/21/2021 instantiate
        instantiate();
        // TODO: 12/21/2021 getSharedPr
        initUserInfo();
        // TODO: 2021/11/25 add modify username
        modifyName.setOnClickListener(view -> {
            modifyUserInfo("name", userName);
            Toast.makeText(MainActivity.this, "用户名修改成功！", Toast.LENGTH_LONG).show();
        });
        // TODO: 2021/11/25 add modify motto
        modifyMotto.setOnClickListener(view -> {
            modifyUserInfo("motto", userMotto);
            Toast.makeText(MainActivity.this, "座右铭修改成功！", Toast.LENGTH_LONG).show();
        });
        // TODO: 12/21/2021 update list view
        updateListView();
        // TODO: 12/21/2021 add add task
        ActivityResultLauncher<Intent> noteActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent intent = result.getData();
                            if (intent == null) return;
                            // TODO: 12/21/2021 add task list item
                            ContentValues cv = new ContentValues();
                            cv.put("date", intent.getStringExtra("date"));
                            cv.put("task", intent.getStringExtra("task"));
                            cv.put("note", intent.getStringExtra("remark"));
                            SQLiteDatabase wdb = new MyDBHelper(MainActivity.this).getWritableDatabase();
                            wdb.insert("notepad", null, cv);
                            Toast.makeText(MainActivity.this,"插入成功",Toast.LENGTH_LONG).show();
                            updateListView();
                        }
                    }
                });
        addTask.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            noteActivityResultLauncher.launch(intent);
        });
        // TODO: 12/21/2021 add clear task
        clearTask.setOnClickListener(view -> {
            SQLiteDatabase wdb = new MyDBHelper(MainActivity.this).getWritableDatabase();
            wdb.execSQL("DELETE FROM notepad");
            wdb.execSQL("DELETE FROM sqlite_sequence");
            updateListView();
        });
    }

    // TODO: 12/21/2021 add smart focus
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        return false;
    }

    private void hideKeyboard(IBinder token) {
        userName.clearFocus();
        userMotto.clearFocus();
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}