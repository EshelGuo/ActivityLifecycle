package com.eshel.tools.lifecycle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.lang.ref.ReferenceQueue;

/**
 * createBy Eshel
 * createTime: 2019/5/9 16:59
 * desc: Activity 生命周期检测相关工具
 */
public class ActivityHelper {

	public static final String FRAGMENT_TAG = "ACT_CALLBACK";

	/**
	 * 注册Activity生命周期
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void registerActivityLifecycle(FragmentActivity activity, @NonNull ActivityLifecycle adapter){
		getFragment(activity).addCallback(adapter);
	}

	/**
	 * 兼容 app.Fragment
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void registerActivityLifecycle(Activity activity, @NonNull ActivityLifecycle adapter){
		if(activity instanceof  FragmentActivity)
			registerActivityLifecycle(((FragmentActivity)activity), adapter);
		else {
			getOldFragment(activity).addCallback(adapter);
		}
	}

	/**
	 * 取消注册生命周期
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void unregisterActivityLifecycle(FragmentActivity activity, ActivityLifecycleAdapter adapter){
		if(activity == null || adapter == null)
			return;

		getFragment(activity).removeCallback(adapter);
	}

	/**
	 * 兼容 app.Fragment
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void unregisterActivityLifecycle(Activity activity, @NonNull ActivityLifecycleAdapter adapter){
		if(activity instanceof  FragmentActivity)
			unregisterActivityLifecycle(((FragmentActivity)activity), adapter);
		else {
			getOldFragment(activity).removeCallback(adapter);
		}
	}

	/**
	 * 包装 startActivityForResult(), 使用该方法会走 ActivityLifecycle 的 onActivityForResult() 方法
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void startActivityForResult(FragmentActivity activity, Intent intent, int requestCode, Bundle options){
		getFragment(activity).startActivityForResult(intent, requestCode, options);
	}

	/**
	 * 兼容 app.Fragment
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void startActivityForResult(Activity activity, Intent intent, int requestCode, Bundle options){
		if(activity instanceof FragmentActivity){
			startActivityForResult(((FragmentActivity)activity), intent, requestCode, options);
		}else {
			getOldFragment(activity).startActivityForResult(intent, requestCode, options);
		}
	}

	/**
	 * @see #startActivityForResult(FragmentActivity, Intent, int, Bundle)
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void startActivityForResult(FragmentActivity activity, Intent intent, int requestCode){
		getFragment(activity).startActivityForResult(intent, requestCode);
	}

	/**
	 * 兼容 app.Fragment
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void startActivityForResult(Activity activity, Intent intent, int requestCode){
		if(activity instanceof FragmentActivity)
			startActivityForResult(((FragmentActivity)activity), intent, requestCode);
		else
			getOldFragment(activity).startActivityForResult(intent, requestCode);
	}


	/**
	 * @see #requestPermissions(FragmentActivity, String[], int)
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	@RequiresApi(api = Build.VERSION_CODES.M)
	public static void requestPermissions(Activity activity, @NonNull String[] permissions, int requestCode){
		if(activity instanceof FragmentActivity)
			requestPermissions((FragmentActivity) activity, permissions, requestCode);
		else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				getOldFragment(activity).requestPermissions(permissions, requestCode);
			}else {
				throw new IllegalArgumentException("use method requestPermissions(), minSdkVersion must >=23, can try use android.support.v4.app.FragmentActivity replace android.app.Activity");
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.M)
	public static void requestPermissions(Activity activity, String[] permissions, int requestCode, PermissionsResultCallback callback){
		if(activity instanceof FragmentActivity) {
			requestPermissions((FragmentActivity) activity, permissions, requestCode);
			getFragment((FragmentActivity) activity).addPermissionResultCallback(requestCode, callback);
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				getOldFragment(activity)
						.addPermissionResultCallback(requestCode, callback)
						.requestPermissions(permissions, requestCode);
			}else {
				throw new IllegalArgumentException("use method requestPermissions(), minSdkVersion must >=23, can try use android.support.v4.app.FragmentActivity replace android.app.Activity");
			}
		}
	}

	/**
	 * 对于请求权限的包装, 使用该方法会执行注册的回调的 onRequestPermissionsResult
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void requestPermissions(FragmentActivity activity, @NonNull String[] permissions, int requestCode){
		getFragment(activity).requestPermissions(permissions, requestCode);
	}

	public static void requestPermissions(FragmentActivity activity, @NonNull String[] permissions, int requestCode, PermissionsResultCallback callback){
		getFragment(activity)
				.addPermissionResultCallback(requestCode, callback)
				.requestPermissions(permissions, requestCode);
	}

	private static ActivityLifecycleFragment getFragment(FragmentActivity activity){
		ActivityLifecycleFragment fragment = (ActivityLifecycleFragment) activity.getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
		if(fragment == null){
			fragment = new ActivityLifecycleFragment();
			activity.getSupportFragmentManager().beginTransaction().add(fragment, FRAGMENT_TAG).commitAllowingStateLoss();
		}
		return fragment;
	}

	private static OldActivityLifecycleFragment getOldFragment(Activity activity) {
		FragmentManager fragmentManager = activity.getFragmentManager();
		OldActivityLifecycleFragment fragment = (OldActivityLifecycleFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
		if(fragment == null){
			fragment = new OldActivityLifecycleFragment();
			fragmentManager.beginTransaction().add(fragment, FRAGMENT_TAG).commitAllowingStateLoss();
		}
		return fragment;
	}

	public static Activity getActivity(Context context){
		if(context == null)
			return null;
		if(context instanceof Activity)
			return (Activity) context;

		while (context instanceof ContextWrapper){
			Context baseContext = ((ContextWrapper) context).getBaseContext();
			if(baseContext instanceof Activity)
				return (Activity) baseContext;
			context = baseContext;
		}
		return null;
	}

	/**
	 * 调整 ActivityForResult 并携带回调
	 * @param callback 调整回调, 用于处理 onActivityResult() 返回结果
	 *           ** 注意: 一个 requestCode 只能对应一个 callback, 否则会丢失之前的 callback **
	 */
	public static void startActivityForResult(FragmentActivity activity, Intent intent, int requestCode, ActivityResultCallback callback){
		getFragment(activity)
				.addActivityResultCallback(requestCode, callback)
				.startActivityForResult(intent, requestCode);
	}
}
