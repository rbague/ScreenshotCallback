package net.rbague.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.rbague.screenshotcallback.ScreenshotObserver;

public class MainActivity extends AppCompatActivity {

    private ScreenshotObserver mObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mObserver = new ScreenshotObserver() {
            @Override
            public void onScreenshotTaken(String path) {
                //Your code here
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mObserver != null) {
            mObserver.startListnening();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mObserver != null) {
            mObserver.stopListening();
        }
    }
}
