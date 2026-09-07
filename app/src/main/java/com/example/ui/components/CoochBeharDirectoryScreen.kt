package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.EmergencyContact
import com.example.data.model.VillageRecord
import com.example.data.seed.CoochBeharAdminDirectoryData
import com.example.ui.EmergencyUiState
import com.example.ui.EmergencyViewModel
import com.example.ui.model.AppStrings
import com.example.ui.model.EmergencyLanguage

@Composable
fun CoochBeharDirectoryScreen(
    uiState: EmergencyUiState,
    viewModel: EmergencyViewModel,
    modifier: Modifier = Modifier
) {
    val lang = uiState.language

    val subdivisions: List<String> = remember {
        listOf("All") + CoochBeharAdminDirectoryData.subdivisions.map { it.nameEn }
    }

    val selectedSubdivisionObj = remember(uiState.cbSubdivision) {
        CoochBeharAdminDirectoryData.subdivisions.firstOrNull { it.nameEn.equals(uiState.cbSubdivision, ignoreCase = true) }
    }

    val availableBlocks: List<String> = remember(uiState.cbSubdivision, selectedSubdivisionObj) {
        val allBlocks = CoochBeharAdminDirectoryData.blocks
        val filtered = if (uiState.cbSubdivision == "All" || selectedSubdivisionObj == null) {
            allBlocks
        } else {
            allBlocks.filter { it.subdivisionId == selectedSubdivisionObj.id }
        }
        listOf("All") + filtered.map { it.nameEn }
    }

    val selectedBlockObj = remember(uiState.cbBlock) {
        CoochBeharAdminDirectoryData.blocks.firstOrNull { it.nameEn.equals(uiState.cbBlock, ignoreCase = true) }
    }

    val availableGps: List<String> = remember(uiState.cbBlock, selectedBlockObj) {
        val allGps = CoochBeharAdminDirectoryData.gramPanchayats
        val filtered = if (uiState.cbBlock == "All" || selectedBlockObj == null) {
            allGps
        } else {
            allGps.filter { it.blockId == selectedBlockObj.id }
        }
        listOf("All") + filtered.map { it.nameEn }
    }

    // Filter contacts matching Cooch Behar subdivision and block
    val filteredContacts: List<EmergencyContact> = remember(
        uiState.allContacts,
        uiState.cbSubdivision,
        uiState.cbBlock,
        uiState.cbSearchQuery
    ) {
        val q = uiState.cbSearchQuery.trim().lowercase()
        uiState.allContacts.filter { c ->
            val matchesDistrict = c.district.equals("Cooch Behar", ignoreCase = true) || c.district.equals("All", ignoreCase = true)
            val matchesSub = uiState.cbSubdivision == "All" || c.subdivision.equals(uiState.cbSubdivision, ignoreCase = true)
            val matchesBlock = uiState.cbBlock == "All" || c.block.equals(uiState.cbBlock, ignoreCase = true) || c.blockTown.contains(uiState.cbBlock, ignoreCase = true)
            val matchesSearch = q.isEmpty() ||
                    c.name.lowercase().contains(q) ||
                    c.address.lowercase().contains(q) ||
                    c.pincode.lowercase().contains(q) ||
                    c.phonePrimary.contains(q)
            matchesDistrict && matchesSub && matchesBlock && matchesSearch
        }
    }

    // Filter verified villages
    val filteredVillages: List<VillageRecord> = remember(
        uiState.cbSubdivision,
        uiState.cbBlock,
        uiState.cbGp,
        uiState.cbSearchQuery
    ) {
        val q = uiState.cbSearchQuery.trim().lowercase()
        CoochBeharAdminDirectoryData.villageRecords.filter { v ->
            val matchesSub = uiState.cbSubdivision == "All" || v.subdivision.equals(uiState.cbSubdivision, ignoreCase = true)
            val matchesBlock = uiState.cbBlock == "All" || v.block.equals(uiState.cbBlock, ignoreCase = true)
            val matchesGp = uiState.cbGp == "All" || v.gpName.equals(uiState.cbGp, ignoreCase = true)
            val matchesSearch = q.isEmpty() ||
                    v.villageName.lowercase().contains(q) ||
                    v.postOffice.lowercase().contains(q) ||
                    v.pincode.contains(q) ||
                    v.gpName.lowercase().contains(q)
            matchesSub && matchesBlock && matchesGp && matchesSearch
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("cooch_behar_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationCity,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = AppStrings.adminDirectoryTitle(lang),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Text(
                        text = AppStrings.adminDirectorySubtitle(lang),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = uiState.cbSearchQuery,
                onValueChange = { viewModel.onCbSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("cb_search_field"),
                placeholder = {
                    Text(
                        text = if (lang == EmergencyLanguage.BENGALI)
                            "গ্রাম, জিপি, ডাকঘর বা পিন কোড দিয়ে খুঁজুন..."
                        else
                            "Search village, GP, Post Office, PIN..."
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (uiState.cbSearchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onCbSearchQueryChange("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Subdivision Chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = AppStrings.filterSubdivision(lang),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    subdivisions.forEach { sub ->
                        FilterChip(
                            selected = uiState.cbSubdivision == sub,
                            onClick = { viewModel.onCbSubdivisionSelected(sub) },
                            label = { Text(sub) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }
        }

        // Block Chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = AppStrings.filterBlock(lang),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableBlocks.forEach { block ->
                        FilterChip(
                            selected = uiState.cbBlock == block,
                            onClick = { viewModel.onCbBlockSelected(block) },
                            label = { Text(block) }
                        )
                    }
                }
            }
        }

        // GP Chips (if block selected)
        if (availableGps.size > 1 && uiState.cbBlock != "All") {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = AppStrings.filterGP(lang),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        availableGps.forEach { gp ->
                            FilterChip(
                                selected = uiState.cbGp == gp,
                                onClick = { viewModel.onCbGpSelected(gp) },
                                label = { Text(gp) }
                            )
                        }
                    }
                }
            }
        }

        // Section: Emergency Services in this area
        item {
            Text(
                text = if (lang == EmergencyLanguage.BENGALI)
                    "এই এলাকার যাচাইকৃত জরুরি পরিষেবা (${filteredContacts.size})"
                else
                    "Verified Emergency Services in this area (${filteredContacts.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (filteredContacts.isEmpty()) {
            item {
                Text(
                    text = if (lang == EmergencyLanguage.BENGALI)
                        "নির্বাচিত এলাকায় নির্দিষ্ট জরুরি নম্বর নেই। কেন্দ্রীয় ১১২ তে ডায়াল করুন।"
                    else
                        "No specific local number found for this filter. Dial 112 for immediate dispatch.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(filteredContacts, key = { it.id }) { contact ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cb_service_${contact.id}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = AppStrings.categoryLabel(contact.category, lang),
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "${contact.subdivision.ifBlank { "Cooch Behar" }} • ${contact.block.ifBlank { contact.blockTown }}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = contact.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = contact.address,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.onDialRequested(contact.phonePrimary) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "${AppStrings.dialBtn(lang)} (${contact.phonePrimary})")
                            }

                            if (contact.latitude != null && contact.longitude != null) {
                                OutlinedButton(
                                    onClick = {
                                        viewModel.onOpenMapRequested(
                                            latitude = contact.latitude,
                                            longitude = contact.longitude,
                                            label = contact.name,
                                            address = contact.address
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Navigation,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = AppStrings.openMapBtn(lang))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Administrative Village Directory (Postal & PIN)
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${AppStrings.villageListTitle(lang)} (${filteredVillages.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(filteredVillages, key = { it.villageCode }) { village ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("village_${village.villageCode}"),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (lang == EmergencyLanguage.BENGALI)
                                village.villageNameBn.ifBlank { village.villageName }
                            else
                                village.villageName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (village.verified == "Yes")
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.errorContainer
                        ) {
                            Text(
                                text = if (village.verified == "Yes")
                                    AppStrings.verifiedBadge(lang)
                                else
                                    AppStrings.needsVerificationBadge(lang),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (village.verified == "Yes")
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = "GP: ${village.gpName} • Block: ${village.block} • Sub: ${village.subdivision}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${AppStrings.postOfficeLabel(lang)} ${village.postOffice}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${AppStrings.pinCodeLabel(lang)} ${village.pincode}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (village.notes.isNotBlank()) {
                        Text(
                            text = village.notes,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}
