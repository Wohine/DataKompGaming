package com.example.datakompgaming.produkt

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.datakompgaming.R
import com.example.datakompgaming.bruktProdukt.BruktProdukt
import com.example.datakompgaming.bruktProdukt.sendSkjemaDB
import com.example.datakompgaming.screen.printBotBarIcon
import com.example.datakompgaming.screen.printTopBarIcon
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.IOException


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@ExperimentalMaterial3Api
@Composable
fun bruktProduktSkjema(navController: NavController) {

    Scaffold(
        bottomBar = {
            printBotBarIcon(navController = navController, 2)
        },
        topBar = {
            printTopBarIcon(navController = navController)
        }
    ) {

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background

        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState(), enabled = true),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                /**
                 * Instansiering av variabler
                 */

                val produktNavn = remember {
                    mutableStateOf(TextFieldValue())
                }

                var kategori = remember {
                    mutableStateOf(String())
                }

                val produsent = remember {
                    mutableStateOf(TextFieldValue())
                }

                val pris = remember {
                    mutableStateOf(TextFieldValue())
                }

                val tilstand = remember {
                    mutableStateOf(TextFieldValue())
                }

                var imageUri by remember { mutableStateOf<Uri?>(null) }

                var storage = Firebase.storage
                val storageRef = storage.reference

                var cont = LocalContext.current

                val imagesRef = storageRef.child("images/" + produktNavn.value.text)

                var expanded by remember {
                    mutableStateOf(false)
                }


                /**
                 * Launcher for bildeuthenting. Sjekker om det er returnert en uri.
                 */
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri: Uri? ->
                    run {
                        if (uri != null) {
                            imageUri = uri
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Image(painter = painterResource(R.drawable.datakomplogo), contentDescription = null)

                Spacer(modifier = Modifier.height(15.dp))

                /**
                 * Inntastingsfelt for brukte produkter.
                 */
                Text(
                    text = "Send inn ditt brukte produkt!",
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(15.dp))

                TextField(
                    label = { Text(text = "Produktnavn") },
                    value = produktNavn.value,
                    onValueChange = { produktNavn.value = it }
                )

                Spacer(modifier = Modifier.height(15.dp))

                TextField(
                    label = { Text(text = "Produsent") },
                    value = produsent.value,
                    onValueChange = { produsent.value = it },
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(text = "Hva slags produkt ønsker du å selge?")
                Spacer(modifier = Modifier.height(15.dp))


                /**
                 * Drop down meny for å velge hvilken produkt kategori produktet tilhører.
                 */
                Box {

                Button(onClick = { expanded = true }) {
                        Text(text = kategori.value)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Hovedkort") },
                            onClick = {
                                kategori.value = "Hovedkort"
                                expanded = false
                                Toast.makeText(cont, kategori.value, Toast.LENGTH_LONG).show()
                                Log.d(ContentValues.TAG, kategori.value)
                            })
                        DropdownMenuItem(
                            text = { Text("Skjermkort") },
                            onClick = {
                                kategori.value = "Skjermkort"
                                expanded = false
                                Toast.makeText(cont, kategori.value, Toast.LENGTH_LONG).show()
                                Log.d(ContentValues.TAG, kategori.value)
                            })
                        DropdownMenuItem(
                            text = { Text("Prosessorer") },
                            onClick = {
                                kategori.value = "Prosessorer"
                                expanded = false
                                Toast.makeText(cont, kategori.value, Toast.LENGTH_LONG).show()
                                Log.d(ContentValues.TAG, kategori.value)
                            })
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                TextField(
                    label = { Text(text = "Ønsket pris") },
                    value = pris.value,
                    onValueChange = { pris.value = it },
                )

                Spacer(modifier = Modifier.height(15.dp))

                TextField(
                    label = { Text(text = "Beskrivelse av produktet") },
                    value = tilstand.value,
                    onValueChange = { tilstand.value = it },
                )

                Spacer(modifier = Modifier.height(15.dp))

                /**
                 * Knapp for å starte bildeuthentingsprosessen. Benytter seg av launcher definert
                 * ved variablene. Kjører kun om Android enheten kjører på en android versjon
                 * som støtter denne uthentingsmetoden.
                 * https://ngengesenior.medium.com/pick-image-from-gallery-in-jetpack-compose-5fa0d0a8ddafhttps://ngengesenior.medium.com/pick-image-from-gallery-in-jetpack-compose-5fa0d0a8ddaf
                 */

                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= 29) {
                            try {
                                launcher.launch("image/*")
                            } catch (ex: IOException) {
                                Log.i("Catch", ex.toString())
                            }

                        }
                    },
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(10.dp)
                ) {
                    Text(text = "Last opp bilde fra galleri")
                }

                Spacer(modifier = Modifier.height(15.dp))


                /**
                 * Knapp som starter konvertering av bilde Uri til byteArray.
                 */
                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                    Button(
                        onClick = {

                            val imagePFD: ParcelFileDescriptor? =
                                imageUri?.let { it1 ->
                                    cont.contentResolver.openFileDescriptor(
                                        it1, "r"
                                    )
                                }


                            /**
                             * Sjekker om brukeren har lastet opp et bilde. Hvis dette er sant
                             * lager den et Bitmap objekt.
                             */
                            if (imagePFD != null) {
                                val bitmap =
                                    BitmapFactory.decodeFileDescriptor(imagePFD.fileDescriptor)
                                val byteArrayOutputStream = ByteArrayOutputStream()

                                /**
                                 * Definerer fildatatypen for bildet.
                                 */
                                bitmap.compress(
                                    Bitmap.CompressFormat.JPEG,
                                    100,
                                    byteArrayOutputStream
                                )

                                val data = byteArrayOutputStream.toByteArray()

                                /**
                                 * Laster opp resultatet til Firebase Storage. Resultatet
                                 * av denne tasken returnerer enten en positiv eller negativ konsoll melding.
                                 */
                                var uploadTask = imagesRef.putBytes(data)

                                uploadTask.addOnFailureListener {
                                    Log.d(ContentValues.TAG, "Feilet bildeopplastning!")
                                }.addOnSuccessListener { taskSnapshot ->
                                    Log.d(ContentValues.TAG, "Suksessfull bildeopplastning!")

                                    /**
                                     * Fikk ikke denne downloadUri uthentingen til å fungere,
                                     * bygger heller denne manuelt i Firestore opplasningen.
                                     */
                                    val downloadUri = uploadTask.result
                                    Log.d(ContentValues.TAG, downloadUri.toString())
                                }
                            }


                            /**
                             * Konvertering av inndata til tekststrenger.
                             */
                            val produktNavnString = produktNavn.value.text
                            val kategoriString = kategori.value
                            val produsentString = produsent.value.text
                            val prisString = pris.value.text
                            val tilstandString = tilstand.value.text
                            val varebeholdningString = "1"
                            /**
                             * Bygger downloadUrl manuelt grunnet at vi ikke fikk Firebase sin metode til å returnere riktig verdi.
                             */
                            val bildeString =
                                "https://firebasestorage.googleapis.com/v0/b/datakompkotlin.appspot.com/o/images%2F" + produktNavnString + "?alt=media"


                            if(isNumericToX(prisString)) {
                                val bruktProdukt = BruktProdukt(
                                    produktNavnString,
                                    kategoriString,
                                    produsentString,
                                    prisString,
                                    tilstandString,
                                    varebeholdningString,
                                    bildeString,
                                )
                                /**
                                 * Kaller på send skjema metoden
                                 */
                                sendSkjemaDB(bruktProdukt, cont)
                            }
                            else{
                                Toast.makeText(cont, "ugyldig pris", Toast.LENGTH_LONG).show()
                            }
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Send inn")
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    text = "Du er ikke forpliktet til å sende inn produktet ved å sende inn dette skjemaet.",
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(100.dp))
            }

        }

    }
}
fun isNumericToX(toCheck: String): Boolean {
    return toCheck.toDoubleOrNull() != null
}



