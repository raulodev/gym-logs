package com.raulodev.gymlogs.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raulodev.gymlogs.ui.theme.GymLogsTheme


@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "placeholder",
    trailingIcon: @Composable() (() -> Unit)? = null
) {

    val shape = RoundedCornerShape(5.dp)

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        singleLine = true,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        shape = shape,
        trailingIcon = trailingIcon
    )
}


@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "InputDark",
)
@Preview(showBackground = true)
@Composable
fun InputView() {
    var value by rememberSaveable { mutableStateOf("") }

    GymLogsTheme {
        Surface {
            Input(value, onValueChange = { value = it }, placeholder = "Nombre del gimnasio")
        }
    }
}