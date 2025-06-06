package com.luckydut97.feature_home.main.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.feature.home.R
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.tennispark.core.ui.theme.Pretendard

@Composable
fun WeeklyApplicationSection(
    onApplicationClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp)
            .padding(top = 20.dp)
    ) {
        // 주간 신청서 제목
        Text(
            text = "주간 신청서",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Pretendard,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 이번주 활동 신청 카드
        WeeklyApplicationCard(onClick = onApplicationClick)
    }
}

@Composable
fun WeeklyApplicationCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(161.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // 첫 번째 Row: 제목 + 화살표
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "이번주 활동 신청",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Pretendard,
                    color = Color.Black
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "Arrow Right",
                    modifier = Modifier.size(width = 6.dp, height = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 두 번째 Row: 텍스트 Column + 이미지
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 텍스트들을 담는 Column
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Black)) {
                                append("매주 ")
                            }
                            withStyle(style = SpanStyle(color = AppColors.Primary, fontWeight = FontWeight.Bold)) {
                                append("금요일 08:30분")
                            }
                            withStyle(style = SpanStyle(color = Color.Black)) {
                                append("\n신청 가능합니다.")
                            }
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Pretendard,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "게스트는 월요일부터 가능합니다.",
                        fontSize = 14.sp,
                        fontFamily = Pretendard,
                        color = Color(0xFF8B9096)
                    )
                }

                // 로고 이미지
                Image(
                    painter = painterResource(id = R.drawable.ic_main_logo),
                    contentDescription = "Tennis Logo",
                    modifier = Modifier.size(83.dp)
                )
            }
        }
    }
}
