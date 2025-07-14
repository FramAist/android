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
#1.基本指令区
# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5

# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames

# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers

# 不进行优化，建议使用此选项，
-dontoptimize

# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify 能够加快混淆速度。
-dontpreverify

# 使我们的项目混淆后产生映射文件包含有类名->混淆后类名的映射关系
-verbose

# 使用printmapping指定映射文件的名称
-printmapping mapping.txt

# 屏蔽警告
-ignorewarnings

# 指定混淆是采用的算法，后面的参数是一个过滤器这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/,!class/merging/


# ------不混淆泛型和反射----
# 避免混淆泛型
-keepattributes Signature
# 保留Annotation不混淆
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation { *; }


# 网易滑块
-keep public class com.netease.nis.captcha.**{*;}

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
#2.默认保留区
# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用


# 保留四大组件，自定义的Application,Fragment等这些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends androidx.appcompat.app.AppCompatActivity

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.google.vending.licensing.ILicensingService
# 保留support下的所有类及其内部类
-keep class android.support.** {*;}
# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**



# 保留所有的AndroidX类和内部类
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-keep public class * extends androidx.**
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.annotation.**
-keep public class * extends android.support.v7.**

# 混淆时保留所有的AndroidX的资源类
-keep class androidx.**.R { *; }
-keep class android.support.v7.app.** { *; }
-keep class android.support.v7.widget.** { *; }
-keep class android.support.v4.** { *; }
-keep class android.support.annotation.** { *; }
-keep class androidx.arch.core.** { *; }


# 保留在Activity中的方法参数是view的方法，这样一来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}


# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 保留R下面的资源
-keep class **.R$* {
 *;
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# 避免layout中onclick方法（android:onclick="onClick"）混淆
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

# webview
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}

################ ViewBinding & DataBinding ###############
-keep class * implements androidx.viewbinding.ViewBinding {
    *;
}



# 保护Lifecycle相关的类不被混淆
-keep public class android.arch.lifecycle.** {*;}
-keep public class androidx.lifecycle.** {*;}

-keepclassmembers public class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}

# 如果你有自定义的ViewModel工厂方法，也需要保留
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    public <methods>;
}


#自定义不混淆某包中的指定内容---

# 本地bean相关
-keep class com.zss.framaist.bean.** {*;}
-keep class com.zss.common.bean.** {*;}



# 本地api相关
-keep class com.zss.framaist.net.** {*;}

#本地provider
#-keep class com.pxb7.common.provider.** {*;}
#-keep class com.pxb7.entrance.provider.** {*;}
#-keep class com.pxb7.game.provider.** {*;}
#-keep class com.pxb7.login.provider.** {*;}
#-keep class com.pxb7.merchant.provider.** {*;}
#-keep class com.pxb7.order.provider.** {*;}
#-keep class pxb7.com.update.provider.** {*;}
#-keep class com.pxb7.user.provider.** {*;}
#-keep class com.pxb7.im.provider.** {*;}




# 对于所有的第三方库，你可能需要添加特定的保留规则

# 友盟混淆配置
-keep class com.umeng.** {*;}
-keep class org.repackage.** {*;}
-keep class com.uyumao.** { *; }

# Alipay
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}

# Retrofit2
-keep class retrofit2.** { *; }
-keepattributes Exceptions

# Gson
-keep class com.google.gson.** { *; }


# Eventbus
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

# ImmersionBar
-keep class com.gyf.immersionbar.** { *; }

# PictureSelector
-keep class io.github.lucksiege.pictureselector.** { *; }

# RefreshLayout
-keep class io.github.scwang90.refreshlayout.** { *; }

# MMKV
-keep class com.tencent.mmkv.** { *; }

# JSBridge
-keep class ink.lodz.jsbridge.** { *; }

# ShapeView
-keep class com.hjq.shape.** { *; }

# ShapeDrawable
-keep class com.hjq.shapedrawable.** { *; }

# GsonFactory
-keep class com.hjq.gson.** { *; }

# Pag
-keep class com.tencent.pag.** { *; }

# XPopup
-keep class com.lxj.xpopup.** { *; }

# UtilCodeX
-keep class com.blankj.utilcode.** { *; }

# Timber
-keep class timber.log.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# Glide transformations
-keep class jp.wasabeef.glide.transformations.** { *; }

# Glide
-keep class com.bumptech.glide.** { *; }

# Glide OkHttp3
-keep class com.bumptech.glide.integration.okhttp3.** { *; }

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep public class com.squareup.okhttp3.** { *; }

# RollingText
-keep class com.yveschau.rolltext.** { *; }

# TheRouter
-keep class cn.therouter.** { *; }


# DslTablayout
-keep class com.angcyo.tablayout.** { *; }


# RoundedImageView
-keep class com.makeramen.roundedimageview.** { *; }

# SpannedGridLayout
-keep class com.arasthel.spannedgridlayoutmanager.** { *; }


# Pag
-keep class com.tencent.tav.** { *; }

# XPopup
-keep class com.github.li_xiaojun.xpopup.** { *; }

# UtilCodeX
-keep class com.blankj.utilcode.util.** { *; }

# ImmersionBar
-keep class com.gyf.immersionbar.** { *; }

# Timber
-keep class com.jakewharton.timber.** { *; }

# Convert-Gson
-keep class com.squareup.retrofit2.converter.gson.** { *; }

# ShapeView
-keep class com.hjq.shape.** {*;}

-keep class com.luck.picture.lib.** { *; }
-keep class com.luck.lib.camerax.** { *; }
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

-keep @androidx.annotation.Keep class *
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# GlideWebpSupport
-keep public class com.bumptech.glide.integration.webp.WebpImage { *; }
-keep public class com.bumptech.glide.integration.webp.WebpFrame { *; }
-keep public class com.bumptech.glide.integration.webp.WebpBitmapFactory { *; }


#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}


# 对于libpag库的混淆
-keep class org.libpag.** {*;}
-keep class androidx.exifinterface.** {*;}


#rongyun
-keepattributes Exceptions,InnerClasses
-keep class io.rong.** {*;}
-keep class cn.rongcloud.** {*;}
-keep class * implements io.rong.imlib.model.MessageContent {*;}
-dontwarn io.rong.push.**
-dontnote com.xiaomi.**
-dontnote com.google.android.gms.gcm.**
-dontnote io.rong.**
-ignorewarnings


#-dontwarn androidx.viewpager2.**
#-keep class androidx.viewpager2.** {*;}
#-dontwarn androidx.recyclerview.widget.RecyclerView
#-keep class androidx.recyclerview.widget.RecyclerView{*;}


#eventbus
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

#LiveEventBus
-dontwarn com.jeremyliao.liveeventbus.**
-keep class com.jeremyliao.liveeventbus.** { *; }
-keep class android.arch.lifecycle.** { *; }
-keep class android.arch.core.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.arch.core.** { *; }


#极光推送
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.service.JPushMessageService { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#小米
-dontwarn com.xiaomi.push.**
-keep class com.xiaomi.push.** { *; }
#华为
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}

#魅族
-dontwarn com.meizu.cloud.**
-keep class com.meizu.cloud.** { *; }

#vivo
-dontwarn com.vivo.push.**
-keep class com.vivo.push.**{*; }
-keep class com.vivo.vms.**{*; }

#oppo
-dontwarn com.coloros.mcsdk.**
-keep class com.coloros.mcsdk.** { *; }

-dontwarn com.heytap.**
-keep class com.heytap.** { *; }

-dontwarn com.mcs.**
-keep class com.mcs.** { *; }

#荣耀

-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.hihonor.push.**{*;}







#tbs混淆
-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**

-keep class com.tencent.smtt.** {
    *;
}

-keep class com.tencent.tbs.** {
    *;
}

#友盟分享
-dontshrink
-dontoptimize
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes EnclosingMethod
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}

-keep class com.umeng.commonsdk.statistics.common.MLog {*;}
-keep class com.umeng.commonsdk.UMConfigure {*;}
-keep class com.umeng.** {*;}
-keep class com.umeng.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.umeng.socialize.impl.ImageImpl {*;}
-keep class com.sina.** {*;}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keepattributes Signature

# spiderman
-keep class com.simple.spiderman.** { *; }
-keepnames class com.simple.spiderman.** { *; }
-keep public class * extends android.app.Activity
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
# support
-keep public class * extends android.support.annotation.** { *; }
-keep public class * extends android.support.v4.content.FileProvider
# androidx
-keep public class * extends androidx.annotation.** { *; }
-keep public class * extends androidx.core.content.FileProvider
#埋点sdk：SDK中已经默认集成了混淆规则，R8 在编译项目时会自动应用其规则。
#如果混淆后还会出现问题，需要添加一下代码
-keep class com.growingio.** {
    *;
}
-dontwarn com.growingio.**

-dontwarn com.vmos.vasdk.**
-keep class com.vmos.vasdk.** {*;}
-keep class kotlin.reflect.** { *; }
-keep class kotlin.Metadata { *; }
# gio不参与混淆
#-keep class com.pxb7.gio.** {*;}


#-keep class com.simple.spiderman.** { *; }
#-keepnames class com.simple.spiderman.** { *; }
#-keep public class * extends android.app.Activity
#-keep class * implements android.os.Parcelable {
#    public static final android.os.Parcelable$Creator *;
#}
## support
#-keep public class * extends android.support.annotation.** { *; }
#-keep public class * extends android.support.v4.content.FileProvider
## androidx
#-keep public class * extends androidx.annotation.** { *; }
#-keep public class * extends androidx.core.content.FileProvider


