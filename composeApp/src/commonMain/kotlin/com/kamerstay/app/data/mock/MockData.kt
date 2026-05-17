package com.kamerstay.app.data.mock

import com.kamerstay.app.model.Hotel
import com.kamerstay.app.model.Landmark
import com.kamerstay.app.model.Room
import com.kamerstay.app.model.enums.RoomStatus
import com.kamerstay.app.model.enums.RoomType
import com.kamerstay.app.model.enums.LandmarkType

object MockData {

    val landmarks = listOf(
        Landmark(id = "1", name = "Near Hospital", type = LandmarkType.HOSPITAL, city = "Yaoundé"),
        Landmark(id = "2", name = "Near University", type = LandmarkType.UNIVERSITY, city = "Yaoundé"),
        Landmark(id = "3", name = "Near Stadium", type = LandmarkType.STADIUM, city = "Douala"),
        Landmark(id = "4", name = "Near Market", type = LandmarkType.MARKET, city = "Douala"),
        Landmark(id = "5", name = "Near Airport", type = LandmarkType.AIRPORT, city = "Yaoundé"),
        Landmark(id = "6", name = "Near Bus Station", type = LandmarkType.BUS_STATION, city = "Bafoussam"),
    )

    val hotels = listOf(
        Hotel(
            id = "1",
            name = "The Douala Zenith",
            description = "Hôtel de luxe au cœur d'Akwa",
            address = "Akwa, Douala",
            city = "Douala",
            pricePerNight = 75000.0,
            rating = 4.9,
            reviewCount = 128,
            isVerified = true,
            amenities = listOf("WiFi", "Piscine", "Restaurant", "Parking"),
            imageUrls = listOf("https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800"),
            nearbyLandmarks = listOf(landmarks[0]),
            availableRooms = 5,
            totalRooms = 20
        ),

        Hotel(
            id = "2",
            name = "Hilton Retreat",
            description = "Près du Monument de la Réunification",
            address = "Near Reunification Monument, Yaoundé",
            city = "Yaoundé",
            pricePerNight = 55000.0,
            rating = 4.5,
            reviewCount = 89,
            isVerified = true,
            amenities = listOf("WiFi", "Restaurant", "Bar"),
            imageUrls = listOf("https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=800"),
            nearbyLandmarks = listOf(landmarks[1]),
            availableRooms = 8,
            totalRooms = 30
        ),

        Hotel(
            id = "3",
            name = "Coastal Breeze Kribi",
            description = "Vue sur l'océan, terrasse panoramique",
            address = "Oceanfront Terrace, Kribi",
            city = "Kribi",
            pricePerNight = 90000.0,
            rating = 4.7,
            reviewCount = 64,
            isVerified = true,
            amenities = listOf("WiFi", "Piscine", "Bar", "Parking"),
            imageUrls = listOf("https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800"),
            availableRooms = 3,
            totalRooms = 15
        ),
        Hotel(
            id = "4",
            name = "Buea Heights",
            description = "Face au Mont Cameroun",
            address = "Mount Cameroon, Buea",
            city = "Buea",
            pricePerNight = 45000.0,
            rating = 4.3,
            reviewCount = 42,
            isVerified = true,
            amenities = listOf("WiFi", "Restaurant"),
            imageUrls = listOf("https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=800"),
            availableRooms = 10,
            totalRooms = 25
        ),
        Hotel(
            id = "5",
            name = "L'Avenue Hotel",
            description = "Près du Marché Central",
            address = "Near Central Market, Yaoundé",
            city = "Yaoundé",
            pricePerNight = 62000.0,
            rating = 4.6,
            reviewCount = 95,
            isVerified = true,
            amenities = listOf("WiFi", "Parking", "Restaurant"),
            imageUrls = listOf("https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=800"),
            availableRooms = 6,
            totalRooms = 18
        ),
    )

    val rooms = listOf(
        Room(
            id = "r1", hotelId = "1",
            roomNumber = "101",
            type = RoomType.SUITE,
            status = RoomStatus.AVAILABLE,
            pricePerNight = 75000.0,
            capacity = 2,
            description = "King Bed • City View",
            features = listOf("King Bed", "City View", "Climatisation", "Mini-bar"),
            floor = 1
        ),
        Room(
            id = "r2", hotelId = "1",
            roomNumber = "102",
            type = RoomType.TWIN,
            status = RoomStatus.AVAILABLE,
            pricePerNight = 55000.0,
            capacity = 2,
            description = "2 Single Beds • Balcon",
            features = listOf("2 Single Beds", "Balcon", "Climatisation"),
            floor = 1
        ),
        Room(
            id = "r3", hotelId = "2",
            roomNumber = "201",
            type = RoomType.DOUBLE,
            status = RoomStatus.AVAILABLE,
            pricePerNight = 45000.0,
            capacity = 2,
            description = "Double Bed • Garden View",
            features = listOf("Double Bed", "Garden View", "WiFi"),
            floor = 2
        ),
    )

    val recommendedHotels = hotels.sortedByDescending { it.rating }
    val popularHotels = hotels.sortedByDescending { it.reviewCount }

    fun getRoomsForHotel(hotelId: String) = rooms.filter { it.hotelId == hotelId }
    fun getHotelById(id: String) = hotels.find { it.id == id }

}