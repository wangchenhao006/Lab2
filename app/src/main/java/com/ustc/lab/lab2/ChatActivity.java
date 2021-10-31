package com.ustc.lab.lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChatActivity extends AppCompatActivity {
    private String[] data = {
            "张三",
            "里斯","王赛","厂长",
            "夫人","阿大","赵四",
            "马哥","李哥","王博","张三",
            "里斯","王赛","厂长",
            "夫人","阿大","赵四",
            "马哥","李哥","王博"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ChatActivity.this,   // Context上下文
                android.R.layout.simple_list_item_1,  // 子项布局id
                data);                                // 数据
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);


    }
}
