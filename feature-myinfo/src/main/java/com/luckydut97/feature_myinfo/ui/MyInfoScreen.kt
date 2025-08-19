package com.luckydut97.feature_myinfo.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.tennispark.core.R
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.feature_myinfo.viewmodel.MyInfoViewModel
import com.luckydut97.tennispark.core.data.model.PointHistoryItem
import com.luckydut97.tennispark.core.ui.components.navigation.NoArrowTopBar
import com.luckydut97.tennispark.core.ui.components.ad.UnifiedAdBannerApi
import com.luckydut97.tennispark.core.data.model.Advertisement
import com.luckydut97.tennispark.core.data.model.AdPosition
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.data.repository.AdBannerRepositoryImpl
import com.luckydut97.tennispark.core.ui.components.animation.PressableComponent
import com.luckydut97.tennispark.core.ui.components.button.MyPageDetailButton
import com.luckydut97.tennispark.core.ui.components.button.MyPageDetailDivider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 내 정보 화면
 */
@Composable
fun MyInfoScreen(
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onPointHistoryClick: () -> Unit = {},
    onActivityHistoryClick: () -> Unit = {},
    viewModel: MyInfoViewModel = viewModel()
) {
    val tag = "🔍 디버깅: MyInfoScreen"

    // ViewModel에서 데이터 구독
    val points by viewModel.points.collectAsState()
    val histories by viewModel.histories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val memberInfo by viewModel.memberInfo.collectAsState()
    val matchRecord by viewModel.matchRecord.collectAsState()

    // 광고 배너 상태 - MEMBER position
    var advertisements by remember { mutableStateOf<List<Advertisement>>(emptyList()) }
    var isLoadingAds by remember { mutableStateOf(false) }

    val adBannerRepository = remember {
        AdBannerRepositoryImpl(NetworkModule.apiService)
    }

    // 포인트 새로고침 핸들러
    val coroutineScope = rememberCoroutineScope()
    val refreshPoints = remember { { viewModel.refreshAllData() } }

    // 실제 API 데이터 또는 기본값 사용
    val userName = memberInfo?.name ?: "로딩 중..."

    // 매치 기록을 새로운 API에서 가져오기 (기존 memberInfo.record 대신)
    val gameRecord = matchRecord?.let { record ->
        com.luckydut97.tennispark.core.data.model.GameRecord(
            wins = record.wins,
            draws = record.draws,
            losses = record.losses,
            score = record.matchPoint, // matchPoint를 score로 매핑
            ranking = record.ranking
        )
    } ?: com.luckydut97.tennispark.core.data.model.GameRecord(
        wins = 0, draws = 0, losses = 0, score = 0, ranking = 0
    )

    // 🔥 화면 진입 시마다 자동 새로고침
    LaunchedEffect(Unit) {
        viewModel.refreshAllData()

        // MEMBER position 광고 로드
        Log.d(tag, "[MyInfoScreen] loading MEMBER advertisements")
        isLoadingAds = true
        try {
            adBannerRepository.getAdvertisements(AdPosition.MEMBER).collect { ads ->
                Log.d(tag, "[MyInfoScreen] received ${ads.size} MEMBER advertisements")
                advertisements = ads
            }
        } catch (e: Exception) {
            Log.e(tag, "[MyInfoScreen] Exception: ${e.message}", e)
            advertisements = emptyList()
        } finally {
            isLoadingAds = false
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            NoArrowTopBar(
                title = "회원 정보" // 이미지 요청대로 "마이살래"

            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 상단 고정 영역
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 17.dp)
                ) {
                    // 회원명
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$userName 회원님",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Pretendard,
                            color = Color.Black
                        )

                        PressableComponent(
                            onClick = onSettingsClick,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = com.luckydut97.feature_myinfo.R.drawable.ic_setting),
                                contentDescription = "설정",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 포인트 및 경기 기록 박스
                    PressableComponent(
                        onClick = refreshPoints,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .background(
                                    color = Color(0xFFF2FAF4),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 24.dp),
                            contentAlignment = Alignment.Center // 중앙 정렬
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center // 수직 중앙 정렬
                            ) {
                                // 내 포인트 Row (실제 API 데이터 사용)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "내 포인트",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = Pretendard,
                                        color = Color.Black
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_coin_black),
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp),
                                            tint = Color.Unspecified
                                        )

                                        Spacer(modifier = Modifier.width(4.dp))

                                        Text(
                                            text = String.format(
                                                "%,d",
                                                points
                                            ), // ViewModel에서 받은 실제 포인트
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color.Black
                                        )

                                        Text(
                                            text = "P",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color.Black
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // 경기 기록 Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "경기 기록",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = Pretendard,
                                        color = Color.Black
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${gameRecord.wins}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color(0xFF145F44)
                                        )
                                        Text(
                                            text = "승 ",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color(0xFF145F44)
                                        )

                                        Text(
                                            text = "${gameRecord.draws}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color(0xFFCBA439)
                                        )
                                        Text(
                                            text = "무 ",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color(0xFFCBA439)
                                        )

                                        Text(
                                            text = "${gameRecord.losses}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color(0xFFEF3629)
                                        )
                                        Text(
                                            text = "패 ",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color(0xFFEF3629)
                                        )

                                        Text(
                                            text = "(${gameRecord.score}점/${gameRecord.ranking}위)",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = Pretendard,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // 광고 배너 - API 기반으로 변경
            item {
                if (advertisements.isNotEmpty()) {
                    Log.d(
                        tag,
                        "[MyInfoScreen] showing ${advertisements.size} MEMBER advertisements"
                    )
                    UnifiedAdBannerApi(
                        advertisements = advertisements
                    )
                } else if (!isLoadingAds) {
                    Log.d(tag, "[MyInfoScreen] no MEMBER advertisements available")
                    // 광고가 없으면 높이 조정을 위한 Spacer
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }

            // 여백 및 구분선
            item {
                Spacer(modifier = Modifier.height(36.dp))

                // 구분선 (좌우 여백 없이)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .background(Color(0xFFF5F5F5))
                )
            }

            // 마이페이지 세부 버튼들
            item {
                Spacer(modifier = Modifier.height(20.dp))

                MyPageDetailButton(
                    iconRes = R.drawable.ic_coin_black,
                    text = "나의 포인트 내역",
                    iconSize = 14.dp,
                    onClick = onPointHistoryClick
                )

                MyPageDetailDivider()

                /*MyPageDetailButton(
                    iconRes = R.drawable.ic_tennis,
                    text = "나의 활동신청 내역",
                    iconSize = 20.dp,
                    onClick = onActivityHistoryClick
                )*/

                Spacer(modifier = Modifier.height(40.dp)) // 하단 여백
            }
        }
    }
}
