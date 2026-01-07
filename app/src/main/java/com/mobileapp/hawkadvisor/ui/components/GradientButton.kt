package com.mobileapp.hawkadvisor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import com.mobileapp.hawkadvisor.ui.theme.BlueDarkPrimary
import com.mobileapp.hawkadvisor.ui.theme.BlueLightPrimary
import com.mobileapp.hawkadvisor.ui.theme.Orange

@Composable
fun GradientButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Determine gradient colors based on system theme
    val gradientColors = if (isSystemInDarkTheme()) {
        listOf(BlueDarkPrimary, Orange)
    } else {
        listOf(BlueLightPrimary, Orange)
    }

    val gradient = Brush.horizontalGradient(colors = gradientColors)

    val contentColorForButton = Color.White

    val cornerRadius = 12.dp

    Box(
        modifier = modifier
            .height(50.dp)
            // Clip the Box to the desired shape before applying the background
            .clip(RoundedCornerShape(cornerRadius))
            .background(gradient)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .padding(PaddingValues(horizontal = 16.dp)),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColorForButton) {
            content()
        }
    }
}