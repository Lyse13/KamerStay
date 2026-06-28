package com.kamerstay.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kamerstay.app.features.auth.ChangePasswordScreen
import com.kamerstay.app.features.auth.ForgotPasswordScreen
import com.kamerstay.app.features.auth.PasswordResetSuccessScreen
import com.kamerstay.app.features.auth.ResetPasswordScreen
import com.kamerstay.app.features.auth.RoleSelectionScreen
import com.kamerstay.app.features.auth.SignInScreen
import com.kamerstay.app.features.auth.SignUpScreen
import com.kamerstay.app.features.auth.OnboardingScreen
import com.kamerstay.app.features.auth.SplashScreen
import com.kamerstay.app.features.auth.VerificationCodeScreen
import com.kamerstay.app.features.auth.WelcomeScreen
import com.kamerstay.app.features.manager.AddEditRoomScreen
import com.kamerstay.app.features.manager.AddEditPromotionScreen
import com.kamerstay.app.features.manager.AddEditStaffScreen
import com.kamerstay.app.features.manager.CheckInScreen
import com.kamerstay.app.features.manager.CheckOutScreen
import com.kamerstay.app.features.manager.HotelAmenitiesScreen
import com.kamerstay.app.features.manager.ManageHotelScreen
import com.kamerstay.app.features.manager.ManagerDashboardScreen
import com.kamerstay.app.features.manager.ManagerPersonalInfoScreen
import com.kamerstay.app.features.manager.ManagerProfileScreen
import com.kamerstay.app.features.manager.ManagerSupportScreen
import com.kamerstay.app.features.manager.ManagerVerificationScreen
import com.kamerstay.app.features.manager.PaymentMethodsScreen
import com.kamerstay.app.features.manager.RegisterHotelScreen
import com.kamerstay.app.features.manager.ReservationDetailsScreen
import com.kamerstay.app.features.manager.ReservationsScreen
import com.kamerstay.app.features.manager.RevenueAnalyticsScreen
import com.kamerstay.app.features.manager.RevenueReportScreen
import com.kamerstay.app.features.manager.RoomManagementScreen
import com.kamerstay.app.features.manager.ManagerNotificationsScreen
import com.kamerstay.app.features.manager.ManagerOnboardingScreen
import com.kamerstay.app.features.manager.ManagerPrivacyTermsScreen
import com.kamerstay.app.features.manager.ManagerSettingsScreen
import com.kamerstay.app.features.manager.PromotionsScreen
import com.kamerstay.app.features.manager.StaffManagementScreen
import com.kamerstay.app.features.traveler.BookingCancellationScreen
import com.kamerstay.app.features.traveler.BookingConfirmationScreen
import com.kamerstay.app.features.traveler.BookingDetailsScreen
import com.kamerstay.app.features.traveler.BookingHistoryScreen
import com.kamerstay.app.features.traveler.BookingReviewScreen
import com.kamerstay.app.features.traveler.BookingScreen
import com.kamerstay.app.features.traveler.BookingVoucherScreen
import com.kamerstay.app.features.traveler.FilterScreen
import com.kamerstay.app.features.traveler.HelpCenterScreen
import com.kamerstay.app.features.traveler.HotelDetailsScreen
import com.kamerstay.app.features.traveler.HotelReviewsScreen
import com.kamerstay.app.features.traveler.HotelSearchScreen
import com.kamerstay.app.features.traveler.LocalTravelGuideScreen
import com.kamerstay.app.features.traveler.MapLocationWrapper
import com.kamerstay.app.features.traveler.NoResultScreen
import com.kamerstay.app.features.traveler.NotificationsScreen
import com.kamerstay.app.features.traveler.PaymentFailedScreen
import com.kamerstay.app.features.traveler.PaymentScreen
import com.kamerstay.app.features.traveler.PrivacyTermsScreen
import com.kamerstay.app.features.traveler.RefundTrackingScreen
import com.kamerstay.app.features.traveler.RoomDetailsScreen
import com.kamerstay.app.features.traveler.SettingsScreen
import com.kamerstay.app.features.traveler.TravelerHomeScreen
import com.kamerstay.app.features.traveler.TravelerPersonalInfoScreen
import com.kamerstay.app.features.traveler.TravelerProfileScreen
import com.kamerstay.app.features.traveler.TravelerSupportScreen
import com.kamerstay.app.features.traveler.TravelerPaymentMethodsScreen
import com.kamerstay.app.features.traveler.AIConciergeScreen
import com.kamerstay.app.features.traveler.WishlistScreen
import com.kamerstay.app.features.traveler.WriteReviewScreen

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
        composable(Routes.Onboarding.route) {
            OnboardingScreen(navController)
        }
        composable(Routes.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(Routes.RoleSelection.route) {
            RoleSelectionScreen(navController)
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
        composable(Routes.ChangePassword.route) {
            ChangePasswordScreen(navController)
        }

        // ── Traveler ──────────────────────────────────
        composable(Routes.TravelerHome.route) {
            TravelerHomeScreen(navController)
        }
        composable(Routes.AIConcierge.route) {
            AIConciergeScreen(navController)
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
        composable(Routes.MapLocation.route) {
            MapLocationWrapper(navController)
        }
        composable(Routes.AddEditStaff.route) {
            AddEditStaffScreen(navController, staffId = NavigationState.selectedStaffId)
        }
        composable(Routes.Booking.route) {
            BookingScreen(
                navController = navController,
                hotelId = NavigationState.selectedHotelId,
                roomId = NavigationState.selectedRoomId
            )
        }
        composable(Routes.HotelReviews.route) {
            HotelReviewsScreen(navController)
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
        composable(Routes.TravelerPersonalInfo.route) {
            TravelerPersonalInfoScreen(navController)
        }
        composable(Routes.BookingVoucher.route) {
            BookingVoucherScreen(navController)
        }
        composable(Routes.LocalGuide.route) {
            LocalTravelGuideScreen(navController)
        }
        composable(Routes.PrivacyTerms.route) {
            PrivacyTermsScreen(navController)
        }
        composable(Routes.Wishlist.route) {
            WishlistScreen(navController)
        }
        composable(Routes.NoResult.route) {
            NoResultScreen(navController)
        }
        composable(Routes.PaymentFailed.route) {
            PaymentFailedScreen(navController)
        }
        composable(Routes.TravelerSupport.route) {
            TravelerSupportScreen(navController)
        }
        composable(Routes.BookingReview.route) {
            BookingReviewScreen(navController)
        }

        // ── Manager ───────────────────────────────────
        composable(Routes.ManagerDashboard.route) {
            ManagerDashboardScreen(navController)
        }
        composable(Routes.RoomManagement.route) { entry ->
            val hotelId = entry.arguments?.getString("hotelId") ?: NavigationState.selectedHotelId
            RoomManagementScreen(navController, hotelId)
        }
        composable(Routes.AddEditRoom.route) { entry ->
            val hotelId = entry.arguments?.getString("hotelId") ?: NavigationState.selectedHotelId
            val roomId  = entry.arguments?.getString("roomId")?.takeIf { it.isNotBlank() }
            AddEditRoomScreen(navController, hotelId, roomId)
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
        composable(Routes.RegisterHotel.route) {
            RegisterHotelScreen(navController)
        }
        composable(Routes.ManageHotel.route) {
            ManageHotelScreen(navController)
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
        composable(Routes.PaymentMethods.route) {
            PaymentMethodsScreen(navController)
        }
        composable(Routes.ManagerProfile.route) {
            ManagerProfileScreen(navController)
        }
        composable(Routes.StaffManagement.route) {
            StaffManagementScreen(navController)
        }
        composable(Routes.RevenueAnalytics.route) {
            RevenueAnalyticsScreen(navController)
        }
        composable(Routes.ManagerVerification.route) {
            ManagerVerificationScreen(navController)
        }
        composable(Routes.ManagerPersonalInfo.route) {
            ManagerPersonalInfoScreen(navController)
        }
        composable(Routes.HotelAmenities.route) {
            HotelAmenitiesScreen(navController)
        }
        composable(Routes.RevenueReport.route) {
            RevenueReportScreen(navController)
        }
        composable(Routes.ManagerSupport.route) {
            ManagerSupportScreen(navController)
        }
        composable(Routes.WriteReview.route) {
            WriteReviewScreen(navController)
        }
        composable(Routes.ManagerSettings.route) {
            ManagerSettingsScreen(navController)
        }
        composable(Routes.ManagerNotifications.route) {
            ManagerNotificationsScreen(navController)
        }
        composable(Routes.Promotions.route) {
            PromotionsScreen(navController)
        }
        composable(Routes.AddEditPromotion.route) {
            AddEditPromotionScreen(navController)
        }
        composable(Routes.ManagerPrivacyTerms.route) {
            ManagerPrivacyTermsScreen(navController)
        }
        composable(Routes.HelpCenter.route) {
            HelpCenterScreen(navController)
        }
        composable(Routes.ManagerOnboarding.route) {
            ManagerOnboardingScreen(navController)
        }

        // ── Shared ────────────────────────────────────
        composable(Routes.Notifications.route) {
            NotificationsScreen(navController)
        }
        composable(Routes.Settings.route) {
            SettingsScreen(navController)
        }
        composable(Routes.TravelerPaymentMethods.route) {
            TravelerPaymentMethodsScreen(navController)
        }
        composable(Routes.BookingCancellation.route) {
            BookingCancellationScreen(
                navController = navController,
                bookingId = NavigationState.selectedBookingId
            )
        }
        composable(Routes.RefundTracking.route) {
            RefundTrackingScreen(
                navController = navController,
                bookingId = NavigationState.selectedBookingId
            )
        }
    }
}