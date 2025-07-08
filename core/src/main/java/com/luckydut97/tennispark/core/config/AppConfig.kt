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

    // 🎯 Google Play 리뷰어용 특별 인증 (배포 후 주석 처리)
    const val ENABLE_REVIEWER_MODE = true  // 배포 후 false로 변경 또는 주석 처리
    const val REVIEWER_PHONE_NUMBER = "00000000"  // 리뷰어용 특별 번호

    // 개발용 하드코딩된 토큰 (실제 발급받은 토큰)
    const val DEV_ACCESS_TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMTA1NTAwMDcxNSIsInJvbGUiOiJVU0VSIiwiZXhwIjoyMTExMjg4OTI0LCJpYXQiOjE3NTEyODg5MjR9.x_HDgzgqVV1UgUTZlYWUzLdp-Vyf7zoWmpS8KxF5dIk"
    const val DEV_REFRESH_TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMTA1NTAwMDcxNSIsInJvbGUiOiJVU0VSIiwiZXhwIjoyMzU2MDg4OTI0LCJpYXQiOjE3NTEyODg5MjR9.MsgsEuiMUAqZ2dpsGHHRpFwxPaIK3jTr7lrIiizX5yw"

    // 개발 모드 설정
    val isDevelopment: Boolean get() = IS_DEV_MODE
    val isProduction: Boolean get() = !IS_DEV_MODE

    // 리뷰어 모드 체크 함수
    fun isReviewerPhone(phoneNumber: String): Boolean {
        return ENABLE_REVIEWER_MODE && phoneNumber == REVIEWER_PHONE_NUMBER
    }

    // 개발용 사용자 정보
    const val DEV_USER_PHONE = "01088469260"
    const val DEV_USER_NAME = "개발자"
}