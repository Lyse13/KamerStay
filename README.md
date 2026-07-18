KamerStay

A smart, location-based hotel booking and stay management platform for travelers in Cameroon.

Built with Kotlin Multiplatform · Compose Multiplatform · Ktor · MongoDB

Report Bug · Request Feature

Table of Contents


About the Project
Key Features
Tech Stack
Architecture
Project Structure
Getting Started

Prerequisites
Environment Variables
Build & Run



Usage
API Documentation
Testing
Contributing
Author
License



About the Project

Booking a hotel room online in Cameroon remains a real struggle. Travelers rely on phone calls, word of mouth, or physically visiting hotels on arrival — often facing uncertainty about availability, pricing, and safety. On the other side, small and medium hotels lack digital visibility and manage reservations manually, leading to overbooking and lost revenue.

KamerStay bridges this gap with a dual-sided mobile platform:


For travelers: discover hotels by city or by proximity to known landmarks, chat with an AI concierge, and pay via Mobile Money.
For hotel managers: manage reservations, rooms, staff, and revenue from a dedicated console.


The platform is designed from the ground up for the Cameroonian context — its languages, its landmarks, and its mobile-money payment ecosystem.


Key Features

Travelers


Smart hotel discovery by city and by landmark proximity (with real distance & travel time)
Kamsa — an AI concierge that understands French, English, and Cameroonian Pidgin
Mobile Money payment (MTN MoMo & Orange Money)
Booking flow with real-time availability and double-booking prevention
Booking history, digital voucher, wishlist, and hotel reviews


Hotel Managers


Real-time reservation dashboard
Check-in / check-out processing
Room and staff management
Revenue analytics and reporting
Property registration and verification



Tech Stack

LayerTechnologyFrontend (mobile/desktop/web)Kotlin Multiplatform, Compose MultiplatformArchitecture patternMVVMDependency InjectionKoinBackend APIKtor (REST)DatabaseMongoDB (Atlas)AuthenticationJWT + BCryptAI ConciergeAnthropic Claude APIMapsGoogle Maps (Android)PaymentsCampay (MTN MoMo / Orange Money)EmailsResendContainerizationDocker

Architecture

KamerStay follows a three-tier client–server architecture:
┌─────────────────┐      REST/JSON      ┌──────────────────┐      ┌──────────────┐
│  Client (KMP)   │  ───────────────▶   │  Server (Ktor)   │ ───▶ │  MongoDB     │
│ Compose UI      │  ◀───────────────   │  Auth · Business │ ◀─── │  Atlas       │
│ Android/iOS/Web │      JWT auth       │  Logic · Routes  │      │              │
└─────────────────┘                     └──────────────────┘      └──────────────┘
                                               │
                                     External services:
                                   Anthropic · Campay · Resend
                                   
Roughly 95% of the application code is shared across platforms through Kotlin Multiplatform, with platform-specific code (e.g. Google Maps on Android) isolated via the expect/actual mechanism.


Project Structure

This is a Kotlin Multiplatform project targeting Android, iOS, Web, Desktop (JVM), and Server.


/composeApp — shared Compose Multiplatform application code.

commonMain — code common to all targets (UI, ViewModels, state, models, DI).
androidMain, iosMain, jvmMain, wasmJsMain — platform-specific code (e.g. Google Maps lives in androidMain).



/iosApp — iOS application entry point (SwiftUI host).
/server — the Ktor server application (REST API, repositories, auth).
/shared — code shared between all targets (data models, utilities).



Getting Started

Prerequisites


JDK 17 or higher
Android Studio (Meerkat or newer) with the Kotlin Multiplatform plugin
A MongoDB Atlas cluster (or local MongoDB)
API keys: Anthropic, Resend, and Campay (for full functionality)


Environment Variables

The server reads its secrets from environment variables. Never commit these. Set them in your environment or in a non-versioned local.properties / .env:

VariableDescriptionRequiredJWT_SECRETSecret used to sign JWT tokens (≥ 32 chars)YesMONGODB_URIMongoDB Atlas connection stringYesANTHROPIC_API_KEYKey for the Kamsa AI conciergeYes (for AI)RESEND_API_KEYKey for transactional emailsYes (for email)CAMPAY_API_KEYKey for Mobile Money paymentsYes (for payments)DEV_MODEtrue only for local developmentNo

Generate a strong JWT secret with:

bashopenssl rand -base64 48

Build & Run

Android

bash./gradlew :composeApp:assembleDebug        # macOS/Linux
.\gradlew.bat :composeApp:assembleDebug    # Windows

Desktop (JVM)

bash./gradlew :composeApp:run

Web (Wasm — modern browsers)

bash./gradlew :composeApp:wasmJsBrowserDevelopmentRun

Web (JS — older browsers)

bash./gradlew :composeApp:jsBrowserDevelopmentRun

Server

bash./gradlew :server:run

iOS — open /iosApp in Xcode and run, or use the run configuration in Android Studio.

Docker (server)

bashdocker build -t kamerstay-backend .
docker run -p 8080:8080 --env-file .env kamerstay-backend


Usage


Launch the app and sign up as a Traveler or a Hotel Manager.
As a traveler: search a city or tap "Recherche par lieu" to find hotels near a landmark. Chat with Kamsa for guided recommendations. Select a hotel, choose a room, and pay the deposit via Mobile Money. Your booking appears in your history with a digital voucher.
As a manager: register your hotel, add rooms, and track incoming reservations in real time from the dashboard. Process check-ins and check-outs, and view your revenue analytics.



API Documentation

The REST API is documented with OpenAPI (Swagger) and a Postman collection.


Swagger UI: available at /swagger when the server is running.
Postman collection: see /docs/KamerStay.postman_collection.json.


Main endpoint groups: /auth, /hotels, /rooms, /bookings, /payments, /reviews, /landmarks, /ai, /managers.


Testing

Run the test suite and generate a coverage report:

bash./gradlew test              # run unit & integration tests
./gradlew koverHtmlReport   # generate coverage report

The coverage report is generated at build/reports/kover/html/index.html.


Contributing

Contributions are welcome. To contribute:


Fork the repository.
Create a feature branch: git checkout -b feature/your-feature.
Follow the existing code style (Kotlin official conventions; keep UI in composables, logic in ViewModels/repositories).
Write or update tests for your changes.
Commit with a clear message: git commit -m "feat: add X" (Conventional Commits encouraged).
Push and open a Pull Request describing what and why.


Please open an issue first for major changes to discuss the approach. Report bugs and request features via GitHub Issues.


Author

Mouandeu Pangop Lysette Merveille
BSc Software Engineering — The ICT University, Yaoundé, Cameroon
GitHub: @Lyse13


License

This project is released under the MIT License. See the LICENSE file for details.




                                   
