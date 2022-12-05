package com.example.myapplication.Data.Local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.Asteroid


@Entity(tableName = "asteroid_table")
class AsteroidTable(
    @PrimaryKey
    var id: Long = 0L,
    var codename: String,

    @ColumnInfo(name = "closeApproachDate")
    var closeApproachDate: String,

    @ColumnInfo(name = "absoluteMagnitude")
    var absoluteMagnitude: Double,

    @ColumnInfo(name = "estimatedDiameter")
    var estimatedDiameter: Double,

    @ColumnInfo(name = "relativeVelocity")
    var relativeVelocity: Double,

    @ColumnInfo(name = "distanceFromEarth")
    var distanceFromEarth: Double,

    @ColumnInfo(name = "isPotentiallyHazardous")
    var isPotentiallyHazardous: Boolean


)

fun ArrayList<Asteroid>.asDomainModel(): Array<AsteroidTable> {
    return map {
        AsteroidTable(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
        .toTypedArray()
}