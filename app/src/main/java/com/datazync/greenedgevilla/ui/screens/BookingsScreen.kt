package com.datazync.greenedgevilla.ui.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datazync.greenedgevilla.data.model.Booking
import com.datazync.greenedgevilla.data.model.RoomUnit
import com.datazync.greenedgevilla.data.model.UnitAvailabilityStatus
import com.datazync.greenedgevilla.ui.components.OtaSourceBadge
import com.datazync.greenedgevilla.ui.components.RoomUnitCard
import com.datazync.greenedgevilla.ui.components.formatCurrency
import com.datazync.greenedgevilla.ui.theme.ForestGreenDark
import com.datazync.greenedgevilla.ui.theme.ForestGreenLight
import com.datazync.greenedgevilla.ui.theme.ForestGreenPrimary
import com.datazync.greenedgevilla.ui.theme.GoldAccent
import com.datazync.greenedgevilla.ui.theme.StatusAvailable
import com.datazync.greenedgevilla.ui.theme.StatusAvailableBg
import com.datazync.greenedgevilla.ui.theme.StatusBlocked
import com.datazync.greenedgevilla.ui.theme.StatusBlockedBg
import com.datazync.greenedgevilla.ui.theme.StatusBooked
import com.datazync.greenedgevilla.ui.theme.StatusBookedBg
import com.datazync.greenedgevilla.ui.theme.StatusCheckIn
import com.datazync.greenedgevilla.ui.theme.StatusCheckInBg
import com.datazync.greenedgevilla.ui.theme.TextPrimary
import com.datazync.greenedgevilla.ui.theme.TextSecondary
import com.datazync.greenedgevilla.ui.theme.WarmBeigeBackground
import com.datazync.greenedgevilla.ui.theme.WarmBeigeBorder
import com.datazync.greenedgevilla.ui.theme.WarmBeigeSurfaceVariant
import com.datazync.greenedgevilla.ui.viewmodel.VillaViewModel

@Composable
fun BookingsScreen(
    viewModel: VillaViewModel,
    onBookNewStay: () -> Unit,
    onOpenRoomDetail: (RoomUnit) -> Unit = {},
    onBookUnit: (RoomUnit) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allBookings by viewModel.allBookings.collectAsStateWithLifecycle()
    val allUnits by viewModel.allUnits.collectAsStateWithLifecycle()
    val bookingToCancel by viewModel.bookingToCancel.collectAsStateWithLifecycle()

    var mainTab by remember { mutableStateOf("Booked") }
    var selectedStatusTab by remember { mutableStateOf("All") }
    val statusTabs = listOf("All", "Upcoming", "Current Stay", "Completed", "Cancelled")

    val filteredList = remember(allBookings, selectedStatusTab) {
        if (selectedStatusTab == "All") allBookings
        else allBookings.filter { it.bookingStatus.equals(selectedStatusTab, ignoreCase = true) }
    }

    val availableUnits = remember(allUnits, allBookings) {
        allUnits.filter { unit ->
            val status = viewModel.getUnitStatusForToday(unit)
            status == UnitAvailabilityStatus.AVAILABLE || status == UnitAvailabilityStatus.CHECK_OUT_TODAY
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBeigeBackground)
            .testTag("bookings_screen_list"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Bookings",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                    Text(
                        text = "Track stays or see what's open to book right now",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = onBookNewStay,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("+ New Stay", style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        // Booked Rooms / Available Rooms segmented switch
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WarmBeigeSurfaceVariant, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                MainTabButton(
                    label = "Booked Rooms (${allBookings.size})",
                    icon = Icons.Default.ReceiptLong,
                    isSelected = mainTab == "Booked",
                    onClick = { mainTab = "Booked" },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("bookings_tab_booked")
                )
                MainTabButton(
                    label = "Available (${availableUnits.size})",
                    icon = Icons.Default.EventAvailable,
                    isSelected = mainTab == "Available",
                    onClick = { mainTab = "Available" },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("bookings_tab_available")
                )
            }
        }

        if (mainTab == "Booked") {
            // Status Tabs
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(statusTabs) { tab ->
                        val isSelected = selectedStatusTab == tab
                        val count = if (tab == "All") allBookings.size else allBookings.count { it.bookingStatus.equals(tab, ignoreCase = true) }
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedStatusTab = tab },
                            label = { Text("$tab ($count)") },
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

            if (filteredList.isEmpty()) {
                item {
                    EmptyStateCard(
                        icon = Icons.Default.EventBusy,
                        title = "No $selectedStatusTab Bookings Found",
                        subtitle = "Any bookings reserved through the app or synced from OTAs appear here."
                    )
                }
            } else {
                items(filteredList, key = { it.id }) { booking ->
                    BookingCardItem(
                        booking = booking,
                        onRequestCancel = { viewModel.requestCancelBooking(booking) },
                        onShareReceipt = { shareBookingReceipt(context, booking) }
                    )
                }
            }
        } else {
            // Available Rooms
            item {
                Text(
                    text = "Open for booking today",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary
                )
            }

            if (availableUnits.isEmpty()) {
                item {
                    EmptyStateCard(
                        icon = Icons.Default.EventBusy,
                        title = "No Rooms Available Right Now",
                        subtitle = "All villas are currently booked or blocked. Check back soon or contact us for upcoming openings."
                    )
                }
            } else {
                items(availableUnits, key = { it.id }) { unit ->
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

    // Cancellation Confirmation Dialog
    if (bookingToCancel != null) {
        val b = bookingToCancel!!
        AlertDialog(
            onDismissRequest = { viewModel.dismissCancelDialog() },
            title = {
                Text(
                    text = "Cancel Booking?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC62828)
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to cancel booking ${b.id} for Unit ${b.unitId} (${b.checkInDate} to ${b.checkOutDate})?\n\nFree cancellation policy applies up to 48 hours prior to check-in.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmCancelBooking() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                ) {
                    Text("Confirm Cancellation")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissCancelDialog() }) {
                    Text("Keep Booking", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
fun MainTabButton(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(9.dp),
        color = if (isSelected) ForestGreenPrimary else Color.Transparent,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else TextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = if (isSelected) Color.White else TextSecondary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
fun EmptyStateCard(icon: ImageVector, title: String, subtitle: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, WarmBeigeBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun BookingCardItem(
    booking: Booking,
    onRequestCancel: () -> Unit,
    onShareReceipt: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (statusColor, statusBg) = when (booking.bookingStatus) {
        "Upcoming" -> Pair(ForestGreenLight, StatusAvailableBg)
        "Current Stay" -> Pair(StatusCheckIn, StatusCheckInBg)
        "Completed" -> Pair(Color(0xFF558B2F), Color(0xFFF1F8E9))
        else -> Pair(StatusBooked, StatusBookedBg)
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, WarmBeigeBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Booking ID, OTA badge & Status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = booking.id,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                    Text(
                        text = "Guest: ${booking.guestName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    OtaSourceBadge(source = booking.source)

                    Surface(
                        color = statusBg,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = booking.bookingStatus.uppercase(),
                            color = statusColor,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = WarmBeigeBorder)
            Spacer(modifier = Modifier.height(10.dp))

            // Room and Block details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Unit ${booking.unitId} • ${booking.block}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${booking.bhkType} Villa",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatCurrency(booking.totalAmount),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                    Text(
                        text = "${booking.numberOfNights} Nights • ${booking.paymentStatus}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (booking.paymentStatus == "Paid") ForestGreenLight else GoldAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dates and Guests
            Surface(
                color = WarmBeigeSurfaceVariant,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${booking.checkInDate} to ${booking.checkOutDate}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Group, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${booking.adults + booking.children} Guests",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            if (booking.referenceName.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Reference: ${booking.referenceName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onShareReceipt) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = ForestGreenPrimary)
                }

                if (booking.bookingStatus == "Upcoming") {
                    Spacer(modifier = Modifier.width(6.dp))
                    OutlinedButton(
                        onClick = onRequestCancel,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFE57373)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFC62828)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cancel", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

fun shareBookingReceipt(context: Context, booking: Booking) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Green Edge Villa, Ooty — Booking Confirmation\n" +
                    "Booking ID: ${booking.id}\n" +
                    "Guest: ${booking.guestName}\n" +
                    "Unit: ${booking.unitId} (${booking.block} - ${booking.bhkType})\n" +
                    "Check-In: ${booking.checkInDate}\n" +
                    "Check-Out: ${booking.checkOutDate}\n" +
                    "Nights: ${booking.numberOfNights}\n" +
                    "Total Amount: ${formatCurrency(booking.totalAmount)} (${booking.paymentStatus})\n" +
                    "Location: Amma's Kitchen opposite, Rokini Junction, Mysuru Road, Ooty\n" +
                    "Resort Contact: +91 94882 12345"
        )
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(intent, "Share Booking Confirmation"))
}
