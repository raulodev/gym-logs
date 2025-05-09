package com.raulodev.gymlogs.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raulodev.gymlogs.enums.Gender
import com.raulodev.gymlogs.ui.theme.GymLogsTheme


@Composable
fun FilterDropdown(onSelect: (gender: String) -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    var gender by rememberSaveable { mutableStateOf("") }

    fun setButtonTitle(): String {

        if (gender.isBlank()){
            return "Filtrar"
        } else if (gender.equals(Gender.Male.name)){
            return "Hombres"
        }else if (gender.equals(Gender.Female.name)){
            return "Mujeres"
        }
        else {
            return "Sin definir"
        }
    }

    Box {
        TextButton(onClick = {
            expanded = true
        }) {
            Text(setButtonTitle())
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Text("Todos")
                        if (gender == "") Icon(Icons.Filled.Check, contentDescription = "Check")
                    }
                },
                onClick = {
                    gender = ""
                    onSelect(gender)
                },
            )
            DropdownMenuItem(text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Text("Hombres")
                    if (gender == Gender.Male.name) Icon(Icons.Filled.Check, contentDescription = "Check")
                }

            }, onClick = {
                gender = Gender.Male.name
                onSelect(gender)
            })
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Text("Mujeres")
                        if (gender == Gender.Female.name) Icon(Icons.Filled.Check, contentDescription = "Check")
                    }
                },
                onClick = {
                    gender = Gender.Female.name
                    onSelect(gender)
                },
            )
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Text("Sin definir")
                        if (gender == Gender.Unknown.name) Icon(Icons.Filled.Check, contentDescription = "Check")
                    }
                },
                onClick = {
                    gender = Gender.Unknown.name
                    onSelect(gender)
                },
            )
        }
    }
}


@Preview(
    uiMode = UI_MODE_NIGHT_YES,
    name = "FilterDropdownDark",
    showBackground = true,
    heightDp = 200,
    widthDp = 200
)
@Preview(showBackground = true, heightDp = 200, widthDp = 200)
@Composable
fun FilterDropdownPreview() {
    GymLogsTheme {
        Surface {
            FilterDropdown(onSelect = {})
        }
    }
}