package com.luckydut97.tennispark.core.ui.components.selection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.R
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.tennispark.core.ui.theme.Pretendard

@Composable
fun CheckBox(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    fontSize: Int = 14,
    fontWeight: FontWeight = FontWeight.SemiBold,
    letterSpacing: Int = -1
) {
    Row(
        modifier = modifier.clickable { onCheckedChange(!isChecked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(
                id = if (isChecked) R.drawable.ic_checkbox_checked 
                     else R.drawable.ic_checkbox_unchecked
            ),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = androidx.compose.ui.graphics.Color.Unspecified
        )

        Spacer(modifier = Modifier.width(2.dp))

        Text(
            text = text,
            fontSize = fontSize.sp,
            fontWeight = if (isChecked) FontWeight.Bold else fontWeight,
            color = if (isChecked) AppColors.Primary else AppColors.TextPrimary,
            fontFamily = Pretendard,
            letterSpacing = letterSpacing.sp
        )
    }
}
