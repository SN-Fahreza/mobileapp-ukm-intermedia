package com.pmo.intermedia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.splash);
        Thread thread = new Thread(){
            public void run(){
                try
                {
                    sleep(3000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(splash.this,LoginUtama.class));
                    finish();
                }
            }
        };
        thread.start();

    }
}