package com.kamerstay.app.util

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

object EmailSender {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) { json(json) }
    }

    private fun resolveApiKey(): String {
        val fromEnv = System.getenv("RESEND_API_KEY")
        if (!fromEnv.isNullOrBlank()) return fromEnv
        return try {
            val props = java.util.Properties()
            var dir = java.io.File("").absoluteFile
            repeat(6) {
                val candidate = java.io.File(dir, "local.properties")
                if (candidate.exists()) {
                    props.load(candidate.inputStream())
                    return props.getProperty("RESEND_API_KEY") ?: ""
                }
                dir = dir.parentFile ?: return ""
            }
            ""
        } catch (_: Exception) { "" }
    }

    private fun resolveFromEmail(): String =
        System.getenv("RESEND_FROM_EMAIL")?.takeIf { it.isNotBlank() }
            ?: "KamerStay <onboarding@resend.dev>"

    suspend fun sendOtpEmail(toEmail: String, otpCode: String): Boolean {
        val apiKey = resolveApiKey()
        if (apiKey.isBlank()) {
            println("[KamerStay] RESEND_API_KEY non configurée — email non envoyé pour ${toEmail.take(3)}***")
            return false
        }
        return try {
            val body = buildJsonObject {
                put("from", resolveFromEmail())
                put("to", buildJsonArray { add(toEmail) })
                put("subject", "Votre code de vérification KamerStay")
                put("html", buildOtpHtml(otpCode))
            }
            val response = httpClient.post("https://api.resend.com/emails") {
                header("Authorization", "Bearer $apiKey")
                contentType(ContentType.Application.Json)
                setBody(body.toString())
            }
            val success = response.status.value in 200..299
            if (!success) println("[KamerStay] Resend HTTP ${response.status.value}")
            success
        } catch (e: Exception) {
            println("[KamerStay] Erreur envoi email: ${e.message}")
            false
        }
    }

    private fun buildOtpHtml(code: String): String = """
        <!DOCTYPE html>
        <html lang="fr">
        <body style="margin:0;padding:0;background:#f4f6f8;font-family:Arial,sans-serif;">
          <table width="100%" cellpadding="0" cellspacing="0">
            <tr><td align="center" style="padding:40px 16px;">
              <table width="480" cellpadding="0" cellspacing="0" style="background:#ffffff;border-radius:16px;overflow:hidden;">
                <tr>
                  <td style="background:linear-gradient(135deg,#00D5E1,#0099A8);padding:28px 32px;">
                    <h1 style="margin:0;color:#ffffff;font-size:22px;font-weight:700;letter-spacing:-0.5px;">KamerStay</h1>
                    <p style="margin:4px 0 0;color:rgba(255,255,255,0.85);font-size:13px;">Réinitialisation de mot de passe</p>
                  </td>
                </tr>
                <tr>
                  <td style="padding:32px;">
                    <p style="color:#1A2A3A;font-size:15px;margin:0 0 20px;">Bonjour,</p>
                    <p style="color:#444;font-size:14px;line-height:1.6;margin:0 0 24px;">
                      Vous avez demandé la réinitialisation de votre mot de passe. Utilisez le code ci-dessous pour continuer.
                    </p>
                    <div style="background:#f0fafa;border:1.5px solid #00D5E1;border-radius:12px;padding:24px;text-align:center;margin:0 0 24px;">
                      <p style="margin:0 0 8px;color:#666;font-size:12px;text-transform:uppercase;letter-spacing:1px;">Votre code</p>
                      <span style="font-size:42px;font-weight:800;letter-spacing:12px;color:#1A2A3A;">$code</span>
                    </div>
                    <p style="color:#888;font-size:13px;margin:0 0 8px;">⏱ Ce code expire dans <strong>10 minutes</strong>.</p>
                    <p style="color:#888;font-size:13px;margin:0;">Si vous n'avez pas fait cette demande, ignorez ce message.</p>
                  </td>
                </tr>
                <tr>
                  <td style="background:#f8f9fa;padding:16px 32px;border-top:1px solid #eee;">
                    <p style="margin:0;color:#aaa;font-size:11px;text-align:center;">© 2025 KamerStay · Made in Cameroun 🇨🇲</p>
                  </td>
                </tr>
              </table>
            </td></tr>
          </table>
        </body>
        </html>
    """.trimIndent()
}