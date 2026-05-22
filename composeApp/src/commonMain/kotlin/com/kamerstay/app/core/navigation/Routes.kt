package com.kamerstay.app.core.navigation

sealed class Routes(val route: String) {

    data object Splash : Routes("splash")
    data object Welcome : Routes("welcome")
    data object SignIn : Routes("sign_in")
    data object SignUp : Routes("sign_up")
    data object TravelerHome : Routes("traveler_home")
    data object HotelSearch : Routes("hotel_search")
    data object HotelDetails : Routes("hotel_details/{hotelId}") {
        fun createRoute(hotelId: String) = "hotel_details/$hotelId"
    }
    data object Filter : Routes("filter")

    data object ForgotPassword : Routes("forgot_password")
    data object VerificationCode : Routes("verification_code")
    data object ResetPassword : Routes("reset_password")

    data object RoomDetails : Routes("room_details/{roomId}") {
        fun createRoute(roomId: String) = "room_details/$roomId"
    }
    data object Booking : Routes("booking/{hotelId}/{roomId}") {
        fun createRoute(hotelId: String, roomId: String) = "booking/$hotelId/$roomId"
    }
    data object Payment : Routes("payment/{bookingId}") {
        fun createRoute(bookingId: String) = "payment/$bookingId"
    }
    data object BookingConfirmation : Routes("booking_confirmation/{bookingId}") {
        fun createRoute(bookingId: String) = "booking_confirmation/$bookingId"
    }
    data object BookingHistory : Routes("booking_history")
    data object BookingDetails : Routes("booking_details/{bookingId}") {
        fun createRoute(bookingId: String) = "booking_details/$bookingId"
    }
    data object TravelerProfile : Routes("traveler_profile")
    data object ManagerDashboard : Routes("manager_dashboard")
//    data object RegisterHotel : Routes("register_hotel")
//    data object ManageHotel : Routes("manage_hotel/{hotelId}") {
//        fun createRoute(hotelId: String) = "manage_hotel/$hotelId"
//    }
    data object RoomManagement : Routes("room_management/{hotelId}") {
        fun createRoute(hotelId: String) = "room_management/$hotelId"
    }
    data object AddEditRoom : Routes("add_edit_room/{hotelId}?roomId={roomId}") {
        fun createRoute(hotelId: String, roomId: String? = null) =
            "add_edit_room/$hotelId?roomId=${roomId ?: ""}"
    }
    data object Reservations : Routes("reservations")
    data object ReservationDetails : Routes("reservation_details/{reservationId}") {
        fun createRoute(reservationId: String) = "reservation_details/$reservationId"
    }
    data object CheckIn : Routes("check_in/{reservationId}") {
        fun createRoute(reservationId: String) = "check_in/$reservationId"
    }
    data object CheckOut : Routes("check_out/{reservationId}") {
        fun createRoute(reservationId: String) = "check_out/$reservationId"
    }
    data object ManagerProfile : Routes("manager_profile")
    data object Notifications : Routes("notifications")
//    data object MapLocation : Routes("map_location")
    data object Settings : Routes("settings")

    data object StaffManagement : Routes("staff_management")
    data object BookingVoucher : Routes("booking_voucher")
    data object LocalGuide : Routes("local_guide")
}