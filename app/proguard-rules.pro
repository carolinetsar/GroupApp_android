# Keep kotlinx.serialization generated serializers.
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class com.groupapp.data.** {
    *** Companion;
}
-keepclasseswithmembers class com.groupapp.data.** {
    kotlinx.serialization.KSerializer serializer(...);
}
