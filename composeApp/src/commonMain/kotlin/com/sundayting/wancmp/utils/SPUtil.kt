package com.sundayting.wancmp.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object SPUtil : SharedPreferencesOperation {

    val userSpecific: SharedPreferencesOperation

    override suspend fun saveBool(
        key: Preferences.Key<Boolean>,
        value: Boolean
    )

    override suspend fun saveInt(
        key: Preferences.Key<Int>,
        value: Int
    )

    override suspend fun saveLong(
        key: Preferences.Key<Long>,
        value: Long
    )

    override suspend fun saveFloat(
        key: Preferences.Key<Float>,
        value: Float
    )

    override suspend fun saveDouble(
        key: Preferences.Key<Double>,
        value: Double
    )

    override suspend fun saveString(
        key: Preferences.Key<String>,
        value: String
    )

    override suspend fun getBool(
        key: Preferences.Key<Boolean>,
        defaultValue: Boolean
    ): Boolean

    override suspend fun getInt(
        key: Preferences.Key<Int>,
        defaultValue: Int
    ): Int

    override suspend fun getLong(
        key: Preferences.Key<Long>,
        defaultValue: Long
    ): Long

    override suspend fun getFloat(
        key: Preferences.Key<Float>,
        defaultValue: Float
    ): Float

    override suspend fun getDouble(
        key: Preferences.Key<Double>,
        defaultValue: Double
    ): Double

    override suspend fun getString(
        key: Preferences.Key<String>,
        defaultValue: String?
    ): String?


}

internal const val DATA_STORE_FILE_NAME = "prefs.preferences_pb"

class DataStoreOperationDelegate(
    private val dataStoreFactory: () -> DataStore<Preferences>
) : SharedPreferencesOperation {

    private val dataStoreInstance by lazy(mode = LazyThreadSafetyMode.NONE) { dataStoreFactory() }

    override suspend fun saveBool(
        key: Preferences.Key<Boolean>,
        value: Boolean
    ) {
        dataStoreInstance.edit { it[key] = value }
    }

    override suspend fun saveInt(
        key: Preferences.Key<Int>,
        value: Int
    ) {
        dataStoreInstance.edit { it[key] = value }
    }

    override suspend fun saveLong(
        key: Preferences.Key<Long>,
        value: Long
    ) {
        dataStoreInstance.edit { it[key] = value }
    }

    override suspend fun saveFloat(
        key: Preferences.Key<Float>,
        value: Float
    ) {
        dataStoreInstance.edit { it[key] = value }
    }

    override suspend fun saveDouble(
        key: Preferences.Key<Double>,
        value: Double
    ) {
        dataStoreInstance.edit { it[key] = value }
    }

    override suspend fun saveString(
        key: Preferences.Key<String>,
        value: String
    ) {
        dataStoreInstance.edit { it[key] = value }
    }

    override suspend fun getBool(
        key: Preferences.Key<Boolean>,
        defaultValue: Boolean
    ): Boolean = dataStoreInstance.data.map { it[key] }.first() ?: defaultValue

    override suspend fun getInt(
        key: Preferences.Key<Int>,
        defaultValue: Int
    ): Int = dataStoreInstance.data.map { it[key] }.first() ?: defaultValue

    override suspend fun getLong(
        key: Preferences.Key<Long>,
        defaultValue: Long
    ): Long = dataStoreInstance.data.map { it[key] }.first() ?: defaultValue

    override suspend fun getFloat(
        key: Preferences.Key<Float>,
        defaultValue: Float
    ): Float = dataStoreInstance.data.map { it[key] }.first() ?: defaultValue

    override suspend fun getDouble(
        key: Preferences.Key<Double>,
        defaultValue: Double
    ): Double = dataStoreInstance.data.map { it[key] }.first() ?: defaultValue

    override suspend fun getString(
        key: Preferences.Key<String>,
        defaultValue: String?
    ): String? = dataStoreInstance.data.map { it[key] }.first() ?: defaultValue

}


/**
 * 操作sp的方法集
 */
interface SharedPreferencesOperation {

    suspend fun saveBool(key: Preferences.Key<Boolean>, value: Boolean)

    suspend fun saveInt(key: Preferences.Key<Int>, value: Int)

    suspend fun saveLong(key: Preferences.Key<Long>, value: Long)

    suspend fun saveFloat(key: Preferences.Key<Float>, value: Float)

    suspend fun saveDouble(key: Preferences.Key<Double>, value: Double)

    suspend fun saveString(key: Preferences.Key<String>, value: String)

    suspend fun getBool(key: Preferences.Key<Boolean>, defaultValue: Boolean = false): Boolean

    suspend fun getInt(key: Preferences.Key<Int>, defaultValue: Int = 0): Int

    suspend fun getLong(key: Preferences.Key<Long>, defaultValue: Long = 0L): Long

    suspend fun getFloat(key: Preferences.Key<Float>, defaultValue: Float = 0f): Float

    suspend fun getDouble(key: Preferences.Key<Double>, defaultValue: Double = 0.0): Double

    suspend fun getString(key: Preferences.Key<String>, defaultValue: String? = null): String?

}