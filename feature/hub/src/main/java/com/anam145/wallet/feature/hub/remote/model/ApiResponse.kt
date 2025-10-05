package com.anam145.wallet.feature.hub.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Generic API response wrapper from AnamHub server
 */
@Keep
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: T?
)