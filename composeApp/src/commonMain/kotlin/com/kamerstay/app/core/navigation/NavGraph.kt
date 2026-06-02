package com.kamerstay.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kamerstay.app.features.auth.ForgotPasswordScreen
import com.kamerstay.app.features.auth.PasswordResetSuccessScreen
import com.kamerstay.app.features.auth.ResetPasswordScreen
import com.kamerstay.app.features.auth.SignInScreen
import com.kamerstay.app.features.auth.SignUpScreen
import com.kamerstay.app.features.auth.SplashScreen
import com.kamerstay.app.features.auth.VerificationCodeScreen
import com.kamerstay.app.features.auth.WelcomeScreen
import com.kamerstay.app.features.manager.AddEditRoomScreen
import com.kamerstay.app.features.manager.CheckInScreen
import com.kamerstay.app.features.manager.CheckOutScreen
import com.kamerstay.app.features.manager.ManagerDashboardScreen
import com.kamerstay.app.features.manager.ManagerProfileScreen
import com.kamerstay.app.features.manager.ReservationDetailsScreen
import com.kamerstay.app.features.manager.ReservationsScreen
import com.kamerstay.app.features.manager.RoomManagementScreen
import com.kamerstay.app.features.manager.StaffManagementScreen
import com.kamerstay.app.features.traveler.BookingConfirmationScreen
import com.kamerstay.app.features.traveler.BookingDetailsScreen
import com.kamerstay.app.features.traveler.BookingHistoryScreen
import com.kamerstay.app.features.traveler.BookingScreen
import com.kamerstay.app.features.traveler.BookingVoucherScreen
import com.kamerstay.app.features.traveler.FilterScreen
import com.kamerstay.app.features.traveler.HotelDetailsScreen
import com.kamerstay.app.features.traveler.HotelSearchScreen
import com.kamerstay.app.features.traveler.LocalTravelGuideScreen
import com.kamerstay.app.features.traveler.NotificationsScreen
import com.kamerstay.app.features.traveler.PaymentScreen
import com.kamerstay.app.features.traveler.RoomDetailsScreen
import com.kamerstay.app.features.traveler.SettingsScreen
import com.kamerstay.app.features.traveler.TravelerHomeScreen
import com.kamerstay.app.features.traveler.TravelerProfileScreen

@Composable
fun KamerStayNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        // ── Auth ──────────────────────────────────────
        composable(Routes.Splash.route) {
            SplashScreen(navController)
        }
        composable(Routes.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(Routes.SignIn.route) {
            SignInScreen(navController)
        }
        composable(Routes.SignUp.route) {
            SignUpScreen(navController)
        }
        composable(Routes.ForgotPassword.route) {
            ForgotPasswordScreen(navController)
        }
        composable(Routes.VerificationCode.route) {
            VerificationCodeScreen(navController)
        }
        composable(Routes.ResetPassword.route) {
            ResetPasswordScreen(navController)
        }
        composable(Routes.PasswordResetSuccess.route) {
            PasswordResetSuccessScreen(navController)
        }

        // ── Traveler ──────────────────────────────────
        composable(Routes.TravelerHome.route) {
            TravelerHomeScreen(navController)
        }
        composable(Routes.HotelSearch.route) {
            HotelSearchScreen(navController)
        }
        composable(Routes.Filter.route) {
            FilterScreen(navController)
        }
        composable(Routes.HotelDetails.route) {
            HotelDetailsScreen(
                navController = navController,
                hotelId = NavigationState.selectedHotelId
            )
        }
        composable(Routes.RoomDetails.route) {
            RoomDetailsScreen(
                navController = navController,
                roomId = NavigationState.selectedRoomId
            )
        }
        composable(Routes.Booking.route) {
            BookingScreen(
                navController = navController,
                hotelId = NavigationState.selectedHotelId,
                roomId = NavigationState.selectedRoomId
            )
        }
        composable(Routes.Payment.route) {
            PaymentScreen(
                navController = navController,
                bookingId = NavigationState.selectedBookingId
            )
        }
        composable(Routes.BookingConfirmation.route) {
            BookingConfirmationScreen(
                navController = navController,
                bookingId = NavigationState.selectedBookingId
            )
        }
        composable(Routes.BookingHistory.route) {
            BookingHistoryScreen(navController)
        }
        composable(Routes.BookingDetails.route) {
            BookingDetailsScreen(
                navController = navController,
                bookingId = NavigationState.selectedBookingId
            )
        }
        composable(Routes.TravelerProfile.route) {
             TravelerProfileScreen(navController)
        }
        composable(Routes.BookingVoucher.route) {
            BookingVoucherScreen(navController)
        }
        composable(Routes.LocalGuide.route) {
            LocalTravelGuideScreen(navController)
        }

        // ── Manager ───────────────────────────────────
        composable(Routes.ManagerDashboard.route) {
            ManagerDashboardScreen(navController)
        }
        composable(Routes.RoomManagement.route) {
            RoomManagementScreen(navController)
        }
        composable(Routes.AddEditRoom.route) {
            AddEditRoomScreen(navController)
        }
        composable(Routes.Reservations.route) {
            ReservationsScreen(navController)
        }
        composable(Routes.ReservationDetails.route) {
            ReservationDetailsScreen(
                navController = navController,
                reservationId = NavigationState.selectedBookingId
            )
        }
        composable(Routes.CheckIn.route) {
            CheckInScreen(
                navController = navController,
                reservationId = NavigationState.selectedBookingId
            )
        }
        composable(Routes.CheckOut.route) {
            CheckOutScreen(
                navController = navController,
                reservationId = NavigationState.selectedBookingId
            )
        }
        composable(Routes.ManagerProfile.route) {
            ManagerProfileScreen(navController)
        }
        composable(Routes.StaffManagement.route) {
            StaffManagementScreen(navController)
        }

        // ── Shared ────────────────────────────────────
        composable(Routes.Notifications.route) {
            NotificationsScreen(navController)
        }
        composable(Routes.Settings.route) {
             SettingsScreen(navController)
        }
    }
}