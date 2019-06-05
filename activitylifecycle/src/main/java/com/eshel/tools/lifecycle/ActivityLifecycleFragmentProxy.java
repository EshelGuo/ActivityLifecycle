package com.eshel.tools.lifecycle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * createBy Eshel
 * createTime: 2019/5/23 19:34
 * desc: 用于代理 ActivityLifecycleFragment 和 OldActivityLifecycleFragment
 */
public class ActivityLifecycleFragmentProxy implements IActivityLifecycleFragment<ActivityLifecycleFragmentProxy>{
	private static final int CAPACITY = 5;
	//	final AtomicReference<ArrayList<ActivityLifecycle>> mCallbacks = new AtomicReference<ArrayList<ActivityLifecycle>>(new ArrayList<>(5));

	private Provider mProvider;
	private List<ActivityLifecycle> mCallbacks = new ArrayList<>(CAPACITY);
	private SparseArray<ActivityResultCallback> mActivityResultCallback = new SparseArray<>(CAPACITY);
	private SparseArray<PermissionsResultCallback> mRequestPermissionCallback = new SparseArray<>(CAPACITY);

	private Map<String, Object> extFields = new HashMap<>(4);

	public ActivityLifecycleFragmentProxy(Provider provider) {
		mProvider = provider;
	}

	private Activity getActivity(){
		if(mProvider != null)
			return mProvider.getActivity();
		return null;
	}

	private Fragment getV4Fragment(){
		if(mProvider != null)
			mProvider.getV4Fragment();
		return null;
	}

	private android.app.Fragment getAppFragment(){
		if(mProvider != null)
			mProvider.getAppFragment();
		return null;
	}

	private boolean isV4Fragment() {
		return mProvider != null && mProvider.isV4Fragment();
	}

	@Override
	public ActivityLifecycleFragmentProxy addCallback(ActivityLifecycle callbacks) {
		mCallbacks.add(callbacks);
		return this;
	}

	@Override
	public ActivityLifecycleFragmentProxy removeCallback(ActivityLifecycle callbacks){
		mCallbacks.remove(callbacks);
		return this;
	}

	@Override
	public ActivityLifecycleFragmentProxy addActivityResultCallback(int requestCode, ActivityResultCallback callback){
		mActivityResultCallback.put(requestCode, callback);
		return this;
	}

	public ActivityLifecycleFragmentProxy addPermissionResultCallback(int requestCode, PermissionsResultCallback callback){
		mRequestPermissionCallback.put(requestCode, callback);
		return this;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		for (ActivityLifecycle callback : mCallbacks) {
			callback.onActivityCreated(getActivity(), savedInstanceState);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		ActivityResultCallback tempProcess = mActivityResultCallback.get(requestCode);
		if(tempProcess != null){
			tempProcess.onActivityResult(getActivity(), requestCode, resultCode, data);
			mActivityResultCallback.remove(requestCode);
			return;
		}

		for (ActivityLifecycle callback : mCallbacks) {
			callback.onActivityResult(getActivity(), requestCode, resultCode, data);
		}
	}

	@Override
	public void onResume() {
		for (ActivityLifecycle callback : mCallbacks) {
			callback.onActivityResumed(getActivity());
		}
	}

	@Override
	public void onPause() {
		for (ActivityLifecycle callback : mCallbacks) {
			callback.onActivityPaused(getActivity());
		}
	}

	@Override
	public void onStart() {
		for (ActivityLifecycle callback : mCallbacks) {
			callback.onActivityStarted(getActivity());
		}
	}

	@Override
	public void onStop() {
		for (ActivityLifecycle callback : mCallbacks) {
			callback.onActivityStopped(getActivity());
		}
	}

	@Override
	public void onDestroy() {
		for (ActivityLifecycle callback : mCallbacks) {
			callback.onActivityDestroyed(getActivity());
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		PermissionsResultCallback tempCallback = mRequestPermissionCallback.get(requestCode);
		if(tempCallback != null){
			tempCallback.onRequestPermissionsResult(getActivity(), requestCode, permissions, grantResults);
			mRequestPermissionCallback.remove(requestCode);
			return;
		}
		for (ActivityLifecycle callback : mCallbacks) {
			callback.onRequestPermissionsResult(getActivity(), requestCode, permissions, grantResults);
		}
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		for (ActivityLifecycle callback : mCallbacks) {
			callback.onActivitySaveInstanceState(getActivity(), outState);
		}
	}

	@Override
	public void put(String key, Object value) {
		extFields.put(key, value);
	}

	@Override
	public <T> T get(String key, Class<T> type) {
		try {
			Object value = extFields.get(key);
			return (T) value;
		}catch (Exception e){
			return null;
		}
	}

	public interface Provider{
		Activity getActivity();
		Fragment getV4Fragment();
		android.app.Fragment getAppFragment();
		boolean isV4Fragment();
	}
}
