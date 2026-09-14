package com.datazync.greenedgevilla.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OutdoorGrill
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.WaterDamage
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.datazync.greenedgevilla.data.model.Offer
import com.datazync.greenedgevilla.ui.theme.ForestGreenDark
import com.datazync.greenedgevilla.ui.theme.ForestGreenLight
import com.datazync.greenedgevilla.ui.theme.ForestGreenPrimary
import com.datazync.greenedgevilla.ui.theme.GoldAccent
import com.datazync.greenedgevilla.ui.theme.GoldContainer
import com.datazync.greenedgevilla.ui.theme.OnGoldContainer
import com.datazync.greenedgevilla.ui.theme.TextPrimary
import com.datazync.greenedgevilla.ui.theme.TextSecondary
import com.datazync.greenedgevilla.ui.theme.WarmBeigeBackground
import com.datazync.greenedgevilla.ui.theme.WarmBeigeBorder
import com.datazync.greenedgevilla.ui.theme.WarmBeigeSurfaceVariant
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSearchCard(
    checkInDate: LocalDate,
    checkOutDate: LocalDate,
    adults: Int,
    children: Int,
    onCheckInChange: (LocalDate) -> Unit,
    onCheckOutChange: (LocalDate) -> Unit,
    onGuestsChange: (Int, Int) -> Unit,
    onSearchAvailability: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCheckInPicker by remember { mutableStateOf(false) }
    var showCheckOutPicker by remember { mutableStateOf(false) }
    var showGuestDialog by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM")

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, WarmBeigeBorder),
        modifier = modifier
            .fillMaxWidth()
            .testTag("booking_search_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Plan Your Ooty Stay",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary
            )
            Text(
                text = "Check real-time villa availability and best rates",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Dates Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Check-in Selector
                DateBox(
                    label = "CHECK-IN",
                    dateText = checkInDate.format(dateFormatter),
                    onClick = { showCheckInPicker = true },
                    modifier = Modifier.weight(1f)
                )

                // Check-out Selector
                DateBox(
                    label = "CHECK-OUT",
                    dateText = checkOutDate.format(dateFormatter),
                    onClick = { showCheckOutPicker = true },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Guests Box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = WarmBeigeSurfaceVariant,
                border = BorderStroke(1.dp, WarmBeigeBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showGuestDialog = true }
                    .testTag("guests_selector_card")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = ForestGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "GUESTS",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$adults Adults" + if (children > 0) ", $children Children" else "",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }
                    }

                    Text(
                        text = "Edit",
                        style = MaterialTheme.typography.labelMedium,
                        color = ForestGreenPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Availability Button
            Button(
                onClick = onSearchAvailability,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ForestGreenPrimary,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("search_availability_cta")
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Search Availability",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Check-in DatePickerDialog
    if (showCheckInPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = checkInDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showCheckInPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selected = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                        onCheckInChange(selected)
                    }
                    showCheckInPicker = false
                }) {
                    Text("Select", color = ForestGreenPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCheckInPicker = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Check-out DatePickerDialog
    if (showCheckOutPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = checkOutDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showCheckOutPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selected = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                        onCheckOutChange(selected)
                    }
                    showCheckOutPicker = false
                }) {
                    Text("Select", color = ForestGreenPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCheckOutPicker = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Guest Dialog
    if (showGuestDialog) {
        GuestCountDialog(
            adults = adults,
            children = children,
            onConfirm = { a, c ->
                onGuestsChange(a, c)
                showGuestDialog = false
            },
            onDismiss = { showGuestDialog = false }
        )
    }
}

@Composable
fun DateBox(
    label: String,
    dateText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = WarmBeigeSurfaceVariant,
        border = BorderStroke(1.dp, WarmBeigeBorder),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = ForestGreenPrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
fun GuestCountDialog(
    adults: Int,
    children: Int,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var curAdults by remember { mutableStateOf(adults) }
    var curChildren by remember { mutableStateOf(children) }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Select Guests",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary
                )
                Text(
                    text = "Green Edge Villa offers 1, 2 and 3 BHK units.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Adults Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Adults", fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Age 12+ years", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                    CounterRow(
                        count = curAdults,
                        min = 1,
                        max = 12,
                        onChange = { curAdults = it }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Children Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Children", fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Age 0-11 years", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                    CounterRow(
                        count = curChildren,
                        min = 0,
                        max = 6,
                        onChange = { curChildren = it }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirm(curAdults, curChildren) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}

@Composable
fun CounterRow(
    count: Int,
    min: Int,
    max: Int,
    onChange: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = CircleShape,
            color = if (count > min) WarmBeigeSurfaceVariant else Color.LightGray.copy(alpha = 0.4f),
            modifier = Modifier
                .size(36.dp)
                .clickable(enabled = count > min) { onChange(count - 1) }
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("-", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ForestGreenPrimary)
            }
        }

        Text(
            text = "$count",
            modifier = Modifier.padding(horizontal = 14.dp),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        Surface(
            shape = CircleShape,
            color = if (count < max) WarmBeigeSurfaceVariant else Color.LightGray.copy(alpha = 0.4f),
            modifier = Modifier
                .size(36.dp)
                .clickable(enabled = count < max) { onChange(count + 1) }
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ForestGreenPrimary)
            }
        }
    }
}

@Composable
fun ResortFacilitiesSection(modifier: Modifier = Modifier) {
    val facilities = listOf(
        Pair(Icons.Default.Waves, "Misty Mountain View"),
        Pair(Icons.Default.Fireplace, "Fireplace & Bonfire"),
        Pair(Icons.Default.Wifi, "300 Mbps Fiber Wi-Fi"),
        Pair(Icons.Default.WaterDamage, "24/7 Geyser Hot Water"),
        Pair(Icons.Default.LocalParking, "Free Safe Parking"),
        Pair(Icons.Default.RoomService, "Caretaker & Room Service"),
        Pair(Icons.Default.OutdoorGrill, "BBQ Setup on Request"),
        Pair(Icons.Default.LocalFlorist, "Lush Lawn & Tea Garden")
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Resort Facilities",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = ForestGreenPrimary
        )
        Text(
            text = "Carefully curated amenities for your peaceful Nilgiri holiday",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                facilities.take(4).forEach { (icon, label) ->
                    FacilityPill(icon = icon, label = label)
                }
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                facilities.drop(4).forEach { (icon, label) ->
                    FacilityPill(icon = icon, label = label)
                }
            }
        }
    }
}

@Composable
fun FacilityPill(icon: ImageVector, label: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = WarmBeigeSurfaceVariant,
        border = BorderStroke(0.8.dp, WarmBeigeBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ForestGreenPrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun WhyChooseSection(modifier: Modifier = Modifier) {
    val highlights = listOf(
        Triple("Scenic Location", "Right opposite Amma's Kitchen at Rokini Junction on Mysuru Road, peaceful yet easily accessible.", Icons.Default.LocationOn),
        Triple("Individually Bookable BHKs", "Complete 1, 2, and 3 BHK private villas with up to 6 cots per block, perfect for families and groups.", Icons.Default.ElectricBolt),
        Triple("Zero Overbooking Guarantee", "Accurate real-time availability tracking across OTA channels and direct reservations.", Icons.Default.Star),
        Triple("Warm Nilgiri Hospitality", "Dedicated staff, fresh home-style meals, and authentic local guidance across Ooty.", Icons.Default.LocalAtm)
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Why Choose Green Edge Villa",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = ForestGreenPrimary
        )
        Text(
            text = "Experience Ooty with unparalleled comfort and scenic solitude",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        highlights.forEach { (title, desc, icon) ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, WarmBeigeBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        shape = CircleShape,
                        color = GoldContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = OnGoldContainer,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GuestReviewsSection(modifier: Modifier = Modifier) {
    val reviews = listOf(
        Triple("Dr. Arvind Krishnan", "Stayed in 3 BHK Unit A1 with family", "The tea estate view from Block A is magical in the morning mist. Spacious rooms, spotless bathrooms, and attentive hospitality!"),
        Triple("Malini & Rohit", "Stayed in 1 BHK Unit D1", "Perfect cozy cottage for couples. Fireplace kept us warm at night, and the host arranged hot tea and bonfire."),
        Triple("Shashank Verma", "Corporate Group Stay in Block B", "Booked both B1 and B2 for our team retreat. 9 cots total accommodated everyone comfortably. Super easy booking.")
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Guest Reviews",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = ForestGreenPrimary
        )
        Text(
            text = "Rated 4.8 / 5.0 by hundreds of happy travelers",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reviews) { (name, tag, comment) ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, WarmBeigeBorder),
                    modifier = Modifier.width(280.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(5) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = GoldAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "\"$comment\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = name,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocationCardSection(
    onOpenMap: () -> Unit,
    onShareLocation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ForestGreenDark),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Green Edge Villa",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Surface(
                    color = GoldAccent.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Ooty, Tamil Nadu",
                        color = GoldAccent,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Amma's Kitchen opposite, Rokini Junction, Mysuru Road, Ooty, Tamil Nadu 643001",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.85f),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onOpenMap,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldAccent,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("View Map", style = MaterialTheme.typography.labelMedium)
                }

                OutlinedButton(
                    onClick = onShareLocation,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
