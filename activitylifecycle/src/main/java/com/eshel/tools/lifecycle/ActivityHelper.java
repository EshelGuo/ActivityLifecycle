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

	private static final ActivityLifecycleRetriever ALRetriever = new ActivityLifecycleRetriever();

	/**
	 * 注册Activity生命周期
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void registerActivityLifecycle(FragmentActivity activity, @NonNull ActivityLifecycle adapter){
		ALRetriever.getFragment(activity).addCallback(adapter);
	}

	/**
	 * 兼容 app.Fragment
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void registerActivityLifecycle(Activity activity, @NonNull ActivityLifecycle adapter){
		if(activity instanceof  FragmentActivity)
			registerActivityLifecycle(((FragmentActivity)activity), adapter);
		else {
			ALRetriever.getOldFragment(activity).addCallback(adapter);
		}
	}

	/**
	 * 取消注册生命周期
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void unregisterActivityLifecycle(FragmentActivity activity, ActivityLifecycleAdapter adapter){
		if(activity == null || adapter == null)
			return;

		ALRetriever.getFragment(activity).removeCallback(adapter);
	}

	/**
	 * 兼容 app.Fragment
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void unregisterActivityLifecycle(Activity activity, @NonNull ActivityLifecycleAdapter adapter){
		if(activity instanceof  FragmentActivity)
			unregisterActivityLifecycle(((FragmentActivity)activity), adapter);
		else {
			ALRetriever.getOldFragment(activity).removeCallback(adapter);
		}
	}

	/**
	 * 包装 startActivityForResult(), 使用该方法会走 ActivityLifecycle 的 onActivityForResult() 方法
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void startActivityForResult(FragmentActivity activity, Intent intent, int requestCode, Bundle options){
		ALRetriever.getFragment(activity).startActivityForResult(intent, requestCode, options);
	}

	/**
	 * 兼容 app.Fragment
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void startActivityForResult(Activity activity, Intent intent, int requestCode, Bundle options){
		if(activity instanceof FragmentActivity){
			startActivityForResult(((FragmentActivity)activity), intent, requestCode, options);
		}else {
			ALRetriever.getOldFragment(activity).startActivityForResult(intent, requestCode, options);
		}
	}

	/**
	 * @see #startActivityForResult(FragmentActivity, Intent, int, Bundle)
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void startActivityForResult(FragmentActivity activity, Intent intent, int requestCode){
		ALRetriever.getFragment(activity).startActivityForResult(intent, requestCode);
	}

	/**
	 * 兼容 app.Fragment
	 * {@link ActivityLifecycle,ActivityLifecycleAdapter}
	 */
	public static void startActivityForResult(Activity activity, Intent intent, int requestCode){
		if(activity instanceof FragmentActivity)
			startActivityForResult(((FragmentActivity)activity), intent, requestCode);
		else
			ALRetriever.getOldFragment(activity).startActivityForResult(intent, requestCode);
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
				ALRetriever.getOldFragment(activity).requestPermissions(permissions, requestCode);
			}else {
				throw new IllegalArgumentException("use method requestPermissions(), minSdkVersion must >=23, can try use android.support.v4.app.FragmentActivity replace android.app.Activity");
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.M)
	public static void requestPermissions(Activity activity, String[] permissions, int requestCode, PermissionsResultCallback callback){
		if(activity instanceof FragmentActivity) {
			requestPermissions((FragmentActivity) activity, permissions, requestCode);
			ALRetriever.getFragment((FragmentActivity) activity).addPermissionResultCallback(requestCode, callback);
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				ALRetriever.getOldFragment(activity)
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
		ALRetriever.getFragment(activity).requestPermissions(permissions, requestCode);
	}

	public static void requestPermissions(FragmentActivity activity, @NonNull String[] permissions, int requestCode, PermissionsResultCallback callback){
		ALRetriever.getFragment(activity)
				.addPermissionResultCallback(requestCode, callback)
				.requestPermissions(permissions, requestCode);
	}

	/**
	 * (像EditText这种部分控件, 会将Activity包装一层, 所以才需要此方法来拆开包装)
	 * 将上下文递归拆开, 如果类型是 Activity, 则返回, 否则返回null
	 * @param context 目标上下文
	 * @return 返回结果(如果context并非Activity, 则返回null)
	 */
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
		ALRetriever.getFragment(activity)
				.addActivityResultCallback(requestCode, callback)
				.startActivityForResult(intent, requestCode);
	}

	/**
	 * 清除Activity的附加字段的数据(将其置为null)
	 * 相当于:
	 * 		activity.key = null;
	 * @param activity 载体Activity
	 * @param key 数据的key值
	 */
	public static void clear(Activity activity, String key){
		if(activity == null || key == null)
			return;
		if(activity instanceof FragmentActivity)
			ALRetriever.getFragment((FragmentActivity) activity).put(key, null);
		else {
			ALRetriever.getOldFragment(activity).put(key, null);
		}
	}

	/**
	 * 给 Activity 添加额外变量而无需在Activity中定义
	 * 相当于:
	 * 		activity.key = value;
	 * @param activity 载体Activity
	 * @param key 变量的key值, 相当于变量名
	 * @param value 变量的值
	 */
	public static void set(Activity activity, String key, Object value){
		if(activity == null || key == null)
			return;
		if(activity instanceof FragmentActivity)
			ALRetriever.getFragment((FragmentActivity) activity).put(key, value);
		else
			ALRetriever.getOldFragment(activity).put(key, value);
	}

	/**
	 * 获取activity中的扩展变量
	 * 相当于:
	 * 		type temp = activity.key;//获取到变量后使用
	 * @param activity 载体
	 * @param key 相当于变量名
	 * @param type 返回的数据的类型
	 * @param <T> 数据类型泛型
	 * @return 返回额外变量
	 */
	public static <T> T get(Activity activity, String key, Class<T> type){
		if(activity == null || key == null)
			return null;
		if(activity instanceof FragmentActivity)
			return ALRetriever.getFragment((FragmentActivity) activity).get(key, type);

		return ALRetriever.getOldFragment(activity).get(key, type);
	}
}
