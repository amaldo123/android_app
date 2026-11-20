# Android Release Keystores & Signing Information

This directory contains dedicated, distinct release keystores for each app flavor.

| App Flavor | Keystore File | Alias | Store & Key Password | SHA-1 Fingerprint |
| :--- | :--- | :--- | :--- | :--- |
| **techRadar** | `techradar.jks` | `techradar` | `techradar_release_2026` | `F1:48:1D:56:BF:DE:7C:EB:E6:36:DB:06:8C:5A:BD:FD:8A:5A:B6:E3` |
| **sportsFlash** | `sportsflash.jks` | `sportsflash` | `sportsflash_release_2026` | `7C:4B:BC:CA:FB:3B:27:AE:9E:9B:72:FD:83:A6:61:A4:C6:FB:43:6F` |
| **marketPulse** | `marketpulse.jks` | `marketpulse` | `marketpulse_release_2026` | `FC:2E:16:51:06:8D:B3:2A:56:D2:60:13:C9:46:8C:8E:E7:2B:5B:1B` |
| **horizonTravel** | `horizontravel.jks` | `horizontravel` | `horizontravel_release_2026` | `3E:8E:42:33:3E:9C:D9:37:6A:12:D9:63:70:0E:C5:FC:97:CA:D9:CD` |
| **dailyDigest** | `dailydigest.jks` | `dailydigest` | `dailydigest_release_2026` | `E3:D2:86:33:44:66:26:A6:2C:C9:F5:32:EC:17:4E:6E:36:00:4A:2A` |
| **newsPulse** | `newspulse.jks` | `newspulse` | `newspulse_release_2026` | `6D:81:AE:68:F0:C8:43:6E:98:91:9D:DB:5D:31:62:3C:51:A9:D0:28` |

### Keystore Specifications:
- **Format:** PKCS12
- **Algorithm:** RSA 2048-bit
- **Validity:** 10,000 days (approx. 27 years)
- **Signature:** SHA384withRSA
