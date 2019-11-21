package com.eshel.tools.lifecycle;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * <br>createBy guoshiwen
 * <br>createTime: 2019/11/21 14:23
 * <br>desc: 参考Glide，解决提交Fragment时获取不到正在提交Fragment问题
 */
public class ActivityLifecycleRetriever implements Handler.Callback {

	private static final String TAG = "ALRetriever";

	private static final String FRAGMENT_TAG = "ACT_CALLBACK";
	private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
	private static final int ID_REMOVE_V4_FRAGMENT_MANAGER = 2;

	private final Map<FragmentManager, OldActivityLifecycleFragment> pendingActivityMemberVarExtFragments =
			new HashMap<FragmentManager, OldActivityLifecycleFragment>();

	private final Map<android.support.v4.app.FragmentManager, ActivityLifecycleFragment> pendingActivityMemberVarExtV4Fragments =
			new HashMap<android.support.v4.app.FragmentManager, ActivityLifecycleFragment>();

	private Handler mHandler = new Handler(Looper.getMainLooper(), this);

	ActivityLifecycleFragment getFragment(FragmentActivity activity){
		android.support.v4.app.FragmentManager v4fm = activity.getSupportFragmentManager();
		ActivityLifecycleFragment fragment = (ActivityLifecycleFragment) v4fm.findFragmentByTag(FRAGMENT_TAG);
		if(fragment == null){
			fragment = pendingActivityMemberVarExtV4Fragments.get(v4fm);

			if(fragment == null){
				fragment = new ActivityLifecycleFragment();
				pendingActivityMemberVarExtV4Fragments.put(v4fm, fragment);
				activity.getSupportFragmentManager().beginTransaction().add(fragment, FRAGMENT_TAG).commitAllowingStateLoss();
				mHandler.obtainMessage(ID_REMOVE_V4_FRAGMENT_MANAGER, v4fm).sendToTarget();
			}
		}

		return fragment;
	}

	OldActivityLifecycleFragment getOldFragment(Activity activity) {
		FragmentManager fm = activity.getFragmentManager();
		OldActivityLifecycleFragment fragment = (OldActivityLifecycleFragment) fm.findFragmentByTag(FRAGMENT_TAG);
		if(fragment == null){
			fragment = pendingActivityMemberVarExtFragments.get(fm);

			if(fragment == null) {
				fragment = new OldActivityLifecycleFragment();
				pendingActivityMemberVarExtFragments.put(fm, fragment);
				fm.beginTransaction().add(fragment, FRAGMENT_TAG).commitAllowingStateLoss();
				mHandler.obtainMessage(ID_REMOVE_FRAGMENT_MANAGER, fm);
			}
		}
		return fragment;
	}

	@Override
	public boolean handleMessage(Message msg) {
		boolean handled = true;
		Object removed = null;
		Object key = null;

		switch (msg.what){
			case ID_REMOVE_FRAGMENT_MANAGER:
				FragmentManager fm = (FragmentManager) msg.obj;
				key = fm;
				removed = pendingActivityMemberVarExtFragments.remove(fm);
				break;
			case ID_REMOVE_V4_FRAGMENT_MANAGER:
				android.support.v4.app.FragmentManager v4fm = (android.support.v4.app.FragmentManager) msg.obj;
				key = v4fm;
				removed = pendingActivityMemberVarExtV4Fragments.remove(v4fm);
				break;
				default:
					handled = false;
					break;
		}

		if (handled && removed == null && Log.isLoggable(TAG, Log.WARN)) {
			Log.w(TAG, "Failed to remove expected request manager fragment, manager: " + key);
		}
		return handled;
	}
}
