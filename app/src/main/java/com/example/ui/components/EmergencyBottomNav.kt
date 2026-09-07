package com.example.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainTab
import com.example.ui.model.AppStrings
import com.example.ui.model.EmergencyLanguage
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun EmergencyBottomNav(
    currentTab: MainTab,
    language: EmergencyLanguage,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceCard)
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .testTag("emergency_bottom_nav")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                icon = Icons.Default.MenuBook,
                label = AppStrings.tabDirectory(language),
                isSelected = currentTab == MainTab.DIRECTORY,
                activeColor = EmergencyRed,
                onClick = { onTabSelected(MainTab.DIRECTORY) },
                testTag = "nav_item_directory"
            )

            NavItem(
                icon = Icons.Default.LocationOn,
                label = AppStrings.tabLocation(language),
                isSelected = currentTab == MainTab.LOCATION,
                activeColor = Color(0xFF38BDF8),
                hasBadge = true,
                badgeText = if (language == EmergencyLanguage.BENGALI) "নতুন" else "NEW",
                onClick = { onTabSelected(MainTab.LOCATION) },
                testTag = "nav_item_location"
            )

            NavItem(
                icon = Icons.Default.LocationCity,
                label = AppStrings.tabAdminDirectory(language),
                isSelected = currentTab == MainTab.COOCH_BEHAR,
                activeColor = Color(0xFF34D399),
                hasBadge = true,
                badgeText = if (language == EmergencyLanguage.BENGALI) "নতুন" else "NEW",
                onClick = { onTabSelected(MainTab.COOCH_BEHAR) },
                testTag = "nav_item_cooch_behar"
            )

            NavItem(
                icon = Icons.Default.AdminPanelSettings,
                label = AppStrings.tabAdminPortal(language),
                isSelected = currentTab == MainTab.ADMIN,
                activeColor = Color(0xFFF59E0B),
                hasBadge = true,
                badgeText = if (language == EmergencyLanguage.BENGALI) "নতুন" else "NEW",
                onClick = { onTabSelected(MainTab.ADMIN) },
                testTag = "nav_item_admin"
            )

            NavItem(
                icon = Icons.Default.Info,
                label = AppStrings.tabAbout(language),
                isSelected = currentTab == MainTab.ABOUT ||
                        currentTab == MainTab.DISCLAIMER ||
                        currentTab == MainTab.PRIVACY ||
                        currentTab == MainTab.SUPPORT,
                activeColor = Color(0xFFA78BFA),
                onClick = { onTabSelected(MainTab.ABOUT) },
                testTag = "nav_item_about"
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    hasBadge: Boolean = false,
    badgeText: String = "NEW",
    onClick: () -> Unit,
    testTag: String
) {
    val tint = if (isSelected) activeColor else TextMuted

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = tint,
                    modifier = Modifier.size(20.dp)
                )
                if (hasBadge) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(start = 12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(EmergencyRed)
                            .padding(horizontal = 3.dp, vertical = 0.5.dp)
                    ) {
                        Text(
                            text = badgeText,
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) activeColor else TextSecondary
                ),
                maxLines = 1
            )
        }
    }
}
