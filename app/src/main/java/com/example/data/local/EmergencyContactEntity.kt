package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.EmergencyContact

@Entity(tableName = "emergency_contacts")
data class EmergencyContactEntity(
    @PrimaryKey val id: String,
    val state: String,
    val district: String,
    val blockTown: String,
    val subdivision: String = "",
    val block: String = "",
    val gpName: String = "",
    val gpCode: String = "",
    val villageName: String = "",
    val villageCode: String = "",
    val postOffice: String = "",
    val pincode: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val category: String,
    val name: String,
    val phonePrimary: String,
    val phoneAlternate: String,
    val address: String,
    val sourceUrl: String,
    val verified: String,
    val verifiedAt: String,
    val reviewDue: String,
    val status: String,
    val notes: String,
    val cachedAt: Long = System.currentTimeMillis()
) {
    fun toModel(): EmergencyContact = EmergencyContact(
        id = id,
        state = state,
        district = district,
        blockTown = blockTown,
        subdivision = subdivision,
        block = block,
        gpName = gpName,
        gpCode = gpCode,
        villageName = villageName,
        villageCode = villageCode,
        postOffice = postOffice,
        pincode = pincode,
        latitude = latitude,
        longitude = longitude,
        category = category,
        name = name,
        phonePrimary = phonePrimary,
        phoneAlternate = phoneAlternate,
        address = address,
        sourceUrl = sourceUrl,
        verified = verified,
        verifiedAt = verifiedAt,
        reviewDue = reviewDue,
        status = status,
        notes = notes
    )

    companion object {
        fun fromModel(model: EmergencyContact): EmergencyContactEntity = EmergencyContactEntity(
            id = model.id,
            state = model.state,
            district = model.district,
            blockTown = model.blockTown,
            subdivision = model.subdivision,
            block = model.block,
            gpName = model.gpName,
            gpCode = model.gpCode,
            villageName = model.villageName,
            villageCode = model.villageCode,
            postOffice = model.postOffice,
            pincode = model.pincode,
            latitude = model.latitude,
            longitude = model.longitude,
            category = model.category,
            name = model.name,
            phonePrimary = model.phonePrimary,
            phoneAlternate = model.phoneAlternate,
            address = model.address,
            sourceUrl = model.sourceUrl,
            verified = model.verified,
            verifiedAt = model.verifiedAt,
            reviewDue = model.reviewDue,
            status = model.status,
            notes = model.notes
        )
    }
}
