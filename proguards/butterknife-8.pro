#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-keepattributes *Annotation*

#-keepclassmembers class ** {
#
#@org.greenrobot.eventbus.Subscribe ;
#
#}

-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor

#-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
#
#(Java.lang.Throwable);
#
#}