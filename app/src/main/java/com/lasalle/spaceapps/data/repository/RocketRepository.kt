package com.lasalle.spaceapps.data.repository

import android.util.Log
import com.lasalle.spaceapps.data.local.RocketDao
import com.lasalle.spaceapps.data.model.Rocket
import com.lasalle.spaceapps.data.remote.SpaceXApi

class RocketRepository(
    private val api: SpaceXApi,
    private val rocketDao: RocketDao
) {
    suspend fun getRockets(): List<Rocket> {
        return try {
            Log.d("RocketRepository", "Llamando a la API...")
            val rockets = api.getRockets()
            Log.d("RocketRepository", "API respondió con ${rockets.size} cohetes")

            // Guardar en base de datos local
            Log.d("RocketRepository", "Guardando en BD local...")
            rocketDao.insertAll(rockets)
            Log.d("RocketRepository", "Guardado exitoso")

            rockets
        } catch (e: Exception) {
            Log.e("RocketRepository", "Error en API, intentando BD local", e)

            // Si falla, intentar cargar desde BD local
            val localRockets = rocketDao.getAllRockets()
            Log.d("RocketRepository", "BD local devolvió ${localRockets.size} cohetes")

            if (localRockets.isEmpty()) {
                Log.e("RocketRepository", "BD local también vacía, lanzando excepción")
                throw e
            }
            localRockets  // 
        }
    }

    suspend fun getRocketById(id: String): Rocket? {
        Log.d("RocketRepository", "Buscando cohete con ID: $id")
        val rocket = rocketDao.getRocketById(id)
        Log.d("RocketRepository", "Cohete encontrado: ${rocket?.name ?: "null"}")
        return rocket
    }
}