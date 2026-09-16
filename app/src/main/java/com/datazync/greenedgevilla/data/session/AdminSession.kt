package com.datazync.greenedgevilla.data.session

import android.content.Context

/**
 * In-memory-only admin login flag — deliberately NOT persisted to disk, so the admin is
 * asked for the password again every time the app is freshly opened (i.e. after the splash
 * screen on a real cold start), even if they logged in during a previous run. Staying logged
 * in while simply switching tabs within the same app session still works, since this object
 * lives as long as the ViewModel does.
 *
 * This is a local-device gate for the Admin dashboard UI, not a Firebase-authenticated
 * identity — Firestore rules still need to be tightened separately before this is treated
 * as a real security boundary.
 */
class AdminSession(@Suppress("UNUSED_PARAMETER") context: Context) {
    var isLoggedIn: Boolean = false
}

object AdminCredentials {
    const val EMAIL = "greenedgevila@gmail.com"
    const val PASSWORD = "Ge@12345"
}
