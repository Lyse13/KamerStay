package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.AppNotification
import com.kamerstay.app.data.model.NotificationType

object NotificationsMockData {

    val todayNotifications = listOf(
        AppNotification(
            id = "1",
            title = "Réservation Confirmée",
            message = "Votre séjour au Hilton Yaoundé est confirmé du 15 au 20 août. Nous vous attendons avec plaisir !",
            time = "10:24",
            type = NotificationType.BOOKING,
            isRead = false
        ),
        AppNotification(
            id = "2",
            title = "Prêt pour l'arrivée ?",
            message = "L'enregistrement pour la Chambre 402 est ouvert. Utilisez l'appli pour une arrivée sans contact.",
            time = "08:15",
            type = NotificationType.CHECK_IN,
            isRead = false,
            hasAction = true,
            actionLabel = "Démarrer l'enregistrement"
        ),
        AppNotification(
            id = "3",
            title = "Alerte Maintenance",
            message = "Mise à jour système de climatisation en Aile Est, Chambre 204. Prévue à 11h00.",
            time = "07:00",
            type = NotificationType.ALERT,
            isRead = false,
            isAlert = true
        ),
    )

    val earlierNotifications = listOf(
        AppNotification(
            id = "4",
            title = "Offre Week-end",
            message = "Profitez de 20% de réduction sur votre prochaine réservation dans nos hôtels partenaires. Offre valable jusqu'à dimanche !",
            time = "Hier",
            type = NotificationType.PROMO,
            isRead = true
        ),
        AppNotification(
            id = "5",
            title = "Paiement Reçu",
            message = "Transaction #88123 de 324 000 FCFA traitée avec succès pour votre séjour de juillet via MTN Mobile Money.",
            time = "10 Jul",
            type = NotificationType.PAYMENT,
            isRead = true
        ),
    )
}