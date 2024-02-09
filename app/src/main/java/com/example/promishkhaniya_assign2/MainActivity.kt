package com.example.promishkhaniya_assign2
import android.annotation.SuppressLint
import android.content.ContentResolver
import androidx.activity.compose.setContent
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.promishkhaniya_assign2.ui.theme.PromishKhaniya_Assign2Theme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PromishKhaniya_Assign2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContactsList(this)
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsList(context: ComponentActivity) {
    var contactName by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
// LaunchedEffect to perform data loading
//    LaunchedEffect(Unit) {
// Load contacts
//        contacts = loadContacts(context)
//    }
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            value = contactName,
            onValueChange = { contactName = it },
            label = { Text(text = "Contact Name") }
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            value = contactNumber,
            onValueChange = { contactNumber = it },
            label = { Text(text = "Contact Number") }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
//                val newContact = Contact("$contactName", "$contactNumber")
//                addContact(context, newContact)
//                contactName = ""
//                contactNumber = ""
//// Update contacts list
//                contacts = loadContacts(context)
            }) {
                Text("Add")
            }
        }
        Divider(modifier = Modifier.padding(vertical = 2.dp))
//        LazyColumn() {
//            items(contacts) { contact ->
//                ContactItem(contact) {
//                    deleteContact(context.contentResolver, contact.id)
//// Update contacts list
//                    contacts = loadContacts(context)
//                }
//            }
//        }

// About section
        AboutSection()
    }
}
@Composable
fun AboutSection() {
    Column() {
        Text("About section:")
        Text("Student Name: Abi Chitrakar", fontSize = 16.sp)
        Text("Student ID: 301369773", fontSize = 16.sp)
    }
}