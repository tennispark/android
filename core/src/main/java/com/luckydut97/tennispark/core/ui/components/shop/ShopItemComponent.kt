package com.luckydut97.tennispark.core.ui.components.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.R
import com.luckydut97.tennispark.core.shop.data.model.ShopItem
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 상품 아이템 컴포넌트
 * 크기: fillMaxWidth × 100dp
 */
@Composable
fun ShopItemComponent(
    item: ShopItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 왼쪽 이미지 박스
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            // 나중에 서버에서 이미지가 오면 여기에 AsyncImage 등으로 표시
        }

        Spacer(modifier = Modifier.width(20.dp))

        // 오른쪽 상품 정보
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // 브랜드명
            Text(
                text = item.brandName,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Pretendard,
                color = Color(0xFF959595)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 제품명
            Text(
                text = item.productName,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Pretendard,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 가격 정보
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 코인 아이콘 (임시로 원형 박스)
                Icon(
                    painter = painterResource(id = R.drawable.ic_coin_green),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Unspecified // 원본 색상 유지
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
    }
}
