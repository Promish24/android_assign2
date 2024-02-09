package com.example.promishkhaniya_assign2
import android.annotation.SuppressLint
import android.content.ContentResolver
import androidx.activity.compose.setContent
import android.content.ContentValues
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var firstTimeLoaded by remember { mutableStateOf(true) }

    var contactName by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var contacts by remember { mutableStateOf(emptyList<Contact>()) }

// LaunchedEffect to perform data loading
    LaunchedEffect(firstTimeLoaded) {
        if (firstTimeLoaded) {
            // Load contacts when it's the first time
            contacts = loadContacts(context)
            firstTimeLoaded = false
        }
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),

        ) {
            Button(onClick = {
                if(contactName.isEmpty() && contactNumber.isEmpty()){
                    val alertDialogBuilder = AlertDialog.Builder(context)
                    alertDialogBuilder.apply {
                        setTitle("Add Contacts Failed!")
                        setMessage("Please insert all the values")
                        setPositiveButton("OK") { dialog, which ->
                            // Do something when the user clicks OK
                        }
                    }

                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }else{
                    val newContact = Contact("$contactName", "$contactNumber")
                    addContact(context, newContact)
                    contacts = loadContacts(context)
                    contactName = ""
                    contactNumber = ""
                }
            },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(2.dp, Color.Blue),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Gray,
                    containerColor = Color.White
                )) {
                Text("Add")
            }
            Button(onClick = {
                // Update contacts list
                contactName = ""
                contactNumber = ""
            },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(2.dp, Color.Blue),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Gray,
                    containerColor = Color.White
                )
            ) {
                Text("Clear")
            }
            Button(onClick = {
            // Update contacts list
                contacts = loadContacts(context)
            },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(2.dp, Color.Blue),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Gray,
                    containerColor = Color.White
                )
            ) {
                Text("Load")
            }
        }
        Divider(modifier = Modifier.padding(vertical = 2.dp))
        if (contacts.isEmpty()) {
            Text(text = "No contacts available")
        } else {
            Column (modifier = Modifier.height(300.dp)){

                LazyColumn() {
                    items(contacts) { contact ->
                        ContactItem(contact) {
                            deleteContact(context.contentResolver, contact.id)
                            // Update contacts list
                            contacts = loadContacts(context)
                        }
                    }
                }
            }

        }
        Divider(modifier = Modifier.padding(vertical = 2.dp))
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("About section:")
        Text("Student Name: Promish Khaniya", fontSize = 16.sp)
        Text("Student ID: 301369717", fontSize = 16.sp)
    }
}

fun addContact(context: ComponentActivity, contact: Contact) {
    try {
        val values = ContentValues().apply {
            put(ContactsContract.RawContacts.ACCOUNT_TYPE, "")
            put(ContactsContract.RawContacts.ACCOUNT_NAME, "")
        }
        val rawContactUri = context.contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values)
        val rawContactId = rawContactUri?.lastPathSegment?.toLongOrNull()
// Insert display name
        val displayNameValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.displayName)
        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, displayNameValues)
// Insert phone number
        val phoneNumberValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            put(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.phoneNumber)
            put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, phoneNumberValues)
    } catch (e: Exception) {
// Handle the exception appropriately (e.g., log the error, display a message to the user)
        throw e;
    }
}
fun deleteContact(contentResolver: ContentResolver, contactId: Long? = null) {
    val whereClause = "${ContactsContract.CommonDataKinds.Phone._ID} = ?"
    val whereArgs = arrayOf(contactId.toString())
    contentResolver.delete(
        ContactsContract.RawContacts.CONTENT_URI,
        whereClause,
        whereArgs
    )
}