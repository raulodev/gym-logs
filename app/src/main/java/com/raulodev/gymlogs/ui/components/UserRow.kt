package com.raulodev.gymlogs.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raulodev.gymlogs.database.Payment
import com.raulodev.gymlogs.database.User
import com.raulodev.gymlogs.database.UserAndCurrentPaymentDataClass
import com.raulodev.gymlogs.ui.theme.GymLogsTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserRow(
    data: UserAndCurrentPaymentDataClass,
    paid: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onLongClick: (data: UserAndCurrentPaymentDataClass) -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .combinedClickable(onLongClick = {
                    onLongClick(data)
                }) {}) {
            Text(
                "${data.user.name}",
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        }

        Checkbox(checked = paid, onCheckedChange = {
            onCheckedChange(it)
        })
    }
}

@Preview(
    uiMode = UI_MODE_NIGHT_YES, name = "UserRowDark", showBackground = true
)
@Preview(showBackground = true)
@Composable
fun UserRowPreview() {
    GymLogsTheme {
        Surface {
            Column {
                UserRow(data = UserAndCurrentPaymentDataClass(
                    user = User(name = "Raul enrique Cobiellas", gender = "Masculino"),
                    payment = Payment(year = 2025, month = 1, paymentOwnerId = 1)
                ), paid = false, onCheckedChange = {}, onLongClick = {})
            }
        }
    }
}




