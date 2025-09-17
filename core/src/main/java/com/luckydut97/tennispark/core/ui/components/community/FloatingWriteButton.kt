package com.luckydut97.tennispark.core.ui.components.community

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.R

/**
 * 플로팅 글쓰기 버튼 컴포넌트
 */
@Composable
fun FloatingWriteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(101.dp)
            .height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1C7756)
        ),
        shape = RoundedCornerShape(50.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_white_plus),
                contentDescription = "글쓰기",
                tint = Color.White,
                modifier = Modifier.size(17.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "글쓰기",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
                /*modifier = Modifier.offset(y = (-2).dp)*/
            )
        }
    }
}