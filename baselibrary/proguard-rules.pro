# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#  1.动态权限(工具类)；
#  2.RecyclerView(封装，适配器)
#  3.EventBus(封装)
#  4.时间，省市区的选择器
#  5.沉浸式状态栏
#  6.版本更新框架
#
#
#
#
#
#
#

-keep class com.gyf.barlibrary.* {*;}