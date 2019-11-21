# ActivityLifecycle
#### 目的:
* 在任意位置监听Activity的生命周期
* 在任意位置向Activity中新增成员变量（扩展变量）
* 在任意位置请求权限但无需在Activity中重写 `onRequestPermissionsResult()`方法

#### 使用方法:
build.gradle配置：
```groovy
dependencies {
    implementation 'com.eshel.lib:ActivityLifecycle:1.1'
}
```


* 监听生命周期：
```java
public class DemoView extends View{
    
    public DemoView(Context context){
        super(context);
        ActivityHelper.registerActivityLifecycle(ActivityHelper.getActivity(getContext()), new ActivityLifecycleAdapter() {
            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
            }

            @Override
            public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                super.onActivityResult(activity, requestCode, resultCode, data);
            }
        });
    }
}
```
* 请求权限
```java
public class DemoView extends View{
        
    public DemoView(Context context){
        super(context);
        Activity act = ActivityHelper.getActivity(v.getContext());
        ActivityHelper.registerActivityLifecycle(act, new ActivityLifecycleAdapter() {
            @Override
            public void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                String result = "onRequestPermissionsResult() called with: activity = [" + activity + "], requestCode = [" + requestCode + "], permissions = [" + permissions + "], grantResults = [" + grantResults + "]";
                Log.i(TAG, result);
            }
        });
        ActivityHelper.requestPermissions(act, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2000);
        //无需在Activity中重写 onRequestPermissionsResult 方法
    }
}
```
* 向Activity添加扩展变量
```java
public class DemoView extends View{
        
    public DemoView(Context context){
        super(context);
        Activity act = ActivityHelper.getActivity(v.getContext());
        ActivityHelper.set(act, "key", "hahahaha");
        String hahaha = ActivityHelper.get(act, "key", String.class);
    }
}
```
详细使用方法请参考DemoView
