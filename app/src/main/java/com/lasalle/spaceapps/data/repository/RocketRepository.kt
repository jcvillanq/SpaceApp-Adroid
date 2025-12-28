package com.lasalle.spaceapps.data.repository

import com.lasalle.spaceapps.data.local.RocketDao
import com.lasalle.spaceapps.data.model.Rocket
import com.lasalle.spaceapps.data.remote.SpaceXApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RocketRepository(
    private val api: SpaceXApi,
    private val dao: RocketDao
) {
    fun getRockets(): Flow<Result<List<Rocket>>> = flow {
        emit(Result.Loading)

        try {
            // Intenta obtener de la API
            val rockets = api.getRockets()

            // Guarda en la base de datos local
            dao.deleteAllRockets()
            dao.insertRockets(rockets)

            emit(Result.Success(rockets))
        } catch (e: Exception) {
            // Si falla, intenta cargar de la base de datos local
            dao.getAllRockets().collect { localRockets ->
                if (localRockets.isNotEmpty()) {
                    emit(Result.Success(localRockets))
                } else {
                    emit(Result.Error("No se pudieron cargar los cohetes. Verifica tu conexión."))
                }
            }
        }
    }

    suspend fun getRocketById(id: String): Rocket? {
        return dao.getRocketById(id)
    }
}

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}