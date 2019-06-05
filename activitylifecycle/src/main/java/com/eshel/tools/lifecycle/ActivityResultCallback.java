package com.eshel.tools.lifecycle;

import android.app.Activity;
import android.content.Intent;

/**
 * createBy Eshel
 * createTime: 2019/5/23 19:20
 */
public interface ActivityResultCallback {
	void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data);
}
