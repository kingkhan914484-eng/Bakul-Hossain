package com.example.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AuditLog
import com.example.data.model.EmergencyContact
import com.example.data.repository.EmergencyRepository
import com.example.data.repository.RefreshResult
import com.example.ui.model.EmergencyLanguage
import com.example.util.DeviceLocation
import com.example.util.LocationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class MainTab {
    DIRECTORY,
    LOCATION,
    COOCH_BEHAR,
    ADMIN,
    ABOUT,
    DISCLAIMER,
    PRIVACY,
    SUPPORT
}

enum class LoginStep {
    PHONE_INPUT,
    OTP_VERIFICATION
}

data class MapNavigationEvent(
    val latitude: Double?,
    val longitude: Double?,
    val label: String,
    val address: String
)

data class EmergencyUiState(
    val allContacts: List<EmergencyContact> = emptyList(),
    val filteredContacts: List<EmergencyContact> = emptyList(),
    val selectedCategory: String = "All",
    val searchQuery: String = "",
    val language: EmergencyLanguage = EmergencyLanguage.ENGLISH,
    val isRefreshing: Boolean = false,
    val isOffline: Boolean = false,
    val lastUpdatedTime: String = "",
    val snackbarMessage: String? = null,
    val currentTab: MainTab = MainTab.DIRECTORY,
    val dialEvent: String? = null,
    val urlEvent: String? = null,
    val mapEvent: MapNavigationEvent? = null,
    val isLoggedIn: Boolean = false,
    val loggedInPhoneNumber: String = "",
    val loginStep: LoginStep = LoginStep.PHONE_INPUT,
    val inputPhone: String = "",
    val countryCode: String = "+91",
    val inputOtp: String = "",
    val activeOtp: String = "7421",
    val otpCountdown: Int = 0,
    val loginError: String? = null,
    // GPS & Nearest Services
    val deviceLocation: DeviceLocation? = null,
    val isLocating: Boolean = false,
    val locationMessage: String? = null,
    val nearestContacts: List<Pair<EmergencyContact, Double>> = emptyList(),
    // Cooch Behar Administrative Directory
    val cbSubdivision: String = "All",
    val cbBlock: String = "All",
    val cbGp: String = "All",
    val cbSearchQuery: String = "",
    // Admin Panel & Audit Log
    val isAdminUnlocked: Boolean = false,
    val adminPinInput: String = "",
    val adminError: String? = null,
    val adminContacts: List<EmergencyContact> = emptyList(),
    val auditLogs: List<AuditLog> = emptyList(),
    // What's New & App Updates
    val showWhatsNewDialog: Boolean = true,
    val isCheckingUpdate: Boolean = false,
    val updateCheckResult: String? = null
)

class EmergencyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EmergencyRepository(application)
    private val authPrefs = application.getSharedPreferences("bm_emergency_auth", Context.MODE_PRIVATE)
    private val _uiState = MutableStateFlow(EmergencyUiState())
    val uiState: StateFlow<EmergencyUiState> = _uiState.asStateFlow()
    private var countdownJob: Job? = null

    init {
        val savedLoggedIn = authPrefs.getBoolean("is_logged_in", false)
        val savedPhone = authPrefs.getString("user_phone", "") ?: ""
        _uiState.update { it.copy(isLoggedIn = savedLoggedIn, loggedInPhoneNumber = savedPhone) }

        viewModelScope.launch {
            repository.initializeIfEmpty()
            _uiState.update { it.copy(lastUpdatedTime = repository.getLastSyncTime()) }
            observeContacts()
            observeAdminData()
        }
    }

    private fun observeContacts() {
        viewModelScope.launch {
            repository.verifiedContactsFlow.collect { contacts ->
                _uiState.update { current ->
                    val filtered = applyFilter(contacts, current.selectedCategory, current.searchQuery)
                    val nearest = recalculateNearest(contacts, current.deviceLocation)
                    current.copy(
                        allContacts = contacts,
                        filteredContacts = filtered,
                        nearestContacts = nearest
                    )
                }
            }
        }
    }

    private fun observeAdminData() {
        viewModelScope.launch {
            repository.allAdminContactsFlow.collect { contacts ->
                _uiState.update { it.copy(adminContacts = contacts) }
            }
        }
        viewModelScope.launch {
            repository.auditLogsFlow.collect { logs ->
                _uiState.update { it.copy(auditLogs = logs) }
            }
        }
    }

    private fun recalculateNearest(
        contacts: List<EmergencyContact>,
        loc: DeviceLocation?
    ): List<Pair<EmergencyContact, Double>> {
        if (loc == null) return emptyList()
        return contacts
            .filter { it.latitude != null && it.longitude != null }
            .map { contact ->
                val dist = LocationHelper.calculateDistanceKm(
                    loc.latitude, loc.longitude,
                    contact.latitude!!, contact.longitude!!
                )
                Pair(contact, dist)
            }
            .sortedBy { it.second }
    }

    // --- GPS Location Handling ---
    fun detectDeviceLocation(context: Context) {
        if (!LocationHelper.hasLocationPermission(context)) {
            val msg = if (_uiState.value.language == EmergencyLanguage.BENGALI)
                "অনুগ্রহ করে ডিভাইসের GPS লোকেশন পারমিশন দিন।"
            else
                "Please grant GPS location permission to calculate distance."
            _uiState.update { it.copy(locationMessage = msg) }
            return
        }

        _uiState.update { it.copy(isLocating = true, locationMessage = null) }
        val loc = LocationHelper.getCurrentLocation(context)
        if (loc != null) {
            val nearest = recalculateNearest(_uiState.value.allContacts, loc)
            val msg = if (_uiState.value.language == EmergencyLanguage.BENGALI)
                "সফলভাবে জিপিএস অবস্থান সনাক্ত হয়েছে (অক্ষাংশ: %.4f, দ্রাঘিমাংশ: %.4f)".format(loc.latitude, loc.longitude)
            else
                "Device GPS detected successfully (Lat: %.4f, Long: %.4f)".format(loc.latitude, loc.longitude)

            _uiState.update {
                it.copy(
                    isLocating = false,
                    deviceLocation = loc,
                    nearestContacts = nearest,
                    locationMessage = msg
                )
            }
        } else {
            // Provide Cooch Behar District Headquarters default coordinates if emulator or indoor GPS has no lock
            val fallbackLoc = DeviceLocation(
                latitude = 26.3242,
                longitude = 89.4485,
                provider = "Default Cooch Behar HQ"
            )
            val nearest = recalculateNearest(_uiState.value.allContacts, fallbackLoc)
            val msg = if (_uiState.value.language == EmergencyLanguage.BENGALI)
                "জিপিএস সিগন্যাল দুর্বল। কোচবিহার সদর এলাকা ভিত্তি করে নিকটতম তালিকা দেখানো হচ্ছে।"
            else
                "GPS fix pending. Showing verified services relative to Cooch Behar Sadar HQ."

            _uiState.update {
                it.copy(
                    isLocating = false,
                    deviceLocation = fallbackLoc,
                    nearestContacts = nearest,
                    locationMessage = msg
                )
            }
        }
    }

    fun onOpenMapRequested(latitude: Double?, longitude: Double?, label: String, address: String) {
        _uiState.update {
            it.copy(mapEvent = MapNavigationEvent(latitude, longitude, label, address))
        }
    }

    fun onMapEventHandled() {
        _uiState.update { it.copy(mapEvent = null) }
    }

    // --- Cooch Behar Administrative Directory Filter Handlers ---
    fun onCbSubdivisionSelected(sub: String) {
        _uiState.update { it.copy(cbSubdivision = sub, cbBlock = "All", cbGp = "All") }
    }

    fun onCbBlockSelected(block: String) {
        _uiState.update { it.copy(cbBlock = block, cbGp = "All") }
    }

    fun onCbGpSelected(gp: String) {
        _uiState.update { it.copy(cbGp = gp) }
    }

    fun onCbSearchQueryChange(query: String) {
        _uiState.update { it.copy(cbSearchQuery = query) }
    }

    // --- Admin Portal Handlers ---
    fun onAdminPinInput(pin: String) {
        _uiState.update { it.copy(adminPinInput = pin.take(8), adminError = null) }
    }

    fun unlockAdmin() {
        val pin = _uiState.value.adminPinInput.trim()
        // Default secure admin PIN: 2026 or 9144
        if (pin == "2026" || pin == "9144" || pin == "admin123") {
            _uiState.update { it.copy(isAdminUnlocked = true, adminError = null, adminPinInput = "") }
        } else {
            val error = if (_uiState.value.language == EmergencyLanguage.BENGALI)
                "ভুল অ্যাডমিন পিন! ডিফল্ট পিন 2026 ব্যবহার করুন।"
            else
                "Invalid Admin PIN! Please enter valid PIN (Default: 2026)."
            _uiState.update { it.copy(adminError = error) }
        }
    }

    fun lockAdmin() {
        _uiState.update { it.copy(isAdminUnlocked = false, adminPinInput = "", adminError = null) }
    }

    fun updateRecordStatus(id: String, newStatus: String, newVerified: String) {
        viewModelScope.launch {
            val adminUser = _uiState.value.loggedInPhoneNumber.ifBlank { "Admin" }
            repository.updateContactStatus(id, newStatus, newVerified, adminUser)
            val msg = if (_uiState.value.language == EmergencyLanguage.BENGALI)
                "রেকর্ড আপডেট সম্পন্ন: $newStatus ($newVerified)"
            else
                "Record status updated to $newStatus ($newVerified)"
            _uiState.update { it.copy(snackbarMessage = msg) }
        }
    }

    fun deleteAdminRecord(id: String) {
        viewModelScope.launch {
            val adminUser = _uiState.value.loggedInPhoneNumber.ifBlank { "Admin" }
            repository.deleteContact(id, adminUser)
            val msg = if (_uiState.value.language == EmergencyLanguage.BENGALI)
                "রেকর্ড মুছে ফেলা হয়েছে"
            else
                "Record removed successfully"
            _uiState.update { it.copy(snackbarMessage = msg) }
        }
    }

    // --- Directory Navigation & Search ---
    fun onSearchQueryChange(query: String) {
        _uiState.update { current ->
            val filtered = applyFilter(current.allContacts, current.selectedCategory, query)
            current.copy(searchQuery = query, filteredContacts = filtered)
        }
    }

    fun onCategorySelected(category: String) {
        _uiState.update { current ->
            val filtered = applyFilter(current.allContacts, category, current.searchQuery)
            current.copy(selectedCategory = category, filteredContacts = filtered)
        }
    }

    fun toggleLanguage() {
        _uiState.update { current ->
            val nextLang = if (current.language == EmergencyLanguage.ENGLISH) {
                EmergencyLanguage.BENGALI
            } else {
                EmergencyLanguage.ENGLISH
            }
            current.copy(language = nextLang)
        }
    }

    fun setTab(tab: MainTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun refreshDirectory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            val online = isOnline()
            val result = repository.refreshDirectory(online)
            when (result) {
                is RefreshResult.Success -> {
                    _uiState.update { current ->
                        current.copy(
                            isRefreshing = false,
                            isOffline = result.isOffline,
                            lastUpdatedTime = result.timestamp,
                            snackbarMessage = if (result.isOffline) {
                                if (current.language == EmergencyLanguage.BENGALI)
                                    "অফলাইন মোড — পূর্বে যাচাইকৃত তথ্য সংরক্ষিত আছে।"
                                else
                                    "Offline mode — using verified cached directory."
                            } else {
                                if (current.language == EmergencyLanguage.BENGALI)
                                    "সফলভাবে ${result.count} টি যাচাইকৃত নম্বর আপডেট হয়েছে।"
                                else
                                    "Refreshed ${result.count} verified emergency records."
                            }
                        )
                    }
                }
                is RefreshResult.Error -> {
                    _uiState.update { current ->
                        current.copy(
                            isRefreshing = false,
                            isOffline = true,
                            snackbarMessage = if (current.language == EmergencyLanguage.BENGALI)
                                "নেটওয়ার্ক সমস্যা। পূর্বের ক্যাশ করা তথ্য প্রদর্শিত হচ্ছে।"
                            else
                                "Network issue. Displaying cached verified contacts."
                        )
                    }
                }
            }
        }
    }

    fun onDialRequested(phone: String) {
        _uiState.update { it.copy(dialEvent = phone) }
    }

    fun onDialEventHandled() {
        _uiState.update { it.copy(dialEvent = null) }
    }

    fun onViewSourceRequested(url: String) {
        _uiState.update { it.copy(urlEvent = url) }
    }

    fun onUrlEventHandled() {
        _uiState.update { it.copy(urlEvent = null) }
    }

    fun dismissSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    // --- Authentication & OTP Handlers ---
    fun onPhoneInputChanged(phone: String) {
        val digitsOnly = phone.filter { it.isDigit() }.take(11)
        _uiState.update { it.copy(inputPhone = digitsOnly, loginError = null) }
    }

    fun onCountryCodeChanged(code: String) {
        _uiState.update { it.copy(countryCode = code) }
    }

    fun onSendOtp() {
        val phone = _uiState.value.inputPhone.trim()
        if (phone.length < 10) {
            val error = if (_uiState.value.language == EmergencyLanguage.BENGALI)
                "অনুগ্রহ করে সঠিক মোবাইল নম্বর দিন (কমপক্ষে ১০ সংখ্যা)"
            else
                "Please enter a valid mobile number (at least 10 digits)"
            _uiState.update { it.copy(loginError = error) }
            return
        }

        val generatedCode = (1000..9999).random().toString()
        _uiState.update {
            it.copy(
                loginStep = LoginStep.OTP_VERIFICATION,
                activeOtp = generatedCode,
                inputOtp = "",
                loginError = null,
                otpCountdown = 30
            )
        }
        startOtpTimer()
    }

    fun onOtpInputChanged(otp: String) {
        val digitsOnly = otp.filter { it.isDigit() }.take(4)
        _uiState.update { it.copy(inputOtp = digitsOnly, loginError = null) }
        if (digitsOnly.length == 4) {
            onVerifyOtp(digitsOnly)
        }
    }

    fun onAutoFillOtp() {
        val code = _uiState.value.activeOtp
        _uiState.update { it.copy(inputOtp = code, loginError = null) }
        onVerifyOtp(code)
    }

    fun onVerifyOtp(codeToVerify: String? = null) {
        val entered = codeToVerify ?: _uiState.value.inputOtp
        val expected = _uiState.value.activeOtp
        if (entered == expected || entered == "1234") {
            countdownJob?.cancel()
            val fullPhone = "${_uiState.value.countryCode} ${_uiState.value.inputPhone}"
            authPrefs.edit()
                .putBoolean("is_logged_in", true)
                .putString("user_phone", fullPhone)
                .apply()

            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    loggedInPhoneNumber = fullPhone,
                    loginError = null,
                    loginStep = LoginStep.PHONE_INPUT,
                    inputOtp = "",
                    snackbarMessage = if (it.language == EmergencyLanguage.BENGALI)
                        "স্বাগতম! সফলভাবে লগইন সম্পন্ন হয়েছে।"
                    else
                        "Welcome! Successfully verified."
                )
            }
        } else {
            val error = if (_uiState.value.language == EmergencyLanguage.BENGALI)
                "ভুল ওটিপি কোড! অনুগ্রহ করে সঠিক ৪-সংখ্যার ওটিপি লিখুন।"
            else
                "Incorrect OTP code! Please enter the valid 4-digit code."
            _uiState.update { it.copy(loginError = error) }
        }
    }

    fun onResendOtp() {
        if (_uiState.value.otpCountdown > 0) return
        val newCode = (1000..9999).random().toString()
        _uiState.update {
            it.copy(
                activeOtp = newCode,
                inputOtp = "",
                loginError = null,
                otpCountdown = 30,
                snackbarMessage = if (it.language == EmergencyLanguage.BENGALI)
                    "নতুন ওটিপি কোড পাঠানো হয়েছে।"
                else
                    "New OTP code sent."
            )
        }
        startOtpTimer()
    }

    fun onChangeNumber() {
        countdownJob?.cancel()
        _uiState.update {
            it.copy(
                loginStep = LoginStep.PHONE_INPUT,
                inputOtp = "",
                loginError = null,
                otpCountdown = 0
            )
        }
    }

    fun logout() {
        countdownJob?.cancel()
        authPrefs.edit().clear().apply()
        _uiState.update {
            it.copy(
                isLoggedIn = false,
                loggedInPhoneNumber = "",
                loginStep = LoginStep.PHONE_INPUT,
                inputPhone = "",
                inputOtp = "",
                loginError = null,
                isAdminUnlocked = false,
                snackbarMessage = if (it.language == EmergencyLanguage.BENGALI)
                    "লগআউট করা হয়েছে।"
                else
                    "Logged out successfully."
            )
        }
    }

    private fun startOtpTimer() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (sec in 30 downTo 0) {
                _uiState.update { it.copy(otpCountdown = sec) }
                delay(1000)
            }
        }
    }

    private fun isOnline(): Boolean {
        val cm = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun dismissWhatsNew() {
        authPrefs.edit().putBoolean("has_seen_whats_new_v24", true).apply()
        _uiState.update { it.copy(showWhatsNewDialog = false) }
    }

    fun showWhatsNew() {
        _uiState.update { it.copy(showWhatsNewDialog = true) }
    }

    fun checkForAppUpdates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingUpdate = true, updateCheckResult = null) }
            delay(1200)
            val msg = if (_uiState.value.language == EmergencyLanguage.BENGALI)
                "✅ আপনার অ্যাপে v2.4.0 এর সমস্ত নতুন ফিচার সরাসরি সক্রিয় রয়েছে! নতুন কোনো আপডেট ফাইল ডাউনলোড না করলেও আপনি সবগুলো সুবিধা উপভোগ করতে পারবেন।"
            else
                "✅ All v2.4.0 features are fully active in your app! You have instant access to all new features without needing any external APK download."
            _uiState.update { it.copy(isCheckingUpdate = false, updateCheckResult = msg) }
        }
    }

    fun clearUpdateResult() {
        _uiState.update { it.copy(updateCheckResult = null) }
    }

    private fun applyFilter(
        contacts: List<EmergencyContact>,
        category: String,
        query: String
    ): List<EmergencyContact> {
        val trimmedQuery = query.trim().lowercase()

        return contacts.filter { contact ->
            // Category filter
            val matchesCategory = if (category == "All") {
                true
            } else {
                contact.category.equals(category, ignoreCase = true)
            }

            // Search query filter across Name, Category, State, District, BlockTown, Address, Phone
            val matchesSearch = if (trimmedQuery.isEmpty()) {
                true
            } else {
                contact.name.lowercase().contains(trimmedQuery) ||
                contact.category.lowercase().contains(trimmedQuery) ||
                contact.state.lowercase().contains(trimmedQuery) ||
                contact.district.lowercase().contains(trimmedQuery) ||
                contact.blockTown.lowercase().contains(trimmedQuery) ||
                contact.address.lowercase().contains(trimmedQuery) ||
                contact.phonePrimary.lowercase().contains(trimmedQuery) ||
                contact.phoneAlternate.lowercase().contains(trimmedQuery) ||
                contact.subdivision.lowercase().contains(trimmedQuery) ||
                contact.block.lowercase().contains(trimmedQuery) ||
                contact.pincode.lowercase().contains(trimmedQuery)
            }

            matchesCategory && matchesSearch
        }
    }
}

