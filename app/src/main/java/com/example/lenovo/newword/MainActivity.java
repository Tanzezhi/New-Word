package com.example.lenovo.newword;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MyDatabaseHelper dbHelper;
    Button insert = null;
    Button search = null;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDatabaseHelper(this, "myDict.db3", 1);

        insert = (Button) findViewById(R.id.insert);
        search = (Button) findViewById(R.id.search);
        insert.setOnClickListener(this);
        search.setOnClickListener(this);

        listView = (ListView)findViewById(R.id.listView);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.insert:
                String word = ((EditText) findViewById(R.id.word)).getText().toString();
                String detail = ((EditText) findViewById(R.id.detail)).getText().toString();

                if(word.equals("") || detail.equals("") ){
                    Toast.makeText(MainActivity.this, "请填写生词和解释", Toast.LENGTH_SHORT).show();

                }else {

                    insertDate(dbHelper.getReadableDatabase(), word, detail);

                    Toast.makeText(MainActivity.this, "添加生词成功", Toast.LENGTH_SHORT).show();

                    Cursor cursor_1 = dbHelper.getReadableDatabase()
                            .rawQuery("select * from dict "
                                    ,null);
                    SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(MainActivity.this,R.layout.dictionary,
                            cursor_1,new String[]{"word","detail"},new int[]{R.id.word_2,R.id.detail_2},
                            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                    listView.setAdapter(cursorAdapter);
                }

                break;
            case R.id.search:
                String key = ((EditText) findViewById(R.id.key)).getText().toString();
                if(key.equals("")){
                    Toast.makeText(MainActivity.this, "请填写搜索内容", Toast.LENGTH_SHORT).show();
                }else{
                    Cursor cursor = dbHelper.getReadableDatabase()
                        .rawQuery("select * from dict where word like ? or detail like ?"
                                ,new String[]{"%" + key + "%","%" + key + "%"});

                Bundle data = new Bundle();
                data.putSerializable("data",converCursorToList(cursor));

                Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                intent.putExtras(data);
                startActivity(intent);}
                break;
        }
    }

    protected ArrayList<Map<String,String>> converCursorToList(Cursor cursor){
        ArrayList<Map<String,String>> result = new  ArrayList<Map<String,String>>();
        while (cursor.moveToNext()){
            Map<String,String> map = new HashMap<>();
            map.put("word",cursor.getString(1));
            map.put("detail",cursor.getString(2));
            result.add(map);
        }
        return result;
    }

    public void insertDate(SQLiteDatabase db, String word, String detail){
        db.execSQL("insert into dict values(null,?,?)", new String[]{ word,detail });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbHelper != null){
            dbHelper.close();
        }
    }
}

