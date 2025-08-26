package com.cpen321.usermanagement.config

object AppConfig {
    // API Configuration
    // For Android emulator:
    const val API_BASE_URL = "http://10.0.2.2:3000/api/"
    const val IMAGE_BASE_URL = "http://10.0.2.2:3000"
    
    // For real device testing, update these values:
    // const val API_BASE_URL = "http://YOUR_LOCAL_IP:3000/api/"
    // const val IMAGE_BASE_URL = "http://YOUR_LOCAL_IP:3000"
    
    // For production, update these values:
    // const val API_BASE_URL = "https://your-production-domain.com/api/"
    // const val IMAGE_BASE_URL = "https://your-production-domain.com"
    
    // Other configuration constants
    const val CONNECT_TIMEOUT_SECONDS = 30L
    const val READ_TIMEOUT_SECONDS = 30L
    const val WRITE_TIMEOUT_SECONDS = 30L
}
