package com.raulodev.gymlogs.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(
    label: String,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(5.dp),
        colors = colors
    ) {
        Text(
            label, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
        )
    }
}