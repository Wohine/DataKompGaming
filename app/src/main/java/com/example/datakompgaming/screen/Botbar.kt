package com.example.datakompgaming.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.datakompgaming.bestillingfiler.Produkt
import com.example.datakompgaming.ui.theme.Purple700

@Composable
fun printBotBar(navController: NavController){
    Row(modifier = Modifier
        .background(Color(0xFF6200FF))
        .fillMaxWidth()) {
        ClickableText(
            text = AnnotatedString("bestilling"),
            modifier = Modifier
                .padding(5.dp),
            onClick = {
                navController.navigate("Bestilling")
            }
        )
        ClickableText(
            text = AnnotatedString("login"),
            modifier = Modifier
                .padding(5.dp),
            onClick = {
                navController.navigate("Login")
            }
        )
        ClickableText(
            text = AnnotatedString("chat"),
            modifier = Modifier
                .padding(5.dp),
            onClick = {
                navController.navigate("Chat")
            }
        )
        ClickableText(
            text = AnnotatedString("om oss"),
            modifier = Modifier
                .padding(5.dp),
            onClick = {
                navController.navigate("OmOss")
            }
        )
        ClickableText(
            text = AnnotatedString("Kundeservice"),
            modifier = Modifier
                .padding(5.dp),
            onClick = {
                navController.navigate("Kundeservice")
            }
        )
    }
}