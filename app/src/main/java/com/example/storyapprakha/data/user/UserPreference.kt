package com.example.storyapprakha.data.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreference private constructor(private val dataStore : DataStore<Preferences>) {
    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val TOKEN = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }


    suspend fun saveToken(token :String){
        dataStore.edit {
            it[TOKEN] = token
        }
    }

    suspend fun deleteToken(){
        dataStore.edit {
            it.remove(TOKEN)
        }
    }

    fun getToken():Flow<String>{
        return dataStore.data.map{
            it[TOKEN]?:""
        }
    }
}

