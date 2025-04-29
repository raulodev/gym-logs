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
import com.raulodev.gymlogs.ui.theme.GymLogsTheme


@Composable
fun SortDropdown(onSelect: (isAsc: Boolean) -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    var isAsc by rememberSaveable { mutableStateOf(true) }

    Box {
        TextButton(onClick = {
            expanded = true
        }) {
            Text("Ordenar")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Text("Ordenar A-Z")
                    if (isAsc) Icon(Icons.Filled.Check, contentDescription = "Check")
                }

            }, onClick = {
                isAsc = true
                onSelect(true)
            })
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Text("Ordenar Z-A")
                        if (!isAsc) Icon(Icons.Filled.Check, contentDescription = "Check")
                    }
                },
                onClick = {
                    isAsc = false
                    onSelect(false)
                },
            )
        }
    }
}


@Preview(
    uiMode = UI_MODE_NIGHT_YES,
    name = "CustomDropdownDark",
    showBackground = true,
    heightDp = 200,
    widthDp = 200
)
@Preview(showBackground = true, heightDp = 200, widthDp = 200)
@Composable
fun CustomDropdownPreview() {
    GymLogsTheme {
        Surface {
            SortDropdown(onSelect = {})
        }
    }
}