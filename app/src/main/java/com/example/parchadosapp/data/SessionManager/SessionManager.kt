package com.example.parchadosapp.data.SessionManager

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_session")

object SessionManager {
    private val USER_ID = stringPreferencesKey("user_id")

    suspend fun saveUserId(context: Context, userId: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = userId
        }
    }

    suspend fun getUserId(context: Context): String? {
        return context.dataStore.data
            .map { prefs -> prefs[USER_ID] }
            .first()
    }

    suspend fun clearSession(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}
