package com.datazync.greenedgevilla.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datazync.greenedgevilla.data.model.RoomUnit
import com.datazync.greenedgevilla.ui.components.RoomUnitCard
import com.datazync.greenedgevilla.ui.components.formatCurrency
import com.datazync.greenedgevilla.ui.theme.ForestGreenDark
import com.datazync.greenedgevilla.ui.theme.ForestGreenLight
import com.datazync.greenedgevilla.ui.theme.ForestGreenPrimary
import com.datazync.greenedgevilla.ui.theme.GoldAccent
import com.datazync.greenedgevilla.ui.theme.TextPrimary
import com.datazync.greenedgevilla.ui.theme.TextSecondary
import com.datazync.greenedgevilla.ui.theme.WarmBeigeBackground
import com.datazync.greenedgevilla.ui.theme.WarmBeigeBorder
import com.datazync.greenedgevilla.ui.theme.WarmBeigeSurfaceVariant
import com.datazync.greenedgevilla.ui.viewmodel.VillaViewModel

@Composable
fun RoomsScreen(
    viewModel: VillaViewModel,
    onOpenRoomDetail: (RoomUnit) -> Unit,
    onBookUnit: (RoomUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredUnits by viewModel.filteredUnits.collectAsStateWithLifecycle()
    val filterBlock by viewModel.filterBlock.collectAsStateWithLifecycle()
    val filterBhk by viewModel.filterBhk.collectAsStateWithLifecycle()
    val filterPriceMax by viewModel.filterPriceMax.collectAsStateWithLifecycle()
    val filterOnlyAvail by viewModel.filterOnlyAvailable.collectAsStateWithLifecycle()

    var showAdvancedFilters by remember { mutableStateOf(false) }

    val blocks = listOf("All", "Block A", "Block B", "Block C", "Block D")
    val bhkTypes = listOf("All", "3 BHK", "2 BHK", "1 BHK")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBeigeBackground)
            .testTag("rooms_screen_list"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Villas & Rooms",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                    Text(
                        text = "6 private units across 4 blocks • 15 cots total",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (showAdvancedFilters) ForestGreenPrimary else WarmBeigeSurfaceVariant,
                    border = BorderStroke(1.dp, WarmBeigeBorder),
                    modifier = Modifier.testTag("toggle_filters_button")
                ) {
                    TextButton(
                        onClick = { showAdvancedFilters = !showAdvancedFilters },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filters",
                            tint = if (showAdvancedFilters) Color.White else ForestGreenPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (showAdvancedFilters) "Hide" else "Filter",
                            color = if (showAdvancedFilters) Color.White else ForestGreenPrimary,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Quick Block Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(blocks) { block ->
                    val isSelected = filterBlock.equals(block, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setBlockFilter(block) },
                        label = { Text(block) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ForestGreenPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = TextPrimary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = WarmBeigeBorder,
                            selectedBorderColor = ForestGreenPrimary,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }
        }

        // Advanced Filter Expandable Panel
        if (showAdvancedFilters) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, WarmBeigeBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Filter by BHK Configuration",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            bhkTypes.forEach { bhk ->
                                val isSelected = filterBhk.equals(bhk, ignoreCase = true)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.setBhkFilter(bhk) },
                                    label = { Text(bhk) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ForestGreenPrimary,
                                        selectedLabelColor = Color.White,
                                        containerColor = WarmBeigeBackground,
                                        labelColor = TextPrimary
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Price range filter
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Max Price per Night",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenPrimary
                            )
                            Text(
                                text = "Up to ${formatCurrency(filterPriceMax.toDouble())}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = GoldAccent
                            )
                        }

                        Slider(
                            value = filterPriceMax,
                            onValueChange = { viewModel.setPriceMaxFilter(it) },
                            valueRange = 4000f..15000f,
                            steps = 11,
                            colors = SliderDefaults.colors(
                                thumbColor = ForestGreenPrimary,
                                activeTrackColor = ForestGreenPrimary,
                                inactiveTrackColor = WarmBeigeBorder
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Availability only toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Show Available Only",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Hide booked or blocked units",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                            Switch(
                                checked = filterOnlyAvail,
                                onCheckedChange = { viewModel.setOnlyAvailableFilter(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = ForestGreenPrimary
                                )
                            )
                        }
                    }
                }
            }
        }

        // Room Units List
        if (filteredUnits.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, WarmBeigeBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Rooms Match Your Filters",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try adjusting the block, BHK type or price range to see more units.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        TextButton(onClick = {
                            viewModel.setBlockFilter("All")
                            viewModel.setBhkFilter("All")
                            viewModel.setPriceMaxFilter(15000f)
                            viewModel.setOnlyAvailableFilter(false)
                        }) {
                            Text("Reset Filters", color = ForestGreenPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(filteredUnits, key = { it.id }) { unit ->
                val status = viewModel.getUnitStatusForToday(unit)
                RoomUnitCard(
                    unit = unit,
                    status = status,
                    onViewDetails = { onOpenRoomDetail(unit) },
                    onBookNow = { onBookUnit(unit) }
                )
            }
        }
    }
}
