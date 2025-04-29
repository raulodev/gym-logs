package com.raulodev.gymlogs.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raulodev.gymlogs.ui.theme.GymLogsTheme

@Composable
fun Modal(
    label: String, isVisible: Boolean = false, onClose: () -> Unit, content: @Composable () -> Unit
) {

    AnimatedVisibility(isVisible) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1F))
                .fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .padding(20.dp),

                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {

                    Text(label, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    IconButton(onClick = onClose) {
                        Icon(Icons.Filled.Close, contentDescription = "close")
                    }
                }
                content()
            }
        }
    }
}


@Preview(
    uiMode = UI_MODE_NIGHT_YES, name = "ModalDark", showBackground = true
)
@Preview(showBackground = true)
@Composable
fun ModalPreview() {
    GymLogsTheme {
        Surface {
            Column {
                Modal(label = "Editar usuario", isVisible = true, onClose = {}) {
                    Text("Contenido")
                }
            }
        }
    }
}