package com.raulodev.gymlogs.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.raulodev.gymlogs.database.User
import com.raulodev.gymlogs.database.UserAndCurrentPaymentDataClass
import com.raulodev.gymlogs.enums.Gender


@Composable
fun EditUserModal(
    data: UserAndCurrentPaymentDataClass? = null,
    isVisible: Boolean,
    onClose: () -> Unit,
    onSave: (user: User) -> Unit
) {

    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Gender.Unknown.name) }

    if (data != null) {

        if (data.user.name != null) {
            name = data.user.name
        }

        if (data.user.gender != null) {
            gender = data.user.gender
        }
    }

    Modal(label = "Editar usuario", isVisible = isVisible, onClose = onClose) {
        Input(
            value = name,
            onValueChange = { name = it },
            placeholder = "Nombre",
        )

        Row {
            TextRadioButton("Hombre",
                selected = gender == Gender.Male.name,
                onClick = { gender = Gender.Male.name })
            TextRadioButton("Mujer",
                selected = gender == Gender.Female.name,
                onClick = { gender = Gender.Female.name })
        }

        Row {
            Box(modifier = Modifier.weight(1F)) {
                CustomButton("Guardar", onClick = {
                    if (data != null) {
                        val user = data.user.copy(name = name, gender = gender)
                        onSave(user)
                    }
                })
            }
        }
    }
}