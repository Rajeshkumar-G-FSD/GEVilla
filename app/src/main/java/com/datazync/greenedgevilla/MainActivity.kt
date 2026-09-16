package com.datazync.greenedgevilla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.datazync.greenedgevilla.ui.screens.AdminDashboardScreen
import com.datazync.greenedgevilla.ui.screens.AdminLoginScreen
import com.datazync.greenedgevilla.ui.screens.BookingFlowScreen
import com.datazync.greenedgevilla.ui.screens.BookingsScreen
import com.datazync.greenedgevilla.ui.screens.HomeScreen
import com.datazync.greenedgevilla.ui.screens.OffersScreen
import com.datazync.greenedgevilla.ui.screens.ProfileScreen
import com.datazync.greenedgevilla.ui.screens.RoomDetailScreen
import com.datazync.greenedgevilla.ui.screens.RoomsScreen
import com.datazync.greenedgevilla.ui.screens.SPLASH_DURATION_MILLIS
import com.datazync.greenedgevilla.ui.screens.SplashScreen
import com.datazync.greenedgevilla.ui.theme.ForestGreenDark
import com.datazync.greenedgevilla.ui.theme.ForestGreenPrimary
import com.datazync.greenedgevilla.ui.theme.GoldAccent
import com.datazync.greenedgevilla.ui.theme.GreenEdgeVillaTheme
import com.datazync.greenedgevilla.ui.theme.TextSecondary
import com.datazync.greenedgevilla.ui.theme.WarmBeigeBackground
import com.datazync.greenedgevilla.ui.viewmodel.NavTab
import com.datazync.greenedgevilla.ui.viewmodel.VillaViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GreenEdgeVillaTheme {
                var showSplash by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(SPLASH_DURATION_MILLIS)
                    showSplash = false
                }

                if (showSplash) {
                    SplashScreen()
                } else {
                    val context = androidx.compose.ui.platform.LocalContext.current
                    val viewModel: VillaViewModel = viewModel(factory = VillaViewModel.provideFactory(context))
                    MainAppScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: VillaViewModel) {
    val currentTab by viewModel.currentNavTab.collectAsStateWithLifecycle()
    val selectedDetail by viewModel.selectedRoomDetail.collectAsStateWithLifecycle()
    val isBookingFlowActive by viewModel.isBookingFlowActive.collectAsStateWithLifecycle()
    val isAdminAuthenticated by viewModel.isAdminAuthenticated.collectAsStateWithLifecycle()

    // Handle Android system back button
    BackHandler(enabled = isBookingFlowActive || selectedDetail != null || currentTab != NavTab.ROOMS) {
        if (isBookingFlowActive) {
            viewModel.dismissBookingFlow()
        } else if (selectedDetail != null) {
            viewModel.clearSelectedRoomDetail()
        } else if (currentTab != NavTab.ROOMS) {
            viewModel.setNavTab(NavTab.ROOMS)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(WarmBeigeBackground)) {
        if (isBookingFlowActive) {
            // Active Step-by-Step Booking Flow (Steps 1 to 8)
            BookingFlowScreen(
                viewModel = viewModel,
                onFinish = { viewModel.dismissBookingFlow() },
                onViewBookingTab = {
                    viewModel.dismissBookingFlow()
                    viewModel.setNavTab(NavTab.BOOKINGS)
                }
            )
        } else if (selectedDetail != null) {
            // Selected Room Details Screen
            RoomDetailScreen(
                unit = selectedDetail!!,
                viewModel = viewModel,
                onBack = { viewModel.clearSelectedRoomDetail() },
                onBookNow = {
                    viewModel.startBookingFlowForUnit(selectedDetail!!)
                }
            )
        } else {
            // Main Bottom Navigation Scaffolding
            Scaffold(
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        modifier = Modifier.testTag("main_bottom_nav_bar")
                    ) {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.MeetingRoom, contentDescription = "Book Rooms") },
                            label = { Text("Book Rooms", fontWeight = if (currentTab == NavTab.ROOMS) FontWeight.Bold else FontWeight.Normal) },
                            selected = currentTab == NavTab.ROOMS,
                            onClick = { viewModel.setNavTab(NavTab.ROOMS) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ForestGreenPrimary,
                                selectedTextColor = ForestGreenPrimary,
                                indicatorColor = GoldAccent.copy(alpha = 0.2f),
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            ),
                            modifier = Modifier.testTag("nav_tab_rooms")
                        )

                        NavigationBarItem(
                            icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "My Bookings") },
                            label = { Text("My Bookings", fontWeight = if (currentTab == NavTab.BOOKINGS) FontWeight.Bold else FontWeight.Normal) },
                            selected = currentTab == NavTab.BOOKINGS,
                            onClick = { viewModel.setNavTab(NavTab.BOOKINGS) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ForestGreenPrimary,
                                selectedTextColor = ForestGreenPrimary,
                                indicatorColor = GoldAccent.copy(alpha = 0.2f),
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            ),
                            modifier = Modifier.testTag("nav_tab_bookings")
                        )

                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Phone, contentDescription = "Contact") },
                            label = { Text("Contact", fontWeight = if (currentTab == NavTab.PROFILE) FontWeight.Bold else FontWeight.Normal) },
                            selected = currentTab == NavTab.PROFILE,
                            onClick = { viewModel.setNavTab(NavTab.PROFILE) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ForestGreenPrimary,
                                selectedTextColor = ForestGreenPrimary,
                                indicatorColor = GoldAccent.copy(alpha = 0.2f),
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            ),
                            modifier = Modifier.testTag("nav_tab_contact")
                        )

                        NavigationBarItem(
                            icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                            label = { Text("Admin", fontWeight = if (currentTab == NavTab.ADMIN) FontWeight.Bold else FontWeight.Normal) },
                            selected = currentTab == NavTab.ADMIN,
                            onClick = { viewModel.setNavTab(NavTab.ADMIN) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ForestGreenPrimary,
                                selectedTextColor = ForestGreenPrimary,
                                indicatorColor = GoldAccent.copy(alpha = 0.2f),
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            ),
                            modifier = Modifier.testTag("nav_tab_admin")
                        )
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    when (currentTab) {
                        NavTab.HOME -> {
                            HomeScreen(
                                viewModel = viewModel,
                                onNavigateToRooms = { viewModel.setNavTab(NavTab.ROOMS) },
                                onNavigateToOffers = { viewModel.setNavTab(NavTab.OFFERS) },
                                onOpenRoomDetail = { viewModel.setSelectedRoomDetail(it) },
                                onBookUnit = { viewModel.startBookingFlowForUnit(it) }
                            )
                        }

                        NavTab.ROOMS -> {
                            RoomsScreen(
                                viewModel = viewModel,
                                onOpenRoomDetail = { viewModel.setSelectedRoomDetail(it) },
                                onBookUnit = { viewModel.startBookingFlowForUnit(it) }
                            )
                        }

                        NavTab.BOOKINGS -> {
                            BookingsScreen(
                                viewModel = viewModel,
                                onBookNewStay = { viewModel.startBookingFlow() },
                                onOpenRoomDetail = { viewModel.setSelectedRoomDetail(it) },
                                onBookUnit = { viewModel.startBookingFlowForUnit(it) }
                            )
                        }

                        NavTab.OFFERS -> {
                            OffersScreen(
                                viewModel = viewModel,
                                onBookWithOffer = {
                                    viewModel.startBookingFlow()
                                }
                            )
                        }

                        NavTab.ADMIN -> {
                            if (isAdminAuthenticated) {
                                AdminDashboardScreen(
                                    viewModel = viewModel
                                )
                            } else {
                                AdminLoginScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.setNavTab(NavTab.ROOMS) }
                                )
                            }
                        }

                        NavTab.PROFILE -> {
                            ProfileScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

