package com.eshel.demo.activitylifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * createBy Eshel
 * createTime: 2019/5/15 19:36
 */
public class SecondActivity extends AppCompatActivity {

	private TextView mTextView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		mTextView = findViewById(R.id.textView);
	}

	@Override
	public void finish() {
		setResult(RESULT_OK, new Intent().putExtra("result", mTextView.getText().toString()));
		super.finish();
	}
}
