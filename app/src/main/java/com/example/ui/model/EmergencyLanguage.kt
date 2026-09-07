package com.example.ui.model

enum class EmergencyLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    BENGALI("bn", "বাংলা")
}

object AppStrings {
    fun appTitle(lang: EmergencyLanguage): String = "BM Emergency Global 24 HS 🌍🆘"

    fun tagline(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "One Tap Emergency Help"
        EmergencyLanguage.BENGALI -> "এক ট্যাপে জরুরি সাহায্য"
    }

    fun emergency112Btn(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "112 EMERGENCY"
        EmergencyLanguage.BENGALI -> "১১২ জরুরি সেবা"
    }

    fun emergency112Subtitle(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "National 24x7 Helpline (Police • Fire • Ambulance)"
        EmergencyLanguage.BENGALI -> "জাতীয় জরুরি সহায়তা (পুলিশ • ফায়ার • অ্যাম্বুলেন্স)"
    }

    fun searchPlaceholder(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Search name, category, district, town, phone..."
        EmergencyLanguage.BENGALI -> "নাম, বিভাগ, জেলা, শহর, ফোন দিয়ে খুঁজুন..."
    }

    fun emptyState(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "No verified emergency contact found."
        EmergencyLanguage.BENGALI -> "কোনো verified emergency contact পাওয়া যায়নি।"
    }

    fun offlineBanner(lang: EmergencyLanguage, timestamp: String): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Offline mode — showing previously verified contacts. (Last updated: $timestamp)"
        EmergencyLanguage.BENGALI -> "অফলাইন মোড — পূর্বে যাচাইকৃত পরিচিতিগুলি প্রদর্শিত হচ্ছে। (সর্বশেষ আপডেট: $timestamp)"
    }

    fun verifiedBadge(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "VERIFIED"
        EmergencyLanguage.BENGALI -> "ভেরিফায়েড"
    }

    fun lastVerifiedLabel(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Last Verified:"
        EmergencyLanguage.BENGALI -> "সর্বশেষ যাচাই:"
    }

    fun reviewDueLabel(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Review Due:"
        EmergencyLanguage.BENGALI -> "পুনঃযাচাই মেয়াদ:"
    }

    fun primaryPhoneLabel(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Primary Phone:"
        EmergencyLanguage.BENGALI -> "প্রাথমিক ফোন:"
    }

    fun alternatePhoneLabel(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Alternate Phone:"
        EmergencyLanguage.BENGALI -> "বিকল্প ফোন:"
    }

    fun addressLabel(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Address:"
        EmergencyLanguage.BENGALI -> "ঠিকানা:"
    }

    fun sourceLabel(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Source:"
        EmergencyLanguage.BENGALI -> "উৎস:"
    }

    fun dialBtn(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "DIAL"
        EmergencyLanguage.BENGALI -> "ডায়াল করুন"
    }

    fun viewSourceBtn(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "VIEW SOURCE"
        EmergencyLanguage.BENGALI -> "উৎস দেখুন"
    }

    fun refresh(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Refresh"
        EmergencyLanguage.BENGALI -> "রিফ্রেশ"
    }

    fun categoryLabel(category: String, lang: EmergencyLanguage): String = when (category) {
        "All" -> if (lang == EmergencyLanguage.BENGALI) "সকল" else "All"
        "Emergency" -> if (lang == EmergencyLanguage.BENGALI) "জরুরি অবস্থা" else "Emergency"
        "Police" -> if (lang == EmergencyLanguage.BENGALI) "পুলিশ" else "Police"
        "Fire" -> if (lang == EmergencyLanguage.BENGALI) "ফায়ার সার্ভিস" else "Fire"
        "Ambulance" -> if (lang == EmergencyLanguage.BENGALI) "অ্যাম্বুলেন্স" else "Ambulance"
        "Hospital" -> if (lang == EmergencyLanguage.BENGALI) "হাসপাতাল" else "Hospital"
        "Women" -> if (lang == EmergencyLanguage.BENGALI) "মহিলা সহায়তা" else "Women"
        "Child" -> if (lang == EmergencyLanguage.BENGALI) "শিশু সহায়তা" else "Child"
        "Cyber Crime" -> if (lang == EmergencyLanguage.BENGALI) "সাইবার ক্রাইম" else "Cyber Crime"
        "Disaster" -> if (lang == EmergencyLanguage.BENGALI) "দুর্যোগ ত্রাণ" else "Disaster"
        "Railway" -> if (lang == EmergencyLanguage.BENGALI) "রেলওয়ে" else "Railway"
        "Electricity" -> if (lang == EmergencyLanguage.BENGALI) "বিদ্যুৎ" else "Electricity"
        "Water" -> if (lang == EmergencyLanguage.BENGALI) "জল সরবরাহ" else "Water"
        "Blood Bank" -> if (lang == EmergencyLanguage.BENGALI) "ব্লাড ব্যাংক" else "Blood Bank"
        "Other" -> if (lang == EmergencyLanguage.BENGALI) "অন্যান্য" else "Other"
        else -> category
    }

    val allCategories = listOf(
        "All",
        "Emergency",
        "Police",
        "Fire",
        "Ambulance",
        "Hospital",
        "Women",
        "Child",
        "Cyber Crime",
        "Disaster",
        "Railway",
        "Electricity",
        "Water",
        "Blood Bank",
        "Other"
    )

    fun tabDirectory(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "ডিরেক্টরি" else "Directory"
    fun tabLocation(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "বর্তমান অবস্থান" else "My Location"
    fun tabAdminDirectory(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "কোচবিহার ডিরেক্টরি" else "Cooch Behar"
    fun tabAdminPortal(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "অ্যাডমিন" else "Admin"
    fun tabAbout(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "সম্পর্কে" else "About"
    fun tabDisclaimer(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "শর্তাবলী" else "Disclaimer"
    fun tabPrivacy(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "গোপনীয়তা" else "Privacy Policy"
    fun tabSupport(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "যোগাযোগ" else "Support"

    // Location & GPS strings
    fun myLocationTitle(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "আমার বর্তমান জিপিএস অবস্থান" else "My Current GPS Location"
    fun locationSubtitle(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "ডিভাইসের আসল অবস্থান অনুযায়ী নিকটতম জরুরি সেবা জানুন" else "Find verified emergency services nearest to your real GPS location"
    fun requestLocationBtn(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "অবস্থান সন্ধান করুন (GPS)" else "Detect My Location"
    fun locationPermissionRationale(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "নিকটতম পুলিশ স্টেশন, হাসপাতাল, ও ফায়ার সার্ভিসের দূরত্ব নির্ণয়ের জন্য লোকেশন পারমিশন প্রয়োজন। আপনার অবস্থান কোনো সার্ভারে জমা রাখা হয় না।" else "Location permission is needed solely to calculate distance to the nearest police station, hospital, and emergency units. Your location is never stored or tracked."
    fun nearestServicesTitle(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "নিকটতম জরুরি পরিসেবাসমূহ" else "Nearest Emergency Services"
    fun distanceLabel(distKm: Double, lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "আনুমানিক দূরত্ব: %.1f কিমি".format(distKm) else "Approx. Distance: %.1f km".format(distKm)
    fun openMapBtn(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "ম্যাপে দেখুন" else "VIEW MAP"
    fun getDirectionsBtn(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "দিকনির্দেশনা" else "DIRECTIONS"

    // Cooch Behar Administrative Directory strings
    fun adminDirectoryTitle(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "কোচবিহার প্রশাসনিক ডিরেক্টরি" else "Cooch Behar Administrative Directory"
    fun adminDirectorySubtitle(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "মহকুমা • ব্লক • গ্রাম পঞ্চায়েত / অঞ্চল • গ্রাম • পিন কোড" else "Subdivision • Block • Gram Panchayat / Anchal • Village • PIN"
    fun filterSubdivision(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "মহকুমা নির্বাচন করুন" else "Select Subdivision"
    fun filterBlock(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "ব্লক নির্বাচন করুন" else "Select Block"
    fun filterGP(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "গ্রাম পঞ্চায়েত / অঞ্চল" else "Gram Panchayat / Anchal"
    fun villageListTitle(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "গ্রাম ও ডাকঘরের তালিকা" else "Villages & Post Offices"
    fun postOfficeLabel(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "ডাকঘর:" else "Post Office:"
    fun pinCodeLabel(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "পিন কোড:" else "PIN Code:"
    fun needsVerificationBadge(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "যাচাই প্রক্রিয়াধীন" else "Needs Verification"

    // Admin Panel strings
    fun adminPortalTitle(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "অ্যাডমিন পোর্টাল ও অডিট লগ" else "Admin Portal & Audit Log"
    fun adminPinPrompt(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "অ্যাডমিন সিকিউরিটি পিন দিন" else "Enter Admin Security PIN"
    fun adminUnlockBtn(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "লগইন করুন" else "Unlock Admin Mode"
    fun auditLogsTitle(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "অডিট লগ (সর্বশেষ ১০০টি পরিবর্তন)" else "Audit Logs (Last 100 Actions)"
    fun markVerifiedBtn(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "যাচাইকৃত চিহ্নিত করুন" else "Mark Verified"
    fun markNeedsVerificationBtn(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "যাচাই প্রক্রিয়াধীন রাখুন" else "Mark Needs Verification"
    fun markInactiveBtn(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "নিষ্ক্রিয় করুন" else "Mark Inactive"
    fun recordStatusLabel(lang: EmergencyLanguage) = if (lang == EmergencyLanguage.BENGALI) "স্ট্যাটাস:" else "Status:"

    fun disclaimerTitle(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Critical Safety Notice & Disclaimer"
        EmergencyLanguage.BENGALI -> "জরুরি সতর্কবার্তা ও ডিসক্লেইমার"
    }

    fun disclaimerNotice(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH ->
            "In an emergency, dial 112 first.\n\n" +
            "BM Emergency Global 24 HS is an independent emergency directory and is not an official government app.\n\n" +
            "Local numbers may change. Check the source and last verified date before using local information."
        EmergencyLanguage.BENGALI ->
            "জরুরি অবস্থায় প্রথমে 112 ডায়াল করুন।\n\n" +
            "BM Emergency Global 24 HS একটি independent emergency directory; এটি কোনো সরকারি official app নয়।\n\n" +
            "স্থানীয় নম্বর পরিবর্তিত হতে পারে। Source এবং last verified date দেখে ব্যবহার করুন।"
    }

    fun aboutDescription(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH ->
            "BM Emergency Global 24 HS 🌍🆘\n" +
            "One Tap Emergency Help\n" +
            "Version 1.0.0\n\n" +
            "BM Emergency Global 24 HS is an independent emergency directory designed to help users find verified emergency contact information quickly.\n\n" +
            "Every listing is cataloged with strict multi-step source verification directly from official administrative, police, healthcare, and civil defence channels."
        EmergencyLanguage.BENGALI ->
            "BM Emergency Global 24 HS 🌍🆘\n" +
            "One Tap Emergency Help\n" +
            "ভার্সন 1.0.0\n\n" +
            "BM Emergency Global 24 HS একটি independent emergency directory। এটি কোনো সরকারি official app নয়।\n\n" +
            "ব্যবহারকারীদের দ্রুত ও নির্ভুলভাবে যাচাইকৃত জরুরি পরিষেবার যোগাযোগ তথ্য প্রদানে এটি তৈরি।"
    }

    fun privacyContent(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH ->
            "Privacy by Design & Zero-Tracking Policy:\n\n" +
            "• Minimal Permissions: We do NOT ask for or require access to your Contacts, SMS, Call History, Media/Photos, Camera, Microphone, or Precise Location.\n\n" +
            "• Safe Phone Dialer: Tapping DIAL opens your native phone dialer (ACTION_DIAL) with the phone number pre-filled. We NEVER make calls automatically.\n\n" +
            "• No Registration: No account or personal identification is needed for emergency lookup.\n\n" +
            "• Offline First: Verified contact records are cached securely on your local device for instant offline access."
        EmergencyLanguage.BENGALI ->
            "গোপনীয়তা ও ডেটা সুরক্ষা নীতি:\n\n" +
            "• ন্যূনতম অ্যাক্সেস: এই অ্যাপ্লিকেশনটি আপনার পরিচিতি (Contacts), এসএমএস (SMS), কল হিস্ট্রি, ছবি, ক্যামেরা, মাইক্রোফোন বা সূক্ষ্ম লোকেশন সংগ্রহ করে না।\n\n" +
            "• নিরাপদ ডায়ালার: ডায়াল চাপলে আপনার ফোনের নিজস্ব ডায়ালার ওপেন হয়, স্বয়ংক্রিয়ভাবে কোনো কল করা হয় না।\n\n" +
            "• অ্যাকাউন্ট বিহীন ব্যবহার: সাধারণ জরুরি অনুসন্ধানের জন্য কোনো অ্যাকাউন্টের প্রয়োজন নেই।\n\n" +
            "• নিরাপদ অফলাইন ক্যাশ: অফলাইনেও দ্রুত তথ্য পাওয়ার জন্য যাচাইকৃত নম্বরগুলি নিরাপদে স্থানীয় ডিভাইসে সংরক্ষিত থাকে।"
    }

    fun supportContent(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH ->
            "BM Emergency Directory Operations & Support:\n\n" +
            "• WhatsApp Support: +91 9144844323\n" +
            "• Instagram: @bakul_king_10k\n" +
            "• Report Outdated Contact / Update Request: support@bmemergency.org\n" +
            "• Administrative Verification Desk: verification@bmemergency.org\n\n" +
            "• Emergency Protocol Notice: For life-threatening emergencies, always dial 112 immediately."
        EmergencyLanguage.BENGALI ->
            "যোগাযোগ ও সহায়তা ডেস্ক:\n\n" +
            "• হোয়াটসঅ্যাপ সাপোর্ট: +91 9144844323\n" +
            "• ইনস্টাগ্রাম: @bakul_king_10k\n" +
            "• পুরনো নম্বর সংশোধন বা তথ্যের অনুরোধ: support@bmemergency.org\n" +
            "• ভেরিফিকেশন টিম: verification@bmemergency.org\n\n" +
            "• জরুরি সতর্কবার্তা: জীবন-বিপন্নকারী পরিস্থিতিতে সর্বদা অবিলম্বে ১১২ ডায়াল করুন।"
    }

    // Login & OTP strings
    fun loginTitle(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "BM Emergency Access"
        EmergencyLanguage.BENGALI -> "জরুরি সেবায় প্রবেশ করুন"
    }

    fun loginSubtitle(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Please verify your mobile number and OTP to continue"
        EmergencyLanguage.BENGALI -> "চালিয়ে যেতে মোবাইল নম্বর ও ওটিপি দিয়ে লগইন করুন"
    }

    fun phoneInputLabel(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Mobile Number"
        EmergencyLanguage.BENGALI -> "মোবাইল নম্বর"
    }

    fun phoneHint(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Enter 10-digit number"
        EmergencyLanguage.BENGALI -> "১০ বা ১১ সংখ্যার নম্বর লিখুন"
    }

    fun sendOtpButton(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Send OTP"
        EmergencyLanguage.BENGALI -> "ওটিপি পাঠান"
    }

    fun otpVerificationTitle(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Enter Verification OTP"
        EmergencyLanguage.BENGALI -> "ওটিপি কোডটি লিখুন"
    }

    fun otpSentTo(phone: String, lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "4-digit verification code sent to $phone"
        EmergencyLanguage.BENGALI -> "$phone নম্বরে ৪ সংখ্যার ওটিপি কোড পাঠানো হয়েছে"
    }

    fun verifyButton(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Verify & Open App"
        EmergencyLanguage.BENGALI -> "যাচাই করুন ও অ্যাপে প্রবেশ করুন"
    }

    fun resendOtp(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Resend OTP"
        EmergencyLanguage.BENGALI -> "পুনরায় ওটিপি পাঠান"
    }

    fun resendInSeconds(seconds: Int, lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Resend code in ${seconds}s"
        EmergencyLanguage.BENGALI -> "${seconds} সেকেন্ড পর পুনরায় ওটিপি পাঠান"
    }

    fun changeNumber(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Change Number"
        EmergencyLanguage.BENGALI -> "নম্বর পরিবর্তন করুন"
    }

    fun logout(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Logout"
        EmergencyLanguage.BENGALI -> "লগআউট"
    }

    fun demoOtpNotice(code: String, lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Demo OTP: $code (Tap to auto-fill)"
        EmergencyLanguage.BENGALI -> "আপনার ওটিপি: $code (অটো-ফিল করতে ট্যাপ করুন)"
    }

    // What's New & App Updates strings
    fun whatsNewTitle(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "What's New in BM Emergency 🚀"
        EmergencyLanguage.BENGALI -> "নতুন ফিচারসমূহ 🚀 (v2.4.0)"
    }

    fun whatsNewSubtitle(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "We've added Live GPS distance, Cooch Behar administrative directory, and secure Admin portal."
        EmergencyLanguage.BENGALI -> "আপনার সুরক্ষায় যুক্ত হয়েছে লাইভ জিপিএস দূরত্ব, কোচবিহার পূর্ণাঙ্গ প্রশাসনিক ডিরেক্টরি ও অ্যাডমিন সুরক্ষা পোর্টাল।"
    }

    fun whatsNewBannerTitle(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "🌟 New Features Active in App!"
        EmergencyLanguage.BENGALI -> "🌟 নতুন সমস্ত ফিচার সরাসরি সক্রিয় আছে!"
    }

    fun whatsNewBannerSubtitle(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Tap to see Live GPS, Cooch Behar Directory & Admin Portal"
        EmergencyLanguage.BENGALI -> "লাইভ জিপিএস, কোচবিহার ডিরেক্টরি ও অ্যাডমিন পোর্টাল দেখতে ট্যাপ করুন"
    }

    fun whatsNewBannerBtn(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "View What's New"
        EmergencyLanguage.BENGALI -> "নতুন কি আছে দেখুন"
    }

    fun whatsNewDismissBtn(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Enter App / Got It"
        EmergencyLanguage.BENGALI -> "অ্যাপে প্রবেশ করুন / বুঝেছি"
    }

    fun updateSectionTitle(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "App Update & Version Info"
        EmergencyLanguage.BENGALI -> "অ্যাপ আপডেট ও সংস্করণ তথ্য"
    }

    fun appVersionLabel(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Installed Version: v2.4.0 (Latest 2026 Build)"
        EmergencyLanguage.BENGALI -> "ইনস্টল করা সংস্করণ: v2.4.0 (লেটেস্ট ২০২৬ সংস্করণ)"
    }

    fun updateStatusNotice(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "All new features are already compiled and active inside this app — no separate APK download needed to enjoy them!"
        EmergencyLanguage.BENGALI -> "নতুন সব ফিচার সরাসরি এই অ্যাপেই সক্রিয় করা আছে — কোনো নতুন ফাইল বা আপডেট ডাউনলোড না করলেও আপনি সবগুলো ফিচার ব্যবহার করতে পারবেন!"
    }

    fun checkUpdateBtn(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Check for Updates"
        EmergencyLanguage.BENGALI -> "আপডেট স্ট্যাটাস চেক করুন"
    }

    fun checkingUpdate(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Checking official repository..."
        EmergencyLanguage.BENGALI -> "অফিসিয়াল সার্ভারে চেক করা হচ্ছে..."
    }

    fun downloadUpdateBtn(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "Official Release Repository"
        EmergencyLanguage.BENGALI -> "অফিসিয়াল রিলিজ ও ডাউনলোড লিঙ্ক"
    }

    fun featuresSummaryTitle(lang: EmergencyLanguage): String = when (lang) {
        EmergencyLanguage.ENGLISH -> "All New Features Included in this Version:"
        EmergencyLanguage.BENGALI -> "যুক্ত হওয়া সমস্ত নতুন ফিচারসমূহ:"
    }
}
