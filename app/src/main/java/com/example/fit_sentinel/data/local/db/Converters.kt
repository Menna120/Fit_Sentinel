package com.example.fit_sentinel.data.local.db

import androidx.room.TypeConverter
import com.example.fit_sentinel.domain.model.Gender

class Converters {
    @TypeConverter
    fun fromGender(gender: Gender): String {
        return gender.name
    }

    @TypeConverter
    fun toGender(genderString: String): Gender {
        return try {
            Gender.valueOf(genderString)
        } catch (_: IllegalArgumentException) {
            Gender.Male
        }
    }

}