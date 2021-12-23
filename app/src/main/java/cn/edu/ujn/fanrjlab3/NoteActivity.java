package cn.edu.ujn.fanrjlab3;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    private Button back, addTask;
    private SharedPreferences sharedPr;
    private EditText dateText, taskText, remarkText;

    private void instantiate()
    {
        back = findViewById(R.id.back);
        dateText = findViewById(R.id.date);
        taskText = findViewById(R.id.task);
        addTask = findViewById(R.id.add_task);
        remarkText = findViewById(R.id.remark);
        sharedPr = getSharedPreferences("eqnoxx", MODE_PRIVATE);
    }

    private void modBySharedPre(String key, String value)
    {
        SharedPreferences.Editor editor = sharedPr.edit();
        editor.putString(key, value).apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        // TODO: 12/21/2021 instantiate
        instantiate();
        // TODO: 2021/11/25 add learning task
        addTask.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("date", dateText.getText().toString());
            intent.putExtra("task", taskText.getText().toString());
            intent.putExtra("remark", remarkText.getText().toString());
            setResult(RESULT_OK, intent);
//            modBySharedPre("date", dateText.getText().toString());
//            modBySharedPre("task", taskText.getText().toString());
//            modBySharedPre("remark", remarkText.getText().toString());
            NoteActivity.this.finish();
        });
        // TODO: 12/21/2021 add back
        back.setOnClickListener(view -> {
            dateText.setText("");
            taskText.setText("");
            remarkText.setText("");
            setResult(RESULT_OK, null);
            NoteActivity.this.finish();
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
        dateText.clearFocus();
        taskText.clearFocus();
        remarkText.clearFocus();
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}