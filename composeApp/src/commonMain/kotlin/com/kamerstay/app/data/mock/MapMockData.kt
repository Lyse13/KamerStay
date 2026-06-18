package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.CameroonLandmark
import com.kamerstay.app.data.model.LandmarkType
import com.kamerstay.app.data.model.MapCity
import com.kamerstay.app.data.model.MapHotel

object MapMockData {

    val cities = linkedMapOf(
        "douala"     to MapCity("douala",     "Douala",      4.0511,  9.7679,  15.0),
        "yaounde"    to MapCity("yaounde",    "Yaoundé",     3.8612,  11.5213, 15.0),
        "bafoussam"  to MapCity("bafoussam",  "Bafoussam",   5.4777,  10.4173, 15.5),
        "limbe"      to MapCity("limbe",      "Limbé",       4.0160,  9.2000,  15.5),
        "kribi"      to MapCity("kribi",      "Kribi",       2.9388,  9.9080,  15.5),
        "bamenda"    to MapCity("bamenda",    "Bamenda",     5.9527,  10.1583, 15.5),
        "buea"       to MapCity("buea",       "Buea",        4.1527,  9.2421,  15.5),
        "ngaoundere" to MapCity("ngaoundere", "Ngaoundéré",  7.3256,  13.5835, 15.5)
    )

    val DOUALA_LAT get() = cities["douala"]!!.lat
    val DOUALA_LNG get() = cities["douala"]!!.lng

    // ── Repères camerounais avec coordonnées GPS réelles ────────────────────
    val landmarks = listOf(

        // ── Douala ──────────────────────────────────────────────────────────
        CameroonLandmark("dl_mkt_central",  "Marché Central",         "à côté du marché central",         4.0511, 9.7023, LandmarkType.MARKET,     "douala"),
        CameroonLandmark("dl_mkt_sandaga",  "Marché Sandaga",         "près du marché Sandaga",            4.0496, 9.7142, LandmarkType.MARKET,     "douala"),
        CameroonLandmark("dl_mkt_nkoul",    "Marché Nkoulouloun",     "à côté du marché Nkoulouloun",     4.0452, 9.7680, LandmarkType.MARKET,     "douala"),
        CameroonLandmark("dl_stade_reun",   "Stade de la Réunification", "près du stade de la Réunification", 4.0583, 9.7044, LandmarkType.STADIUM,  "douala"),
        CameroonLandmark("dl_airport",      "Aéroport de Douala",     "près de l'aéroport de Douala",     4.0079, 9.7177, LandmarkType.AIRPORT,    "douala"),
        CameroonLandmark("dl_akwa",         "Quartier Akwa",          "en plein cœur d'Akwa",             4.0510, 9.7080, LandmarkType.QUARTIER,   "douala"),
        CameroonLandmark("dl_bonapriso",    "Quartier Bonapriso",     "dans le quartier Bonapriso",       4.0493, 9.7549, LandmarkType.QUARTIER,   "douala"),
        CameroonLandmark("dl_bonanjo",      "Quartier Bonanjo",       "dans le quartier Bonanjo",         4.0416, 9.7167, LandmarkType.QUARTIER,   "douala"),
        CameroonLandmark("dl_palais",       "Palais des Congrès",     "près du palais des congrès",       4.0467, 9.7094, LandmarkType.MONUMENT,   "douala"),
        CameroonLandmark("dl_port",         "Port de Douala",         "à côté du port de Douala",         4.0400, 9.7200, LandmarkType.PORT,       "douala"),

        // ── Yaoundé ─────────────────────────────────────────────────────────
        CameroonLandmark("yde_mkt_mfoundi", "Marché Mfoundi",         "à côté du marché Mfoundi",         3.8635, 11.5177, LandmarkType.MARKET,    "yaounde"),
        CameroonLandmark("yde_mkt_mokolo",  "Marché Mokolo",          "près du marché Mokolo",            3.8756, 11.5094, LandmarkType.MARKET,    "yaounde"),
        CameroonLandmark("yde_mkt_etoudi",  "Marché Etoudi",          "à côté du marché Etoudi",          3.8834, 11.5083, LandmarkType.MARKET,    "yaounde"),
        CameroonLandmark("yde_mkt_nkol",    "Marché Nkolbong",        "près du marché Nkolbong",          3.8567, 11.5398, LandmarkType.MARKET,    "yaounde"),
        CameroonLandmark("yde_stade_olembe","Stade Olembe",           "à côté du stade Olembe",           3.9091, 11.4812, LandmarkType.STADIUM,   "yaounde"),
        CameroonLandmark("yde_stade_ahidjo","Stade Ahmadou Ahidjo",   "près du stade Ahmadou Ahidjo",     3.8650, 11.5178, LandmarkType.STADIUM,   "yaounde"),
        CameroonLandmark("yde_airport",     "Aéroport Nsimalen",      "près de l'aéroport Nsimalen",      3.7228, 11.5530, LandmarkType.AIRPORT,   "yaounde"),
        CameroonLandmark("yde_mairie_tsinga","Mairie de Tsinga",      "près de la mairie de Tsinga",      3.8816, 11.5018, LandmarkType.MAIRIE,    "yaounde"),
        CameroonLandmark("yde_mairie_1",    "Mairie de Yaoundé 1",    "à côté de la mairie de Yaoundé 1", 3.8699, 11.5215, LandmarkType.MAIRIE,   "yaounde"),
        CameroonLandmark("yde_mairie_6",    "Mairie de Yaoundé 6",    "près de la mairie de Biyem-Assi",  3.8481, 11.4915, LandmarkType.MAIRIE,   "yaounde"),
        CameroonLandmark("yde_uy1",         "Université YDE I",       "à côté de l'université de Yaoundé I", 3.8688, 11.5082, LandmarkType.UNIVERSITY, "yaounde"),
        CameroonLandmark("yde_hopital",     "Hôpital Central",        "près de l'hôpital central",        3.8669, 11.5182, LandmarkType.HOSPITAL,  "yaounde"),
        CameroonLandmark("yde_palais_unite","Palais de l'Unité",      "près du palais de l'Unité",        3.8844, 11.5153, LandmarkType.MONUMENT,  "yaounde"),
        CameroonLandmark("yde_monument",    "Monument Réunification", "près du monument de la Réunification", 3.8645, 11.5160, LandmarkType.MONUMENT, "yaounde"),
        CameroonLandmark("yde_bastos",      "Quartier Bastos",        "dans le quartier Bastos",          3.8750, 11.5200, LandmarkType.QUARTIER,  "yaounde"),
        CameroonLandmark("yde_nlongkak",    "Carrefour Nlongkak",     "au carrefour Nlongkak",            3.8780, 11.5150, LandmarkType.QUARTIER,  "yaounde"),
        CameroonLandmark("yde_melen",       "Quartier Melen",         "dans le quartier Melen",           3.8682, 11.5300, LandmarkType.QUARTIER,  "yaounde"),

        // ── Limbé ───────────────────────────────────────────────────────────
        CameroonLandmark("lmb_down_beach",  "Down Beach",             "à côté de Down Beach",             4.0200, 9.2100, LandmarkType.QUARTIER,   "limbe"),
        CameroonLandmark("lmb_botanique",   "Jardin Botanique",       "près du jardin botanique",         4.0139, 9.1989, LandmarkType.MONUMENT,   "limbe"),
        CameroonLandmark("lmb_centre",      "Centre-ville Limbé",     "au centre-ville de Limbé",         4.0160, 9.2000, LandmarkType.QUARTIER,   "limbe"),

        // ── Kribi ───────────────────────────────────────────────────────────
        CameroonLandmark("krb_plage",       "Plage de Kribi",         "à côté de la plage de Kribi",      2.9400, 9.9100, LandmarkType.QUARTIER,   "kribi"),
        CameroonLandmark("krb_lobe",        "Chutes de la Lobé",      "près des chutes de la Lobé",       2.8960, 9.8901, LandmarkType.MONUMENT,   "kribi"),
        CameroonLandmark("krb_port",        "Port de Kribi",          "à côté du port de Kribi",          2.9388, 9.9075, LandmarkType.PORT,       "kribi"),

        // ── Bafoussam ───────────────────────────────────────────────────────
        CameroonLandmark("bf_mkt_a",        "Marché A",               "à côté du marché A",               5.4787, 10.4201, LandmarkType.MARKET,    "bafoussam"),
        CameroonLandmark("bf_centre",       "Centre-ville",           "au centre-ville de Bafoussam",     5.4777, 10.4173, LandmarkType.QUARTIER,  "bafoussam"),

        // ── Bamenda ─────────────────────────────────────────────────────────
        CameroonLandmark("bda_commercial",  "Commercial Avenue",      "sur la Commercial Avenue",         5.9527, 10.1583, LandmarkType.QUARTIER,  "bamenda"),
        CameroonLandmark("bda_up_station",  "Up Station",             "dans le quartier Up Station",      5.9610, 10.1720, LandmarkType.QUARTIER,  "bamenda"),

        // ── Buea ────────────────────────────────────────────────────────────
        CameroonLandmark("bu_ub",           "Université de Buea",     "à côté de l'université de Buea",  4.1530, 9.2409, LandmarkType.UNIVERSITY, "buea"),
        CameroonLandmark("bu_molyko",       "Molyko",                 "dans le quartier Molyko",          4.1540, 9.2430, LandmarkType.QUARTIER,   "buea"),

        // ── Ngaoundéré ──────────────────────────────────────────────────────
        CameroonLandmark("ng_gare",         "Gare de Ngaoundéré",     "près de la gare de Ngaoundéré",   7.3210, 13.5835, LandmarkType.GARE,      "ngaoundere"),
        CameroonLandmark("ng_lamido",       "Palais du Lamido",       "près du palais du Lamido",         7.3256, 13.5820, LandmarkType.MONUMENT,  "ngaoundere")
    )

    fun landmarksForCity(city: String) = landmarks.filter { it.city == city }

    // ── Hôtels avec repères associés ────────────────────────────────────────
    val hotels = listOf(

        // ── Douala ──────────────────────────────────────────────────────────
        MapHotel(
            id = "d1", city = "douala",
            name = "Sawa Hôtel & SPA",
            priceXaf = 110_000, rating = 4.8, distance = "0.5 km", location = "Akwa",
            amenities = listOf("WI-FI", "PISCINE", "SPA"),
            lat = 4.0530, lng = 9.7425,
            imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=400&h=250&fit=crop&auto=format",
            availableRooms = 3,
            nearbyLandmarks = listOf("dl_akwa", "dl_mkt_sandaga", "dl_palais")
        ),
        MapHotel(
            id = "d2", city = "douala",
            name = "Ibis Styles Douala",
            priceXaf = 71_000, rating = 4.3, distance = "1.2 km", location = "Akwa",
            amenities = listOf("WI-FI", "BAR", "RESTO"),
            lat = 4.0469, lng = 9.7699,
            imageUrl = "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=400&h=250&fit=crop&auto=format",
            availableRooms = 8,
            nearbyLandmarks = listOf("dl_akwa", "dl_mkt_nkoul", "dl_bonanjo")
        ),
        MapHotel(
            id = "d3", city = "douala",
            name = "Hôtel La Falaise",
            priceXaf = 56_000, rating = 4.0, distance = "1.8 km", location = "Bonapriso",
            amenities = listOf("WI-FI", "RESTAURANT", "PARKING"),
            lat = 4.0553, lng = 9.7613,
            imageUrl = "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=400&h=250&fit=crop&auto=format",
            availableRooms = 5,
            nearbyLandmarks = listOf("dl_bonapriso", "dl_mkt_nkoul")
        ),
        MapHotel(
            id = "d4", city = "douala",
            name = "Résidence du Wouri",
            priceXaf = 42_000, rating = 3.9, distance = "2.3 km", location = "Bonanjo",
            amenities = listOf("WI-FI", "CLIM", "PARKING"),
            lat = 4.0400, lng = 9.7500,
            imageUrl = "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=400&h=250&fit=crop&auto=format",
            availableRooms = 12,
            nearbyLandmarks = listOf("dl_bonanjo", "dl_port", "dl_palais")
        ),
        MapHotel(
            id = "d5", city = "douala",
            name = "Prince de Galles Douala",
            priceXaf = 65_000, rating = 4.2, distance = "1.5 km", location = "Bonapriso",
            amenities = listOf("WI-FI", "GYM", "BAR"),
            lat = 4.0450, lng = 9.7580,
            imageUrl = "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=400&h=250&fit=crop&auto=format",
            availableRooms = 6,
            nearbyLandmarks = listOf("dl_bonapriso", "dl_mkt_central")
        ),
        MapHotel(
            id = "d6", city = "douala",
            name = "Mercure Hôtel Douala",
            priceXaf = 95_000, rating = 4.5, distance = "3.0 km", location = "Deido",
            amenities = listOf("WI-FI", "PISCINE", "GYM"),
            lat = 4.0600, lng = 9.7750,
            imageUrl = "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=400&h=250&fit=crop&auto=format",
            availableRooms = 4,
            nearbyLandmarks = listOf("dl_mkt_nkoul")
        ),
        MapHotel(
            id = "d7", city = "douala",
            name = "Pullman Hôtel Douala",
            priceXaf = 135_000, rating = 4.7, distance = "2.0 km", location = "Akwa Nord",
            amenities = listOf("WI-FI", "PISCINE", "SPA", "GYM"),
            lat = 4.0580, lng = 9.7460,
            imageUrl = "https://images.unsplash.com/photo-1578774255626-30cc019a2e4e?w=400&h=250&fit=crop&auto=format",
            availableRooms = 2,
            nearbyLandmarks = listOf("dl_akwa", "dl_stade_reun", "dl_palais")
        ),
        MapHotel(
            id = "d8", city = "douala",
            name = "Best Western Plus Douala",
            priceXaf = 78_000, rating = 4.3, distance = "1.8 km", location = "Akwa",
            amenities = listOf("WI-FI", "BAR", "GROUPE ÉLEC"),
            lat = 4.0495, lng = 9.7520,
            imageUrl = "https://images.unsplash.com/photo-1445019980597-93fa8acb246c?w=400&h=250&fit=crop&auto=format",
            availableRooms = 7,
            nearbyLandmarks = listOf("dl_akwa", "dl_mkt_sandaga")
        ),

        // ── Yaoundé ─────────────────────────────────────────────────────────
        MapHotel(
            id = "y1", city = "yaounde",
            name = "Hilton Yaoundé",
            priceXaf = 130_000, rating = 4.9, distance = "0.3 km", location = "Centre-Ville",
            amenities = listOf("WI-FI", "PISCINE", "SPA", "GYM"),
            lat = 3.8620, lng = 11.5200,
            imageUrl = "https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=400&h=250&fit=crop&auto=format",
            availableRooms = 3,
            nearbyLandmarks = listOf("yde_mairie_1", "yde_stade_ahidjo", "yde_hopital", "yde_monument")
        ),
        MapHotel(
            id = "y2", city = "yaounde",
            name = "Mont Fébé Hôtel",
            priceXaf = 89_000, rating = 4.6, distance = "4.5 km", location = "Mont Fébé",
            amenities = listOf("WI-FI", "PISCINE", "TENNIS"),
            lat = 3.8786, lng = 11.4909,
            imageUrl = "https://images.unsplash.com/photo-1510798831971-661eb04b3739?w=400&h=250&fit=crop&auto=format",
            availableRooms = 6,
            nearbyLandmarks = listOf("yde_stade_olembe", "yde_mairie_tsinga")
        ),
        MapHotel(
            id = "y3", city = "yaounde",
            name = "Hôtel Meumi Palace",
            priceXaf = 77_000, rating = 4.4, distance = "1.2 km", location = "Bastos",
            amenities = listOf("WI-FI", "RESTAURANT", "BAR"),
            lat = 3.8684, lng = 11.5190,
            imageUrl = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=400&h=250&fit=crop&auto=format",
            availableRooms = 9,
            nearbyLandmarks = listOf("yde_bastos", "yde_nlongkak", "yde_palais_unite")
        ),
        MapHotel(
            id = "y4", city = "yaounde",
            name = "Hôtel des Parlementaires",
            priceXaf = 48_000, rating = 4.0, distance = "2.1 km", location = "Ngousso",
            amenities = listOf("WI-FI", "PARKING", "CLIM"),
            lat = 3.8650, lng = 11.5050,
            imageUrl = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=400&h=250&fit=crop&auto=format",
            availableRooms = 15,
            nearbyLandmarks = listOf("yde_uy1", "yde_mkt_mokolo", "yde_mairie_tsinga")
        ),
        MapHotel(
            id = "y5", city = "yaounde",
            name = "Résidence des Nations",
            priceXaf = 62_000, rating = 4.2, distance = "1.8 km", location = "Bastos",
            amenities = listOf("WI-FI", "BAR", "GROUPE ÉLEC"),
            lat = 3.8720, lng = 11.5300,
            imageUrl = "https://images.unsplash.com/photo-1544124124897-3b9becc7c0db?w=400&h=250&fit=crop&auto=format",
            availableRooms = 7,
            nearbyLandmarks = listOf("yde_bastos", "yde_melen")
        ),
        MapHotel(
            id = "y6", city = "yaounde",
            name = "Yaoundé Palace Hôtel",
            priceXaf = 55_000, rating = 4.1, distance = "0.8 km", location = "Centre",
            amenities = listOf("WI-FI", "RESTAURANT", "PARKING"),
            lat = 3.8680, lng = 11.5170,
            imageUrl = "https://images.unsplash.com/photo-1540518614846-7eded433c457?w=400&h=250&fit=crop&auto=format",
            availableRooms = 10,
            nearbyLandmarks = listOf("yde_mairie_1", "yde_mkt_mfoundi", "yde_hopital", "yde_stade_ahidjo")
        ),

        // ── Bafoussam ───────────────────────────────────────────────────────
        MapHotel(
            id = "bf1", city = "bafoussam",
            name = "Hôtel Mifi",
            priceXaf = 38_000, rating = 3.9, distance = "0.8 km", location = "Centre",
            amenities = listOf("WI-FI", "RESTAURANT", "CLIM"),
            lat = 5.4800, lng = 10.4200,
            imageUrl = "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=400&h=250&fit=crop&auto=format",
            availableRooms = 11,
            nearbyLandmarks = listOf("bf_mkt_a", "bf_centre")
        ),
        MapHotel(
            id = "bf2", city = "bafoussam",
            name = "La Falaise des Bamboutos",
            priceXaf = 47_000, rating = 4.1, distance = "2.0 km", location = "Colline",
            amenities = listOf("WI-FI", "VUE PANO.", "BAR"),
            lat = 5.4750, lng = 10.4150,
            imageUrl = "https://images.unsplash.com/photo-1598928506311-c55ded91a20c?w=400&h=250&fit=crop&auto=format",
            availableRooms = 5,
            nearbyLandmarks = listOf("bf_centre")
        ),
        MapHotel(
            id = "bf3", city = "bafoussam",
            name = "Hôtel Président Bafoussam",
            priceXaf = 32_000, rating = 3.7, distance = "0.3 km", location = "Centre-Ville",
            amenities = listOf("WI-FI", "PARKING", "CLIM"),
            lat = 5.4820, lng = 10.4220,
            imageUrl = "https://images.unsplash.com/photo-1520637836862-4d197d17c33a?w=400&h=250&fit=crop&auto=format",
            availableRooms = 14,
            nearbyLandmarks = listOf("bf_mkt_a", "bf_centre")
        ),

        // ── Limbé ───────────────────────────────────────────────────────────
        MapHotel(
            id = "lm1", city = "limbe",
            name = "Atlantic Beach Resort",
            priceXaf = 83_000, rating = 4.6, distance = "0.1 km", location = "Down Beach",
            amenities = listOf("WI-FI", "PISCINE", "PLAGE PRIVÉE"),
            lat = 4.0200, lng = 9.2100,
            imageUrl = "https://images.unsplash.com/photo-1540541338287-41700207dee6?w=400&h=250&fit=crop&auto=format",
            availableRooms = 4,
            nearbyLandmarks = listOf("lmb_down_beach", "lmb_centre")
        ),
        MapHotel(
            id = "lm2", city = "limbe",
            name = "Hôtel Seme Beach",
            priceXaf = 53_000, rating = 4.0, distance = "1.5 km", location = "Bota",
            amenities = listOf("WI-FI", "RESTAURANT", "VUE MER"),
            lat = 4.0120, lng = 9.1950,
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=400&h=250&fit=crop&auto=format",
            availableRooms = 8,
            nearbyLandmarks = listOf("lmb_botanique", "lmb_centre")
        ),
        MapHotel(
            id = "lm3", city = "limbe",
            name = "Miramar Hôtel Limbé",
            priceXaf = 65_000, rating = 4.3, distance = "0.8 km", location = "Mile 4",
            amenities = listOf("WI-FI", "PISCINE", "BAR"),
            lat = 4.0240, lng = 9.2050,
            imageUrl = "https://images.unsplash.com/photo-1467987505890-854f18ca6a63?w=400&h=250&fit=crop&auto=format",
            availableRooms = 6,
            nearbyLandmarks = listOf("lmb_down_beach", "lmb_botanique")
        ),

        // ── Kribi ───────────────────────────────────────────────────────────
        MapHotel(
            id = "kb1", city = "kribi",
            name = "Hôtel de la Plage Kribi",
            priceXaf = 60_000, rating = 4.4, distance = "50 m", location = "Plage de Kribi",
            amenities = listOf("WI-FI", "PISCINE", "PLAGE PRIVÉE"),
            lat = 2.9400, lng = 9.9100,
            imageUrl = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=400&h=250&fit=crop&auto=format",
            availableRooms = 5,
            nearbyLandmarks = listOf("krb_plage", "krb_port")
        ),
        MapHotel(
            id = "kb2", city = "kribi",
            name = "Résidence Chutes de la Lobé",
            priceXaf = 45_000, rating = 4.2, distance = "3.0 km", location = "Lobé",
            amenities = listOf("WI-FI", "RESTAURANT", "EXCURSIONS"),
            lat = 2.9200, lng = 9.8950,
            imageUrl = "https://images.unsplash.com/photo-1505118380757-91f5f5632de0?w=400&h=250&fit=crop&auto=format",
            availableRooms = 9,
            nearbyLandmarks = listOf("krb_lobe")
        ),
        MapHotel(
            id = "kb3", city = "kribi",
            name = "Hôtel Ilomba",
            priceXaf = 35_000, rating = 3.8, distance = "0.5 km", location = "Centre Kribi",
            amenities = listOf("WI-FI", "BAR", "CLIM"),
            lat = 2.9380, lng = 9.9090,
            imageUrl = "https://images.unsplash.com/photo-1498503182468-3b51cbb6cb24?w=400&h=250&fit=crop&auto=format",
            availableRooms = 12,
            nearbyLandmarks = listOf("krb_plage", "krb_port")
        ),

        // ── Bamenda ─────────────────────────────────────────────────────────
        MapHotel(
            id = "bm1", city = "bamenda",
            name = "Ayaba Hôtel",
            priceXaf = 55_000, rating = 4.3, distance = "0.3 km", location = "Commercial Avenue",
            amenities = listOf("WI-FI", "RESTAURANT", "GYM"),
            lat = 5.9540, lng = 10.1600,
            imageUrl = "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=400&h=250&fit=crop&auto=format",
            availableRooms = 7,
            nearbyLandmarks = listOf("bda_commercial")
        ),
        MapHotel(
            id = "bm2", city = "bamenda",
            name = "Mondial Hôtel Bamenda",
            priceXaf = 38_000, rating = 4.0, distance = "0.6 km", location = "Nkwen",
            amenities = listOf("WI-FI", "RESTAURANT", "BAR"),
            lat = 5.9500, lng = 10.1550,
            imageUrl = "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=400&h=250&fit=crop&auto=format",
            availableRooms = 8,
            nearbyLandmarks = listOf("bda_commercial")
        ),
        MapHotel(
            id = "bm3", city = "bamenda",
            name = "Hôtel Up Station",
            priceXaf = 28_000, rating = 3.6, distance = "1.2 km", location = "Up Station",
            amenities = listOf("WI-FI", "VUE MONTAGNE", "PARKING"),
            lat = 5.9610, lng = 10.1720,
            imageUrl = "https://images.unsplash.com/photo-1510798831971-661eb04b3739?w=400&h=250&fit=crop&auto=format",
            availableRooms = 16,
            nearbyLandmarks = listOf("bda_up_station")
        ),

        // ── Buea ────────────────────────────────────────────────────────────
        MapHotel(
            id = "bu1", city = "buea",
            name = "Mountain Hôtel Buea",
            priceXaf = 48_000, rating = 4.2, distance = "0.5 km", location = "Molyko",
            amenities = listOf("WI-FI", "VUE FAKO", "BAR"),
            lat = 4.1540, lng = 9.2430,
            imageUrl = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=400&h=250&fit=crop&auto=format",
            availableRooms = 9,
            nearbyLandmarks = listOf("bu_molyko", "bu_ub")
        ),
        MapHotel(
            id = "bu2", city = "buea",
            name = "Hôtel Fako",
            priceXaf = 35_000, rating = 3.9, distance = "1.2 km", location = "Buea Town",
            amenities = listOf("WI-FI", "RESTAURANT", "PARKING"),
            lat = 4.1500, lng = 9.2390,
            imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=400&h=250&fit=crop&auto=format",
            availableRooms = 13,
            nearbyLandmarks = listOf("bu_ub", "bu_molyko")
        ),

        // ── Ngaoundéré ──────────────────────────────────────────────────────
        MapHotel(
            id = "ng1", city = "ngaoundere",
            name = "Transcam Hôtel",
            priceXaf = 42_000, rating = 4.0, distance = "0.8 km", location = "Centre-Ville",
            amenities = listOf("WI-FI", "RESTAURANT", "CLIM"),
            lat = 7.3260, lng = 13.5850,
            imageUrl = "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=400&h=250&fit=crop&auto=format",
            availableRooms = 10,
            nearbyLandmarks = listOf("ng_gare", "ng_lamido")
        ),
        MapHotel(
            id = "ng2", city = "ngaoundere",
            name = "Auberge de l'Adamaoua",
            priceXaf = 28_000, rating = 3.7, distance = "1.5 km", location = "Marouarou",
            amenities = listOf("WI-FI", "PARKING", "GROUPE ÉLEC"),
            lat = 7.3200, lng = 13.5780,
            imageUrl = "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=400&h=250&fit=crop&auto=format",
            availableRooms = 18,
            nearbyLandmarks = listOf("ng_gare")
        )
    )
}