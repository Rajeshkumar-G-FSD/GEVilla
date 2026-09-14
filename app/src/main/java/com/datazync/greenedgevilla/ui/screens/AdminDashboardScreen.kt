package com.datazync.greenedgevilla.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datazync.greenedgevilla.data.model.Booking
import com.datazync.greenedgevilla.data.model.RoomUnit
import com.datazync.greenedgevilla.data.model.UnitAvailabilityStatus
import com.datazync.greenedgevilla.ui.components.AvailabilityBadge
import com.datazync.greenedgevilla.ui.components.OtaSourceBadge
import com.datazync.greenedgevilla.ui.components.formatCurrency
import com.datazync.greenedgevilla.ui.theme.ForestGreenDark
import com.datazync.greenedgevilla.ui.theme.ForestGreenLight
import com.datazync.greenedgevilla.ui.theme.ForestGreenPrimary
import com.datazync.greenedgevilla.ui.theme.GoldAccent
import com.datazync.greenedgevilla.ui.theme.GoldContainer
import com.datazync.greenedgevilla.ui.theme.OnGoldContainer
import com.datazync.greenedgevilla.ui.theme.StatusAvailable
import com.datazync.greenedgevilla.ui.theme.StatusAvailableBg
import com.datazync.greenedgevilla.ui.theme.StatusBlocked
import com.datazync.greenedgevilla.ui.theme.StatusBlockedBg
import com.datazync.greenedgevilla.ui.theme.StatusBooked
import com.datazync.greenedgevilla.ui.theme.StatusBookedBg
import com.datazync.greenedgevilla.ui.theme.StatusCheckIn
import com.datazync.greenedgevilla.ui.theme.StatusCheckInBg
import com.datazync.greenedgevilla.ui.theme.StatusCheckOut
import com.datazync.greenedgevilla.ui.theme.StatusCheckOutBg
import com.datazync.greenedgevilla.ui.theme.TextPrimary
import com.datazync.greenedgevilla.ui.theme.TextSecondary
import com.datazync.greenedgevilla.ui.theme.WarmBeigeBackground
import com.datazync.greenedgevilla.ui.theme.WarmBeigeBorder
import com.datazync.greenedgevilla.ui.theme.WarmBeigeSurfaceVariant
import com.datazync.greenedgevilla.ui.viewmodel.VillaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: VillaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allUnits by viewModel.allUnits.collectAsStateWithLifecycle()
    val allBookings by viewModel.allBookings.collectAsStateWithLifecycle()
    val stats by viewModel.adminSummaryStats.collectAsStateWithLifecycle()

    var selectedDashboardTab by remember { mutableIntStateOf(0) } // 0: Overview & Units, 1: Current Date, 2: Upcoming Dates
    var selectedOtaFilter by remember { mutableStateOf("All Sources") }
    var showAddBookingDialog by remember { mutableStateOf(false) }

    val otaSources = listOf("All Sources", "Direct", "Agoda", "MakeMyTrip", "VOYE", "Booking.com", "Walk-in")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBeigeBackground)
            .testTag("admin_dashboard_scroll"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Dashboard Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Villa Manager",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                    Text(
                        text = "Real-time inventory, OTA tracking & reservations",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = { showAddBookingDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("admin_record_booking_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Booking", style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        // Summary Metric Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Total Units",
                    value = "${stats.totalUnits} (15 Cots)",
                    icon = Icons.Default.MeetingRoom,
                    iconTint = ForestGreenPrimary,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Available Today",
                    value = "${stats.availableUnitsCount} Units",
                    icon = Icons.Default.CheckCircle,
                    iconTint = StatusAvailable,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Active Bookings",
                    value = "${stats.bookedUnitsCount}",
                    icon = Icons.Default.Receipt,
                    iconTint = GoldAccent,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Secondary Metric Row: Current Today vs Upcoming Count & Total Revenue
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ForestGreenDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("TOTAL REVENUE LOGGED", style = MaterialTheme.typography.labelSmall, color = GoldAccent, fontWeight = FontWeight.Bold)
                        Text(formatCurrency(stats.totalRevenue), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text("TODAY STAYS", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                            Text("${stats.currentDateBookingsCount}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("UPCOMING", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                            Text("${stats.upcomingDateBookingsCount}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // Navigation Tabs: Overview & Units | Current Date | Upcoming Dates
        item {
            PrimaryTabRow(
                selectedTabIndex = selectedDashboardTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = ForestGreenPrimary
            ) {
                Tab(
                    selected = selectedDashboardTab == 0,
                    onClick = { selectedDashboardTab = 0 },
                    text = { Text("Inventory (6 Units)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium) }
                )
                Tab(
                    selected = selectedDashboardTab == 1,
                    onClick = { selectedDashboardTab = 1 },
                    text = { Text("Current Date (${stats.currentDateBookingsCount})", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium) }
                )
                Tab(
                    selected = selectedDashboardTab == 2,
                    onClick = { selectedDashboardTab = 2 },
                    text = { Text("Upcoming (${stats.upcomingDateBookingsCount})", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium) }
                )
            }
        }

        // Content Based on Selected Tab
        when (selectedDashboardTab) {
            0 -> {
                // INVENTORY OVERVIEW TAB
                item {
                    Text(
                        text = "Unit Status & Maintenance Controls",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                }

                items(allUnits, key = { it.id }) { unit ->
                    val unitStatus = viewModel.getUnitStatusForToday(unit)
                    val currentBooking = allBookings.find {
                        it.unitId == unit.id && (it.bookingStatus == "Current Stay" || it.bookingStatus == "Upcoming")
                    }

                    AdminUnitCard(
                        unit = unit,
                        status = unitStatus,
                        activeBooking = currentBooking,
                        onToggleBlock = {
                            viewModel.toggleUnitBlock(unit)
                            val action = if (unit.isBlocked) "Unblocked" else "Blocked"
                            Toast.makeText(context, "Unit ${unit.id} $action for maintenance", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            1 -> {
                // CURRENT DATE TAB
                item {
                    Column {
                        Text(
                            text = "Current Date Bookings (Guests In-House / Checking In Today)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                        Text(
                            text = "Filtered by current calendar date across all OTA channels",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // OTA Source Filter Chips
                item {
                    OtaFilterChips(
                        sources = otaSources,
                        selected = selectedOtaFilter,
                        onSelect = { selectedOtaFilter = it }
                    )
                }

                val currentBookings = allBookings.filter {
                    (it.bookingStatus == "Current Stay" || it.checkInDate.contains("Today")) &&
                            (selectedOtaFilter == "All Sources" || it.source.contains(selectedOtaFilter, ignoreCase = true))
                }

                if (currentBookings.isEmpty()) {
                    item {
                        EmptyBookingsCard(title = "No Stays for Current Date", subtitle = "No guests currently checked in for the selected filter.")
                    }
                } else {
                    items(currentBookings, key = { it.id }) { booking ->
                        AdminBookingItem(booking = booking)
                    }
                }
            }

            2 -> {
                // UPCOMING DATES TAB
                item {
                    Column {
                        Text(
                            text = "Upcoming Date Bookings & Future Reservations",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                        Text(
                            text = "All future check-ins booked via Agoda, MakeMyTrip, VOYE & Direct",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // OTA Source Filter Chips
                item {
                    OtaFilterChips(
                        sources = otaSources,
                        selected = selectedOtaFilter,
                        onSelect = { selectedOtaFilter = it }
                    )
                }

                val upcomingBookings = allBookings.filter {
                    it.bookingStatus == "Upcoming" &&
                            (selectedOtaFilter == "All Sources" || it.source.contains(selectedOtaFilter, ignoreCase = true))
                }

                if (upcomingBookings.isEmpty()) {
                    item {
                        EmptyBookingsCard(title = "No Upcoming Bookings", subtitle = "Future bookings will appear here when guests reserve or OTA syncs.")
                    }
                } else {
                    items(upcomingBookings, key = { it.id }) { booking ->
                        AdminBookingItem(booking = booking)
                    }
                }
            }
        }
    }

    // Manual/OTA Booking Dialog
    if (showAddBookingDialog) {
        RecordBookingDialog(
            allUnits = allUnits,
            onDismiss = { showAddBookingDialog = false },
            onSave = { unitId, guestName, mobile, checkIn, checkOut, adults, children, amount, source, ref, notes ->
                viewModel.recordManualBooking(
                    unitId = unitId,
                    guestName = guestName,
                    guestMobile = mobile,
                    checkIn = checkIn,
                    checkOut = checkOut,
                    adults = adults,
                    children = children,
                    totalAmount = amount,
                    source = source,
                    refName = ref,
                    notes = notes
                )
                showAddBookingDialog = false
                Toast.makeText(context, "Booking recorded from $source", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, WarmBeigeBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
}

@Composable
fun OtaFilterChips(
    sources: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(sources) { src ->
            val isSelected = selected == src
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(src) },
                label = { Text(src) },
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

@Composable
fun AdminUnitCard(
    unit: RoomUnit,
    status: UnitAvailabilityStatus,
    activeBooking: Booking?,
    onToggleBlock: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, WarmBeigeBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${unit.block} — Unit ${unit.id}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(color = WarmBeigeSurfaceVariant, shape = RoundedCornerShape(6.dp)) {
                        Text(
                            text = unit.bhkType,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                AvailabilityBadge(status = status)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${unit.bedroomBreakdown} • ${unit.cotCapacity} Cots Total • ${formatCurrency(unit.pricePerNight)}/night",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            // If active booking exists, show occupant and OTA source
            if (activeBooking != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = WarmBeigeSurfaceVariant,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Guest: ${activeBooking.guestName}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${activeBooking.checkInDate} → ${activeBooking.checkOutDate}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                            if (activeBooking.referenceName.isNotBlank()) {
                                Text(
                                    text = "Ref: ${activeBooking.referenceName}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        OtaSourceBadge(source = activeBooking.source)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = WarmBeigeBorder)
            Spacer(modifier = Modifier.height(8.dp))

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onToggleBlock,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (unit.isBlocked) ForestGreenPrimary else Color(0xFFC62828)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (unit.isBlocked) ForestGreenPrimary else Color(0xFFC62828)
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (unit.isBlocked) Icons.Default.LockOpen else Icons.Default.Block,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (unit.isBlocked) "Unblock Unit" else "Block for Maintenance",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
fun AdminBookingItem(booking: Booking) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, WarmBeigeBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = booking.id,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OtaSourceBadge(source = booking.source)
                    }
                    Text(
                        text = "Ref: ${booking.referenceName.ifBlank { "Direct Booking" }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Text(
                    text = formatCurrency(booking.totalAmount),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = WarmBeigeBorder)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Guest Name", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(text = booking.guestName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Text(text = booking.guestMobile, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Allocated Unit", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(text = "Unit ${booking.unitId} (${booking.block})", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "${booking.bhkType} • ${booking.adults + booking.children} Guests", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = WarmBeigeSurfaceVariant,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Dates: ${booking.checkInDate} to ${booking.checkOutDate}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Status: ${booking.bookingStatus}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyBookingsCard(title: String, subtitle: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, WarmBeigeBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordBookingDialog(
    allUnits: List<RoomUnit>,
    onDismiss: () -> Unit,
    onSave: (
        unitId: String,
        guestName: String,
        mobile: String,
        checkIn: String,
        checkOut: String,
        adults: Int,
        children: Int,
        amount: Double,
        source: String,
        ref: String,
        notes: String
    ) -> Unit
) {
    var selectedUnitId by remember { mutableStateOf(allUnits.firstOrNull()?.id ?: "A1") }
    var guestName by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var checkIn by remember { mutableStateOf("2026-09-15") }
    var checkOut by remember { mutableStateOf("2026-09-17") }
    var adults by remember { mutableIntStateOf(2) }
    var children by remember { mutableIntStateOf(0) }
    var amountText by remember { mutableStateOf("12000") }
    var selectedSource by remember { mutableStateOf("Agoda") }
    var refName by remember { mutableStateOf("AGD-998822") }
    var notes by remember { mutableStateOf("") }

    val sources = listOf("Agoda", "MakeMyTrip", "VOYE", "Booking.com", "Direct", "Walk-in")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "Record Manual / OTA Booking",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                    Text(
                        text = "Add reservation from Agoda, MakeMyTrip, VOYE, etc.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                // Booking Source Selector
                item {
                    Text("Booking Source / Channel *", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(sources) { src ->
                            FilterChip(
                                selected = selectedSource == src,
                                onClick = { selectedSource = src },
                                label = { Text(src) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ForestGreenPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Room Unit Selector
                item {
                    Text("Select Unit *", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(allUnits) { u ->
                            FilterChip(
                                selected = selectedUnitId == u.id,
                                onClick = { selectedUnitId = u.id },
                                label = { Text("${u.id} (${u.bhkType})") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ForestGreenPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Guest Details
                item {
                    OutlinedTextField(
                        value = guestName,
                        onValueChange = { guestName = it },
                        label = { Text("Guest Name *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = mobile,
                        onValueChange = { mobile = it },
                        label = { Text("Guest Phone *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = refName,
                        onValueChange = { refName = it },
                        label = { Text("OTA Reference / Booking ID") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = checkIn,
                            onValueChange = { checkIn = it },
                            label = { Text("Check-In") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = checkOut,
                            onValueChange = { checkOut = it },
                            label = { Text("Check-Out") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = { Text("Total Amount (₹) *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes / Special Requests") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel", color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                val amt = amountText.toDoubleOrNull() ?: 10000.0
                                onSave(selectedUnitId, guestName, mobile, checkIn, checkOut, adults, children, amt, selectedSource, refName, notes)
                            },
                            enabled = guestName.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                        ) {
                            Text("Save Reservation")
                        }
                    }
                }
            }
        }
    }
}
