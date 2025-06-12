# Proguard rules for the OptiRoute application.
# These rules help ensure that code shrinking and obfuscation during release builds
# do not break the application, especially for libraries that use reflection.

# Add general Proguard rules for Android.
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Keep annotations used by Dagger/Hilt.
-keepattributes *Annotation*
-keep class dagger.hilt.internal.aggregatedroot.AggregatedRoot
-keep class dagger.hilt.android.HiltAndroidApp
-keep class javax.inject.** { *; }
-keep class dagger.** { *; }
-keep interface dagger.** { *; }
-keep @dagger.hilt.android.HiltAndroidApp class *
-keep @dagger.hilt.DefineComponent class * { *; }
-keep @dagger.hilt.EntryPoint class * { *; }
-keep @dagger.hilt.InstallIn class * { *; }
-keep @dagger.hilt.components.SingletonComponent class * { *; }
-keep @dagger.Module class * { *; }
-keep @dagger.Provides class * { *; }
-keep @javax.inject.Inject class * { *; }
-keep @javax.inject.Singleton class * { *; }

# Keep Room entities, DAOs, and Database classes.
# Replace com.optiroute.com.data.local.entity.* with your actual entity package if different.
-keep class * extends androidx.room.RoomDatabase
-keep class androidx.room.TypeConverter
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public static final androidx.room.RoomDatabase$Callback Companion;
    public static final androidx.room.RoomDatabase$Callback sCallback;
}
-keep class com.optiroute.com.data.local.entity.** { *; } # Sesuaikan dengan package entity Anda
-keep interface com.optiroute.com.data.local.dao.** { *; } # Sesuaikan dengan package DAO Anda

# Keep Kotlin Coroutines specific classes.
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory { *; }
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler { *; }
-keepclassmembernames class kotlinx.coroutines.flow.internal.AbstractSharedFlow {
    kotlinx.coroutines.flow.SharingStarted $started;
}
-keepclassmembernames class kotlinx.coroutines.flow.internal.ChannelFlow {
    kotlinx.coroutines.channels.BufferOverflow $onBufferOverflow;
}

# Keep Kotlin specific classes and metadata.
-keepattributes Signature,RuntimeVisibleAnnotations,RuntimeInvisibleAnnotations,InnerClasses,EnclosingMethod,NestHost,NestMembers,PermittedSubclasses
-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.coroutines.jvm.internal.BaseContinuationImpl {
    kotlin.coroutines.Continuation getCompletion();
    java.lang.Object getResult();
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class kotlin.jvm.internal.DefaultConstructorMarker { *; }
-keepclassmembernames class ** {
    # Preserve names of properties that are annotated with @kotlin.jvm.JvmField
    @kotlin.jvm.JvmField <fields>;
}

# For Jetpack Compose
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
-keepclassmembers class * {
    @androidx.compose.ui.tooling.preview.Preview <methods>;
}
-keep class androidx.compose.runtime.** # Common pattern for Compose generated classes

# For Parcelize
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep class **$$ParcelableCreator* { *; }


# If you use Google Maps SDK, it's generally handled well by default Proguard rules.
# However, if you encounter issues with map functionality in release builds,
# you might need to add specific rules for Google Play Services.
# -keep class com.google.android.gms.maps.** { *; }

# If using Retrofit and Gson (not in current scope but for future reference):
# -keepattributes Signature
# -keepattributes *Annotation*
# -keepclassmembers,allowshrinking,allowobfuscation interface * {
#     @retrofit2.http.* <methods>;
# }
# -keep class com.google.gson.reflect.TypeToken { *; }
# -keep class * extends com.google.gson.TypeAdapter { *; }
# -keep class com.google.gson.annotations.SerializedName

# Keep Timber classes if you have custom trees for release builds.
# -keep class com.jakewharton.timber.** { *; }

# Add any other library-specific Proguard rules here.

# Optional: You can enable more aggressive obfuscation if needed,
# but test thoroughly.
# -repackageclasses ''
# -allowaccessmodification
# -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# Keep names of classes annotated with @Keep for any reason.
-keep @androidx.annotation.Keep class *
-keep @androidx.annotation.Keep interface *
-keep @androidx.annotation.Keep enum *
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}

