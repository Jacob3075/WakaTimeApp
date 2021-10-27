package com.jacob.wakatimeapp.common.data

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<UserDetailsStore> {
    override val defaultValue: UserDetailsStore = UserDetailsStore.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserDetailsStore {
        try {
            return UserDetailsStore.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: UserDetailsStore,
        output: OutputStream
    ) = t.writeTo(output)
}

val Context.userDetailsDataStore: DataStore<UserDetailsStore> by dataStore(
    fileName = "userDetails.pb",
    serializer = SettingsSerializer
)
