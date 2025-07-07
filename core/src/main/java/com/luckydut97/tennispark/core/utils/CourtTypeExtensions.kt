package com.luckydut97.tennispark.core.utils

/**
 * 코트 타입을 한글로 변환하는 확장 함수
 * 정의된 타입만 변환하고, 그 외에는 원본 그대로 반환
 */
fun String.toKoreanCourtType(): String {
    return when (this) {
        "GAME" -> "게임"
        "CHALLENGE" -> "게임도전"
        "RALLY" -> "랠리코트"
        "STUDY" -> "게임스터디"
        "BEGINNER" -> "초보코트"
        else -> this // 정의되지 않은 값은 원본 그대로 반환
    }
}