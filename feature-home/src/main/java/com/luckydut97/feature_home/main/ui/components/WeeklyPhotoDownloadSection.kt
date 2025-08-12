package com.luckydut97.feature_home.main.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.components.animation.PressableComponent
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.core.utils.launchUrl
import com.luckydut97.tennispark.feature.home.R

@Composable
fun WeeklyPhotoDownloadSection() {
    val context = LocalContext.current
    val downloadUrl = "https://drive.google.com/drive/folders/1KzdEnuQZWQTyhzeyceNlT0UmZSE2Xy6i"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp)
            .padding(top = 10.dp, bottom = 50.dp) // 활동사진과 10dp 간격, 하단 50dp 여백
    ) {
        PressableComponent(
            onClick = {
                context.launchUrl(downloadUrl)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(47.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(47.dp)
                    .background(
                        color = Color(0xFF1C7756),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "활동 사진 다운받으러 가기",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Pretendard,
                        color = Color.White
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = "이동",
                        modifier = Modifier.size(12.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}