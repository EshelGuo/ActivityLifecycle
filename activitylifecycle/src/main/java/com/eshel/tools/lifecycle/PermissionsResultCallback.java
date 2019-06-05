package com.eshel.tools.lifecycle;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * createBy Eshel
 * createTime: 2019/5/23 19:57
 */
public interface PermissionsResultCallback {
	void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
