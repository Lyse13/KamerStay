package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.FaqCategory
import com.kamerstay.app.data.model.FaqItem

object FaqMockData {

    // ── Traveler ───────────────────────────────────────────

    val travelerCategories = listOf(
        FaqCategory("all",      "All",          "all"),
        FaqCategory("bookings", "Bookings",     "booking"),
        FaqCategory("payments", "Payments",     "payments"),
        FaqCategory("account",  "Account",      "account"),
        FaqCategory("travel",   "Travel",       "travel")
    )

    val travelerArticles = listOf(
        // Bookings
        FaqItem("t1", "bookings",
            "How do I cancel my booking?",
            "Go to \"My Bookings\", select the reservation you want to cancel, then tap \"Cancel Booking\". You'll see the applicable cancellation policy and estimated refund before confirming. Cancellations are processed immediately and a confirmation email is sent."
        ),
        FaqItem("t2", "bookings",
            "Can I modify my reservation dates after booking?",
            "Date modifications depend on the hotel's policy. Go to \"My Bookings\", select the reservation, and look for the \"Modify Dates\" option. If unavailable, contact the hotel directly via the in-app message feature or cancel and rebook."
        ),
        FaqItem("t3", "bookings",
            "What happens if the hotel cancels my reservation?",
            "If the hotel cancels your confirmed reservation, you will receive a full refund to your original payment method within 3–5 business days. You'll also receive an email notification with details and, where possible, alternative accommodation suggestions."
        ),
        FaqItem("t4", "bookings",
            "How do I view my booking confirmation?",
            "Your booking confirmation is available in \"My Bookings\" → \"Past\" or \"Upcoming\" tab. Tap the booking to see full details. You can also download your voucher as a PDF from the booking details screen."
        ),
        // Payments
        FaqItem("t5", "payments",
            "What payment methods are accepted?",
            "KamerStay accepts MTN Mobile Money, Orange Money, Visa, Mastercard, and bank transfers for supported banks in Cameroon and neighbouring CEMAC countries. Available methods are shown during checkout based on your location."
        ),
        FaqItem("t6", "payments",
            "How long does a refund take?",
            "Refunds are typically processed within 5–7 business days after your cancellation is confirmed. Mobile money refunds are usually faster (1–3 days). You can track the status of your refund in My Bookings → Cancelled → \"Track Refund\"."
        ),
        FaqItem("t7", "payments",
            "Why was my payment declined?",
            "Common reasons include insufficient funds, card limit reached, incorrect CVV or expiry date, or a temporary block by your bank. Try a different payment method or contact your bank. If the problem persists, contact our support team with the error code shown."
        ),
        FaqItem("t8", "payments",
            "Can I pay at the hotel?",
            "Some properties offer a \"Pay at Hotel\" option, visible during checkout. If available, you'll only be asked for a card to hold the reservation. Check your booking confirmation for the payment terms specific to your property."
        ),
        // Account
        FaqItem("t9", "account",
            "How do I change my password?",
            "Go to Settings → \"Change Password\". You'll need to enter your current password, then choose a new one meeting security requirements (8+ characters, one uppercase letter, one number or symbol). Changes take effect immediately."
        ),
        FaqItem("t10", "account",
            "How do I update my profile information?",
            "Go to Profile → \"Personal Information\" to update your name, phone number, profile photo, and preferences. Email address changes require re-verification via a code sent to your new email address."
        ),
        FaqItem("t11", "account",
            "How do I delete my account?",
            "Account deletion is permanent. Go to Settings → scroll to the bottom and look for \"Delete Account\". You'll be asked to confirm your password. Active or future bookings must be cancelled first. Deleted accounts cannot be recovered."
        ),
        FaqItem("t12", "account",
            "What is the KamerStay loyalty program?",
            "The loyalty program rewards you with points for every completed stay. Points can be redeemed for discounts on future bookings. Your tier (Standard, Gold, Elite) is shown in your profile and determines the number of points earned per booking."
        ),
        // Travel
        FaqItem("t13", "travel",
            "What are the standard check-in and check-out times?",
            "Standard check-in is from 2:00 PM and check-out is by 12:00 PM (noon). These times vary by property and are shown on the booking details page. Some hotels offer flexible check-in/out for an additional fee."
        ),
        FaqItem("t14", "travel",
            "Can I request an early check-in or late check-out?",
            "Yes — use the \"Special Requests\" field during booking or message the hotel directly through the app. Early check-in and late check-out are subject to availability and may incur an extra charge. Confirmation is at the hotel's discretion."
        ),
        FaqItem("t15", "travel",
            "Are pets allowed in hotels listed on KamerStay?",
            "Pet policies vary by property. Look for the \"Pets Allowed\" tag on the hotel listing page. If the policy is not listed, contact the hotel directly via in-app messaging before booking to avoid issues at arrival."
        ),
        FaqItem("t16", "travel",
            "What should I do if I lose my room key or key card?",
            "Report the loss immediately to the hotel's front desk. Most properties will issue a replacement key card at no charge for the first replacement. A fee may apply for subsequent replacements. Do not leave valuables in your room if you suspect your key has been stolen."
        )
    )

    // ── Manager ────────────────────────────────────────────

    val managerCategories = listOf(
        FaqCategory("all",         "All",          "all"),
        FaqCategory("payouts",     "Payouts",      "payments"),
        FaqCategory("reservations","Reservations", "booking"),
        FaqCategory("property",    "Property",     "settings"),
        FaqCategory("technical",   "Technical",    "tech")
    )

    val managerArticles = listOf(
        // Payouts
        FaqItem("m1", "payouts",
            "When will I receive my payout?",
            "Payouts are processed within 2 business days after a guest's check-in is confirmed by you via the app. Funds are transferred to the bank account or mobile money number in your payout profile. Processing times may vary by financial institution."
        ),
        FaqItem("m2", "payouts",
            "How is the KamerStay commission calculated?",
            "The commission is a percentage of the gross booking amount (excluding taxes and fees the guest pays separately). Standard rate is 12%, reduced to 9% for Verified properties. Promotional bookings carry a 15% rate. Your rate is shown in your host agreement."
        ),
        FaqItem("m3", "payouts",
            "How do I update my bank account or mobile money details?",
            "Go to Settings → \"Payout Methods\" to add, edit, or remove payment accounts. Changes to payout details take effect on the next payout cycle. For security, a verification step is required when adding a new account."
        ),
        FaqItem("m4", "payouts",
            "What happens if a guest disputes a charge?",
            "You will be notified via the app and by email. You have 5 business days to provide evidence (photos, check-in records, receipts). KamerStay mediates the dispute and makes a final decision within 10 business days. Disputed amounts are held pending resolution."
        ),
        // Reservations
        FaqItem("m5", "reservations",
            "How do I approve or decline a booking request?",
            "Go to Reservations → find the pending booking with \"Pending\" status → tap Details → choose \"Approve\" or \"Decline\". If you decline, you must provide a reason. Unanswered requests expire after 24 hours and are automatically declined."
        ),
        FaqItem("m6", "reservations",
            "How do I block dates on my calendar?",
            "In the Reservations screen, switch to Calendar view and tap the day(s) you want to block. A \"Block Dates\" option appears. Blocked dates prevent new bookings but don't affect existing confirmed reservations."
        ),
        FaqItem("m7", "reservations",
            "How do I handle a guest no-show?",
            "If a guest does not arrive by check-in time + 2 hours, mark the reservation as \"No Show\" in Reservation Details. Depending on the cancellation policy applied to the booking, you may be entitled to retain the full amount. Contact support if unsure."
        ),
        FaqItem("m8", "reservations",
            "What is the policy for accidental overbooking?",
            "If you are unable to accommodate a confirmed booking, contact our host support immediately (+237 6XX XXX XXX). KamerStay will assist in relocating the guest. You may incur a relocation fee and a temporary listing suspension for repeated incidents."
        ),
        // Property
        FaqItem("m9", "property",
            "How do I add or edit room types?",
            "Go to Manage Hotel → Room Management → tap the \"+\" button to add a room, or tap an existing room to edit it. You can set the room name, capacity, bed type, description, price, and upload photos. Changes are visible to travellers immediately."
        ),
        FaqItem("m10", "property",
            "How do I update room pricing?",
            "Open the room you want to update in Room Management, then tap \"Edit\" → update the \"Price Per Night\" field. For seasonal pricing, use the Promotions feature (Dashboard → Promotions) to apply time-limited discounts instead of changing base rates."
        ),
        FaqItem("m11", "property",
            "How do I add or update photos on my listing?",
            "Go to Manage Hotel → tap \"Edit Hotel\" → scroll to the Photos section. You can upload up to 10 photos per room and 5 for the hotel overview. High-quality, well-lit photos significantly increase your booking conversion rate."
        ),
        FaqItem("m12", "property",
            "What amenities can I list on my property?",
            "Go to Hotel Amenities (available from Manage Hotel) to select from our full list: Wi-Fi, Pool, Gym, Spa, Parking, Restaurant, Breakfast, etc. Only list amenities that are genuinely available — inaccurate listings can result in guest complaints and policy violations."
        ),
        // Technical
        FaqItem("m13", "technical",
            "The app is not loading or running slowly — what should I do?",
            "Try closing and restarting the app. If the issue persists, check your internet connection and ensure you have the latest version installed. Clear the app cache in your phone settings if needed. Report persistent issues to support@mystays.cm with your device model."
        ),
        FaqItem("m14", "technical",
            "How do I reset my manager account password?",
            "On the login screen, tap \"Forgot Password\" and enter your registered email. A verification code will be sent to your email. Enter the code, then set a new password. If you no longer have access to your email, contact host support."
        ),
        FaqItem("m15", "technical",
            "I'm not receiving booking notifications — how do I fix this?",
            "Go to Settings → \"Booking Alerts\" and confirm they are enabled. Also check your phone's notification settings for the KamerStay app. If notifications were recently enabled, allow up to 10 minutes for them to activate. Contact support if the issue persists."
        ),
        FaqItem("m16", "technical",
            "How do I contact the KamerStay technical team?",
            "For urgent technical issues affecting live bookings, call +237 6XX XXX XXX (24/7 host line). For non-urgent issues, email support@mystays.cm with screenshots and a description of the problem. Response time is within 4 hours on business days."
        )
    )
}