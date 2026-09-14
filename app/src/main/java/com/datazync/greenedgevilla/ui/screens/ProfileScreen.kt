package com.datazync.greenedgevilla.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.datazync.greenedgevilla.ui.components.LocationCardSection
import com.datazync.greenedgevilla.ui.theme.ForestGreenDark
import com.datazync.greenedgevilla.ui.theme.ForestGreenPrimary
import com.datazync.greenedgevilla.ui.theme.GoldAccent
import com.datazync.greenedgevilla.ui.theme.TextPrimary
import com.datazync.greenedgevilla.ui.theme.TextSecondary
import com.datazync.greenedgevilla.ui.theme.WarmBeigeBackground
import com.datazync.greenedgevilla.ui.theme.WarmBeigeBorder
import com.datazync.greenedgevilla.ui.theme.WarmBeigeSurfaceVariant
import com.datazync.greenedgevilla.ui.viewmodel.VillaViewModel

@Composable
fun ProfileScreen(
    viewModel: VillaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBeigeBackground)
            .testTag("profile_screen_scroll"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Resort Profile Header
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, WarmBeigeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = ForestGreenPrimary,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "GEV",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Green Edge Villa",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )

                    Text(
                        text = "Luxury Nature Retreat • Ooty, Nilgiris",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+919488212345"))
                                context.startActivity(callIntent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call Resort")
                        }

                        OutlinedButton(
                            onClick = {
                                val url = "https://api.whatsapp.com/send?phone=919488212345&text=Hello%20Green%20Edge%20Villa%2C%20I%20have%20an%20inquiry%20regarding%20booking."
                                val waIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(waIntent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, ForestGreenPrimary),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreenPrimary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WhatsApp")
                        }
                    }
                }
            }
        }

        // Location & Directions Section with Map and Share
        item {
            LocationCardSection(
                onOpenMap = { viewModel.openLocationMap() },
                onShareLocation = { shareLocationIntent(context) }
            )
        }

        // Contact Information Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, WarmBeigeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Contact & Helpdesk",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ContactRow(Icons.Default.Call, "Phone / Front Desk", "+91 94882 12345 / +91 94433 67890")
                    ContactRow(Icons.Default.Email, "Email Inquiries", "reservations@greenedgevilla.com")
                    ContactRow(Icons.Default.LocationOn, "Landmark", "Opposite Amma's Kitchen, Rokini Junction, Mysuru Road")
                }
            }
        }

        // Resort Policies Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, WarmBeigeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Resort Policies & Guest Guidelines",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PolicyItem("Check-In Time", "2:00 PM onwards (Early check-in subject to availability)")
                    PolicyItem("Check-Out Time", "11:00 AM")
                    PolicyItem("Cancellation Policy", "100% refund up to 48 hours before check-in date.")
                    PolicyItem("Quiet Hours", "10:00 PM to 7:00 AM to preserve serene hill tranquility.")
                    PolicyItem("Bonfire & BBQ", "Arranged in lawn area upon prior evening notice.")
                }
            }
        }
    }
}

@Composable
fun ContactRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = TextPrimary)
        }
    }
}

@Composable
fun PolicyItem(title: String, desc: String) {
    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        Text(title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
        Text(desc, style = MaterialTheme.typography.bodySmall, color = TextSecondary, lineHeight = 18.sp)
    }
}
