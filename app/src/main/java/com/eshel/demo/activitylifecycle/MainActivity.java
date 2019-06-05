package com.eshel.demo.activitylifecycle;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/*
	1<<<1	2	2*1
	1<<<2	4	2*2
	1<<<3	8	2*3
	最高位符号位, 0为正, 1为负
	10000000 00000000 00000000 00000001
	011111111 11111111 11111111 1111111
	
	...
	1<<<n	?	2*n		这个结论正确吗?
 */

public class MainActivity extends AppCompatActivity {

	Handler mHandler = new Handler(Looper.getMainLooper());
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(DemoView.TAG, "onCreate: ");

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				setContentView(new DemoView(MainActivity.this));
			}
		}, 3000);

		/*mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				setContentView(new DemoView(MainActivity.this));
			}
		}, 9000);*/

	}
}
