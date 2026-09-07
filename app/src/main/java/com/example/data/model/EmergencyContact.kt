package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmergencyContact(
    @Json(name = "ID") val id: String,
    @Json(name = "State") val state: String,
    @Json(name = "District") val district: String,
    @Json(name = "BlockTown") val blockTown: String,
    @Json(name = "Subdivision") val subdivision: String = "",
    @Json(name = "Block") val block: String = "",
    @Json(name = "GPName") val gpName: String = "",
    @Json(name = "GPCode") val gpCode: String = "",
    @Json(name = "VillageName") val villageName: String = "",
    @Json(name = "VillageCode") val villageCode: String = "",
    @Json(name = "PostOffice") val postOffice: String = "",
    @Json(name = "Pincode") val pincode: String = "",
    @Json(name = "Latitude") val latitude: Double? = null,
    @Json(name = "Longitude") val longitude: Double? = null,
    @Json(name = "Category") val category: String,
    @Json(name = "Name") val name: String,
    @Json(name = "PhonePrimary") val phonePrimary: String,
    @Json(name = "PhoneAlternate") val phoneAlternate: String = "",
    @Json(name = "Address") val address: String = "",
    @Json(name = "SourceURL") val sourceUrl: String,
    @Json(name = "Verified") val verified: String, // "Yes" or "No"
    @Json(name = "VerifiedAt") val verifiedAt: String, // ISO date YYYY-MM-DD
    @Json(name = "ReviewDue") val reviewDue: String,   // ISO date YYYY-MM-DD
    @Json(name = "Status") val status: String,         // "Active", "Draft", "Needs Review", "Inactive", "Rejected", "Expired"
    @Json(name = "Notes") val notes: String = ""
) {
    /**
     * Strict public publishing validation rule:
     * Only show a record publicly when:
     * 1. Verified == "Yes"
     * 2. Status == "Active"
     * 3. PhonePrimary is valid (non-blank, digits/valid pattern, not placeholder)
     * 4. SourceURL exists and uses HTTPS
     * 5. VerifiedAt exists and is non-blank
     * 6. ReviewDue has not expired
     */
    fun isPubliclyVisible(currentDateIso: String): Boolean {
        if (!verified.equals("Yes", ignoreCase = true)) return false
        if (!status.equals("Active", ignoreCase = true)) return false
        if (!isValidPhoneNumber(phonePrimary)) return false
        if (!sourceUrl.startsWith("https://", ignoreCase = true)) return false
        if (verifiedAt.isBlank()) return false
        if (reviewDue.isBlank() || isDateExpired(reviewDue, currentDateIso)) return false
        return true
    }

    companion object {
        fun isValidPhoneNumber(phone: String): Boolean {
            val cleaned = phone.replace("[^0-9+]".toRegex(), "")
            // Must have at least 3 digits (e.g. 112, 100) and max 15 digits
            if (cleaned.length < 3 || cleaned.length > 15) return false
            // Disallow obvious fake/placeholder numbers like 00000000, 1234567890 etc.
            if (cleaned.matches("^0+$".toRegex())) return false
            if (cleaned.equals("1234567890") || cleaned.equals("9999999999")) return false
            return true
        }

        fun isDateExpired(reviewDueDateIso: String, currentDateIso: String): Boolean {
            return try {
                // ISO YYYY-MM-DD strings are lexicographically comparable
                reviewDueDateIso.trim() < currentDateIso.trim()
            } catch (e: Exception) {
                true
            }
        }
    }
}
