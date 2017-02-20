package com.example.resolver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ContentResolver contentResolver;
    Button insert = null;
    Button search = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentResolver = getContentResolver();
        insert = (Button)findViewById(R.id.insert);
        search = (Button)findViewById(R.id.search);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = ((EditText)findViewById(R.id.word)).getText().toString();
                String detail = ((EditText)findViewById(R.id.detail)).getText().toString();

                ContentValues values = new ContentValues();
                values.put(Words.Word.WORD,word);
                values.put(Words.Word.DETAIL,detail);

                contentResolver.insert(Words.Word.DICT_CONTENT_URI,values);

                Toast.makeText(MainActivity.this,"添加生词成功",Toast.LENGTH_SHORT).show();

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = ((EditText)findViewById(R.id.key)).getText().toString();
                Cursor cursor = contentResolver.query(Words.Word.DICT_CONTENT_URI,null,
                        "word like ? or detail like ?",new String[]{"%" + key + "%","%" + key + "%"},null);
                Bundle data = new Bundle();
                data.putSerializable("data",list(cursor));
            }
        });

    }
    public ArrayList<Map<String,String>> list(Cursor cursor){
        ArrayList<Map<String,String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String,String> map = new HashMap<>();
            map.put("word",cursor.getString(1) );
            map.put("detail",cursor.getString(2) );
            list.add(map);
        }

        return list;
    }
}
