package com.kamerstay.app.data.mock

import com.kamerstay.app.model.Hotel
import com.kamerstay.app.model.Landmark
import com.kamerstay.app.model.Room
import com.kamerstay.app.model.enums.RoomStatus
import com.kamerstay.app.model.enums.RoomType
import com.kamerstay.app.model.enums.LandmarkType

object MockData {

    val landmarks = listOf(
        Landmark(id = "1", name = "Near Hospital", type = LandmarkType.HOSPITAL.name, city = "Yaoundé"),
        Landmark(id = "2", name = "Near University", type = LandmarkType.UNIVERSITY.name, city = "Yaoundé"),
        Landmark(id = "3", name = "Near Stadium", type = LandmarkType.STADIUM.name, city = "Douala"),
        Landmark(id = "4", name = "Near Market", type = LandmarkType.MARKET.name, city = "Douala"),
        Landmark(id = "5", name = "Near Airport", type = LandmarkType.AIRPORT.name, city = "Yaoundé"),
        Landmark(id = "6", name = "Near Bus Station", type = LandmarkType.BUS_STATION.name, city = "Bafoussam"),
    )

    private fun u(id: String) = "https://images.unsplash.com/photo-$id?w=800&fit=crop&auto=format"

    val hotels = listOf(
        Hotel(
            id = "1",
            name = "Sawa Hôtel & SPA",
            description = "Hôtel 5 étoiles au cœur d'Akwa, Douala. Piscine à débordement, spa luxueux, restaurant gastronomique et vue imprenable sur le fleuve Wouri. L'adresse incontournable des hommes d'affaires et des voyageurs exigeants.",
            address = "Rue du General de Gaulle, Akwa, Douala",
            city = "Douala",
            pricePerNight = 110000.0,
            rating = 4.9,
            reviewCount = 312,
            isVerified = true,
            amenities = listOf("Wi-Fi Gratuit", "Piscine Infinie", "SPA", "Restaurant", "Bar", "GYM", "Parking"),
            imageUrls = listOf(
                u("1566073771259-6a8506099945"),
                u("1445019980597-93fa8acb246c"),
                u("1578774255626-30cc019a2e4e"),
                u("1571003123894-1f0594d2b5d9"),
                u("1414235077428-338989a2e8c0")
            ),
            nearbyLandmarks = listOf(landmarks[0]),
            availableRooms = 3,
            totalRooms = 80
        ),
        Hotel(
            id = "2",
            name = "Hilton Yaoundé",
            description = "Hôtel de prestige au centre de Yaoundé, à quelques pas du Palais de l'Unité et du Monument de la Réunification. Chambres spacieuses, piscine olympique, spa complet et 3 restaurants.",
            address = "Boulevard du 20 Mai, Centre-Ville, Yaoundé",
            city = "Yaoundé",
            pricePerNight = 130000.0,
            rating = 4.8,
            reviewCount = 489,
            isVerified = true,
            amenities = listOf("Wi-Fi Gratuit", "Piscine", "SPA", "Restaurant", "Bar", "GYM", "Business Center"),
            imageUrls = listOf(
                u("1564501049412-61c2a3083791"),
                u("1598928506311-c55ded91a20c"),
                u("1631049307264-da0ec9d70304"),
                u("1540518614846-7eded433c457"),
                u("1555396273-367ea4eb4db5")
            ),
            nearbyLandmarks = listOf(landmarks[1]),
            availableRooms = 5,
            totalRooms = 200
        ),
        Hotel(
            id = "3",
            name = "Atlantic Beach Resort",
            description = "Resort de luxe les pieds dans l'eau à Limbé. Plage privée de sable volcanique noir, bungalows avec vue sur l'océan Atlantique et le Mont Cameroun en toile de fond.",
            address = "Down Beach, Limbé",
            city = "Kribi",
            pricePerNight = 83000.0,
            rating = 4.7,
            reviewCount = 178,
            isVerified = true,
            amenities = listOf("Plage Privée", "Piscine", "Restaurant", "Bar", "Wi-Fi", "Parking"),
            imageUrls = listOf(
                u("1520250497591-112f2f40a3f4"),
                u("1540541338287-41700207dee6"),
                u("1507525428034-b723cf961d3e"),
                u("1544124124897-3b9becc7c0db"),
                u("1414235077428-338989a2e8c0")
            ),
            availableRooms = 4,
            totalRooms = 45
        ),
        Hotel(
            id = "4",
            name = "Mont Fébé Hôtel",
            description = "Joyau architectural niché sur les hauteurs de Yaoundé avec une vue à 360° sur la capitale. Immense piscine en terrasse, tennis, golf et restaurant panoramique.",
            address = "Colline du Mont Fébé, Yaoundé",
            city = "Buea",
            pricePerNight = 89000.0,
            rating = 4.6,
            reviewCount = 234,
            isVerified = true,
            amenities = listOf("Vue Panoramique", "Piscine", "Tennis", "Golf", "Restaurant", "Wi-Fi"),
            imageUrls = listOf(
                u("1510798831971-661eb04b3739"),
                u("1441974231531-c6227db76b6e"),
                u("1571896349842-33c89424de2d"),
                u("1571003123894-1f0594d2b5d9"),
                u("1555396273-367ea4eb4db5")
            ),
            nearbyLandmarks = listOf(landmarks[2]),
            availableRooms = 8,
            totalRooms = 120
        ),
        Hotel(
            id = "5",
            name = "Pullman Hôtel Douala",
            description = "Hôtel d'affaires international au cœur d'Akwa Nord. Chambres ultra-modernes, piscine en rooftop, spa et centre de fitness. Le meilleur pour les voyages professionnels.",
            address = "Avenue du Général de Gaulle, Akwa Nord, Douala",
            city = "Yaoundé",
            pricePerNight = 135000.0,
            rating = 4.7,
            reviewCount = 267,
            isVerified = true,
            amenities = listOf("Wi-Fi Gratuit", "Piscine Rooftop", "SPA", "GYM", "Restaurant", "Bar"),
            imageUrls = listOf(
                u("1551882547-ff40c63fe5fa"),
                u("1542314831-068cd1dbfeeb"),
                u("1578774255626-30cc019a2e4e"),
                u("1516450360452-9312f5e86fc7"),
                u("1414235077428-338989a2e8c0")
            ),
            availableRooms = 2,
            totalRooms = 150
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