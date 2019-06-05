package com.eshel.tools.lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * createBy Eshel
 * createTime: 2019/5/23 19:36
 */
public interface IActivityLifecycleFragment<T extends IActivityLifecycleFragment> {

	T addCallback(ActivityLifecycle callbacks);
	T removeCallback(ActivityLifecycle callbacks);
	T addActivityResultCallback(int requestCode, ActivityResultCallback callback);
	T addPermissionResultCallback(int requestCode, PermissionsResultCallback callback);
	void onActivityCreated(@Nullable Bundle savedInstanceState);
	void onActivityResult(int requestCode, int resultCode, Intent data);
	void onResume();
	void onPause();
	void onStart();
	void onStop();
	void onDestroy();
	void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
	void onSaveInstanceState(@NonNull Bundle outState);
	void put(String key, Object value);
	<T> T get(String key, Class<T> type);
}
