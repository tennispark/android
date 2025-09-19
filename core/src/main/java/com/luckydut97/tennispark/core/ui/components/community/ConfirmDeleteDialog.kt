package com.luckydut97.tennispark.core.ui.components.community

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    message: String = "삭제하시겠습니까?"
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier
                .width(345.dp)
                .height(210.dp)
                .background(Color.White, RoundedCornerShape(10.dp))
                .padding(horizontal = 18.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = message,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF8B9096)
            )

            Spacer(modifier = Modifier.height(18.dp))

            TextButton(
                onClick = onConfirm,
                modifier = Modifier
                    .width(309.dp)
                    .height(45.dp)
                    .border(1.dp, Color(0x00145F44), RoundedCornerShape(6.dp)),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color(0xFF145F44),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "확인",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .width(309.dp)
                    .height(45.dp)
                    .border(1.dp, Color(0xFFCECECE), RoundedCornerShape(6.dp)),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF000000)
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "취소",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
