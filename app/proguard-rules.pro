# 멀티모듈 관련 keep 규칙들
-keep class com.luckydut97.** { *; }
-keep class com.luckydut97.tennispark.** { *; }
-keep class com.luckydut97.feature.** { *; }
-keep class com.luckydut97.feature_** { *; }

# Compose 관련
-keep class androidx.compose.** { *; }
-keep class kotlin.Metadata { *; }