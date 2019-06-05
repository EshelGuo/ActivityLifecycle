package com.eshel.demo.activitylifecycle;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eshel.tools.lifecycle.ActivityHelper;
import com.eshel.tools.lifecycle.ActivityLifecycle;
import com.eshel.tools.lifecycle.ActivityLifecycleAdapter;

/**
 * createBy Eshel
 * createTime: 2019/5/15 19:41
 * desc: TODO
 */
public class DemoView extends FrameLayout implements ActivityLifecycle{

	public static final String TAG = DemoView.class.getSimpleName();

	public static final int REQUEST_CODE = 2000;

	private Button mRequestPermission;
	private Button mStartActivity;
	private TextView mTvResult;

	public DemoView(@NonNull Context context) {
		super(context);
		init();
	}

	public DemoView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DemoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init(){
		View.inflate(getContext(), R.layout.activity_main, this);
		mRequestPermission = findViewById(R.id.request_permission);
		mStartActivity = findViewById(R.id.start_activity);
		mTvResult = findViewById(R.id.tv_result);
		ActivityHelper.registerActivityLifecycle(ActivityHelper.getActivity(getContext()), this);

		mRequestPermission.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Activity act = ActivityHelper.getActivity(v.getContext());
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					ActivityHelper.requestPermissions(act, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2000);
				}
			}
		});

		mStartActivity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Activity activity = ActivityHelper.getActivity(v.getContext());
				ActivityHelper.startActivityForResult(activity, new Intent(getContext(), SecondActivity.class), 2000);
			}
		});
	}

	@Override
	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult() called with: activity = [" + activity + "], requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
		if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
			String result = data.getStringExtra("result");
			mTvResult.setText(result);
		}
	}

	@Override
	public void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		String result = "onRequestPermissionsResult() called with: activity = [" + activity + "], requestCode = [" + requestCode + "], permissions = [" + permissions + "], grantResults = [" + grantResults + "]";
		Log.i(TAG, result);
		if(requestCode == REQUEST_CODE)
			mTvResult.setText(result);
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		Log.i(TAG, "onActivityCreated() called with: activity = [" + activity + "], savedInstanceState = [" + savedInstanceState + "]");
		Toast.makeText(getContext(),"onCreate" ,Toast.LENGTH_LONG).show();
	}

	@Override
	public void onActivityStarted(Activity activity) {
		Log.i(TAG, "onActivityStarted() called with: activity = [" + activity + "]");
		Toast.makeText(getContext(),"onStart",Toast.LENGTH_LONG).show();
	}

	@Override
	public void onActivityResumed(Activity activity) {
		Toast.makeText(getContext(),"onResume",Toast.LENGTH_LONG).show();
		Log.i(TAG, "onActivityResumed() called with: activity = [" + activity + "]");
	}

	@Override
	public void onActivityPaused(Activity activity) {
		Toast.makeText(getContext(),"onPause",Toast.LENGTH_LONG).show();
		Log.i(TAG, "onActivityPaused() called with: activity = [" + activity + "]");
	}

	@Override
	public void onActivityStopped(Activity activity) {
		Toast.makeText(getContext(),"onStop",Toast.LENGTH_LONG).show();
		Log.i(TAG, "onActivityStopped() called with: activity = [" + activity + "]");
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		Toast.makeText(getContext(),"onActivitySaveInstanceState",Toast.LENGTH_LONG).show();
		Log.i(TAG, "onActivitySaveInstanceState() called with: activity = [" + activity + "], outState = [" + outState + "]");
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		Toast.makeText(getContext(),"onDestory",Toast.LENGTH_LONG).show();
		Log.i(TAG, "onActivityDestroyed() called with: activity = [" + activity + "]");
	}
}
