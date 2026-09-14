package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.Booking
import com.example.data.model.RoomUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [RoomUnit::class, Booking::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "green_edge_villa_db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.let { database ->
                                seedInitialData(database)
                            }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedInitialData(database: AppDatabase) {
            val roomDao = database.roomDao()
            val bookingDao = database.bookingDao()

            if (roomDao.getUnitCount() == 0) {
                val units = listOf(
                    RoomUnit(
                        id = "A1",
                        block = "Block A",
                        bhkType = "3 BHK",
                        bedroomCount = 3,
                        cotCapacity = 6,
                        bedroomBreakdown = "3 bedrooms • Each bedroom has 2 cots (6 cots total)",
                        maxGuests = 8,
                        pricePerNight = 12000.0,
                        description = "Luxurious 3 BHK villa unit in Block A offering panoramic Nilgiri tea garden vistas, spacious private sit-out, stone hearth fireplace, and handcrafted rosewood furniture.",
                        amenities = listOf(
                            "Tea Garden & Mountain View",
                            "High-Speed Wi-Fi",
                            "Stone Fireplace",
                            "Kitchenette & Kettle",
                            "Private Balcony & Lawn",
                            "24/7 Geyser Hot Water",
                            "Smart TV with OTT",
                            "Complimentary Breakfast",
                            "Free On-site Parking"
                        ),
                        houseRules = "Quiet hours after 10:30 PM • No smoking inside bedrooms • Bonfire upon prior request • Pets permitted with advance notice.",
                        checkInTime = "2:00 PM",
                        checkOutTime = "11:00 AM",
                        primaryImageResName = "resort_hero_ooty"
                    ),
                    RoomUnit(
                        id = "A2",
                        block = "Block A",
                        bhkType = "3 BHK",
                        bedroomCount = 3,
                        cotCapacity = 6,
                        bedroomBreakdown = "3 bedrooms • Each bedroom has 2 cots (6 cots total)",
                        maxGuests = 8,
                        pricePerNight = 12000.0,
                        description = "Elegant upper-level 3 BHK luxury retreat in Block A. Features cathedral-height wooden ceilings, private glass patio overlooking the mist-clad valley, and attached modern baths.",
                        amenities = listOf(
                            "Valley & Mist View",
                            "High-Speed Wi-Fi",
                            "Living & Dining Space",
                            "Tea/Coffee Station",
                            "Private Glass Patio",
                            "24/7 Geyser Hot Water",
                            "Room Service on Request",
                            "Complimentary Breakfast",
                            "Lawn Access"
                        ),
                        houseRules = "No loud music post 10:00 PM • Valid photo ID required for all adults at check-in • Non-smoking suites.",
                        checkInTime = "2:00 PM",
                        checkOutTime = "11:00 AM",
                        primaryImageResName = "room_villa_bedroom"
                    ),
                    RoomUnit(
                        id = "B1",
                        block = "Block B",
                        bhkType = "3 BHK",
                        bedroomCount = 3,
                        cotCapacity = 6,
                        bedroomBreakdown = "3 bedrooms • Each bedroom has 2 cots (6 cots total)",
                        maxGuests = 8,
                        pricePerNight = 11500.0,
                        description = "Cozy family-styled 3 BHK suite located in Block B. Surrounded by silver oak trees and fragrant eucalyptus with immediate access to children's play area and outdoor campfire zone.",
                        amenities = listOf(
                            "Pine Forest View",
                            "High-Speed Wi-Fi",
                            "Plush Living Room",
                            "Electric Kettle & Snacks",
                            "Private Wooden Veranda",
                            "Instant Hot Water",
                            "Barbeque Facility Available",
                            "Complimentary Breakfast"
                        ),
                        houseRules = "Family friendly • Alcohol allowed in designated areas only • Check-in requires ID proof.",
                        checkInTime = "2:00 PM",
                        checkOutTime = "11:00 AM",
                        primaryImageResName = "room_villa_living"
                    ),
                    RoomUnit(
                        id = "B2",
                        block = "Block B",
                        bhkType = "2 BHK",
                        bedroomCount = 2,
                        cotCapacity = 3,
                        bedroomBreakdown = "2 bedrooms • Bedroom 1: 2 cots, Bedroom 2: 1 cot (3 cots total)",
                        maxGuests = 5,
                        pricePerNight = 8500.0,
                        description = "Charming 2 BHK bungalow in Block B tailored for smaller families or traveling groups of friends. Intimate garden porch, cozy lounge, and tranquil hillside breeze.",
                        amenities = listOf(
                            "Garden & Mountain View",
                            "High-Speed Wi-Fi",
                            "Tea/Coffee Maker",
                            "Private Veranda",
                            "Hot Water Geyser",
                            "Daily Housekeeping",
                            "Room Service",
                            "Complimentary Breakfast"
                        ),
                        houseRules = "Max occupancy 5 guests • Outside food allowed in dining area • Smoking only in outdoor garden.",
                        checkInTime = "2:00 PM",
                        checkOutTime = "11:00 AM",
                        primaryImageResName = "room_villa_bedroom"
                    ),
                    RoomUnit(
                        id = "C1",
                        block = "Block C",
                        bhkType = "2 BHK",
                        bedroomCount = 2,
                        cotCapacity = 3,
                        bedroomBreakdown = "2 bedrooms • Bedroom 1: 2 cots, Bedroom 2: 1 cot (3 cots total)",
                        maxGuests = 5,
                        pricePerNight = 8500.0,
                        description = "Standalone private 2 BHK villa in Block C surrounded by private hedge boundary. Features open sun deck, wooden-beamed interiors, and peaceful hill silence.",
                        amenities = listOf(
                            "Sunrise Hill View",
                            "High-Speed Wi-Fi",
                            "Tea & Herbal Brew Setup",
                            "Private Sun Deck",
                            "Hot Water Geyser",
                            "Dedicated Caretaker",
                            "Bonfire on Request",
                            "Complimentary Breakfast"
                        ),
                        houseRules = "No illegal substances • Check-out strictly by 11:00 AM for sanitization • Respect nature & wildlife.",
                        checkInTime = "2:00 PM",
                        checkOutTime = "11:00 AM",
                        primaryImageResName = "resort_hero_ooty"
                    ),
                    RoomUnit(
                        id = "D1",
                        block = "Block D",
                        bhkType = "1 BHK",
                        bedroomCount = 1,
                        cotCapacity = 2,
                        bedroomBreakdown = "1 bedroom • 2 cots (2 cots total)",
                        maxGuests = 3,
                        pricePerNight = 4500.0,
                        description = "Romantic and secluded 1 BHK cottage in Block D, perfect for couples and solo travelers looking for misty mountain tranquility, morning bird songs, and fresh Ooty air.",
                        amenities = listOf(
                            "Misty Valley Panorama",
                            "High-Speed Wi-Fi",
                            "King Bed with 2 Cots",
                            "French Window Balcony",
                            "Hot Water Shower",
                            "Electric Kettle",
                            "Complimentary Breakfast",
                            "In-room Dining"
                        ),
                        houseRules = "Couples welcome with valid ID • Peaceful ambience maintained at all times • Late check-in on request.",
                        checkInTime = "2:00 PM",
                        checkOutTime = "11:00 AM",
                        primaryImageResName = "room_villa_living"
                    )
                )
                roomDao.insertUnits(units)
            }

            if (bookingDao.getBookingCount() == 0) {
                // Seed realistic multi-source bookings (Agoda, MakeMyTrip, VOYE, Direct)
                val sampleBookings = listOf(
                    Booking(
                        id = "GEV-2026-1084",
                        unitId = "B1",
                        block = "Block B",
                        bhkType = "3 BHK",
                        guestName = "Arjun Menon",
                        guestMobile = "+91 98401 23456",
                        guestEmail = "arjun.menon@example.com",
                        adults = 5,
                        children = 2,
                        checkInDate = "2026-09-14",
                        checkOutDate = "2026-09-17",
                        numberOfNights = 3,
                        roomPrice = 34500.0,
                        taxes = 4140.0,
                        totalAmount = 38640.0,
                        specialRequests = "High floor preferred, baby cot needed if available.",
                        paymentMethod = "UPI",
                        paymentStatus = "Paid",
                        bookingStatus = "Current Stay",
                        source = "MakeMyTrip",
                        referenceName = "MMT-IN-982341",
                        createdTimestamp = System.currentTimeMillis() - 86400000L * 2
                    ),
                    Booking(
                        id = "GEV-2026-1092",
                        unitId = "A2",
                        block = "Block A",
                        bhkType = "3 BHK",
                        guestName = "Dr. Priya Swaminathan",
                        guestMobile = "+91 97908 55432",
                        guestEmail = "priya.swami@example.com",
                        adults = 6,
                        children = 1,
                        checkInDate = "2026-09-14",
                        checkOutDate = "2026-09-16",
                        numberOfNights = 2,
                        roomPrice = 24000.0,
                        taxes = 2880.0,
                        totalAmount = 26880.0,
                        specialRequests = "Evening bonfire arrangement requested.",
                        paymentMethod = "Credit/Debit Card",
                        paymentStatus = "Paid",
                        bookingStatus = "Current Stay",
                        source = "Agoda",
                        referenceName = "AGD-8823-BLR",
                        createdTimestamp = System.currentTimeMillis() - 86400000L * 3
                    ),
                    Booking(
                        id = "GEV-2026-1105",
                        unitId = "C1",
                        block = "Block C",
                        bhkType = "2 BHK",
                        guestName = "Vikram & Sneha Patel",
                        guestMobile = "+91 99203 44110",
                        guestEmail = "vikram.patel@example.com",
                        adults = 3,
                        children = 1,
                        checkInDate = "2026-09-18",
                        checkOutDate = "2026-09-20",
                        numberOfNights = 2,
                        roomPrice = 17000.0,
                        taxes = 2040.0,
                        totalAmount = 19040.0,
                        specialRequests = "Late check-in around 8:00 PM by cab from Coimbatore.",
                        paymentMethod = "Net Banking",
                        paymentStatus = "Paid",
                        bookingStatus = "Upcoming",
                        source = "VOYE",
                        referenceName = "VOYE-OOTY-771",
                        createdTimestamp = System.currentTimeMillis() - 86400000L * 1
                    ),
                    Booking(
                        id = "GEV-2026-1110",
                        unitId = "D1",
                        block = "Block D",
                        bhkType = "1 BHK",
                        guestName = "Kavitha Ranganathan",
                        guestMobile = "+91 94441 98765",
                        guestEmail = "kavitha.r@example.com",
                        adults = 2,
                        children = 0,
                        checkInDate = "2026-09-21",
                        checkOutDate = "2026-09-24",
                        numberOfNights = 3,
                        roomPrice = 13500.0,
                        taxes = 1620.0,
                        totalAmount = 15120.0,
                        specialRequests = "Anniversary trip. Flowers in room please.",
                        paymentMethod = "UPI",
                        paymentStatus = "Paid",
                        bookingStatus = "Upcoming",
                        source = "Direct App",
                        referenceName = "App Direct Booking",
                        createdTimestamp = System.currentTimeMillis() - 3600000L * 5
                    ),
                    Booking(
                        id = "GEV-2026-1065",
                        unitId = "A1",
                        block = "Block A",
                        bhkType = "3 BHK",
                        guestName = "Rajeshwari S.",
                        guestMobile = "+91 98840 11223",
                        guestEmail = "rajeshwari@example.com",
                        adults = 6,
                        children = 2,
                        checkInDate = "2026-09-08",
                        checkOutDate = "2026-09-11",
                        numberOfNights = 3,
                        roomPrice = 36000.0,
                        taxes = 4320.0,
                        totalAmount = 40320.0,
                        specialRequests = "Vegetarian breakfast setup.",
                        paymentMethod = "UPI",
                        paymentStatus = "Paid",
                        bookingStatus = "Completed",
                        source = "Booking.com",
                        referenceName = "BK-90212-IN",
                        createdTimestamp = System.currentTimeMillis() - 86400000L * 7
                    ),
                    Booking(
                        id = "GEV-2026-1070",
                        unitId = "B2",
                        block = "Block B",
                        bhkType = "2 BHK",
                        guestName = "Manoj Kumar",
                        guestMobile = "+91 95000 66778",
                        guestEmail = "manoj.k@example.com",
                        adults = 4,
                        children = 1,
                        checkInDate = "2026-09-05",
                        checkOutDate = "2026-09-07",
                        numberOfNights = 2,
                        roomPrice = 17000.0,
                        taxes = 2040.0,
                        totalAmount = 19040.0,
                        specialRequests = "Owner referral discount requested.",
                        paymentMethod = "Pay at Hotel",
                        paymentStatus = "Paid",
                        bookingStatus = "Completed",
                        source = "Walk-in",
                        referenceName = "Suresh Cabs Ooty Referral",
                        createdTimestamp = System.currentTimeMillis() - 86400000L * 10
                    )
                )
                bookingDao.insertBookings(sampleBookings)
            }
        }
    }
}
