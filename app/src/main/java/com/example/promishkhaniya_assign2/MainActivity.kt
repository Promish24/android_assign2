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
    var contacts by remember { mutableStateOf(emptyList<Contact>()) }
// LaunchedEffect to perform data loading
    LaunchedEffect(Unit) {
// Load contacts
        contacts = loadContacts(context)
    }
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
// Update contacts list
//                contacts = loadContacts(context)
            }) {
                Text("Add")
            }
        }
        Divider(modifier = Modifier.padding(vertical = 2.dp))
        LazyColumn() {
            items(contacts) { contact ->
                ContactItem(contact) {
//                    deleteContact(context.contentResolver, contact.id)
// Update contacts list
                    contacts = loadContacts(context)
                }
            }
        }

// About section
        AboutSection()
    }
}
@Composable
fun ContactItem(contact: Contact, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = contact.displayName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = contact.phoneNumber,
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic)

        }
        Button(onClick = onDelete) {
            Text("Delete")
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("Range")
fun loadContacts(context: ComponentActivity): List<Contact> {
    val contacts = mutableListOf<Contact>()
    context.contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        arrayOf(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY, ContactsContract.Contacts._ID),
        null,
        null,
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            do {
                val displayName =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                val contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID))
// Query phone numbers associated with this contact
                context.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                    "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                    arrayOf(contactId.toString()),
                    null
                )?.use { phoneCursor ->
                    if (phoneCursor.moveToFirst()) {
                        val phoneNumber =
                            phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        contacts.add(Contact(displayName, phoneNumber, contactId))
                    }
                }
            } while (cursor.moveToNext())
        }
    }
    return contacts
}

data class Contact(val displayName: String, val phoneNumber: String, val id : Long?= null)
@Composable
fun AboutSection() {
    Column() {
        Text("About section:")
        Text("Student Name: Abi Chitrakar", fontSize = 16.sp)
        Text("Student ID: 301369773", fontSize = 16.sp)
    }
}