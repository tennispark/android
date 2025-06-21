package com.luckydut97.tennispark.core.config

/**
 * 앱 전체 설정 관리
 * 개발/운영 모드 전환을 위한 설정
 *
 * 🔥 사용 방법:
 * 1. 개발할 때: IS_DEV_MODE = true -> 바로 메인 화면, 하드코딩된 토큰 사용
 * 2. 배포할 때: IS_DEV_MODE = false -> 정상 인증 플로우, 실제 토큰 사용
 *
 * 📱 개발 모드 특징:
 * - 스플래시 건너뛰고 바로 메인 화면
 * - 실제 인증 과정 없이 하드코딩된 토큰 사용
 * - 에뮬레이터/실제 기기 상관없이 동일하게 동작
 * - API 개발/테스트에 최적화
 */
object AppConfig {

    // 🔥 개발할 때는 true, 배포할 때는 false로 변경
    const val IS_DEV_MODE = true

    // 개발용 하드코딩된 토큰 (실제 발급받은 토큰)
    const val DEV_ACCESS_TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMTA4ODQ2OTI2MCIsInJvbGUiOiJVU0VSIiwiZXhwIjoyMTEwNDA5MTI0LCJpYXQiOjE3NTA0MDkxMjR9.f1XIlgk3QzIyVizcB628kOGjKvJlFJ_Br9Q_ndl0GgQ"
    const val DEV_REFRESH_TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMTA4ODQ2OTI2MCIsInJvbGUiOiJVU0VSIiwiZXhwIjoyMzU1MjA5MTI0LCJpYXQiOjE3NTA0MDkxMjR9.ZQy0DQv72VkqVsmhrR4eGsv1XPHDs1SpIyCbIU3bHmA"

    // 개발 모드 설정
    val isDevelopment: Boolean get() = IS_DEV_MODE
    val isProduction: Boolean get() = !IS_DEV_MODE

    // 개발용 사용자 정보
    const val DEV_USER_PHONE = "01088469260"
    const val DEV_USER_NAME = "개발자"
}