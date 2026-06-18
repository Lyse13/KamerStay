package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.ManagerNotification
import com.kamerstay.app.data.model.ManagerNotificationType

object ManagerNotificationsMockData {

    val todayNotifications = listOf(
        ManagerNotification(
            id = "m1",
            title = "Nouvelle Réservation",
            message = "La chambre 301 a été réservée par Sandrine M. du 22 au 25 août. Arrivée dans 3 jours.",
            time = "09:42",
            type = ManagerNotificationType.NEW_BOOKING,
            isRead = false,
            hasAction = true,
            actionLabel = "Voir la Réservation"
        ),
        ManagerNotification(
            id = "m2",
            title = "Arrivée Aujourd'hui",
            message = "Le client Modeste T. arrive aujourd'hui à 14h00 pour la Chambre 204. Assurez-vous que la chambre est prête.",
            time = "08:05",
            type = ManagerNotificationType.CHECK_IN,
            isRead = false,
            hasAction = true,
            actionLabel = "Démarrer l'enregistrement"
        ),
        ManagerNotification(
            id = "m3",
            title = "Alerte Maintenance",
            message = "Le système de chauffage de la piscine nécessite une inspection urgente. Technicien prévu à 10h00.",
            time = "07:30",
            type = ManagerNotificationType.ALERT,
            isRead = false,
            isAlert = true
        ),
    )

    val earlierNotifications = listOf(
        ManagerNotification(
            id = "m4",
            title = "Paiement Reçu",
            message = "Paiement de 252 000 FCFA pour la réservation #B20145 traité avec succès via Orange Money.",
            time = "Hier",
            type = ManagerNotificationType.PAYMENT,
            isRead = true
        ),
        ManagerNotification(
            id = "m5",
            title = "Nouvel Avis Client",
            message = "Emilienne R. a laissé un avis 4 étoiles : \"Emplacement idéal et personnel très accueillant !\"",
            time = "Hier",
            type = ManagerNotificationType.REVIEW,
            isRead = true,
            hasAction = true,
            actionLabel = "Répondre"
        ),
        ManagerNotification(
            id = "m6",
            title = "Alerte Personnel",
            message = "2 membres du personnel d'entretien ne se sont pas enregistrés pour l'équipe du matin.",
            time = "12 Jul",
            type = ManagerNotificationType.STAFF,
            isRead = true
        ),
        ManagerNotification(
            id = "m7",
            title = "Départ Enregistré",
            message = "Le client Daniel F. a quitté la Chambre 112 à 11h30. La chambre est en attente de nettoyage.",
            time = "12 Jul",
            type = ManagerNotificationType.CHECK_OUT,
            isRead = true
        ),
    )
}