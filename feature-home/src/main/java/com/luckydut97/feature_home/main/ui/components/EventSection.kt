package com.luckydut97.feature_home.main.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.feature.home.R
import com.luckydut97.tennispark.core.ui.theme.Pretendard

@Composable
fun EventSection(
    currentPage: Int,
    totalPages: Int,
    onMembershipClick: () -> Unit,
    onAcademyClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp)
            .padding(top = 28.dp, bottom = 20.dp)
    ) {
        Text(
            text = "이벤트",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Pretendard,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 이벤트 카드 (페이지 기반으로 표시)
        when (currentPage) {
            0 -> EventCard(
                iconRes = R.drawable.ic_member,
                title = "멤버십 등록하기",
                subtitle = "5월 정기 멤버십 등록",
                onClick = onMembershipClick
            )
            1 -> EventCard(
                iconRes = R.drawable.ic_tennis,
                title = "아카데미 등록하기",
                subtitle = "5월 아카데미 등록",
                onClick = onAcademyClick
            )
        }

        // 페이지 표시기
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "${currentPage + 1}/$totalPages",
                fontSize = 12.sp,
                fontFamily = Pretendard,
                color = Color(0xFF8B9096)
            )
        }
    }
}

@Composable
fun EventCard(
    iconRes: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 아이콘
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(55.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Pretendard,
                    color = Color.Black
                )

                Text(
                    text = subtitle,
                    fontSize = 15.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF8B9096)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "Arrow Right",
                modifier = Modifier.size(width = 6.dp, height = 12.dp)
            )
        }
    }
}