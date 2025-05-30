package com.sundayting.wancmp.utils

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.sundayting.wancmp.utils.SPUtil.createInstance
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object SPUtil :
    SharedPreferencesOperation by DataStoreOperationDelegate({ createInstance("default") }) {

    private var uid = "20"
    private val cacheMutex = Mutex()
    private val userSpecificCache = mutableMapOf<String, SharedPreferencesOperation>()

    @OptIn(ExperimentalForeignApi::class)
    private fun createInstance(
        key: String
    ) = PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            val directory = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null
            )
            (requireNotNull(directory).path + "/${key.trim()}_${DATA_STORE_FILE_NAME}").toPath()
        }
    )

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


}