package com.luckydut97.tennispark.core.data.repository

import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.MemberInfoResponse
import com.luckydut97.tennispark.core.data.model.PointHistoryItem
import com.luckydut97.tennispark.core.data.model.QrPurchaseResponse
import com.luckydut97.tennispark.core.data.model.MatchRecordResponse
import com.luckydut97.tennispark.core.data.network.NetworkModule
import android.util.Log

class PointRepository {

    private val tag = "🔍 디버깅: PointRepository"
    private val apiService = NetworkModule.apiService

    suspend fun purchaseProductWithQr(productId: Long): ApiResponse<QrPurchaseResponse> {
        return try {
            val response = apiService.purchaseProductWithQr(productId)

            if (response.isSuccessful) {
                val body = response.body()

                body ?: ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = 500,
                        message = "응답 본문이 비어있습니다."
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()

                ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = response.code(),
                        message = when (response.code()) {
                            400 -> "잘못된 요청입니다."
                            401 -> "인증이 되지 않았습니다."
                            404 -> "해당 상품을 찾을 수 없습니다."
                            else -> "서버 오류가 발생했습니다."
                        }
                    )
                )
            }
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        }
    }

    suspend fun postQrEvent(eventUrl: String): ApiResponse<Any> {
        return try {
            val response = apiService.postQrEvent(eventUrl)

            if (response.isSuccessful) {
                val body = response.body()

                body ?: ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = 500,
                        message = "응답 본문이 비어있습니다."
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()

                ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = response.code(),
                        message = when (response.code()) {
                            400 -> "잘못된 요청입니다."
                            401 -> "인증이 되지 않았습니다."
                            404 -> "해당 이벤트를 찾을 수 없습니다."
                            else -> "서버 오류가 발생했습니다."
                        }
                    )
                )
            }
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        }
    }

    suspend fun getMyPoints(): ApiResponse<com.luckydut97.tennispark.core.data.model.MyPointResponse> {
        Log.d(tag, "[getMyPoints] API 호출 시작")
        return try {
            val response = apiService.getMyPoints()
            Log.d(
                tag,
                "[getMyPoints] API 응답: success=${response.isSuccessful}, code=${response.code()}"
            )

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "[getMyPoints] 응답 body: $body")

                if (body?.response?.points != null) {
                    Log.d(tag, "[getMyPoints] 포인트 데이터: ${body.response.points}")
                } else {
                    Log.w(tag, "[getMyPoints] 포인트 데이터가 null입니다")
                }

                body ?: ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = 500,
                        message = "응답 본문이 비어있습니다."
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(tag, "[getMyPoints] API 실패: code=${response.code()}, errorBody=$errorBody")

                ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = response.code(),
                        message = when (response.code()) {
                            401 -> "인증이 되지 않았습니다."
                            else -> "서버 오류가 발생했습니다."
                        }
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(tag, "[getMyPoints] Exception: ${e.message}", e)
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        }
    }

    suspend fun getPointHistories(): ApiResponse<com.luckydut97.tennispark.core.data.model.PointHistoryListResponse> {
        Log.d(tag, "[getPointHistories] API 호출 시작")
        return try {
            val response = apiService.getPointHistories()
            Log.d(
                tag,
                "[getPointHistories] API 응답: success=${response.isSuccessful}, code=${response.code()}"
            )

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "[getPointHistories] 응답 body: $body")

                if (body?.response?.histories != null) {
                    Log.d(tag, "[getPointHistories] 내역 개수: ${body.response.histories.size}")
                    body.response.histories.forEachIndexed { index, history ->
                        Log.d(
                            tag,
                            "[getPointHistories] 내역[$index]: ${history.title}, ${history.point}P, ${history.type}"
                        )
                    }
                } else {
                    Log.w(tag, "[getPointHistories] 내역 데이터가 null입니다")
                }

                body ?: ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = 500,
                        message = "응답 본문이 비어있습니다."
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(
                    tag,
                    "[getPointHistories] API 실패: code=${response.code()}, errorBody=$errorBody"
                )

                ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = response.code(),
                        message = when (response.code()) {
                            401 -> "인증이 되지 않았습니다."
                            else -> "서버 오류가 발생했습니다."
                        }
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(tag, "[getPointHistories] Exception: ${e.message}", e)
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        }
    }

    suspend fun getMemberInfo(): ApiResponse<MemberInfoResponse> {
        Log.d(tag, "[getMemberInfo] API 호출 시작")
        return try {
            val response = apiService.getMemberInfo()
            Log.d(
                tag,
                "[getMemberInfo] API 응답: success=${response.isSuccessful}, code=${response.code()}"
            )

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "[getMemberInfo] 응답 body: $body")

                if (body?.response != null) {
                    val memberInfo = body.response
                    Log.d(tag, "[getMemberInfo] 회원명: ${memberInfo.name}")
                    Log.d(tag, "[getMemberInfo] 포인트: ${memberInfo.point}")
                    if (memberInfo.record != null) {
                        Log.d(
                            tag,
                            "[getMemberInfo] 경기기록: 승=${memberInfo.record.wins}, 무=${memberInfo.record.draws}, 패=${memberInfo.record.losses}, 점수=${memberInfo.record.score}, 순위=${memberInfo.record.ranking}"
                        )
                    } else {
                        Log.w(tag, "[getMemberInfo] 경기기록이 null입니다")
                    }
                } else {
                    Log.w(tag, "[getMemberInfo] 회원정보 데이터가 null입니다")
                }

                body ?: ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = 500,
                        message = "응답 본문이 비어있습니다."
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(tag, "[getMemberInfo] API 실패: code=${response.code()}, errorBody=$errorBody")

                ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = response.code(),
                        message = when (response.code()) {
                            401 -> "인증이 되지 않았습니다."
                            else -> "서버 오류가 발생했습니다."
                        }
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(tag, "[getMemberInfo] Exception: ${e.message}", e)
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        }
    }

    suspend fun getMatchRecords(): ApiResponse<MatchRecordResponse> {
        Log.d(tag, "[getMatchRecords] API 호출 시작")
        return try {
            val response = apiService.getMatchRecords()
            Log.d(
                tag,
                "[getMatchRecords] API 응답: success=${response.isSuccessful}, code=${response.code()}"
            )

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "[getMatchRecords] 응답 body: $body")

                if (body?.response != null) {
                    val matchRecord = body.response
                    Log.d(tag, "[getMatchRecords] 승: ${matchRecord.wins}")
                    Log.d(tag, "[getMatchRecords] 무: ${matchRecord.draws}")
                    Log.d(tag, "[getMatchRecords] 패: ${matchRecord.losses}")
                    Log.d(tag, "[getMatchRecords] 점수: ${matchRecord.matchPoint}")
                    Log.d(tag, "[getMatchRecords] 순위: ${matchRecord.ranking}")
                } else {
                    Log.w(tag, "[getMatchRecords] 경기기록 데이터가 null입니다")
                }

                body ?: ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = 500,
                        message = "응답 본문이 비어있습니다."
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(
                    tag,
                    "[getMatchRecords] API 실패: code=${response.code()}, errorBody=$errorBody"
                )

                ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = response.code(),
                        message = when (response.code()) {
                            401 -> "인증이 되지 않았습니다."
                            else -> "서버 오류가 발생했습니다."
                        }
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(tag, "[getMatchRecords] Exception: ${e.message}", e)
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        }
    }
}
