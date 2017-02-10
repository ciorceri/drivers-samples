/*
 * Copyright 2017 Ciorceri Petru Sorin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidthings.driversamples;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.google.android.things.contrib.driver.hx711.hx711;

import java.io.IOException;

/**
 * Sample activity that demonstrates usage of the HX711 driver.
 */
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int FRAME_DELAY_MS = 2000; // 2 seconds

    private hx711 mDevice;
    private Handler mHandler = new Handler();
    private HandlerThread mPioThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "HX711 MainActivity created");

        mPioThread = new HandlerThread("pioThread");
        mPioThread.start();

        mHandler = new Handler(mPioThread.getLooper());

        try {
            Log.d(TAG, "Initializing HX711");
            mDevice = new hx711(BoardDefaults.getSPIPort(), hx711.Gain.Gain32);
        } catch (IOException e) {
            Log.e(TAG, "Error initializing HX711", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove pending sensor Runnable from the handler.
        mHandler.removeCallbacks(mAnimateRunnable);
        Log.d(TAG, "Closing HX711");
        try {
            mDevice.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Runnable mAnimateRunnable = new Runnable() {
        @Override
        public void run() {
            int value = 0;

            try {
                value = mDevice.read();
            } catch (IOException e) {
                Log.e(TAG, "Error while reading from HX711");
                e.printStackTrace();
            }
            Log.e("caca", "" + value);

            mHandler.postDelayed(mAnimateRunnable, FRAME_DELAY_MS);
        }
    };

}