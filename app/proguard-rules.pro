# 기본 keep 규칙들 (난독화 비활성화 상태에서도 유지)
-keep class com.luckydut97.** { *; }

# Compose 관련 (혹시 모를 호환성을 위해 유지)
-keep class androidx.compose.** { *; }
-keep class kotlin.Metadata { *; }