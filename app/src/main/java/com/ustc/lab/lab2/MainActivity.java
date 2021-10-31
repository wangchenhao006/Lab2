package com.ustc.lab.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ImageView imageView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent = new Intent();
            switch (item.getItemId()) {
                case R.id.navigation_chat:
//                    mTextMessage.setText(R.string.title_chat);
                    imageView.setImageResource(R.drawable.bkg);
//                    intent.setClass(MainActivity.this,ChatActivity.class);
//                    startActivity(intent);
                    return true;
                case R.id.navigation_address:
//                    mTextMessage.setText(R.string.title_address);
                    intent.setClass(MainActivity.this,ChatActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_discover:
                    imageView.setImageResource(R.drawable.bkg2);
//                    mTextMessage.setText(R.string.title_discover);
                    return true;
                case R.id.navigation_me:
                    intent.setClass(MainActivity.this,MeActivity.class);
                    startActivity(intent);
//                    mTextMessage.setText(R.string.title_me);

                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mTextMessage = (TextView) findViewById(R.id.message);
        imageView = findViewById(R.id.image_view);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
