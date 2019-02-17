package ru.ic218.mychatapp.data.repository

import android.content.SharedPreferences
import ru.ic218.mychatapp.domain.model.PrefRepository

private const val PREF_IS_COMPLETE_INSTALL = "pre_is_complete_install"
private const val PREF_IS_OWNER_ID = "pre_is_owner_id"
private const val PREF_IS_OWNER_NAME = "pre_is_owner_name"

class PrefRepositoryImpl(private val sp: SharedPreferences): PrefRepository {

    override var isCompleteInstall: Boolean
        get() = sp.getBoolean(PREF_IS_COMPLETE_INSTALL, false)
        set(value) {
            sp.edit().putBoolean(PREF_IS_COMPLETE_INSTALL, value).apply()
        }

    override var ownerId: Int
        get() = sp.getInt(PREF_IS_OWNER_ID, -1)
        set(value) {
            sp.edit().putInt(PREF_IS_OWNER_ID, value).apply()
        }

    override var ownerName: String
        get() = sp.getString(PREF_IS_OWNER_NAME, "")
        set(value) {
            sp.edit().putString(PREF_IS_OWNER_NAME, value).apply()
        }
}