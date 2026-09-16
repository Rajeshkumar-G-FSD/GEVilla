package com.datazync.greenedgevilla.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datazync.greenedgevilla.data.model.Booking
import com.datazync.greenedgevilla.data.model.RoomUnit
import com.datazync.greenedgevilla.ui.components.BookingSearchCard
import com.datazync.greenedgevilla.ui.components.formatCurrency
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
import com.datazync.greenedgevilla.ui.viewmodel.NavTab
import com.datazync.greenedgevilla.ui.viewmodel.VillaViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingFlowScreen(
    viewModel: VillaViewModel,
    onFinish: () -> Unit,
    onViewBookingTab: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentStep by viewModel.bookingFlowStep.collectAsStateWithLifecycle()
    val selectedUnit by viewModel.bookingSelectedUnit.collectAsStateWithLifecycle()
    val checkIn by viewModel.checkInDate.collectAsStateWithLifecycle()
    val checkOut by viewModel.checkOutDate.collectAsStateWithLifecycle()
    val adults by viewModel.adultsCount.collectAsStateWithLifecycle()
    val children by viewModel.childrenCount.collectAsStateWithLifecycle()

    val guestName by viewModel.guestFullName.collectAsStateWithLifecycle()
    val guestMobile by viewModel.guestMobile.collectAsStateWithLifecycle()
    val guestEmail by viewModel.guestEmail.collectAsStateWithLifecycle()
    val specialReq by viewModel.specialRequests.collectAsStateWithLifecycle()
    val sourceOta by viewModel.bookingSourceOta.collectAsStateWithLifecycle()
    val sourceOtherText by viewModel.bookingSourceOtherText.collectAsStateWithLifecycle()

    val paymentMethod by viewModel.selectedPaymentMethod.collectAsStateWithLifecycle()
    val confirmedBooking by viewModel.confirmedBooking.collectAsStateWithLifecycle()
    val errorMessage by viewModel.bookingErrorMessage.collectAsStateWithLifecycle()
    val filteredUnits by viewModel.filteredUnits.collectAsStateWithLifecycle()
    val appliedOffer by viewModel.appliedOffer.collectAsStateWithLifecycle()

    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (currentStep == 8) "Booking Confirmed" else "Reservation • Step $currentStep of 7",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    if (currentStep > 1 && currentStep < 8) {
                        IconButton(onClick = { viewModel.setBookingFlowStep(currentStep - 1) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    } else if (currentStep == 8) {
                        IconButton(onClick = onFinish) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    } else {
                        IconButton(onClick = onFinish) {
                            Icon(Icons.Default.Close, contentDescription = "Cancel", tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ForestGreenPrimary)
            )
        },
        modifier = modifier.background(WarmBeigeBackground)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(WarmBeigeBackground)
        ) {
            // Step Progress Bar
            if (currentStep in 1..7) {
                LinearProgressIndicator(
                    progress = { currentStep / 7f },
                    modifier = Modifier.fillMaxWidth(),
                    color = GoldAccent,
                    trackColor = ForestGreenDark.copy(alpha = 0.2f)
                )
            }

            AnimatedContent(
                targetState = currentStep,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "booking_step_animation"
            ) { step ->
                when (step) {
                    1, 2 -> {
                        // Step 1 & 2: Select Dates and Guests
                        StepDatesAndGuests(
                            checkIn = checkIn,
                            checkOut = checkOut,
                            adults = adults,
                            children = children,
                            onCheckInChange = { viewModel.updateCheckInDate(it) },
                            onCheckOutChange = { viewModel.updateCheckOutDate(it) },
                            onGuestsChange = { a, c -> viewModel.updateGuests(a, c) },
                            onContinue = { viewModel.setBookingFlowStep(3) }
                        )
                    }

                    3, 4 -> {
                        // Step 3 & 4: Show available units and select
                        StepSelectUnit(
                            availableUnits = filteredUnits,
                            selectedUnit = selectedUnit,
                            onUnitSelected = { unit ->
                                viewModel.setBookingSelectedUnit(unit)
                                viewModel.setBookingFlowStep(5)
                            }
                        )
                    }

                    5 -> {
                        // Step 5: Guest Information Form
                        selectedUnit?.let { unit ->
                            StepGuestInfoForm(
                                unit = unit,
                                name = guestName,
                                mobile = guestMobile,
                                email = guestEmail,
                                special = specialReq,
                                sourceOta = sourceOta,
                                sourceOtherText = sourceOtherText,
                                onNameChange = { viewModel.updateGuestDetails(it, guestMobile, guestEmail, specialReq) },
                                onMobileChange = { viewModel.updateGuestDetails(guestName, it, guestEmail, specialReq) },
                                onEmailChange = { viewModel.updateGuestDetails(guestName, guestMobile, it, specialReq) },
                                onSpecialChange = { viewModel.updateGuestDetails(guestName, guestMobile, guestEmail, it) },
                                onSourceChange = { source, other -> viewModel.updateBookingSource(source, other) },
                                onContinue = { viewModel.setBookingFlowStep(6) }
                            )
                        }
                    }

                    6 -> {
                        // Step 6: Booking Summary & Price breakdown
                        selectedUnit?.let { unit ->
                            StepBookingSummary(
                                unit = unit,
                                checkInText = checkIn.format(dateFormatter),
                                checkOutText = checkOut.format(dateFormatter),
                                nights = viewModel.calculateNights(),
                                adults = adults,
                                children = children,
                                pricing = viewModel.calculatePricing(unit),
                                appliedOffer = appliedOffer,
                                onProceedToPayment = { viewModel.setBookingFlowStep(7) }
                            )
                        }
                    }

                    7 -> {
                        // Step 7: Payment Screen
                        selectedUnit?.let { unit ->
                            val pricing = viewModel.calculatePricing(unit)
                            StepPaymentScreen(
                                totalAmount = pricing.third,
                                selectedMethod = paymentMethod,
                                onMethodSelect = { viewModel.setPaymentMethod(it) },
                                errorMessage = errorMessage,
                                onConfirmPayment = { viewModel.completeBooking() }
                            )
                        }
                    }

                    8 -> {
                        // Step 8: Booking Confirmation
                        confirmedBooking?.let { booking ->
                            StepBookingConfirmation(
                                booking = booking,
                                onViewBooking = onViewBookingTab,
                                onDownloadReceipt = {
                                    Toast.makeText(context, "Confirmation receipt saved to device.", Toast.LENGTH_SHORT).show()
                                },
                                onContactResort = {
                                    val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+919488212345"))
                                    context.startActivity(callIntent)
                                }
                            )
                        }
                    }

                    else -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Step $step")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StepDatesAndGuests(
    checkIn: java.time.LocalDate,
    checkOut: java.time.LocalDate,
    adults: Int,
    children: Int,
    onCheckInChange: (java.time.LocalDate) -> Unit,
    onCheckOutChange: (java.time.LocalDate) -> Unit,
    onGuestsChange: (Int, Int) -> Unit,
    onContinue: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Select Stay Dates & Guests",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary
            )
            Text(
                text = "Step 1 & 2: Choose your arrival and departure dates in Ooty",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        item {
            BookingSearchCard(
                checkInDate = checkIn,
                checkOutDate = checkOut,
                adults = adults,
                children = children,
                onCheckInChange = onCheckInChange,
                onCheckOutChange = onCheckOutChange,
                onGuestsChange = onGuestsChange,
                onSearchAvailability = onContinue
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, WarmBeigeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Green Edge Villa Inventory Structure:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                        color = ForestGreenPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("• Block A: A1 & A2 (3 BHK • 6 Cots each)", style = MaterialTheme.typography.bodySmall)
                    Text("• Block B: B1 (3 BHK • 6 Cots) & B2 (2 BHK • 3 Cots)", style = MaterialTheme.typography.bodySmall)
                    Text("• Block C: C1 (2 BHK • 3 Cots)", style = MaterialTheme.typography.bodySmall)
                    Text("• Block D: D1 (1 BHK • 2 Cots)", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun StepSelectUnit(
    availableUnits: List<RoomUnit>,
    selectedUnit: RoomUnit?,
    onUnitSelected: (RoomUnit) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Available Villa Units",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary
            )
            Text(
                text = "Step 3 & 4: Select from units matching your dates without double-booking",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        items(availableUnits, key = { it.id }) { unit ->
            val isSelected = selectedUnit?.id == unit.id
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) ForestGreenPrimary.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) ForestGreenPrimary else WarmBeigeBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onUnitSelected(unit) }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${unit.block} — Unit ${unit.id}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(color = GoldAccent, shape = RoundedCornerShape(6.dp)) {
                                Text(
                                    text = unit.bhkType,
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${unit.bedroomCount} Bedrooms • ${unit.cotCapacity} Cots Total • Up to ${unit.maxGuests} Guests",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${formatCurrency(unit.pricePerNight)} / night",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent
                        )
                    }

                    Button(
                        onClick = { onUnitSelected(unit) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                    ) {
                        Text("Select")
                    }
                }
            }
        }
    }
}

private val OTA_SOURCE_OPTIONS = listOf("Direct", "MakeMyTrip", "Agoda", "Booking.com", "Goibibo", "Others")

@Composable
fun StepGuestInfoForm(
    unit: RoomUnit,
    name: String,
    mobile: String,
    email: String,
    special: String,
    sourceOta: String,
    sourceOtherText: String,
    onNameChange: (String) -> Unit,
    onMobileChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onSpecialChange: (String) -> Unit,
    onSourceChange: (String, String) -> Unit,
    onContinue: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Guest Details",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary
            )
            Text(
                text = "Step 5: Reservation contact for Unit ${unit.id} (${unit.bhkType})",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        // Booking Source / OTA Reference
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, WarmBeigeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "How are you booking this stay?",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                    Text(
                        text = "Select the platform so we can match your reservation",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(OTA_SOURCE_OPTIONS) { option ->
                            val isSelected = sourceOta == option
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSourceChange(option, sourceOtherText) },
                                label = { Text(if (option == "Direct") "Direct / App" else option) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ForestGreenPrimary,
                                    selectedLabelColor = Color.White,
                                    containerColor = WarmBeigeSurfaceVariant,
                                    labelColor = TextPrimary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = WarmBeigeBorder,
                                    selectedBorderColor = ForestGreenPrimary,
                                    enabled = true,
                                    selected = isSelected
                                ),
                                modifier = Modifier.testTag("ota_source_chip_$option")
                            )
                        }
                    }

                    if (sourceOta == "Others") {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = sourceOtherText,
                            onValueChange = { onSourceChange("Others", it) },
                            label = { Text("Enter platform / reference name *") },
                            placeholder = { Text("e.g. Yatra, TripAdvisor, Travel Agent") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ota_source_other_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ForestGreenPrimary,
                                focusedLabelColor = ForestGreenPrimary
                            )
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, WarmBeigeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = onNameChange,
                        label = { Text("Full Name *") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ForestGreenPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForestGreenPrimary,
                            focusedLabelColor = ForestGreenPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = mobile,
                        onValueChange = onMobileChange,
                        label = { Text("Mobile Number (WhatsApp) *") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = ForestGreenPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForestGreenPrimary,
                            focusedLabelColor = ForestGreenPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        label = { Text("Email Address *") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = ForestGreenPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForestGreenPrimary,
                            focusedLabelColor = ForestGreenPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = special,
                        onValueChange = onSpecialChange,
                        label = { Text("Special Requests (Optional)") },
                        placeholder = { Text("e.g. Late check-in, campfire, extra blankets") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForestGreenPrimary,
                            focusedLabelColor = ForestGreenPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = onContinue,
                        enabled = name.isNotBlank() && mobile.isNotBlank() &&
                            (sourceOta != "Others" || sourceOtherText.isNotBlank()),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("continue_to_summary_button")
                    ) {
                        Text("Review Booking Summary", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun StepBookingSummary(
    unit: RoomUnit,
    checkInText: String,
    checkOutText: String,
    nights: Int,
    adults: Int,
    children: Int,
    pricing: Triple<Double, Double, Double>,
    appliedOffer: com.datazync.greenedgevilla.data.model.Offer?,
    onProceedToPayment: () -> Unit
) {
    val (base, taxes, total) = pricing

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Booking Summary",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary
            )
            Text(
                text = "Step 6: Confirm stay details and tariff before proceeding to payment",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, WarmBeigeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Green Edge Villa",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                    Text(
                        text = "Rokini Junction, Mysuru Road, Ooty",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = WarmBeigeBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    SummaryRow("Selected Block", unit.block)
                    SummaryRow("Selected Unit", "Unit ${unit.id}")
                    SummaryRow("BHK Configuration", unit.bhkType)
                    SummaryRow("Cot Capacity", "${unit.cotCapacity} Cots Total")
                    SummaryRow("Check-In", checkInText)
                    SummaryRow("Check-Out", checkOutText)
                    SummaryRow("Duration", "$nights Night${if (nights > 1) "s" else ""}")
                    SummaryRow("Guests", "$adults Adults" + if (children > 0) ", $children Children" else "")

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = WarmBeigeBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    SummaryRow("Room Price ($nights nights)", formatCurrency(base))
                    appliedOffer?.let {
                        SummaryRow("Applied Offer (${it.code})", "-${it.discountPercent}%", highlightColor = GoldAccent)
                    }
                    SummaryRow("Taxes & GST (12%)", formatCurrency(taxes))

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = WarmBeigeBorder)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Payable Amount",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                        Text(
                            text = formatCurrency(total),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onProceedToPayment,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("proceed_to_payment_button")
                    ) {
                        Text("Proceed to Payment", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, highlightColor: Color = TextPrimary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = highlightColor)
    }
}

@Composable
fun StepPaymentScreen(
    totalAmount: Double,
    selectedMethod: String,
    onMethodSelect: (String) -> Unit,
    errorMessage: String?,
    onConfirmPayment: () -> Unit
) {
    val methods = listOf(
        Triple("UPI", "Google Pay, PhonePe, Paytm, BHIM UPI", Icons.Default.QrCode),
        Triple("Credit/Debit Card", "Visa, MasterCard, RuPay", Icons.Default.CreditCard),
        Triple("Net Banking", "All major Indian scheduled banks", Icons.Default.AccountBalance),
        Triple("Pay at Hotel", "Cash / UPI directly at villa reception", Icons.Default.Money)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Select Payment Method",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary
            )
            Text(
                text = "Step 7: Total Amount: ${formatCurrency(totalAmount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GoldAccent
            )
        }

        if (errorMessage != null) {
            item {
                Surface(
                    color = Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFFE57373)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = errorMessage,
                        color = Color(0xFFC62828),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        items(methods) { (method, desc, icon) ->
            val isSelected = selectedMethod == method
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) ForestGreenPrimary.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) ForestGreenPrimary else WarmBeigeBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMethodSelect(method) }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onMethodSelect(method) },
                        colors = RadioButtonDefaults.colors(selectedColor = ForestGreenPrimary)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Surface(
                        shape = CircleShape,
                        color = WarmBeigeSurfaceVariant,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(imageVector = icon, contentDescription = null, tint = ForestGreenPrimary)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = method, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                        Text(text = desc, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onConfirmPayment,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("confirm_and_pay_button")
            ) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Confirm & Pay ${formatCurrency(totalAmount)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun StepBookingConfirmation(
    booking: Booking,
    onViewBooking: () -> Unit,
    onDownloadReceipt: () -> Unit,
    onContactResort: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = CircleShape,
                color = Color(0xFFE8F5E9),
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(54.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Booking Confirmed!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary
            )
            Text(
                text = "We look forward to welcoming you to Green Edge Villa, Ooty.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, WarmBeigeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("BOOKING ID", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            Text(booking.id, fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                        }
                        Surface(
                            color = ForestGreenPrimary,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "CONFIRMED",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = WarmBeigeBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    SummaryRow("Guest Name", booking.guestName)
                    SummaryRow("Room / Unit", "Unit ${booking.unitId} (${booking.block})")
                    SummaryRow("BHK Type", booking.bhkType)
                    SummaryRow("Check-In Date", booking.checkInDate)
                    SummaryRow("Check-Out Date", booking.checkOutDate)
                    SummaryRow("Number of Guests", "${booking.adults} Adults" + if (booking.children > 0) ", ${booking.children} Children" else "")
                    SummaryRow("Payment Status", booking.paymentStatus, highlightColor = ForestGreenLight)
                    SummaryRow("Total Amount", formatCurrency(booking.totalAmount), highlightColor = ForestGreenPrimary)
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onViewBooking,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("view_booking_button")
                ) {
                    Icon(Icons.Default.ReceiptLong, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Booking", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onDownloadReceipt,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ForestGreenPrimary),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("download_confirmation_button")
                ) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Download Confirmation", fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = onContactResort,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, GoldAccent),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("contact_villa_button")
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, tint = GoldAccent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Contact Green Edge Villa", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
