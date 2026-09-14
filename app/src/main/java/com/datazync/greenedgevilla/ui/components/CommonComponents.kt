package com.datazync.greenedgevilla.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.datazync.greenedgevilla.R
import com.datazync.greenedgevilla.data.model.RoomUnit
import com.datazync.greenedgevilla.data.model.UnitAvailabilityStatus
import com.datazync.greenedgevilla.ui.theme.ForestGreenDark
import com.datazync.greenedgevilla.ui.theme.ForestGreenLight
import com.datazync.greenedgevilla.ui.theme.ForestGreenPrimary
import com.datazync.greenedgevilla.ui.theme.GoldAccent
import com.datazync.greenedgevilla.ui.theme.GoldContainer
import com.datazync.greenedgevilla.ui.theme.OnGoldContainer
import com.datazync.greenedgevilla.ui.theme.OtaAgoda
import com.datazync.greenedgevilla.ui.theme.OtaBookingCom
import com.datazync.greenedgevilla.ui.theme.OtaDirect
import com.datazync.greenedgevilla.ui.theme.OtaMakeMyTrip
import com.datazync.greenedgevilla.ui.theme.OtaVoye
import com.datazync.greenedgevilla.ui.theme.OtaWalkIn
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
import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    format.maximumFractionDigits = 0
    return format.format(amount)
}

fun getRoomDrawableRes(imageName: String): Int {
    return when (imageName) {
        "room_villa_bedroom" -> R.drawable.room_villa_bedroom
        "room_villa_living" -> R.drawable.room_villa_living
        else -> R.drawable.resort_hero_ooty
    }
}

@Composable
fun AvailabilityBadge(
    status: UnitAvailabilityStatus,
    modifier: Modifier = Modifier
) {
    val (bg, textColor, label) = when (status) {
        UnitAvailabilityStatus.AVAILABLE -> Triple(StatusAvailableBg, StatusAvailable, "Available")
        UnitAvailabilityStatus.BOOKED -> Triple(StatusBookedBg, StatusBooked, "Booked")
        UnitAvailabilityStatus.BLOCKED -> Triple(StatusBlockedBg, StatusBlocked, "Blocked")
        UnitAvailabilityStatus.CHECK_IN_TODAY -> Triple(StatusCheckInBg, StatusCheckIn, "Check-in Today")
        UnitAvailabilityStatus.CHECK_OUT_TODAY -> Triple(StatusCheckOutBg, StatusCheckOut, "Check-out Today")
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.25f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                color = textColor,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun OtaSourceBadge(
    source: String,
    modifier: Modifier = Modifier
) {
    val (color, shortName) = when {
        source.contains("Agoda", ignoreCase = true) -> Pair(OtaAgoda, "Agoda")
        source.contains("MakeMyTrip", ignoreCase = true) || source.contains("MMT", ignoreCase = true) -> Pair(OtaMakeMyTrip, "MakeMyTrip")
        source.contains("VOYE", ignoreCase = true) -> Pair(OtaVoye, "VOYE")
        source.contains("Booking.com", ignoreCase = true) -> Pair(OtaBookingCom, "Booking.com")
        source.contains("Direct", ignoreCase = true) -> Pair(OtaDirect, "Direct App")
        else -> Pair(OtaWalkIn, source)
    }

    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f)),
        modifier = modifier
    ) {
        Text(
            text = shortName,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RoomUnitCard(
    unit: RoomUnit,
    status: UnitAvailabilityStatus = UnitAvailabilityStatus.AVAILABLE,
    onViewDetails: () -> Unit,
    onBookNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isActionable = status != UnitAvailabilityStatus.BLOCKED && status != UnitAvailabilityStatus.BOOKED

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, WarmBeigeBorder),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewDetails() }
            .testTag("room_card_${unit.id}")
    ) {
        Column {
            // Image with Overlaid Badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = getRoomDrawableRes(unit.primaryImageResName),
                    contentDescription = "${unit.block} ${unit.id}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )

                // Dark subtle gradient overlay at bottom of image
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f)),
                                startY = 80f
                            )
                        )
                )

                // Top Chips (Block & Availability)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = ForestGreenDark.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = unit.block,
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    AvailabilityBadge(status = status)
                }

                // Bottom Image Info
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Unit ${unit.id} • ${unit.bhkType}",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${unit.bedroomCount} Bedrooms • ${unit.cotCapacity} Cots Total",
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Surface(
                        color = GoldAccent,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "${formatCurrency(unit.pricePerNight)} / nt",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Body content
            Column(modifier = Modifier.padding(16.dp)) {
                // Key Specs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(WarmBeigeSurfaceVariant, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SpecItem(icon = Icons.Default.MeetingRoom, label = "${unit.bedroomCount} Beds")
                    SpecItem(icon = Icons.Default.Bed, label = "${unit.cotCapacity} Cots")
                    SpecItem(icon = Icons.Default.Group, label = "Up to ${unit.maxGuests} Guests")
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Amenities chips
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    unit.amenities.take(3).forEach { amenity ->
                        Surface(
                            color = WarmBeigeBackground,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(0.8.dp, WarmBeigeBorder)
                        ) {
                            Text(
                                text = amenity,
                                color = TextSecondary,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                    if (unit.amenities.size > 3) {
                        Text(
                            text = "+${unit.amenities.size - 3} more",
                            color = ForestGreenLight,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onViewDetails,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ForestGreenPrimary),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreenPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("view_details_btn_${unit.id}")
                    ) {
                        Text("View Details", style = MaterialTheme.typography.labelLarge)
                    }

                    Button(
                        onClick = onBookNow,
                        enabled = isActionable,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForestGreenPrimary,
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFDCD8D0),
                            disabledContentColor = Color.Gray
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("book_now_btn_${unit.id}")
                    ) {
                        Text(
                            text = if (isActionable) "Book Now" else "Unavailable",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpecItem(icon: ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ForestGreenPrimary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = TextPrimary,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}
