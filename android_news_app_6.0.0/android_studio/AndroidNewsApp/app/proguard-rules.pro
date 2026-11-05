# =========================================================================
# 1. ANDROIDX WORKMANAGER, ROOM, & STARTUP (CRASH FIX)
# =========================================================================
-keep class androidx.work.** { *; }
-dontwarn androidx.work.**

-keep class androidx.room.** { *; }
-dontwarn androidx.room.**
-keep class * extends androidx.room.RoomDatabase { *; }

-keep class androidx.startup.** { *; }
-keep class androidx.startup.InitializationProvider { *; }


# =========================================================================
# 2. SOLODROID SDK
# =========================================================================
-keep class com.solodroidx.ads.** { *; }
-keepclassmembers class com.solodroidx.ads.** { *; }
-keep class com.solodroid.push.** { *; }
-keep class id.solodroid.validationlibrary.** { *; }


# =========================================================================
# 3. RETROFIT, OKHTTP & GSON
# =========================================================================
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }


# =========================================================================
# 4. GOOGLE PLAY SERVICES & ADMOB
# =========================================================================
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.android.play.core.** { *; }
-dontwarn com.google.android.play.core.**


# =========================================================================
# 5. ONESIGNAL PUSH NOTIFICATION
# =========================================================================
-dontwarn com.onesignal.**
-keep class com.onesignal.** { *; }


# =========================================================================
# 6. GLIDE (IMAGE LOADER)
# =========================================================================
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


# =========================================================================
# 7. MEDIA3 EXOPLAYER (Updated from Exoplayer2 to Media3)
# =========================================================================
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**


# =========================================================================
# 8. YOUTUBE PLAYER
# =========================================================================
-keep class com.pierfrancescosoffritti.androidyoutubeplayer.** { *; }
-dontwarn com.pierfrancescosoffritti.androidyoutubeplayer.**


# =========================================================================
# 9. ANDROID VIEWS & WEBVIEW JAVASCRIPT INTERFACES
# =========================================================================
# Keep custom views (such as Shimmer, SwipeRefresh) to prevent crashes
-keep class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepattributes JavascriptInterface