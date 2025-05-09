package com.raulodev.gymlogs.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.raulodev.gymlogs.database.AppDatabase
import com.raulodev.gymlogs.database.Payment
import com.raulodev.gymlogs.database.User
import com.raulodev.gymlogs.database.UserAndCurrentPaymentDataClass
import com.raulodev.gymlogs.enums.Gender
import com.raulodev.gymlogs.enums.IconEnum
import com.raulodev.gymlogs.enums.Routes
import com.raulodev.gymlogs.ui.components.EditUserModal
import com.raulodev.gymlogs.ui.components.FilterDropdown
import com.raulodev.gymlogs.ui.components.Input
import com.raulodev.gymlogs.ui.components.SortDropdown
import com.raulodev.gymlogs.ui.components.UserRow
import com.raulodev.gymlogs.ui.theme.GymLogsTheme
import com.raulodev.gymlogs.ui.theme.successLight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun UsersScreen(
    db: AppDatabase? = null, navegationController: NavHostController? = null
) {

    val coroutineScope = rememberCoroutineScope()

    var loading by remember { mutableStateOf(true) }
    var inputIcon by rememberSaveable { mutableStateOf(IconEnum.Search.name) }
    var isAsc by rememberSaveable { mutableStateOf(true) }
    var gender by rememberSaveable { mutableStateOf("") }
    val users = remember { mutableStateListOf<UserAndCurrentPaymentDataClass>() }
    var search by rememberSaveable { mutableStateOf("") }
    val userSelected = remember { mutableStateOf<UserAndCurrentPaymentDataClass?>(null) }


    suspend fun filterSortUsers() {
        try {
            var userData = db?.userDao()?.getAllUsersAndCurrentPayment(isAsc) ?: emptyList()

            if (userData.isEmpty() && search.isBlank()) {
                inputIcon = IconEnum.Search.name
                return
            }
            else if (userData.isEmpty() && search.isNotBlank()) {
                inputIcon = IconEnum.Add.name
                return
            }

            if (gender.isNotBlank() && search.isNotBlank()) {

                userData = userData.filter { u ->
                    u.user.gender.equals(gender) && u.user.name?.lowercase()
                        ?.contains(search.trim().lowercase()) ?: false
                }

            } else if (gender.isNotBlank() && search.isBlank()) {

                userData = userData.filter { u ->
                    u.user.gender.equals(gender)
                }

            } else if (gender.isBlank() && search.isNotBlank()) {

                userData = userData.filter { u ->
                    u.user.name?.lowercase()
                        ?.contains(search.trim().lowercase()) ?: false
                }
            }

            if (search.trim().isBlank()) {
                inputIcon = IconEnum.Search.name
            } else if (search.trim().isNotBlank() && userData.isEmpty()){
                inputIcon = IconEnum.Add.name
            } else if (search.trim().isNotBlank() && userData.isNotEmpty()){
                inputIcon = IconEnum.Clear.name
            }

            users.clear()
            users.addAll(userData)

        } catch (e: Exception) {
            Log.e("DevLogs", "Error: ${e.message}")
        }

    }


    LaunchedEffect(Unit) {
        try {
            withContext(Dispatchers.IO) {
                filterSortUsers()
                loading = false
            }
        } catch (e: Exception) {
            Log.e("DevLogs", "Error: ${e.message}")
        }
    }

    suspend fun restartSearch() {
        search = ""
        inputIcon = IconEnum.Search.name
        filterSortUsers()
    }

    suspend fun addUser() {
        try {
            val newUser = User(name = search, gender =  if (gender.isNotBlank()) gender else Gender.Unknown.name)
            db?.userDao()?.insert(newUser)
            filterSortUsers()
        } catch (e: Exception) {
            Log.e("DevLogs", "Error: ${e.message}")
        }
        restartSearch()
    }

    suspend fun updateUser(member: User) {
        db?.userDao()?.updateUser(member)
        userSelected.value = null
        filterSortUsers()
    }

    suspend fun deletePayment(payment: Payment?) {
        try {
            if (payment != null) {
                db?.paymentDao()?.delete(payment)
            }
            filterSortUsers()
        } catch (e: Exception) {
            Log.i("DevLogs", "Error : ${e.message}")
        }
    }

    suspend fun addPayment(member: User) {
        try {
            val rightNow = Calendar.getInstance()
            val month = rightNow.get(Calendar.MONTH) + 1
            val year = rightNow.get(Calendar.YEAR)

            db?.paymentDao()
                ?.insert(Payment(year = year, month = month, paymentOwnerId = member.userId))

            filterSortUsers()
        } catch (e: Exception) {
            Log.i("DevLogs", "Error : ${e.message}")
        }
    }

    Column(modifier = Modifier.padding(20.dp)) {
        Row {
            Box(modifier = Modifier.weight(1F)) {
                Input(value = search,
                    onValueChange = {
                        coroutineScope.launch {
                            search = it
                            filterSortUsers()
                        }
                    },
                    placeholder = "Buscar o agregar",
                    trailingIcon = {
                        when (inputIcon) {
                            IconEnum.Search.name -> Icon(
                                Icons.Filled.Search, contentDescription = "Search"
                            )

                            IconEnum.Add.name -> TextButton(onClick = {
                                coroutineScope.launch { addUser() }
                            }, modifier = Modifier.offset(x = (-10).dp)) {
                                Text("Agregar")
                            }

                            else -> IconButton(onClick = {
                                coroutineScope.launch { restartSearch() }
                            }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Clear")
                            }
                        }
                    })
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                SortDropdown(onSelect = {
                    isAsc = it
                    coroutineScope.launch { filterSortUsers() }
                })
                FilterDropdown(onSelect = {
                    gender = it
                    coroutineScope.launch { filterSortUsers() }
                })
            }
            IconButton(onClick = {
                navegationController?.navigate(Routes.ChartsScreen.name)
            }) {
                Icon(Icons.Filled.BarChart, contentDescription = "Charts", tint = successLight)
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(users) { data ->
                UserRow(data, data.payment != null, onCheckedChange = { isChecked ->
                    coroutineScope.launch {
                        when (isChecked) {
                            false -> deletePayment(data.payment)
                            true -> addPayment(data.user)
                        }
                    }
                }, onLongClick = {
                    userSelected.value = it
                })
            }

            if (users.toList().isEmpty() && !loading) item {
                Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                    Text(if (search.isNotEmpty()) "Sin resultados" else "No hay usuarios")
                }
            }
        }

    }

    EditUserModal(data = userSelected.value,
        isVisible = userSelected.value != null,
        onClose = { userSelected.value = null },
        onSave = { coroutineScope.launch { updateUser(it) } })
}


@Preview(
    uiMode = UI_MODE_NIGHT_YES, name = "MemberDark", showBackground = true
)
@Preview(showBackground = true)
@Composable
fun MemberPreview() {
    GymLogsTheme {
        Surface {
            UsersScreen()
        }
    }
}
