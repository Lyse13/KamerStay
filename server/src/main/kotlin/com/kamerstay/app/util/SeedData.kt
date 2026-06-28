package com.kamerstay.app.util

import com.kamerstay.app.model.Hotel
import com.kamerstay.app.model.Room
import com.kamerstay.app.model.User
import com.kamerstay.app.model.UserCredentials
import com.kamerstay.app.model.enums.RoomStatus
import com.kamerstay.app.model.enums.RoomType
import com.kamerstay.app.model.enums.UserRole
import com.kamerstay.app.repository.HotelRepository
import com.kamerstay.app.repository.RoomRepository
import com.kamerstay.app.repository.UserRepository
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.util.UUID

object SeedData {

    private fun image(id: String) = "https://images.unsplash.com/photo-$id?w=800&fit=crop&auto=format"

    private val hotels = listOf(
        Hotel(
            id = "1",
            name = "Sawa Hôtel & SPA",
            description = "Hôtel 5 étoiles au cœur d'Akwa, Douala. Piscine à débordement, spa luxueux, restaurant gastronomique et vue imprenable sur le fleuve Wouri.",
            address = "Rue du General de Gaulle, Akwa, Douala",
            city = "Douala",
            pricePerNight = 110000.0,
            rating = 4.9,
            reviewCount = 312,
            isVerified = true,
            amenities = listOf("Wi-Fi Gratuit", "Piscine Infinie", "SPA", "Restaurant", "Bar", "GYM", "Parking"),
            imageUrls = listOf(
                image("1566073771259-6a8506099945"),
                image("1445019980597-93fa8acb246c"),
                image("1571896349842-33c89424de2d")
            ),
            availableRooms = 3,
            totalRooms = 80
        ),
        Hotel(
            id = "2",
            name = "Hilton Yaoundé",
            description = "Hôtel de prestige au centre de Yaoundé, à quelques pas du Palais de l'Unité. Chambres spacieuses, piscine olympique, spa complet et 3 restaurants.",
            address = "Boulevard du 20 Mai, Centre-Ville, Yaoundé",
            city = "Yaoundé",
            pricePerNight = 130000.0,
            rating = 4.8,
            reviewCount = 489,
            isVerified = true,
            amenities = listOf("Wi-Fi Gratuit", "Piscine", "SPA", "Restaurant", "Bar", "GYM", "Business Center"),
            imageUrls = listOf(
                image("1564501049412-61c2a3083791"),
                image("1598928506311-c55ded91a20c")
            ),
            availableRooms = 5,
            totalRooms = 200
        ),
        Hotel(
            id = "3",
            name = "Atlantic Beach Resort",
            description = "Resort de luxe les pieds dans l'eau à Limbé. Plage privée de sable volcanique noir et bungalows avec vue sur l'océan Atlantique.",
            address = "Down Beach, Limbé",
            city = "Kribi",
            pricePerNight = 83000.0,
            rating = 4.7,
            reviewCount = 178,
            isVerified = true,
            amenities = listOf("Plage Privée", "Piscine", "Restaurant", "Bar", "Wi-Fi", "Parking"),
            imageUrls = listOf(
                image("1520250497591-112f2f40a3f4"),
                image("1540541338287-41700207dee6")
            ),
            availableRooms = 4,
            totalRooms = 45
        ),
        Hotel(
            id = "4",
            name = "Mont Fébé Hôtel",
            description = "Joyau architectural niché sur les hauteurs de Yaoundé avec une vue à 360° sur la capitale. Immense piscine en terrasse, tennis et golf.",
            address = "Colline du Mont Fébé, Yaoundé",
            city = "Buea",
            pricePerNight = 89000.0,
            rating = 4.6,
            reviewCount = 234,
            isVerified = true,
            amenities = listOf("Vue Panoramique", "Piscine", "Tennis", "Golf", "Restaurant", "Wi-Fi"),
            imageUrls = listOf(
                image("1510798831971-661eb04b3739"),
                image("1441974231531-c6227db76b6e")
            ),
            availableRooms = 8,
            totalRooms = 120
        ),
        Hotel(
            id = "5",
            name = "Pullman Hôtel Douala",
            description = "Hôtel d'affaires international au cœur d'Akwa Nord. Chambres ultra-modernes, piscine en rooftop, spa et centre de fitness.",
            address = "Avenue du Général de Gaulle, Akwa Nord, Douala",
            city = "Yaoundé",
            pricePerNight = 135000.0,
            rating = 4.7,
            reviewCount = 267,
            isVerified = true,
            amenities = listOf("Wi-Fi Gratuit", "Piscine Rooftop", "SPA", "GYM", "Restaurant", "Bar"),
            imageUrls = listOf(
                image("1551882547-ff40c63fe5fa"),
                image("1542314831-068cd1dbfeeb")
            ),
            availableRooms = 2,
            totalRooms = 150
        )
    )

    private val rooms = listOf(
        // Sawa Hôtel & SPA (hotelId = "1")
        Room(id = "r1_1", hotelId = "1", roomNumber = "101", type = RoomType.DELUXE, status = RoomStatus.AVAILABLE,
            pricePerNight = 110000.0, capacity = 2, floor = 1, size = 35.0,
            description = "Chambre Deluxe avec vue sur le fleuve Wouri, lit king-size et salle de bain en marbre.",
            imageUrls = listOf(image("1578774255626-30cc019a2e4e"), image("1571003123894-1f0594d2b5d9")),
            features = listOf("Climatisation", "TV écran plat", "Mini-bar", "Coffre-fort", "Balcon")),
        Room(id = "r1_2", hotelId = "1", roomNumber = "201", type = RoomType.SUITE, status = RoomStatus.AVAILABLE,
            pricePerNight = 185000.0, capacity = 3, floor = 2, size = 65.0,
            description = "Suite présidentielle avec salon séparé, baignoire balnéo et vue panoramique sur Douala.",
            imageUrls = listOf(image("1445019980597-93fa8acb246c")),
            features = listOf("Climatisation", "TV écran plat", "Mini-bar", "Coffre-fort", "Balcon", "Baignoire", "Bureau de travail")),
        Room(id = "r1_3", hotelId = "1", roomNumber = "102", type = RoomType.SINGLE, status = RoomStatus.AVAILABLE,
            pricePerNight = 75000.0, capacity = 2, floor = 1, size = 25.0,
            description = "Chambre Standard confortable avec toutes les commodités modernes.",
            imageUrls = listOf(image("1414235077428-338989a2e8c0")),
            features = listOf("Climatisation", "TV écran plat", "Wi-Fi")),

        // Hilton Yaoundé (hotelId = "2")
        Room(id = "r2_1", hotelId = "2", roomNumber = "301", type = RoomType.DELUXE, status = RoomStatus.AVAILABLE,
            pricePerNight = 130000.0, capacity = 2, floor = 3, size = 40.0,
            description = "Chambre Deluxe avec vue sur le Palais de l'Unité, mobilier haut de gamme.",
            imageUrls = listOf(image("1564501049412-61c2a3083791")),
            features = listOf("Climatisation", "TV écran plat", "Mini-bar", "Coffre-fort", "Vue sur jardin")),
        Room(id = "r2_2", hotelId = "2", roomNumber = "501", type = RoomType.SUITE, status = RoomStatus.AVAILABLE,
            pricePerNight = 220000.0, capacity = 4, floor = 5, size = 80.0,
            description = "Suite Executive avec bureau de travail, salle de conférence privée et terrasse.",
            imageUrls = listOf(image("1598928506311-c55ded91a20c")),
            features = listOf("Climatisation", "TV écran plat", "Mini-bar", "Coffre-fort", "Balcon", "Bureau de travail")),
        Room(id = "r2_3", hotelId = "2", roomNumber = "201", type = RoomType.SINGLE, status = RoomStatus.AVAILABLE,
            pricePerNight = 90000.0, capacity = 2, floor = 2, size = 28.0,
            description = "Chambre Standard Hilton avec le confort classique de la chaîne.",
            imageUrls = listOf(image("1564501049412-61c2a3083791")),
            features = listOf("Climatisation", "TV écran plat", "Wi-Fi")),

        // Atlantic Beach Resort (hotelId = "3")
        Room(id = "r3_1", hotelId = "3", roomNumber = "B01", type = RoomType.DOUBLE, status = RoomStatus.AVAILABLE,
            pricePerNight = 83000.0, capacity = 2, floor = 1, size = 30.0,
            description = "Bungalow vue océan avec accès direct à la plage de sable volcanique.",
            imageUrls = listOf(image("1520250497591-112f2f40a3f4")),
            features = listOf("Climatisation", "TV écran plat", "Balcon", "Vue sur jardin")),
        Room(id = "r3_2", hotelId = "3", roomNumber = "B02", type = RoomType.SUITE, status = RoomStatus.AVAILABLE,
            pricePerNight = 145000.0, capacity = 3, floor = 1, size = 55.0,
            description = "Suite bungalow premium avec piscine privée et terrasse face à l'Atlantique.",
            imageUrls = listOf(image("1540541338287-41700207dee6")),
            features = listOf("Climatisation", "TV écran plat", "Baignoire", "Balcon")),

        // Mont Fébé Hôtel (hotelId = "4")
        Room(id = "r4_1", hotelId = "4", roomNumber = "401", type = RoomType.DELUXE, status = RoomStatus.AVAILABLE,
            pricePerNight = 89000.0, capacity = 2, floor = 4, size = 38.0,
            description = "Chambre Deluxe avec vue à 360° sur Yaoundé et ses collines verdoyantes.",
            imageUrls = listOf(image("1510798831971-661eb04b3739")),
            features = listOf("Climatisation", "TV écran plat", "Mini-bar", "Balcon")),
        Room(id = "r4_2", hotelId = "4", roomNumber = "101", type = RoomType.SINGLE, status = RoomStatus.AVAILABLE,
            pricePerNight = 60000.0, capacity = 2, floor = 1, size = 24.0,
            description = "Chambre Standard avec jardin privatif dans le parc du Mont Fébé.",
            imageUrls = listOf(image("1441974231531-c6227db76b6e")),
            features = listOf("Climatisation", "TV écran plat", "Wi-Fi")),

        // Pullman Hôtel Douala (hotelId = "5")
        Room(id = "r5_1", hotelId = "5", roomNumber = "901", type = RoomType.DELUXE, status = RoomStatus.AVAILABLE,
            pricePerNight = 135000.0, capacity = 2, floor = 9, size = 42.0,
            description = "Chambre Deluxe avec accès à la piscine rooftop et vue sur Douala by night.",
            imageUrls = listOf(image("1551882547-ff40c63fe5fa")),
            features = listOf("Climatisation", "TV écran plat", "Mini-bar", "Coffre-fort", "Balcon")),
        Room(id = "r5_2", hotelId = "5", roomNumber = "1001", type = RoomType.SUITE, status = RoomStatus.AVAILABLE,
            pricePerNight = 230000.0, capacity = 4, floor = 10, size = 90.0,
            description = "Suite Pullman au sommet de l'hôtel avec terrasse privée et service butler 24h.",
            imageUrls = listOf(image("1542314831-068cd1dbfeeb")),
            features = listOf("Climatisation", "TV écran plat", "Mini-bar", "Coffre-fort", "Balcon", "Baignoire", "Bureau de travail"))
    )

    fun seedIfEmpty(hotelRepository: HotelRepository) = runBlocking {
        val count = hotelRepository.countHotels()
        if (count == 0L) {
            hotels.forEach { hotelRepository.insertHotel(it) }
            println("SeedData: ${hotels.size} hotels inseres dans MongoDB.")
        } else {
            println("SeedData: la collection hotels contient deja $count documents, seed ignore.")
        }
    }

    fun seedRoomsIfEmpty(roomRepository: RoomRepository) = runBlocking {
        val total = rooms.sumOf { room -> roomRepository.countRoomsForHotel(room.hotelId) }
        if (total == 0L) {
            rooms.forEach { roomRepository.insertRoom(it) }
            println("SeedData: ${rooms.size} chambres inserees dans MongoDB.")
        } else {
            println("SeedData: chambres deja presentes, seed ignore.")
        }
    }

    fun seedAdminIfNotExists(userRepository: UserRepository) = runBlocking {
        val adminEmail = "lysettemouandeu@gmail.com"
        if (!userRepository.emailExists(adminEmail)) {
            val adminId = UUID.randomUUID().toString()
            val admin = User(
                id = adminId,
                fullName = "Lysette Mouandeu",
                email = adminEmail,
                phoneNumber = "657064991",
                role = UserRole.HOTEL_MANAGER,
                createdAt = Instant.now().toString(),
                isActive = true
            )
            val passwordHash = PasswordHasher.hash("Lysette@21")
            userRepository.createUser(admin, passwordHash)
            println("SeedData: Compte admin cree -> $adminEmail")
        } else {
            println("SeedData: Compte admin deja existant, seed ignore.")
        }
    }
}