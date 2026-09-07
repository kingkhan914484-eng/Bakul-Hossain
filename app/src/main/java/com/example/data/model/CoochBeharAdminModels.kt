package com.example.data.model

data class SubdivisionInfo(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val blocks: List<String>
)

data class BlockInfo(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val subdivisionId: String,
    val gpCount: Int,
    val policeStation: String,
    val policePhone: String,
    val fireStation: String,
    val firePhone: String,
    val hospital: String,
    val hospitalPhone: String,
    val bdoPhone: String
)

data class GramPanchayatInfo(
    val code: String,
    val nameEn: String,
    val nameBn: String,
    val blockId: String,
    val postOffice: String,
    val pincode: String,
    val status: String = "Active"
)

data class VillageRecord(
    val id: String,
    val villageName: String,
    val villageNameBn: String,
    val villageCode: String,
    val gpName: String,
    val gpCode: String,
    val block: String,
    val subdivision: String,
    val district: String = "Cooch Behar",
    val postOffice: String,
    val pincode: String,
    val sourceUrl: String,
    val verified: String, // "Yes" or "No"
    val verifiedAt: String,
    val reviewDue: String,
    val status: String,   // "Active" or "Needs Verification"
    val notes: String
)
