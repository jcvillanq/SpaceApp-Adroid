package com.lasalle.spaceapps.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

@Entity(tableName = "rockets")
data class Rocket(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: String? = null,
    val active: Boolean = false,
    val stages: Int? = null,
    val boosters: Int? = null,
    @SerializedName("cost_per_launch")
    val costPerLaunch: Int? = null,
    @SerializedName("success_rate_pct")
    val successRatePct: Int? = null,
    @SerializedName("first_flight")
    val firstFlight: String? = null,
    val country: String? = null,
    val company: String? = null,
    val wikipedia: String? = null,
    val description: String? = null,
    @SerializedName("flickr_images")
    val flickrImages: List<String> = emptyList(),
    val height: Height? = null,
    val diameter: Diameter? = null,
    val mass: Mass? = null
)

data class Height(
    val meters: Double? = null,
    val feet: Double? = null
)

data class Diameter(
    val meters: Double? = null,
    val feet: Double? = null
)

data class Mass(
    val kg: Int? = null,
    val lb: Int? = null
)

// Converters para Room (para guardar listas y objetos complejos)
class Converters {
    @TypeConverter
    fun fromStringList(value: String?): List<String> {
        if (value == null) return emptyList()
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListString(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromHeight(height: Height?): String? {
        return if (height == null) null else Gson().toJson(height)
    }

    @TypeConverter
    fun toHeight(heightString: String?): Height? {
        return if (heightString == null) null else Gson().fromJson(heightString, Height::class.java)
    }

    @TypeConverter
    fun fromDiameter(diameter: Diameter?): String? {
        return if (diameter == null) null else Gson().toJson(diameter)
    }

    @TypeConverter
    fun toDiameter(diameterString: String?): Diameter? {
        return if (diameterString == null) null else Gson().fromJson(diameterString, Diameter::class.java)
    }

    @TypeConverter
    fun fromMass(mass: Mass?): String? {
        return if (mass == null) null else Gson().toJson(mass)
    }

    @TypeConverter
    fun toMass(massString: String?): Mass? {
        return if (massString == null) null else Gson().fromJson(massString, Mass::class.java)
    }
}