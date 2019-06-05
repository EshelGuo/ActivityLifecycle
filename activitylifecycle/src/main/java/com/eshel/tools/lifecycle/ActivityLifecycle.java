package com.eshel.tools.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * createBy Eshel
 * createTime: 2019/5/9 16:57
 */
public interface ActivityLifecycle extends ActivityLifecycleCallbacks, ActivityResultCallback, PermissionsResultCallback{
}
