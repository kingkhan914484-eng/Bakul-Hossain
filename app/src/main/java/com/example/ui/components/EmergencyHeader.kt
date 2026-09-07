package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.model.AppStrings
import com.example.ui.model.EmergencyLanguage
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun EmergencyHeader(
    language: EmergencyLanguage,
    isRefreshing: Boolean,
    onToggleLanguage: () -> Unit,
    onRefresh: () -> Unit,
    onLogout: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isRefreshing) 360f else 0f,
        label = "refresh_rotation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceCard)
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // App Logo
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .border(2.dp, EmergencyRed, CircleShape)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_bm_logo),
                        contentDescription = "BM Emergency Global 24 HS Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "BM Emergency Global 24 HS 🌍🆘",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        ),
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = AppStrings.tagline(language),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = EmergencyRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action controls: Language switch, Refresh, and Logout
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Bilingual switcher button (BN / EN)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF202738))
                        .border(1.dp, Color(0xFF38435C), RoundedCornerShape(20.dp))
                        .clickable(onClick = onToggleLanguage)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("language_toggle_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Switch Language",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (language == EmergencyLanguage.ENGLISH) "বাংলা" else "EN",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Refresh Button
                IconButton(
                    onClick = onRefresh,
                    enabled = !isRefreshing,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF202738))
                        .border(1.dp, Color(0xFF38435C), CircleShape)
                        .testTag("refresh_button")
                ) {
                    if (isRefreshing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = EmergencyRed,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = AppStrings.refresh(language),
                            tint = TextPrimary,
                            modifier = Modifier
                                .size(18.dp)
                                .rotate(rotation)
                        )
                    }
                }

                // Logout Button (if logged in)
                if (onLogout != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF202738))
                            .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f), CircleShape)
                            .testTag("header_logout_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = AppStrings.logout(language),
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
