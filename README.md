#ActivityLifecycle
####目的:
* 通过Fragment来监听Activity的生命周期

####使用方法:
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

详细使用方法请参考DemoView
