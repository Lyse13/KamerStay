package com.kamerstay.app.util

import com.kamerstay.app.model.Hotel
import com.kamerstay.app.model.User
import com.kamerstay.app.model.UserCredentials
import com.kamerstay.app.model.enums.UserRole
import com.kamerstay.app.repository.HotelRepository
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

    fun seedIfEmpty(hotelRepository: HotelRepository) = runBlocking {
        val count = hotelRepository.countHotels()
        if (count == 0L) {
            hotels.forEach { hotelRepository.insertHotel(it) }
            println("SeedData: ${hotels.size} hotels inseres dans MongoDB.")
        } else {
            println("SeedData: la collection hotels contient deja $count documents, seed ignore.")
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