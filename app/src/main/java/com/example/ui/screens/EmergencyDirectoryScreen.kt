package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.EmergencyViewModel
import com.example.ui.MainTab
import com.example.ui.components.AdminPortalScreen
import com.example.ui.components.ContactCard
import com.example.ui.components.CoochBeharDirectoryScreen
import com.example.ui.components.EmergencyBottomNav
import com.example.ui.components.EmergencyHeader
import com.example.ui.components.EmptyStateView
import com.example.ui.components.LocationScreen
import com.example.ui.components.OfflineBanner
import com.example.ui.components.OneTap112Button
import com.example.ui.components.SearchBarAndCategories
import com.example.ui.components.WhatsNewDialog
import com.example.ui.model.AppStrings
import com.example.ui.theme.BackgroundDark
import com.example.util.LocationHelper

@Composable
fun EmergencyDirectoryScreen(
    viewModel: EmergencyViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle Phone Dialer Intent (ACTION_DIAL)
    // CRITICAL: NEVER automatically make a phone call. Opens device phone dialer with number pre-filled.
    LaunchedEffect(uiState.dialEvent) {
        uiState.dialEvent?.let { phone ->
            try {
                val cleanPhone = phone.replace(" ", "").trim()
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$cleanPhone")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(dialIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Unable to open phone dialer for: $phone",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                viewModel.onDialEventHandled()
            }
        }
    }

    // Handle View Source Intent (HTTPS URL in browser)
    LaunchedEffect(uiState.urlEvent) {
        uiState.urlEvent?.let { url ->
            try {
                val safeUrl = if (url.startsWith("https://")) url else "https://$url"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(safeUrl)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Unable to open source URL: $url",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                viewModel.onUrlEventHandled()
            }
        }
    }

    // Handle Map & Navigation Intent
    LaunchedEffect(uiState.mapEvent) {
        uiState.mapEvent?.let { ev ->
            LocationHelper.openNavigation(
                context = context,
                latitude = ev.latitude,
                longitude = ev.longitude,
                label = ev.label,
                address = ev.address
            )
            viewModel.onMapEventHandled()
        }
    }

    // Handle Snackbars
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.dismissSnackbar()
        }
    }

    // Category count mapping
    val categoryCounts = remember(uiState.allContacts) {
        val map = mutableMapOf<String, Int>()
        map["All"] = uiState.allContacts.size
        AppStrings.allCategories.forEach { cat ->
            if (cat != "All") {
                map[cat] = uiState.allContacts.count { it.category.equals(cat, ignoreCase = true) }
            }
        }
        map
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("emergency_directory_screen"),
        containerColor = BackgroundDark,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            EmergencyHeader(
                language = uiState.language,
                isRefreshing = uiState.isRefreshing,
                onToggleLanguage = { viewModel.toggleLanguage() },
                onRefresh = { viewModel.refreshDirectory() },
                onLogout = { viewModel.logout() }
            )
        },
        bottomBar = {
            EmergencyBottomNav(
                currentTab = uiState.currentTab,
                language = uiState.language,
                onTabSelected = { viewModel.setTab(it) }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 70.dp)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundDark)
        ) {
            when (uiState.currentTab) {
                MainTab.DIRECTORY -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        // Large 112 EMERGENCY button
                        item(key = "112_button") {
                            OneTap112Button(
                                language = uiState.language,
                                onDial112 = { viewModel.onDialRequested("112") }
                            )
                        }

                        // Prominent "New Features Active" Banner
                        item(key = "new_features_banner") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFF14223A))
                                    .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                    .clickable { viewModel.showWhatsNew() }
                                    .padding(12.dp)
                                    .testTag("whats_new_banner")
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF38BDF8).copy(alpha = 0.18f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "🚀", fontSize = 18.sp)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = AppStrings.whatsNewBannerTitle(uiState.language),
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFE0F2FE),
                                                fontSize = 13.sp
                                            )
                                        )
                                        Text(
                                            text = AppStrings.whatsNewBannerSubtitle(uiState.language),
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color(0xFF7DD3FC),
                                                fontSize = 11.sp
                                            ),
                                            maxLines = 1
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF0284C7)
                                    ) {
                                        Text(
                                            text = AppStrings.whatsNewBannerBtn(uiState.language),
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Offline Banner if offline
                        if (uiState.isOffline) {
                            item(key = "offline_banner") {
                                OfflineBanner(
                                    language = uiState.language,
                                    lastUpdatedTime = uiState.lastUpdatedTime
                                )
                            }
                        }

                        // Search Bar & Instant Category Filter Chips
                        item(key = "search_and_categories") {
                            SearchBarAndCategories(
                                searchQuery = uiState.searchQuery,
                                onSearchChange = { viewModel.onSearchQueryChange(it) },
                                selectedCategory = uiState.selectedCategory,
                                onCategorySelect = { viewModel.onCategorySelected(it) },
                                language = uiState.language,
                                categoryCounts = categoryCounts
                            )
                        }

                        // Verified Directory Contacts
                        if (uiState.filteredContacts.isEmpty()) {
                            item(key = "empty_state") {
                                EmptyStateView(language = uiState.language)
                            }
                        } else {
                            items(
                                items = uiState.filteredContacts,
                                key = { it.id }
                            ) { contact ->
                                ContactCard(
                                    contact = contact,
                                    language = uiState.language,
                                    onDial = { phone -> viewModel.onDialRequested(phone) },
                                    onViewSource = { url -> viewModel.onViewSourceRequested(url) }
                                )
                            }
                        }
                    }
                }

                MainTab.LOCATION -> {
                    LocationScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        modifier = Modifier.padding(bottom = 70.dp)
                    )
                }

                MainTab.COOCH_BEHAR -> {
                    CoochBeharDirectoryScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        modifier = Modifier.padding(bottom = 70.dp)
                    )
                }

                MainTab.ADMIN -> {
                    AdminPortalScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        modifier = Modifier.padding(bottom = 70.dp)
                    )
                }

                MainTab.ABOUT, MainTab.DISCLAIMER, MainTab.PRIVACY, MainTab.SUPPORT -> {
                    InfoDialog(
                        tab = uiState.currentTab,
                        language = uiState.language,
                        onDismiss = {
                            viewModel.clearUpdateResult()
                            viewModel.setTab(MainTab.DIRECTORY)
                        },
                        onOpenWhatsNew = {
                            viewModel.setTab(MainTab.DIRECTORY)
                            viewModel.showWhatsNew()
                        },
                        isCheckingUpdate = uiState.isCheckingUpdate,
                        updateResult = uiState.updateCheckResult,
                        onCheckUpdate = { viewModel.checkForAppUpdates() }
                    )
                }
            }
        }

        // What's New Feature Dialog (shows automatically on entry or upon user request)
        if (uiState.showWhatsNewDialog) {
            WhatsNewDialog(
                language = uiState.language,
                onDismiss = { viewModel.dismissWhatsNew() },
                onNavigateToTab = { tab ->
                    viewModel.dismissWhatsNew()
                    viewModel.setTab(tab)
                }
            )
        }
    }
}
