package com.example.compose

import android.animation.ObjectAnimator
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.compose.ui.theme.ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView.iconView,
                View.TRANSLATION_Y,
                0f,
                -splashScreenView.view.height.toFloat()
            )
            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 500L
            slideUp.doOnEnd {
                splashScreenView.remove()
            }
            Toast.makeText(this, "点击进入应用", Toast.LENGTH_SHORT).show()
            splashScreenView.view.setOnClickListener {
                slideUp.start()
            }
        }
        setContent {
            ComposeTheme(false) {
                // A surface container using the 'background' color from the theme
                MainScreen()
            }
        }
    }
}

data class Message(val author: String, val body: String)

object SampleData {
    // Sample conversation data
    val conversationSample = listOf(
        Message(
            "Colleague",
            "Test...Test...Test..."
        ),
        Message(
            "Colleague",
            """List of Android versions:
                |Android KitKat (API 19)
                |Android Lollipop (API 21)
                |Android Marshmallow (API 23)
                |Android Nougat (API 24)
                |Android Oreo (API 26)
                |Android Pie (API 28)
                |Android 10 (API 29)
                |Android 11 (API 30)
                |Android 12 (API 31)""".trimMargin()
        ),
        Message(
            "Colleague",
            """I think Kotlin is my favorite programming language.
                |It's so much fun!""".trimMargin()
        ),
        Message(
            "Colleague",
            "Searching for alternatives to XML layouts..."
        ),
        Message(
            "Colleague",
            """Hey, take a look at Jetpack Compose, it's great!
                |It's the Android's modern toolkit for building native UI.
                |It simplifies and accelerates UI development on Android.
                |Less code, powerful tools, and intuitive Kotlin APIs :)
            """.trimMargin()
        ),
        Message(
            "Colleague",
            "It's available from API 21+ :)"
        ),
        Message(
            "Colleague",
            "Writing Kotlin for UI seems so natural, Compose where have you been all my life?"
        ),
        Message(
            "Colleague",
            "Android Studio next version's name is Arctic Fox"
        ),
        Message(
            "Colleague",
            "Android Studio Arctic Fox tooling for Compose is top notch ^_^"
        ),
        Message(
            "Colleague",
            "I didn't know you can now run the emulator directly from Android Studio"
        ),
        Message(
            "Colleague",
            "Compose Previews are great to check quickly how a composable layout looks like"
        ),
        Message(
            "Colleague",
            "Previews are also interactive after enabling the experimental setting"
        ),
        Message(
            "Colleague",
            "Have you tried writing build.gradle with KTS?"
        )
    )
}

@Composable
private fun MessageCard(msg: Message, i: Int) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val surfaceColor: Color by animateColorAsState(
        targetValue = if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    )
    Row(
        modifier = Modifier.padding(8.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .border(
                        6.dp,
                        MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        12.dp,
                        MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(22.dp)
                    )
                    .border(
                        18.dp,
                        MaterialTheme.colorScheme.inversePrimary,
                        shape = RoundedCornerShape(28.dp)
                    ),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "${msg.author} $i",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.shadow(8.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "${msg.author} $isExpanded",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(4.dp),
                tonalElevation = 1.dp,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
                    .clickable { isExpanded = !isExpanded }
            ) {
                Text(
                    text = msg.body,
                    Modifier.padding(4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1
                )
            }
        }
    }
}

@Composable
private fun Conversation(message: List<Message>) {
    LazyColumn {
        itemsIndexed(message) { i: Int, message: Message ->
            MessageCard(msg = message, i = i)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen() {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Filled.Add, "添加")
            }
        }) {
        Conversation(message = SampleData.conversationSample)
    }
}

@Preview(
    showBackground = false,
    name = "Light Mode"
)
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
private fun DefaultPreview() {
    ComposeTheme {
        MainScreen()
    }
}