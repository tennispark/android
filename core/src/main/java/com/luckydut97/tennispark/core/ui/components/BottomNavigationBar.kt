package com.luckydut97.tennispark.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 앱 하단 네비게이션 바
 */
@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavigationItem(
            route = "home",
            title = "홈",
            activeIconRes = com.luckydut97.tennispark.core.R.drawable.ic_home_active,
            inactiveIconRes = com.luckydut97.tennispark.core.R.drawable.ic_home_deactive
        ),
        NavigationItem(
            route = "shopping",
            title = "상품 구매",
            activeIconRes = com.luckydut97.tennispark.core.R.drawable.ic_buy_active,
            inactiveIconRes = com.luckydut97.tennispark.core.R.drawable.ic_buy_deactive
        ),
        NavigationItem(
            route = "mypage",
            title = "내 정보",
            activeIconRes = com.luckydut97.tennispark.core.R.drawable.ic_info_active,
            inactiveIconRes = com.luckydut97.tennispark.core.R.drawable.ic_info_deactive
        )
    )

    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        containerColor = Color.White,
        contentColor = AppColors.Primary
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (selected) item.activeIconRes else item.inactiveIconRes
                            ),
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = item.title,
                            fontFamily = Pretendard,
                            fontSize = 12.sp,
                            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                },
                selected = selected,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppColors.Primary,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = AppColors.Primary,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.White
                )
            )
        }
    }
}

data class NavigationItem(
    val route: String,
    val title: String,
    val activeIconRes: Int,
    val inactiveIconRes: Int
)