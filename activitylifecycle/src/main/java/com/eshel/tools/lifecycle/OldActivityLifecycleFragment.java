package com.eshel.tools.lifecycle;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * createBy Eshel
 * createTime: 2019/5/9 17:37
 * desc: 兼容 android.app.Fragment
 */
public class OldActivityLifecycleFragment extends Fragment implements IActivityLifecycleFragment<OldActivityLifecycleFragment>, ActivityLifecycleFragmentProxy.Provider{

	ActivityLifecycleFragmentProxy mProxy;
	public OldActivityLifecycleFragment() {
		mProxy = new ActivityLifecycleFragmentProxy(this);
	}

	public OldActivityLifecycleFragment addCallback(ActivityLifecycle callbacks) {
		mProxy.addCallback(callbacks);
		return this;
	}

	public OldActivityLifecycleFragment removeCallback(ActivityLifecycle callbacks){
		mProxy.removeCallback(callbacks);
		return this;
	}

	@Override
	public OldActivityLifecycleFragment addActivityResultCallback(int requestCode, ActivityResultCallback callback) {
		mProxy.addActivityResultCallback(requestCode, callback);
		return this;
	}

	@Override
	public OldActivityLifecycleFragment addPermissionResultCallback(int requestCode, PermissionsResultCallback callback) {
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
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		mProxy.onSaveInstanceState(outState);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		mProxy.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public android.support.v4.app.Fragment getV4Fragment() {
		return null;
	}

	@Override
	public Fragment getAppFragment() {
		return this;
	}

	@Override
	public boolean isV4Fragment() {
		return false;
	}
}
