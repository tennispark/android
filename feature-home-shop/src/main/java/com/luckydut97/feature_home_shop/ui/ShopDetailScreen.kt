package com.luckydut97.feature_home_shop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.luckydut97.tennispark.core.R
import com.luckydut97.feature_home_shop.data.model.ShopItem
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.core.data.repository.PointRepository
import kotlinx.coroutines.launch

/**
 * 제품 상세 화면
 */
@Composable
fun ShopDetailScreen(
    item: ShopItem,
    onBackClick: () -> Unit = {},

) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var qrCodeUrl by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val pointRepository = remember { PointRepository() }

    // QR 구매 함수
    fun purchaseProduct() {
        coroutineScope.launch {
            isLoading = true
            errorMessage = null

            try {
                // ShopItem.id는 String이므로 Long으로 변환 (서버에서 온 실제 productId)
                val productId = item.id.toLongOrNull() ?: 0L
                val response = pointRepository.purchaseProductWithQr(productId)

                if (response.success && response.response != null) {
                    qrCodeUrl = response.response?.qrCodeUrl
                } else {
                    errorMessage = response.error?.message ?: "구매에 실패했습니다."
                }
            } catch (e: Exception) {
                errorMessage = "네트워크 오류가 발생했습니다."
            } finally {
                isLoading = false
            }
        }
    }

    // 다이얼로그가 열릴 때 자동으로 구매 진행
    LaunchedEffect(showConfirmDialog) {
        if (showConfirmDialog && qrCodeUrl == null) {
            purchaseProduct()
        }
    }
    
    if (showConfirmDialog) {
        Dialog(onDismissRequest = { showConfirmDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(400.dp)
                    .heightIn(min = 379.dp)
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 제품명
                    Text(
                        text = item.productName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Pretendard,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // 안내 텍스트
                    Text(
                        text = "교환 코드를 운영진에게 보여주세요\n구매 완료 시 환불이 어렵습니다.",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Pretendard,
                        color = Color(0xFF555555),
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // QR 코드 박스
                    Box(
                        modifier = Modifier
                            .size(145.dp)
                            .background(
                                color = Color(0xFFF5F5F5),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color(0xFF145F44))
                        } else if (qrCodeUrl != null) {
                            AsyncImage(
                                model = qrCodeUrl,
                                contentDescription = "QR Code",
                                modifier = Modifier.fillMaxSize()
                            )
                        } else if (errorMessage != null) {
                            Text(
                                text = errorMessage!!,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = Pretendard,
                                color = Color.Red,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // 구매취소 버튼
                    OutlinedButton(
                        onClick = { 
                            showConfirmDialog = false
                            qrCodeUrl = null
                            errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = Color(0xFF145F44)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF145F44)
                        )
                    ) {
                        Text(
                            text = "구매취소",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Pretendard,
                            color = Color(0xFF145F44)
                        )
                    }
                }
            }
        }
    }
    
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "마이살래",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            // 확인 버튼 (Bottom Bar 역할)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(87.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(47.dp)
                        .padding(horizontal = 17.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF145F44)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "확인",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = Pretendard
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 제품 이미지 (정사각형)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // 정사각형 비율
                    .background(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = "${item.productName} 이미지",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = null, // 플레이스홀더 제거하여 빈 배경 표시
                    error = null // 에러 이미지도 제거하여 빈 배경 표시
                )
            }

            // 제품 정보 컬럼
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(139.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(0.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 브랜드명
                Text(
                    text = item.brandName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Pretendard,
                    color = Color.Black
                )

                // 제품명
                Text(
                    text = item.productName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Pretendard,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                // 가격 정보
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 코인 아이콘
                    Icon(
                        painter = painterResource(id = R.drawable.ic_coin_green),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Unspecified
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // 가격 숫자
                    Text(
                        text = String.format("%,d", item.price),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Pretendard,
                        color = Color(0xFF145F44)
                    )

                    // P 단위
                    Text(
                        text = "P",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Pretendard,
                        color = Color(0xFF145F44)
                    )
                }
            }

            // 26dp 여백
            Spacer(modifier = Modifier.height(26.dp))

            // 상품 설명 박스 (좌우 26dp 여백)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp) // 좌우 26dp 여백
                    .height(278.dp)
                    .background(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    // 제목
                    Text(
                        text = "유의사항",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Pretendard,
                        color = Color(0xFF303030)
                    )

                    // 12dp 여백
                    Spacer(modifier = Modifier.height(12.dp))

                    // 설명 텍스트
                    Text(
                        text = """1. 포인트는 현금이 아닌 앱 전용 적립 수단입니다.
                             - 포인트는 환불, 현금 교환, 타인에게 양도할 수 없습니다.
                            2. 포인트로만 상품 구매가 가능합니다.
                             - 일부 상품은 포인트 전액 결제로만 구매할 수 있으며, 현금 또는 기타 결제수단과의 혼합 결제는 지원되지 않습니다.
                        """.trimMargin(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Pretendard,
                        color = Color(0xFF959595),
                        lineHeight = 16.sp
                    )
                }
            }

            // 스크롤 영역 하단 여백
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
