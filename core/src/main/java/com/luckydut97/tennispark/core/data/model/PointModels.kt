package com.luckydut97.tennispark.core.data.model

import com.google.gson.annotations.SerializedName

/**
 * 내 포인트 잔액 조회 응답 모델
 */
data class MyPointResponse(
    @SerializedName("points")
    val points: Int
)

/**
 * 포인트 내역 아이템 모델
 */
data class PointHistoryItem(
    @SerializedName("historyId")
    val historyId: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("point")
    val point: Int,

    @SerializedName("type")
    val type: String, // "EARNED" | "USED"

    @SerializedName("date")
    val date: String // "YYYY.MM.DD" 형식
)

/**
 * 포인트 내역 조회 응답 모델
 */
data class PointHistoryListResponse(
    @SerializedName("histories")
    val histories: List<PointHistoryItem>
)

/**
 * 경기 기록 모델
 */
data class GameRecord(
    @SerializedName("wins")
    val wins: Int,

    @SerializedName("draws")
    val draws: Int,

    @SerializedName("losses")
    val losses: Int,

    @SerializedName("score")
    val score: Int,

    @SerializedName("ranking")
    val ranking: Int
)

/**
 * 경기 기록 조회 응답 모델 (새로 추가)
 */
data class MatchRecordResponse(
    @SerializedName("wins")
    val wins: Int,

    @SerializedName("draws")
    val draws: Int,

    @SerializedName("losses")
    val losses: Int,

    @SerializedName("matchPoint")
    val matchPoint: Int, // score 대신 matchPoint

    @SerializedName("ranking")
    val ranking: Int
)

/**
 * 회원 정보 응답 모델 (이름, 포인트, 경기기록 통합)
 */
data class MemberInfoResponse(
    @SerializedName("name")
    val name: String,

    @SerializedName("point")
    val point: Int,

    @SerializedName("record")
    val record: GameRecord?
)

/**
 * QR 상품 구매 응답 모델
 */
data class QrPurchaseResponse(
    @SerializedName("qrCodeUrl")
    val qrCodeUrl: String,

    @SerializedName("productId")
    val productId: Long,

    @SerializedName("productName")
    val productName: String,

    @SerializedName("points")
    val points: Int
)
