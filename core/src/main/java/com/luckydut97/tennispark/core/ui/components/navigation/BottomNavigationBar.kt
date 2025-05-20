package com.luckydut97.tennispark.core.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
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
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.tennispark.core.ui.theme.Pretendard

enum class BottomNavigationItem(val route: String, val label: String) {
    HOME("home", "홈"),
    SHOP("shop", "상품 구매"),
    PROFILE("profile", "내 정보")
}

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Divider(
            color = Color(0xFFEEEEEE),
            thickness = 1.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 홈 버튼
            BottomNavItem(
                item = BottomNavigationItem.HOME,
                isSelected = currentRoute == BottomNavigationItem.HOME.route,
                onClick = { onItemClick(BottomNavigationItem.HOME.route) }
            )

            // 상품 구매 버튼
            BottomNavItem(
                item = BottomNavigationItem.SHOP,
                isSelected = currentRoute == BottomNavigationItem.SHOP.route,
                onClick = { onItemClick(BottomNavigationItem.SHOP.route) }
            )

            // 내 정보 버튼
            BottomNavItem(
                item = BottomNavigationItem.PROFILE,
                isSelected = currentRoute == BottomNavigationItem.PROFILE.route,
                onClick = { onItemClick(BottomNavigationItem.PROFILE.route) }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    item: BottomNavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconRes = when (item) {
        BottomNavigationItem.HOME -> if (isSelected) R.drawable.ic_home_active else R.drawable.ic_home_deactive
        BottomNavigationItem.SHOP -> if (isSelected) R.drawable.ic_buy_active else R.drawable.ic_buy_deactive
        BottomNavigationItem.PROFILE -> if (isSelected) R.drawable.ic_info_active else R.drawable.ic_info_deactive
    }

    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = item.label,
            tint = Color.Unspecified,  // 아이콘 원본 색상 유지
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = item.label,
            fontSize = 12.sp,
            fontFamily = Pretendard,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            color = if (isSelected) AppColors.PrimaryVariant else Color(0xFF8B9096)
        )
    }
}