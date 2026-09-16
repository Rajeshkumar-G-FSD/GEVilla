package com.datazync.greenedgevilla.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.datazync.greenedgevilla.R
import com.datazync.greenedgevilla.data.model.Offer
import com.datazync.greenedgevilla.data.model.RoomUnit
import com.datazync.greenedgevilla.ui.components.BookingSearchCard
import com.datazync.greenedgevilla.ui.components.GuestReviewsSection
import com.datazync.greenedgevilla.ui.components.LocationCardSection
import com.datazync.greenedgevilla.ui.components.ResortFacilitiesSection
import com.datazync.greenedgevilla.ui.components.RoomUnitCard
import com.datazync.greenedgevilla.ui.components.WhyChooseSection
import com.datazync.greenedgevilla.ui.theme.ForestGreenDark
import com.datazync.greenedgevilla.ui.theme.ForestGreenPrimary
import com.datazync.greenedgevilla.ui.theme.GoldAccent
import com.datazync.greenedgevilla.ui.theme.GoldContainer
import com.datazync.greenedgevilla.ui.theme.OnGoldContainer
import com.datazync.greenedgevilla.ui.theme.TextPrimary
import com.datazync.greenedgevilla.ui.theme.TextSecondary
import com.datazync.greenedgevilla.ui.theme.WarmBeigeBackground
import com.datazync.greenedgevilla.ui.theme.WarmBeigeBorder
import com.datazync.greenedgevilla.ui.viewmodel.NavTab
import com.datazync.greenedgevilla.ui.viewmodel.VillaViewModel

@Composable
fun HomeScreen(
    viewModel: VillaViewModel,
    onNavigateToRooms: () -> Unit,
    onNavigateToOffers: () -> Unit,
    onOpenRoomDetail: (RoomUnit) -> Unit,
    onBookUnit: (RoomUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val units by viewModel.allUnits.collectAsStateWithLifecycle()
    val checkIn by viewModel.checkInDate.collectAsStateWithLifecycle()
    val checkOut by viewModel.checkOutDate.collectAsStateWithLifecycle()
    val adults by viewModel.adultsCount.collectAsStateWithLifecycle()
    val children by viewModel.childrenCount.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBeigeBackground)
            .testTag("home_screen_scroll"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
            ) {
                // Resort Image
                AsyncImage(
                    model = R.drawable.resort_hero_ooty,
                    contentDescription = "Green Edge Villa Resort, Ooty",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                )

                // High-luxury cinematic gradient
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.4f),
                                    Color.Black.copy(alpha = 0.2f),
                                    ForestGreenDark.copy(alpha = 0.95f)
                                ),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )

                // Hero Content
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Surface(
                        color = GoldAccent,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "PREMIUM HILL RESORT • OOTY",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Welcome to\nGreen Edge Villa",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 34.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Comfortable stays. Beautiful Ooty.",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Primary & Secondary CTAs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { viewModel.startBookingFlow() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldAccent,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("hero_primary_cta_book_stay")
                        ) {
                            Text(
                                text = "Book Your Stay",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = onNavigateToRooms,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.5.dp, Color.White),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("hero_secondary_cta_explore_rooms")
                        ) {
                            Text(
                                text = "Explore Rooms",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Booking Search Card (Overlapping card)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                BookingSearchCard(
                    checkInDate = checkIn,
                    checkOutDate = checkOut,
                    adults = adults,
                    children = children,
                    onCheckInChange = { viewModel.updateCheckInDate(it) },
                    onCheckOutChange = { viewModel.updateCheckOutDate(it) },
                    onGuestsChange = { a, c -> viewModel.updateGuests(a, c) },
                    onSearchAvailability = { onNavigateToRooms() }
                )
            }
        }

        // Featured Rooms
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Featured Rooms",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                        Text(
                            text = "Handcrafted villas across Blocks A, B, C and D",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    TextButton(onClick = onNavigateToRooms) {
                        Text("View All", color = ForestGreenPrimary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = ForestGreenPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Show first 2 units as featured cards
                units.take(2).forEach { unit ->
                    val status = viewModel.getUnitStatusForToday(unit)
                    RoomUnitCard(
                        unit = unit,
                        status = status,
                        onViewDetails = { onOpenRoomDetail(unit) },
                        onBookNow = { onBookUnit(unit) },
                        modifier = Modifier.padding(bottom = 14.dp)
                    )
                }
            }
        }

        // Why Choose Green Edge Villa
        item {
            WhyChooseSection(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp))
        }

        // Resort Facilities
        item {
            ResortFacilitiesSection(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp))
        }

        // Special Offers Preview
        item {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Special Offers",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                        Text(
                            text = "Exclusive deals for weekends and longer stays",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    TextButton(onClick = onNavigateToOffers) {
                        Text("See All", color = ForestGreenPrimary, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModel.offers.take(3)) { offer ->
                        HomeOfferCard(offer = offer, onSelect = {
                            viewModel.applyOffer(offer)
                            viewModel.startBookingFlow()
                        })
                    }
                }
            }
        }

        // Guest Reviews
        item {
            GuestReviewsSection(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp))
        }

        // Location & Directions Section
        item {
            LocationCardSection(
                onOpenMap = { viewModel.openLocationMap() },
                onShareLocation = { shareLocationIntent(context) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
            )
        }
    }
}

@Composable
fun HomeOfferCard(
    offer: Offer,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, WarmBeigeBorder),
        modifier = modifier
            .width(260.dp)
            .clickable { onSelect() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Surface(
                color = GoldContainer,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "${offer.discountPercent}% OFF • ${offer.badge}",
                    color = OnGoldContainer,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = offer.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = offer.description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = WarmBeigeBackground,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(0.8.dp, WarmBeigeBorder)
                ) {
                    Text(
                        text = offer.code,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = "Apply >",
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun shareLocationIntent(context: Context) {
    val message =
        "Green Edge Villa, Ooty\n" +
                "Address: Amma's Kitchen opposite, Rokini Junction, Mysuru Road, Ooty, Tamil Nadu 643001\n" +
                "Google Maps: https://maps.google.com/?q=11.4138,76.6958\n" +
                "Contact: +91 94882 12345 / +91 94433 67890"

    // Prefer sharing straight to WhatsApp; fall back to the system share sheet if it's not installed.
    val whatsAppIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
        setPackage("com.whatsapp")
    }
    try {
        context.startActivity(whatsAppIntent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "WhatsApp not installed — opening share menu instead.", Toast.LENGTH_SHORT).show()
        val genericIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        context.startActivity(Intent.createChooser(genericIntent, "Share Green Edge Villa Location"))
    }
}
