package com.mobileapp.hawkadvisor.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.mobileapp.hawkadvisor.ui.theme.BlueDarkPrimary
import com.mobileapp.hawkadvisor.ui.theme.BlueLightPrimary
import com.mobileapp.hawkadvisor.ui.theme.Orange

@Composable
fun GradientText(
    text: String,
    style: TextStyle = TextStyle.Default,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier, //optional
) {
    var gradientColors = listOf(BlueLightPrimary, Orange)
    if (isSystemInDarkTheme()) {
        gradientColors = listOf(BlueDarkPrimary, Orange)
    }
    Text(
        text = text,
        modifier = modifier,
        style = style.copy(
            brush = Brush.horizontalGradient(
                colors = gradientColors
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
fun GradientTextPreview() {
    GradientText(
        text = "Start Your Journey",
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        ),
    )
}