# BM Emergency Global 24 HS — Secure Admin Panel 🌍🆘

This is the completely separate, secure web-based Admin Panel for **BM Emergency Global 24 HS**.
Per the strict system architecture requirements, **this Admin Panel is decoupled from the public Android APK/AAB** and is never accessible to regular public mobile app users.

## 🔐 Security Architecture

- **Public App Isolation**: The Android APK/AAB only has read-only access to published, verified emergency records. No administrative routes, tokens, credentials, or write endpoints exist in the mobile app.
- **Authentication & Authorization**: Protected with secure admin login, session tokens, and automated session expiry.
- **Controlled Publishing Workflow**:
  A contact record CANNOT become publicly visible (`status = Active`) unless:
  1. `phonePrimary` is a valid emergency or telecom number.
  2. `sourceUrl` exists and uses **HTTPS**.
  3. `verified = Yes`.
  4. `verifiedAt` date exists.
  5. `reviewDue` date has not expired.
- **Immutable Audit Trail**: Every add, edit, verify, unverfiy, activate, deactivate, or delete action generates a timestamped entry in the Audit Log with previous and new values.

## 📁 Key Features

1. **Operations Dashboard**: Real-time stats (Total, Active, Verified, Needs Review, Expired, Inactive), category distribution chart, and publishing health gauges.
2. **Directory Management**: Searchable and filterable table with full editing capabilities.
3. **Review Queue**: Quarantines records that are expired, expiring soon, unverified, or have missing HTTPS sources. Provides a 1-click "Re-Verify (6 Mo)" workflow.
4. **Add / Edit Contact**: Form with real-time field validation matching the schema:
   `id`, `category`, `status`, `name`, `phonePrimary`, `phoneAlternate`, `state`, `district`, `blockTown`, `address`, `sourceUrl`, `verified`, `verifiedAt`, `reviewDue`, `notes`.
5. **Import / Export**:
   - Import CSV with automatic row-by-row validation (flags invalid phone numbers, non-HTTPS URLs, or expired records).
   - Export CSV backup for administration.
   - Export production JSON (`emergency_directory.json`) for the mobile app or cloud CDN sync.
6. **Google Sheets Sync**: Optional synchronization from an official administrative Google Sheets spreadsheet.

## 🚀 Running the Admin Panel

The Admin Panel runs in any modern browser or can be hosted on a secure private cloud server (e.g., Cloudflare Pages, Firebase Hosting, AWS S3 + CloudFront):

```bash
# To run locally:
cd admin-panel
python3 -m http.server 8080
# Open http://localhost:8080 in your browser
```

Default administrator credentials:
- **Email**: `admin@bmemergency.org`
- **Password**: `Admin@24hsEmergency!`
