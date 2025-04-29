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
import com.raulodev.gymlogs.database.UserAndCurrentPayment
import com.raulodev.gymlogs.enums.Gender
import com.raulodev.gymlogs.enums.IconEnum
import com.raulodev.gymlogs.ui.components.EditUserModal
import com.raulodev.gymlogs.ui.components.Input
import com.raulodev.gymlogs.ui.components.SortDropdown
import com.raulodev.gymlogs.ui.components.UserRow
import com.raulodev.gymlogs.ui.theme.GymLogsTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MembersScreen(
    db: AppDatabase? = null, navegationController: NavHostController? = null
) {

    val coroutineScope = rememberCoroutineScope()

    var inputIcon by rememberSaveable { mutableStateOf(IconEnum.Search.name) }
    var isAsc by rememberSaveable { mutableStateOf(true) }
    val members = remember { mutableStateListOf<UserAndCurrentPayment>() }
    var search by rememberSaveable { mutableStateOf("") }
    val memberSelected = remember { mutableStateOf<UserAndCurrentPayment?>(null) }

    suspend fun showAllMembers() {
        try {
            val result = db?.userDao()?.getAllUsersAndCurrentPayment(isAsc)
            members.clear()
            if (!result.isNullOrEmpty()) {
                members.addAll(result)
            }

        } catch (e: Exception) {
            Log.e("DevLogs", "Error: ${e.message}")
        }
    }

    LaunchedEffect(Unit) {
        try {
            withContext(Dispatchers.IO) {
                showAllMembers()
            }
        } catch (e: Exception) {
            Log.e("DevLogs", "Error: ${e.message}")
        }
    }

    fun sortMembers(memberList: List<UserAndCurrentPayment>, asc: Boolean) {
        isAsc = asc
        val membersSorted: List<UserAndCurrentPayment>
        if (asc) {
            membersSorted = memberList.sortedBy { it.user.name }
        } else {
            membersSorted = memberList.sortedByDescending { it.user.name }
        }
        members.clear()
        members.addAll(membersSorted)
    }

    suspend fun restartSearch() {
        search = ""
        inputIcon = IconEnum.Search.name
        showAllMembers()
    }

    suspend fun addNewMember() {
        try {
            val newUser = User(name = search, gender = Gender.Unknown.name)
            db?.userDao()?.insert(newUser)
            showAllMembers()

        } catch (e: Exception) {
            Log.e("DevLogs", "Error: ${e.message}")
        }
        restartSearch()
    }

    suspend fun searchMember(value: String) {
        search = value

        if (search.isEmpty()) {
            inputIcon = IconEnum.Search.name
            showAllMembers()
            return
        }

        try {
            val result = db?.userDao()?.getAllUsersAndCurrentPayment(isAsc)

            if (result != null) {
                val filteredList = result.filter { u ->
                    u.user.name?.lowercase()?.contains(value.lowercase()) ?: false
                }
                if (filteredList.isEmpty()) {
                    inputIcon = IconEnum.Add.name
                } else {
                    inputIcon = IconEnum.Clear.name
                }
                sortMembers(filteredList, isAsc)

            }
        } catch (e: Exception) {
            Log.e("DevLogs", "Error: ${e.message}")
        }
    }

    suspend fun updateMember(member: User) {
        db?.userDao()?.updateUser(member)
        memberSelected.value = null
        searchMember(search)
    }

    suspend fun deletePayment(payment: Payment?) {
        try {
            if (payment != null) {
                db?.paymentDao()?.delete(payment)
            }
            searchMember(search)
        } catch (e: Exception) {
            Log.i("DevLogs", "Error : ${e.message}")
        }
    }

    suspend fun registerPayment(member: User) {
        try {
            val rightNow = Calendar.getInstance()
            val month = rightNow.get(Calendar.MONTH) + 1
            val year = rightNow.get(Calendar.YEAR)

            db?.paymentDao()
                ?.insert(Payment(year = year, month = month, paymentOwnerId = member.userId))

            searchMember(search)
        } catch (e: Exception) {
            Log.i("DevLogs", "Error : ${e.message}")
        }
    }

    Column(modifier = Modifier.padding(20.dp)) {

        Row {
            Box(modifier = Modifier.weight(1F)) {
                Input(value = search,
                    onValueChange = { coroutineScope.launch { searchMember(it) } },
                    placeholder = "Buscar o agregar",
                    trailingIcon = {
                        when (inputIcon) {
                            IconEnum.Search.name -> Icon(
                                Icons.Filled.Search, contentDescription = "Search"
                            )

                            IconEnum.Add.name -> TextButton(onClick = {
                                coroutineScope.launch { addNewMember() }
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
            SortDropdown(onSelect = { isAsc -> sortMembers(members, isAsc) })
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(members) { data ->
                UserRow(data, data.payment != null, onCheckedChange = { isChecked ->
                    coroutineScope.launch {
                        when (isChecked) {
                            false -> deletePayment(data.payment)
                            true -> registerPayment(data.user)
                        }
                    }
                }, onLongClick = {
                    memberSelected.value = it
                })
            }

            if (members.toList().isEmpty()) item {
                Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                    Text(if (search.isNotEmpty()) "Sin resultados" else "No hay usuarios")
                }
            }
        }

    }

    EditUserModal(data = memberSelected.value,
        isVisible = memberSelected.value != null,
        onClose = { memberSelected.value = null },
        onSave = { coroutineScope.launch { updateMember(it) } })
}


@Preview(
    uiMode = UI_MODE_NIGHT_YES, name = "MemberDark", showBackground = true
)
@Preview(showBackground = true)
@Composable
fun MemberPreview() {
    GymLogsTheme {
        Surface {
            MembersScreen()
        }
    }
}
