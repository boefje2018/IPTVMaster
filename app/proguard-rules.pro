-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep class com.iptv.master.** { *; }
-dontwarn com.iptv.master.**

-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }

-keepclassmembers class * { @com.google.gson.annotations.SerializedName <fields>; }

-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes EnclosingMethod

-keepclassmembers class kotlin.Metadata { *; }
-dontwarn kotlin.**

-keepclassmembers class * {
    @androidx.annotation.Keep <fields>;
    @androidx.annotation.Keep <methods>;
}

-keep class com.iptv.master.data.model.** { *; }
-keep class com.iptv.master.data.local.entity.** { *; }
-keep class com.iptv.master.data.remote.dto.** { *; }
