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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import com.example.ui.MainTab
import com.example.ui.model.AppStrings
import com.example.ui.model.EmergencyLanguage
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun WhatsNewDialog(
    language: EmergencyLanguage,
    onDismiss: () -> Unit,
    onNavigateToTab: (MainTab) -> Unit
) {
    val isBn = language == EmergencyLanguage.BENGALI

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = SurfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("whats_new_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with badge and close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EmergencyRed.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmergencyRed.copy(alpha = 0.6f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = EmergencyRed,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isBn) "নতুন আপডেট v2.4.0" else "NEW UPDATE v2.4.0",
                                color = Color(0xFFFF8080),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title & Subtitle
                Text(
                    text = AppStrings.whatsNewTitle(language),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isBn)
                        "আপনার অ্যাপে নিচের সমস্ত নতুন ফিচার সরাসরি যুক্ত করা হয়েছে — আলাদা কোনো ফাইল ডাউনলোড না করলেও আপনি সবগুলো ফিচার ব্যবহার করতে পারবেন!"
                    else
                        "All newly developed features are compiled directly into this app — explore them right now without waiting for any download!",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF6EE7B7),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Feature 1: Location & GPS Distance
                FeatureCard(
                    icon = Icons.Default.LocationOn,
                    iconColor = Color(0xFF38BDF8),
                    title = if (isBn) "১. লাইভ GPS ও দূরত্বের তালিকা" else "1. Live GPS & Nearest Distance",
                    description = if (isBn)
                        "আপনার লাইভ জিপিএস থেকে নিকটতম হাসপাতাল, থানা ও জরুরি সেবার সঠিক দূরত্ব (কিমি) হিসাব এবং ১-ট্যাপে সরাসরি গুগল ম্যাপে নেভিগেশন।"
                    else
                        "Computes precise kilometer distance from your GPS to nearest hospitals and police stations with 1-tap Google Maps navigation.",
                    buttonText = if (isBn) "লোকেশন ফিচার দেখুন ↗" else "Explore Location ↗",
                    onClick = {
                        onDismiss()
                        onNavigateToTab(MainTab.LOCATION)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Feature 2: Cooch Behar Administrative Directory
                FeatureCard(
                    icon = Icons.Default.LocationCity,
                    iconColor = Color(0xFF34D399),
                    title = if (isBn) "২. কোচবিহার পূর্ণাঙ্গ ডিরেক্টরি" else "2. Cooch Behar Admin Directory",
                    description = if (isBn)
                        "জেলার ৫টি মহকুমা, ১২টি ব্লক, ১২৮টি গ্রাম পঞ্চায়েত, গ্রাম, ডাকঘর ও পিন কোড ভিত্তিক জরুরি যোগাযোগ ও স্থানীয় সেবা তালিকা।"
                    else
                        "All 5 Subdivisions, 12 Blocks, 128 Gram Panchayats, Villages, Post Offices, and PIN codes linked with local emergency services.",
                    buttonText = if (isBn) "কোচবিহার ডিরেক্টরি দেখুন ↗" else "Explore Directory ↗",
                    onClick = {
                        onDismiss()
                        onNavigateToTab(MainTab.COOCH_BEHAR)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Feature 3: Admin Portal & Audit Log
                FeatureCard(
                    icon = Icons.Default.AdminPanelSettings,
                    iconColor = Color(0xFFF59E0B),
                    title = if (isBn) "৩. অ্যাডমিন পোর্টাল ও অডিট লগ" else "3. Admin Security & Audit Trail",
                    description = if (isBn)
                        "পিন (ডিফল্ট: 2026) দিয়ে সুরক্ষিত ড্যাশবোর্ড। নম্বর তৈরি, যাচাই, রিভিউ এবং যেকোনো পরিবর্তনের স্বচ্ছ অডিট ট্রেইল।"
                    else
                        "PIN-secured dashboard (PIN: 2026). Create, review, verify records, with a fully persistent audit log.",
                    buttonText = if (isBn) "অ্যাডমিন পোর্টাল দেখুন ↗" else "Explore Admin ↗",
                    onClick = {
                        onDismiss()
                        onNavigateToTab(MainTab.ADMIN)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Feature 4: One-Tap 112 & Authentication
                FeatureCard(
                    icon = Icons.Default.PhoneInTalk,
                    iconColor = EmergencyRed,
                    title = if (isBn) "৪. ১১২ ওয়ান-ট্যাপ ডায়াল ও লগইন" else "4. One-Tap 112 & OTP Login",
                    description = if (isBn)
                        "যেকোনো বিপদে সরাসরি ১১২ ডায়াল এবং নিরাপদ মোবাইল নম্বর ও ৪ সংখ্যার ওটিপি ভেরিফিকেশন।"
                    else
                        "Instant 112 dialer dispatch and phone + 4-digit OTP verified secure access.",
                    buttonText = if (isBn) "মূল ডিরেক্টরি দেখুন ↗" else "View Main Directory ↗",
                    onClick = {
                        onDismiss()
                        onNavigateToTab(MainTab.DIRECTORY)
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Primary Enter / Dismiss Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("whats_new_dismiss_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed)
                ) {
                    Text(
                        text = AppStrings.whatsNewDismissBtn(language),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isBn)
                        "ℹ️ আপনি যেকোনো সময় About (সম্পর্কে) অপশন বা হোম পেজ থেকে এই ফিচারগুলো পুনরায় দেখতে পারবেন।"
                    else
                        "ℹ️ You can re-open this feature overview anytime from the About section or top banner.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMuted,
                        fontSize = 11.sp
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun FeatureCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1B2333))
            .border(1.dp, iconColor.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 14.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconColor.copy(alpha = 0.12f))
                    .clickable(onClick = onClick)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buttonText,
                    color = iconColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}
