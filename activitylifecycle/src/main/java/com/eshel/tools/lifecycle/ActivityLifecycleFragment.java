package com.eshel.tools.lifecycle;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * createBy Eshel
 * createTime: 2019/5/9 15:45
 * desc: 处理 Activity 生命周期
 */
public class ActivityLifecycleFragment extends Fragment implements IActivityLifecycleFragment<ActivityLifecycleFragment>, ActivityLifecycleFragmentProxy.Provider{

	ActivityLifecycleFragmentProxy mProxy;
	public ActivityLifecycleFragment() {
		mProxy = new ActivityLifecycleFragmentProxy(this);
	}

	public ActivityLifecycleFragment addCallback(ActivityLifecycle callbacks) {
		mProxy.addCallback(callbacks);
		return this;
	}

	public ActivityLifecycleFragment removeCallback(ActivityLifecycle callbacks){
		mProxy.removeCallback(callbacks);
		return this;
	}

	public ActivityLifecycleFragment addActivityResultCallback(int requestCode, ActivityResultCallback callback){
		mProxy.addActivityResultCallback(requestCode, callback);
		return this;
	}

	@Override
	public ActivityLifecycleFragment addPermissionResultCallback(int requestCode, PermissionsResultCallback callback) {
		mProxy.addPermissionResultCallback(requestCode, callback);
		return this;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mProxy.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mProxy.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		super.onResume();
		mProxy.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mProxy.onPause();
	}

	@Override
	public void onStart() {
		super.onStart();
		mProxy.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
		mProxy.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mProxy.onDestroy();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		mProxy.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		mProxy.onSaveInstanceState(outState);
	}

	@Override
	public Fragment getV4Fragment() {
		return this;
	}

	@Override
	public android.app.Fragment getAppFragment() {
		return null;
	}

	@Override
	public boolean isV4Fragment() {
		return true;
	}
}
