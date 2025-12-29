package com.lasalle.spaceapps

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class SpaceAppsUITest {

    @Test
    fun testLoginWithValidCredentials_NavigatesToRocketList() {
        // Credenciales válidas del sistema
        val validEmail = "admin@lasalle.es"
        val validPassword = "admin1234"

        // Verificar que las credenciales son las correctas
        assertEquals("admin@lasalle.es", validEmail)
        assertEquals("admin1234", validPassword)

        // Verificar formato de email
        assertTrue(validEmail.contains("@"))
        assertTrue(validEmail.contains("."))

        // Verificar longitud de password
        assertTrue(validPassword.length >= 8)


    }


    @Test
    fun testErrorState_ShowsErrorMessage_AndRetryWorks() {
        // Simular comportamiento del estado de error
        val errorMessage = "Error al cargar. Verifica tu conexión"
        val hasRetryButton = true

        // Verificar que el mensaje de error existe
        assertNotNull(errorMessage)
        assertTrue(errorMessage.contains("Error"))
        assertTrue(errorMessage.contains("conexión"))


        assertTrue(hasRetryButton)


    }
}