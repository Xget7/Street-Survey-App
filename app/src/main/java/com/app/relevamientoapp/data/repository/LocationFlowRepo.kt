package com.app.relevamientoapp.data.repository

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class LocationFlowRepo {
    private val mData: Flow<Location> = flowOf()


    suspend fun getFlow() : Flow<Location> {
        return mData
    }
}