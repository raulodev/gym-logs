package com.raulodev.gymlogs.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.raulodev.gymlogs.ui.theme.GymLogsTheme
import com.raulodev.gymlogs.ui.theme.danger
import com.raulodev.gymlogs.ui.theme.successLight

@Composable
fun TextRadioButton(label: String, selected: Boolean, onClick: () -> Unit, color: Color? = null) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected, onClick = onClick, colors = RadioButtonDefaults.colors(
                selectedColor = color ?: successLight
            )
        )
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
                TextRadioButton(
                    label = "Hombre",
                    selected = true,
                    onClick = {},
                    color = successLight
                )
                TextRadioButton(label = "Mujer", selected = true, onClick = {}, color = danger)
                TextRadioButton(label = "Sin definir", selected = false, onClick = {})
            }
        }
    }
}
