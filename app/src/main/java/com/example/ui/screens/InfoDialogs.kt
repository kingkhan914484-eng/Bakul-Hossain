package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.ui.MainTab
import com.example.ui.model.AppStrings
import com.example.ui.model.EmergencyLanguage
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun InfoDialog(
    tab: MainTab,
    language: EmergencyLanguage,
    onDismiss: () -> Unit,
    onOpenWhatsNew: (() -> Unit)? = null,
    isCheckingUpdate: Boolean = false,
    updateResult: String? = null,
    onCheckUpdate: (() -> Unit)? = null
) {
    val title: String
    val icon: ImageVector
    val iconTint: Color
    val contentText: String

    when (tab) {
        MainTab.ABOUT -> {
            title = AppStrings.tabAbout(language)
            icon = Icons.Default.Info
            iconTint = Color(0xFF38BDF8)
            contentText = AppStrings.aboutDescription(language)
        }
        MainTab.DISCLAIMER -> {
            title = AppStrings.disclaimerTitle(language)
            icon = Icons.Default.Warning
            iconTint = WarningAmber
            contentText = AppStrings.disclaimerNotice(language)
        }
        MainTab.PRIVACY -> {
            title = AppStrings.tabPrivacy(language)
            icon = Icons.Default.PrivacyTip
            iconTint = Color(0xFF34D399)
            contentText = AppStrings.privacyContent(language)
        }
        MainTab.SUPPORT -> {
            title = AppStrings.tabSupport(language)
            icon = Icons.Default.SupportAgent
            iconTint = Color(0xFFA78BFA)
            contentText = AppStrings.supportContent(language)
        }
        else -> return
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = SurfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("info_dialog_${tab.name.lowercase()}")
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header with Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(iconTint.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 17.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )

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

                Spacer(modifier = Modifier.height(16.dp))

                // If About dialog, show the branded logo prominently
                if (tab == MainTab.ABOUT) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .border(2.dp, EmergencyRed, CircleShape)
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_bm_logo),
                                contentDescription = "BM Emergency Logo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(84.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // If Disclaimer, show a prominent high-contrast alert box
                if (tab == MainTab.DISCLAIMER) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF451A03))
                            .border(1.dp, WarningAmber, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = if (language == EmergencyLanguage.BENGALI)
                                "⚠️ জরুরি পরিস্থিতিতে কোনো দেরি না করে প্রথমে ১১২ ডায়াল করুন।"
                            else
                                "⚠️ In an active emergency, dial 112 directly without delay.",
                            color = Color(0xFFFDE68A),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Body content
                Text(
                    text = contentText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )
                )

                // If About dialog, display comprehensive App Update & New Features section
                if (tab == MainTab.ABOUT) {
                    val context = LocalContext.current
                    val isBn = language == EmergencyLanguage.BENGALI
                    Spacer(modifier = Modifier.height(18.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF141C2B))
                            .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                            .testTag("about_update_section")
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF38BDF8).copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SystemUpdate,
                                        contentDescription = null,
                                        tint = Color(0xFF38BDF8),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = AppStrings.updateSectionTitle(language),
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary,
                                            fontSize = 15.sp
                                        )
                                    )
                                    Text(
                                        text = AppStrings.appVersionLabel(language),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF38BDF8),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }

                            // Notice that all new features are already compiled and active
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF0F2D24))
                                    .border(1.dp, Color(0xFF34D399).copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                    .padding(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF34D399),
                                        modifier = Modifier.size(16.dp).padding(top = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = AppStrings.updateStatusNotice(language),
                                        color = Color(0xFFA7F3D0),
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            // Check for update feedback or button
                            if (isCheckingUpdate) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = Color(0xFF38BDF8),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = AppStrings.checkingUpdate(language),
                                        color = Color(0xFF38BDF8),
                                        fontSize = 13.sp
                                    )
                                }
                            } else if (updateResult != null) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF1E293B))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = updateResult,
                                        color = Color(0xFFE2E8F0),
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                            }

                            // Check for Updates action
                            OutlinedButton(
                                onClick = { onCheckUpdate?.invoke() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(38.dp)
                                    .testTag("about_check_update_button"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Color(0xFF38BDF8)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = AppStrings.checkUpdateBtn(language),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF38BDF8)
                                )
                            }

                            // What's New showcase button
                            if (onOpenWhatsNew != null) {
                                Button(
                                    onClick = onOpenWhatsNew,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(38.dp)
                                        .testTag("about_open_whats_new_button"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.NewReleases,
                                        contentDescription = null,
                                        tint = Color(0xFFF59E0B),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isBn) "🌟 নতুন ফিচার পরিচিতি দেখুন" else "🌟 View What's New Guide",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFCD34D)
                                    )
                                }
                            }

                            // Feature List Checklist
                            Column(
                                modifier = Modifier.padding(top = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = AppStrings.featuresSummaryTitle(language),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextMuted,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = if (isBn)
                                        "• 📍 লাইভ GPS ও নিকটতম হাসপাতাল/থানার দূরত্ব (কিমি)\n" +
                                        "• 🏛️ কোচবিহার ১২টি ব্লক ও ১২৮টি গ্রাম পঞ্চায়েত ডিরেক্টরি\n" +
                                        "• 🛡️ পিন-সুরক্ষিত অ্যাডমিন পোর্টাল ও অডিট ট্রেইল\n" +
                                        "• 📞 ১১২ এক-ট্যাপ ডায়ালার ও নিরাপদ ওটিপি লগইন"
                                    else
                                        "• 📍 Live GPS & Nearest Hospital/Police Distance (km)\n" +
                                        "• 🏛️ Cooch Behar 12 Blocks & 128 Gram Panchayats\n" +
                                        "• 🛡️ PIN-secured Admin Portal & Realtime Audit Log\n" +
                                        "• 📞 112 One-Tap Dialer & Secure Mobile OTP Login",
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }

                            // Download / External Link button
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF1E293B))
                                    .border(1.dp, Color(0xFF475569), RoundedCornerShape(10.dp))
                                    .clickable {
                                        try {
                                            val webIntent = Intent(Intent.ACTION_VIEW).apply {
                                                data = Uri.parse("https://coochbehar.gov.in")
                                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            }
                                            context.startActivity(webIntent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Unable to open link", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .testTag("about_download_link")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Download,
                                        contentDescription = null,
                                        tint = TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = AppStrings.downloadUpdateBtn(language),
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "↗",
                                        color = TextMuted,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // If Support dialog, display direct interactive WhatsApp and Instagram action buttons
                if (tab == MainTab.SUPPORT) {
                    val context = LocalContext.current
                    Spacer(modifier = Modifier.height(16.dp))

                    // WhatsApp Action Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF0F382E))
                            .border(1.dp, Color(0xFF25D366), RoundedCornerShape(14.dp))
                            .clickable {
                                try {
                                    val waIntent = Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse("https://wa.me/919144844323")
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                    context.startActivity(waIntent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Unable to open WhatsApp", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 13.dp)
                            .testTag("support_whatsapp_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "💬",
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == EmergencyLanguage.BENGALI) "হোয়াটসঅ্যাপে মেসেজ পাঠান" else "Chat on WhatsApp",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "+91 9144844323",
                                    color = Color(0xFF6EE7B7),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = "↗",
                                color = Color(0xFF25D366),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Instagram Action Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF3B152B))
                            .border(1.dp, Color(0xFFE1306C), RoundedCornerShape(14.dp))
                            .clickable {
                                try {
                                    val igIntent = Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse("https://instagram.com/bakul_king_10k")
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                    context.startActivity(igIntent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Unable to open Instagram", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 13.dp)
                            .testTag("support_instagram_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "📸",
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == EmergencyLanguage.BENGALI) "ইনস্টাগ্রামে যোগাযোগ করুন" else "Connect on Instagram",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "@bakul_king_10k",
                                    color = Color(0xFFF472B6),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = "↗",
                                color = Color(0xFFE1306C),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bottom Action
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222B3D))
                ) {
                    Text(
                        text = if (language == EmergencyLanguage.BENGALI) "বন্ধ করুন" else "Dismiss",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
