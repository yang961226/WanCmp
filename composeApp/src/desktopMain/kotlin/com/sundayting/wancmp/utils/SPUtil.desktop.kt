package com.sundayting.wancmp.utils

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.sundayting.wancmp.utils.SPUtil.createInstance
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okio.Path.Companion.toPath

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object SPUtil : SharedPreferencesOperation by DataStoreOperationDelegate({ createInstance("default") }) {

    private var uid = "11"
    private val cacheMutex = Mutex()
    private val userSpecificCache = mutableMapOf<String, SharedPreferencesOperation>()

    actual val userSpecific: SharedPreferencesOperation
        get() {
            userSpecificCache[uid]?.let { return it }
            return runBlocking {
                cacheMutex.withLock {
                    userSpecificCache[uid]?.let { return@let it }
                    DataStoreOperationDelegate { createInstance(uid) }.also {
                        userSpecificCache[uid] = it
                    }
                }
            }
        }

    private fun createInstance(
        key: String
    ) = PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            ("../spStore/" + "${key.trim()}_${DATA_STORE_FILE_NAME}").toPath()
        }
    )

}