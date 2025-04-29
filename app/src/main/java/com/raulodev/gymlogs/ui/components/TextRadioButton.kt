package com.raulodev.gymlogs.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.raulodev.gymlogs.ui.theme.GymLogsTheme

@Composable
fun TextRadioButton(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = onClick)
        Text(label)
    }
}


@Preview(
    uiMode = UI_MODE_NIGHT_YES, name = "TextRadioButtonDark", showBackground = true
)
@Preview(showBackground = true)
@Composable
fun TextRadioButtonPreview() {
    GymLogsTheme {
        Surface {
            Column {
                TextRadioButton(label = "Masculino", selected = true, onClick = {})
                TextRadioButton(label = "Femenino", selected = false, onClick = {})
                TextRadioButton(label = "Sin establecer", selected = false, onClick = {})
            }
        }
    }
}
