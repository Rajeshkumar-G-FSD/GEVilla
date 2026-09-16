package com.datazync.greenedgevilla.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.datazync.greenedgevilla.data.model.Booking
import com.datazync.greenedgevilla.data.model.Offer
import com.datazync.greenedgevilla.data.model.RoomUnit
import com.datazync.greenedgevilla.data.model.UnitAvailabilityStatus
import com.datazync.greenedgevilla.data.repository.VillaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

enum class NavTab {
    HOME,
    ROOMS,
    BOOKINGS,
    OFFERS,
    ADMIN,
    PROFILE
}

enum class AdminDateFilter {
    TODAY,
    UPCOMING,
    ALL
}

data class AdminSummaryStats(
    val totalRooms: Int = 6,
    val totalCots: Int = 15,
    val availableToday: Int = 0,
    val bookedToday: Int = 0,
    val blockedToday: Int = 0,
    val checkInsToday: Int = 0,
    val checkOutsToday: Int = 0,
    val occupancyPercentage: Int = 0,
    val todayRevenue: Double = 0.0,
    val otaCounts: Map<String, Int> = emptyMap(),
    val totalUnits: Int = 6,
    val availableUnitsCount: Int = 0,
    val bookedUnitsCount: Int = 0,
    val currentDateBookingsCount: Int = 0,
    val upcomingDateBookingsCount: Int = 0,
    val totalRevenue: Double = 0.0
)

class VillaViewModel(
    private val repository: VillaRepository,
    private val adminSession: com.datazync.greenedgevilla.data.session.AdminSession
) : ViewModel() {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val today: LocalDate = LocalDate.now()

    // Navigation & Screen presentation state
    private val _currentTab = MutableStateFlow(NavTab.ROOMS)
    val currentTab: StateFlow<NavTab> = _currentTab.asStateFlow()

    // Admin login gate
    private val _isAdminAuthenticated = MutableStateFlow(adminSession.isLoggedIn)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

    private val _adminLoginError = MutableStateFlow<String?>(null)
    val adminLoginError: StateFlow<String?> = _adminLoginError.asStateFlow()

    private val _isAdminLoggingIn = MutableStateFlow(false)
    val isAdminLoggingIn: StateFlow<Boolean> = _isAdminLoggingIn.asStateFlow()

    private val _selectedRoom = MutableStateFlow<RoomUnit?>(null)
    val selectedRoom: StateFlow<RoomUnit?> = _selectedRoom.asStateFlow()

    private val _isAdminDashboardOpen = MutableStateFlow(false)
    val isAdminDashboardOpen: StateFlow<Boolean> = _isAdminDashboardOpen.asStateFlow()

    private val _isLocationMapOpen = MutableStateFlow(false)
    val isLocationMapOpen: StateFlow<Boolean> = _isLocationMapOpen.asStateFlow()

    // Search dates & guests state
    private val _checkInDate = MutableStateFlow(today)
    val checkInDate: StateFlow<LocalDate> = _checkInDate.asStateFlow()

    private val _checkOutDate = MutableStateFlow(today.plusDays(2))
    val checkOutDate: StateFlow<LocalDate> = _checkOutDate.asStateFlow()

    private val _adultsCount = MutableStateFlow(2)
    val adultsCount: StateFlow<Int> = _adultsCount.asStateFlow()

    private val _childrenCount = MutableStateFlow(0)
    val childrenCount: StateFlow<Int> = _childrenCount.asStateFlow()

    // Filter state for Rooms Screen
    private val _filterBlock = MutableStateFlow("All")
    val filterBlock: StateFlow<String> = _filterBlock.asStateFlow()

    private val _filterBhk = MutableStateFlow("All")
    val filterBhk: StateFlow<String> = _filterBhk.asStateFlow()

    private val _filterPriceMax = MutableStateFlow(15000f)
    val filterPriceMax: StateFlow<Float> = _filterPriceMax.asStateFlow()

    private val _filterOnlyAvailable = MutableStateFlow(false)
    val filterOnlyAvailable: StateFlow<Boolean> = _filterOnlyAvailable.asStateFlow()

    // Booking Flow State
    private val _isBookingFlowActive = MutableStateFlow(false)
    val isBookingFlowActive: StateFlow<Boolean> = _isBookingFlowActive.asStateFlow()

    private val _bookingFlowStep = MutableStateFlow(1) // 1 to 8
    val bookingFlowStep: StateFlow<Int> = _bookingFlowStep.asStateFlow()

    private val _bookingSelectedUnit = MutableStateFlow<RoomUnit?>(null)
    val bookingSelectedUnit: StateFlow<RoomUnit?> = _bookingSelectedUnit.asStateFlow()

    private val _guestFullName = MutableStateFlow("Siddharth Raman")
    val guestFullName: StateFlow<String> = _guestFullName.asStateFlow()

    private val _guestMobile = MutableStateFlow("+91 98410 77654")
    val guestMobile: StateFlow<String> = _guestMobile.asStateFlow()

    private val _guestEmail = MutableStateFlow("siddharth.raman@gmail.com")
    val guestEmail: StateFlow<String> = _guestEmail.asStateFlow()

    private val _specialRequests = MutableStateFlow("")
    val specialRequests: StateFlow<String> = _specialRequests.asStateFlow()

    // Booking source / OTA reference (how the guest found/is booking this stay)
    private val _bookingSourceOta = MutableStateFlow("Direct")
    val bookingSourceOta: StateFlow<String> = _bookingSourceOta.asStateFlow()

    private val _bookingSourceOtherText = MutableStateFlow("")
    val bookingSourceOtherText: StateFlow<String> = _bookingSourceOtherText.asStateFlow()

    private val _selectedPaymentMethod = MutableStateFlow("UPI")
    val selectedPaymentMethod: StateFlow<String> = _selectedPaymentMethod.asStateFlow()

    private val _appliedOffer = MutableStateFlow<Offer?>(null)
    val appliedOffer: StateFlow<Offer?> = _appliedOffer.asStateFlow()

    private val _confirmedBooking = MutableStateFlow<Booking?>(null)
    val confirmedBooking: StateFlow<Booking?> = _confirmedBooking.asStateFlow()

    private val _bookingErrorMessage = MutableStateFlow<String?>(null)
    val bookingErrorMessage: StateFlow<String?> = _bookingErrorMessage.asStateFlow()

    // Admin Dashboard filters
    private val _adminDateFilter = MutableStateFlow(AdminDateFilter.TODAY)
    val adminDateFilter: StateFlow<AdminDateFilter> = _adminDateFilter.asStateFlow()

    private val _adminOtaFilter = MutableStateFlow("All")
    val adminOtaFilter: StateFlow<String> = _adminOtaFilter.asStateFlow()

    // Dialog states
    private val _bookingToCancel = MutableStateFlow<Booking?>(null)
    val bookingToCancel: StateFlow<Booking?> = _bookingToCancel.asStateFlow()

    private val _roomForPriceEdit = MutableStateFlow<RoomUnit?>(null)
    val roomForPriceEdit: StateFlow<RoomUnit?> = _roomForPriceEdit.asStateFlow()

    private val _roomForBlockToggle = MutableStateFlow<RoomUnit?>(null)
    val roomForBlockToggle: StateFlow<RoomUnit?> = _roomForBlockToggle.asStateFlow()

    private val _isQuickAddAdminBookingOpen = MutableStateFlow(false)
    val isQuickAddAdminBookingOpen: StateFlow<Boolean> = _isQuickAddAdminBookingOpen.asStateFlow()

    // Dashboard pull-to-refresh state
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    /** Firestore sync error, if the live listeners hit one (e.g. no network, rules denial). */
    val syncError: StateFlow<String?> = repository.syncError

    // Base flows from repository
    val allUnits: StateFlow<List<RoomUnit>> = repository.allUnits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBookings: StateFlow<List<Booking>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val offers: List<Offer> = repository.getOffers()

    // Admin Stats calculation
    val adminSummaryStats: StateFlow<AdminSummaryStats> = combine(allUnits, allBookings) { units, bookings ->
        val activeBookings = bookings.filter { it.bookingStatus != "Cancelled" }
        var availCount = 0
        var bookedCount = 0
        var blockedCount = 0
        var checkIns = 0
        var checkOuts = 0
        var todayRev = 0.0
        val otaMap = mutableMapOf<String, Int>()

        units.forEach { unit ->
            val status = repository.determineUnitStatus(unit, today, activeBookings)
            when (status) {
                UnitAvailabilityStatus.AVAILABLE -> availCount++
                UnitAvailabilityStatus.BOOKED -> bookedCount++
                UnitAvailabilityStatus.BLOCKED -> blockedCount++
                UnitAvailabilityStatus.CHECK_IN_TODAY -> {
                    bookedCount++
                    checkIns++
                }
                UnitAvailabilityStatus.CHECK_OUT_TODAY -> {
                    checkOuts++
                    availCount++
                }
            }
        }

        var totalLoggedRevenue = 0.0
        var currentStaysCount = 0
        var upcomingCount = 0

        activeBookings.forEach { b ->
            otaMap[b.source] = (otaMap[b.source] ?: 0) + 1
            totalLoggedRevenue += b.totalAmount
            if (b.bookingStatus == "Current Stay" || b.checkInDate.contains("Today")) {
                currentStaysCount++
            } else if (b.bookingStatus == "Upcoming") {
                upcomingCount++
            }
            val bIn = runCatching { LocalDate.parse(b.checkInDate, dateFormatter) }.getOrNull()
            val bOut = runCatching { LocalDate.parse(b.checkOutDate, dateFormatter) }.getOrNull()
            if (bIn != null && bOut != null && !today.isBefore(bIn) && !today.isAfter(bOut)) {
                todayRev += (b.totalAmount / (b.numberOfNights.coerceAtLeast(1)))
            }
        }

        val occ = if (units.isNotEmpty()) ((bookedCount.toDouble() / units.size) * 100).toInt() else 0

        AdminSummaryStats(
            totalRooms = units.size,
            totalCots = units.sumOf { it.cotCapacity },
            availableToday = availCount,
            bookedToday = bookedCount,
            blockedToday = blockedCount,
            checkInsToday = checkIns,
            checkOutsToday = checkOuts,
            occupancyPercentage = occ,
            todayRevenue = todayRev,
            otaCounts = otaMap,
            totalUnits = units.size,
            availableUnitsCount = availCount,
            bookedUnitsCount = bookedCount,
            currentDateBookingsCount = currentStaysCount,
            upcomingDateBookingsCount = upcomingCount,
            totalRevenue = totalLoggedRevenue
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdminSummaryStats())

    // Filtered units for Rooms screen
    val filteredUnits: StateFlow<List<RoomUnit>> = combine(
        allUnits,
        allBookings,
        _filterBlock,
        _filterBhk,
        _filterPriceMax,
        _filterOnlyAvailable,
        _checkInDate,
        _checkOutDate
    ) { args ->
        val units = args[0] as List<RoomUnit>
        val bookings = args[1] as List<Booking>
        val block = args[2] as String
        val bhk = args[3] as String
        val maxPrice = args[4] as Float
        val onlyAvail = args[5] as Boolean
        val inDate = args[6] as LocalDate
        val outDate = args[7] as LocalDate

        units.filter { unit ->
            val matchBlock = (block == "All" || unit.block.equals(block, ignoreCase = true))
            val matchBhk = (bhk == "All" || unit.bhkType.equals(bhk, ignoreCase = true))
            val matchPrice = unit.pricePerNight <= maxPrice
            val isAvail = !unit.isBlocked && isUnitAvailableForDates(unit, inDate, outDate, bookings)

            matchBlock && matchBhk && matchPrice && (!onlyAvail || isAvail)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun isUnitAvailableForDates(
        unit: RoomUnit,
        checkIn: LocalDate,
        checkOut: LocalDate,
        bookings: List<Booking>
    ): Boolean {
        if (unit.isBlocked) return false
        val unitBookings = bookings.filter { it.unitId == unit.id && it.bookingStatus != "Cancelled" }
        for (b in unitBookings) {
            val bIn = runCatching { LocalDate.parse(b.checkInDate, dateFormatter) }.getOrNull() ?: continue
            val bOut = runCatching { LocalDate.parse(b.checkOutDate, dateFormatter) }.getOrNull() ?: continue
            if (checkIn.isBefore(bOut) && checkOut.isAfter(bIn)) {
                return false
            }
        }
        return true
    }

    // Public actions
    fun setTab(tab: NavTab) {
        _currentTab.value = tab
        _selectedRoom.value = null
        _isBookingFlowActive.value = false
        _isAdminDashboardOpen.value = false
        _isLocationMapOpen.value = false
    }

    fun selectRoomForDetail(room: RoomUnit) {
        _selectedRoom.value = room
    }

    fun closeRoomDetail() {
        _selectedRoom.value = null
    }

    fun openAdminDashboard() {
        _isAdminDashboardOpen.value = true
    }

    fun closeAdminDashboard() {
        _isAdminDashboardOpen.value = false
    }

    fun openLocationMap() {
        _isLocationMapOpen.value = true
    }

    fun closeLocationMap() {
        _isLocationMapOpen.value = false
    }

    fun updateCheckInDate(date: LocalDate) {
        _checkInDate.value = date
        if (!_checkOutDate.value.isAfter(date)) {
            _checkOutDate.value = date.plusDays(1)
        }
    }

    fun updateCheckOutDate(date: LocalDate) {
        if (date.isAfter(_checkInDate.value)) {
            _checkOutDate.value = date
        }
    }

    fun updateGuests(adults: Int, children: Int) {
        _adultsCount.value = adults.coerceAtLeast(1)
        _childrenCount.value = children.coerceAtLeast(0)
    }

    fun setBlockFilter(block: String) {
        _filterBlock.value = block
    }

    fun setBhkFilter(bhk: String) {
        _filterBhk.value = bhk
    }

    fun setPriceMaxFilter(price: Float) {
        _filterPriceMax.value = price
    }

    fun setOnlyAvailableFilter(only: Boolean) {
        _filterOnlyAvailable.value = only
    }

    // Booking Flow triggers
    fun startBookingFlow(unit: RoomUnit? = null) {
        _bookingSelectedUnit.value = unit ?: allUnits.value.firstOrNull()
        _bookingFlowStep.value = if (unit != null) 5 else 1
        _isBookingFlowActive.value = true
        _bookingErrorMessage.value = null
        _appliedOffer.value = null
        _bookingSourceOta.value = "Direct"
        _bookingSourceOtherText.value = ""
    }

    fun setBookingFlowStep(step: Int) {
        _bookingFlowStep.value = step
    }

    fun setBookingSelectedUnit(unit: RoomUnit) {
        _bookingSelectedUnit.value = unit
    }

    fun updateGuestDetails(name: String, mobile: String, email: String, special: String) {
        _guestFullName.value = name
        _guestMobile.value = mobile
        _guestEmail.value = email
        _specialRequests.value = special
    }

    fun updateBookingSource(otaSource: String, otherText: String = _bookingSourceOtherText.value) {
        _bookingSourceOta.value = otaSource
        _bookingSourceOtherText.value = if (otaSource == "Others") otherText else ""
    }

    fun setPaymentMethod(method: String) {
        _selectedPaymentMethod.value = method
    }

    fun applyOffer(offer: Offer?) {
        _appliedOffer.value = offer
    }

    fun calculateNights(): Int {
        val nights = ChronoUnit.DAYS.between(_checkInDate.value, _checkOutDate.value)
        return nights.toInt().coerceAtLeast(1)
    }

    fun calculatePricing(unit: RoomUnit): Triple<Double, Double, Double> {
        val nights = calculateNights()
        var base = unit.pricePerNight * nights
        _appliedOffer.value?.let { offer ->
            val discount = base * (offer.discountPercent / 100.0)
            base -= discount
        }
        val taxes = base * 0.12 // 12% GST
        val total = base + taxes
        return Triple(base, taxes, total)
    }

    fun completeBooking() {
        val unit = _bookingSelectedUnit.value ?: return
        val nights = calculateNights()
        val (base, taxes, total) = calculatePricing(unit)
        val bookingId = repository.generateBookingId()

        val booking = Booking(
            id = bookingId,
            unitId = unit.id,
            block = unit.block,
            bhkType = unit.bhkType,
            guestName = _guestFullName.value.ifBlank { "Guest User" },
            guestMobile = _guestMobile.value.ifBlank { "+91 90000 00000" },
            guestEmail = _guestEmail.value.ifBlank { "guest@example.com" },
            adults = _adultsCount.value,
            children = _childrenCount.value,
            checkInDate = _checkInDate.value.format(dateFormatter),
            checkOutDate = _checkOutDate.value.format(dateFormatter),
            numberOfNights = nights,
            roomPrice = base,
            taxes = taxes,
            totalAmount = total,
            specialRequests = _specialRequests.value,
            paymentMethod = _selectedPaymentMethod.value,
            paymentStatus = if (_selectedPaymentMethod.value == "Pay at Hotel") "Pending" else "Paid",
            bookingStatus = "Upcoming",
            source = if (_bookingSourceOta.value == "Others") {
                _bookingSourceOtherText.value.ifBlank { "Other" }
            } else if (_bookingSourceOta.value == "Direct") {
                "Direct App"
            } else {
                _bookingSourceOta.value
            },
            referenceName = if (_bookingSourceOta.value == "Direct") "App Direct Booking" else ""
        )

        viewModelScope.launch {
            val result = repository.createBooking(booking)
            if (result.isSuccess) {
                _confirmedBooking.value = booking
                _bookingFlowStep.value = 8 // Confirmation step
                _bookingErrorMessage.value = null
            } else {
                _bookingErrorMessage.value = result.exceptionOrNull()?.message ?: "Booking failed. Unit unavailable."
            }
        }
    }

    fun closeBookingFlow() {
        _isBookingFlowActive.value = false
        _bookingFlowStep.value = 1
        _bookingErrorMessage.value = null
    }

    // Cancellation & Management
    fun requestCancelBooking(booking: Booking) {
        _bookingToCancel.value = booking
    }

    fun dismissCancelDialog() {
        _bookingToCancel.value = null
    }

    fun confirmCancelBooking() {
        val booking = _bookingToCancel.value ?: return
        viewModelScope.launch {
            repository.updateBookingStatus(booking.id, "Cancelled")
            _bookingToCancel.value = null
        }
    }

    // Admin Quick Actions
    fun setAdminDateFilter(filter: AdminDateFilter) {
        _adminDateFilter.value = filter
    }

    fun setAdminOtaFilter(filter: String) {
        _adminOtaFilter.value = filter
    }

    fun promptPriceEdit(unit: RoomUnit) {
        _roomForPriceEdit.value = unit
    }

    fun dismissPriceEdit() {
        _roomForPriceEdit.value = null
    }

    fun savePriceEdit(newPrice: Double) {
        val unit = _roomForPriceEdit.value ?: return
        viewModelScope.launch {
            repository.updateRoomPrice(unit.id, newPrice)
            _roomForPriceEdit.value = null
        }
    }

    fun promptBlockToggle(unit: RoomUnit) {
        _roomForBlockToggle.value = unit
    }

    fun dismissBlockToggle() {
        _roomForBlockToggle.value = null
    }

    fun confirmBlockToggle(reason: String) {
        val unit = _roomForBlockToggle.value ?: return
        viewModelScope.launch {
            repository.toggleRoomBlock(unit.id, !unit.isBlocked, reason)
            _roomForBlockToggle.value = null
        }
    }

    fun openQuickAddAdminBooking() {
        _isQuickAddAdminBookingOpen.value = true
    }

    fun closeQuickAddAdminBooking() {
        _isQuickAddAdminBookingOpen.value = false
    }

    fun createAdminBooking(
        unitId: String,
        guestName: String,
        guestPhone: String,
        source: String,
        referenceName: String,
        checkIn: LocalDate,
        checkOut: LocalDate,
        adults: Int
    ) {
        val unit = allUnits.value.firstOrNull { it.id == unitId } ?: return
        val nights = ChronoUnit.DAYS.between(checkIn, checkOut).toInt().coerceAtLeast(1)
        val base = unit.pricePerNight * nights
        val taxes = base * 0.12
        val total = base + taxes
        val bookingId = repository.generateBookingId()

        val booking = Booking(
            id = bookingId,
            unitId = unit.id,
            block = unit.block,
            bhkType = unit.bhkType,
            guestName = guestName.ifBlank { "OTA Guest" },
            guestMobile = guestPhone.ifBlank { "+91 90000 00000" },
            guestEmail = "reservations@greenedgevilla.com",
            adults = adults,
            children = 0,
            checkInDate = checkIn.format(dateFormatter),
            checkOutDate = checkOut.format(dateFormatter),
            numberOfNights = nights,
            roomPrice = base,
            taxes = taxes,
            totalAmount = total,
            specialRequests = "Added via Admin Console ($source - Ref: $referenceName)",
            paymentMethod = if (source == "Direct App" || source == "Walk-in") "UPI" else "OTA Pre-paid",
            paymentStatus = "Paid",
            bookingStatus = if (checkIn.isEqual(today)) "Current Stay" else "Upcoming",
            source = source,
            referenceName = referenceName
        )

        viewModelScope.launch {
            repository.createBooking(booking)
            _isQuickAddAdminBookingOpen.value = false
        }
    }

    fun getUnitStatusForToday(unit: RoomUnit): UnitAvailabilityStatus {
        return repository.determineUnitStatus(unit, today, allBookings.value)
    }

    val currentNavTab: StateFlow<NavTab> get() = currentTab
    fun setNavTab(tab: NavTab) = setTab(tab)

    val selectedRoomDetail: StateFlow<RoomUnit?> get() = selectedRoom
    fun setSelectedRoomDetail(unit: RoomUnit) = selectRoomForDetail(unit)
    fun clearSelectedRoomDetail() = closeRoomDetail()

    fun dismissBookingFlow() = closeBookingFlow()
    fun startBookingFlowForUnit(unit: RoomUnit) = startBookingFlow(unit)

    fun attemptAdminLogin(email: String, password: String) {
        if (_isAdminLoggingIn.value) return
        viewModelScope.launch {
            _isAdminLoggingIn.value = true
            _adminLoginError.value = null
            delay(450) // smooth minimum-visible loading state, matches dashboard refresh feel
            val emailMatches = email.trim().equals(
                com.datazync.greenedgevilla.data.session.AdminCredentials.EMAIL,
                ignoreCase = true
            )
            val passwordMatches = password == com.datazync.greenedgevilla.data.session.AdminCredentials.PASSWORD
            if (emailMatches && passwordMatches) {
                adminSession.isLoggedIn = true
                _isAdminAuthenticated.value = true
            } else {
                _adminLoginError.value = "Incorrect email or password."
            }
            _isAdminLoggingIn.value = false
        }
    }

    fun clearAdminLoginError() {
        _adminLoginError.value = null
    }

    fun logoutAdmin() {
        adminSession.isLoggedIn = false
        _isAdminAuthenticated.value = false
        setNavTab(NavTab.ROOMS)
    }

    /**
     * Pull-to-refresh / refresh-button handler for the Admin dashboard. Forces a fresh
     * server fetch and keeps the spinner up for a minimum visible duration so it always
     * reads as a deliberate, smooth action rather than a flash.
     */
    fun refreshDashboard() {
        if (_isRefreshing.value) return
        viewModelScope.launch {
            _isRefreshing.value = true
            val minSpinner = launch { delay(500) }
            repository.refreshNow()
            minSpinner.join()
            _isRefreshing.value = false
        }
    }

    fun toggleUnitBlock(unit: RoomUnit) {
        viewModelScope.launch {
            repository.toggleRoomBlock(unit.id, !unit.isBlocked, if (unit.isBlocked) "Available" else "Maintenance")
        }
    }

    fun recordManualBooking(
        unitId: String,
        guestName: String,
        guestMobile: String,
        checkIn: String,
        checkOut: String,
        adults: Int,
        children: Int,
        totalAmount: Double,
        source: String,
        refName: String,
        notes: String
    ) {
        val unit = allUnits.value.firstOrNull { it.id == unitId } ?: return
        val inDate = runCatching { LocalDate.parse(checkIn, dateFormatter) }.getOrElse { today }
        val outDate = runCatching { LocalDate.parse(checkOut, dateFormatter) }.getOrElse { today.plusDays(1) }
        val nights = ChronoUnit.DAYS.between(inDate, outDate).toInt().coerceAtLeast(1)
        val taxes = totalAmount * 0.12 / 1.12
        val base = totalAmount - taxes
        val booking = Booking(
            id = repository.generateBookingId(),
            unitId = unit.id,
            block = unit.block,
            bhkType = unit.bhkType,
            guestName = guestName,
            guestMobile = guestMobile,
            guestEmail = "reservations@greenedgevilla.com",
            adults = adults,
            children = children,
            checkInDate = checkIn,
            checkOutDate = checkOut,
            numberOfNights = nights,
            roomPrice = base,
            taxes = taxes,
            totalAmount = totalAmount,
            specialRequests = notes,
            paymentMethod = if (source == "Walk-in" || source == "Direct") "Cash" else "OTA Pre-paid",
            paymentStatus = "Paid",
            bookingStatus = if (inDate.isEqual(today)) "Current Stay" else "Upcoming",
            source = source,
            referenceName = refName
        )
        viewModelScope.launch {
            repository.createBooking(booking)
        }
    }

    companion object {
        // One Firestore-backed repository (and its two snapshot listeners) for the whole app,
        // regardless of how many times a ViewModel gets recreated.
        @Volatile private var repositoryInstance: VillaRepository? = null

        private fun getOrCreateRepository(): VillaRepository =
            repositoryInstance ?: synchronized(this) {
                repositoryInstance ?: VillaRepository(com.google.firebase.firestore.FirebaseFirestore.getInstance())
                    .also { repositoryInstance = it }
            }

        fun provideFactory(context: android.content.Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val adminSession = com.datazync.greenedgevilla.data.session.AdminSession(context.applicationContext)
                return VillaViewModel(getOrCreateRepository(), adminSession) as T
            }
        }
    }
}

class VillaViewModelFactory(
    private val repository: VillaRepository,
    private val adminSession: com.datazync.greenedgevilla.data.session.AdminSession
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VillaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VillaViewModel(repository, adminSession) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
