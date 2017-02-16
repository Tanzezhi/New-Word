package com.example.lenovo.newword;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/2/16.
 */
public class ResultActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState );
        setContentView(R.layout.popup);
        ListView listView = (ListView) findViewById(R.id.show);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        @SuppressWarnings("unchecked")
        List<Map<String,String>> list = (List<Map<String,String>>) data.getSerializable("data");

        SimpleAdapter simpleAdapter = new SimpleAdapter(ResultActivity.this
                ,list,R.layout.line,new String[]{"word","detail"},new int[]{R.id.word_1,R.id.detail_1});

        listView.setAdapter(simpleAdapter);
    }
}

