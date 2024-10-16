package com.example.ex2

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.ex2.ui.theme.Ex2Theme
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ex2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Main(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Main(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NavHost(
            navController, startDestination = NavRoutes.Home.route,
            modifier = Modifier.weight(1f)
        ) {
            composable(NavRoutes.Home.route) { Home() }
            composable(NavRoutes.Animal.route) { Animal() }
            composable(NavRoutes.Image1.route) { Image1() }
            composable(NavRoutes.Photo.route) { Photo() }

        }
        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        NavBarItems.BarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = navItem.image,
                        contentDescription = navItem.title
                    )
                },
                label = {
                    Text(text = navItem.title)
                }
            )
        }
    }
}

object NavBarItems {
    val BarItems = listOf(
        BarItem(title = "Home", image = Icons.Filled.Home, route = "home"),
        BarItem(title = "Animal", image = Icons.Filled.FavoriteBorder, route = "animals"),
        BarItem(title = "Image", image = Icons.Filled.AccountCircle, route = "image"),
        BarItem(title = "Photo", image = Icons.Filled.Star, route = "photo")
    )
}

data class BarItem(
    val title: String,
    val image: ImageVector,
    val route: String
)

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Animal : NavRoutes("animals")
    object Image1 : NavRoutes("image")
    object Photo : NavRoutes("photo")
}

@Composable
fun Home(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF826AB4)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var myName by rememberSaveable { mutableStateOf("") }
        val textValue: String = stringResource(R.string.fio)

        Text(
            text = myName,
            modifier = modifier,
            color = Color.Yellow
        )

        Row {
            Button(
                onClick = {
                    myName = textValue
                }

            ) {
                Text(text = "Вывести ФИО")

            }
            Button(
                onClick = {
                    myName = " "
                }
            ) {
                Text(text = "X")
            }
        }

    }
}

data class Animal(val name: String, val kind: String)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Animal(modifier: Modifier = Modifier) {

    val animals = listOf(
        Animal("Медуза", "Беспозвоночные"), Animal("Улитка", "Беспозвоночные"),
        Animal("Мидии", "Беспозвоночные"), Animal("Червяк", "Беспозвоночные"),
        Animal("Карп", "Рыбы"), Animal("Щука", "Рыбы"),
        Animal("Окунь", "Рыбы"), Animal("Лягушка", "Земноводные"),
        Animal("Жаба", "Земноводные"), Animal("Черепаха", "Пресмыкающиеся"),
        Animal("Крокодил", "Пресмыкающиеся"), Animal("Змея", "Пресмыкающиеся"),
        Animal("Ящерица", "Пресмыкающиеся"), Animal("Голубь", "Птицы"),
        Animal("Синица", "Птицы"), Animal("Снегирь", "Птицы"),
        Animal("Жираф", "Млекопитающие"), Animal("Котик", "Млекопитающие"),
        Animal("Слон", "Млекопитающие"), Animal("Собака", "Млекопитающие")
    )
    val groups = animals.groupBy { it.kind }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        contentPadding = PaddingValues(5.dp),
        state = listState
    ) {
        groups.forEach { (kind, name) ->
            stickyHeader {
                Text(
                    text = kind,
                    fontSize = 28.sp,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.Magenta)
                        .padding(5.dp)
                        .fillMaxWidth()
                        .clickable {
                            coroutineScope.launch {
                                listState.animateScrollToItem(6)
                            }
                        }
                )

            }
            items(name) { n ->
                Text(n.name, Modifier.padding(5.dp), fontSize = 28.sp)
            }
        }

    }
}

@Composable
fun Image1(modifier: Modifier = Modifier) {
    var rotated by rememberSaveable { mutableStateOf(false) }
    val angle by animateFloatAsState(
        targetValue = if (rotated) 360f else 0f,
        animationSpec = tween(4000),
        label = ""
    )
    val configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Image(
                painter = painterResource(R.drawable.img),
                contentScale = ContentScale.Crop,
                contentDescription = "Ezhic",
                modifier = Modifier
                    .size(360.dp)
                    .clip(CircleShape)
                    .rotate(angle)
            )
            Button({ rotated = !rotated }) {
                Text(text = "Повернуть")
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Image(
                painter = painterResource(R.drawable.img),
                contentScale = ContentScale.Crop,
                contentDescription = "Ezhic",
                modifier = Modifier
                    .size(360.dp)
                    .clip(CircleShape)
                    .rotate(angle)
            )
            Button({ rotated = !rotated }) {
                Text(text = "Повернуть")
            }
        }
    }
}

@Composable
fun Photo(modifier: Modifier = Modifier) {
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var hasImage by rememberSaveable { mutableStateOf(false) }
    var currentUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
            if(success) imageUri = currentUri
        }
    )
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            hasImage = uri != null
            imageUri = uri
        }
    )
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if(isGranted){
                Toast.makeText(
                    context, "Разрешение получено",
                    Toast.LENGTH_SHORT
                ).show()
                currentUri?.let { cameraLauncher.launch(it) }
            }
            else {
                Toast.makeText(
                    context, "В разрешении отказано",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    Box(modifier = modifier) {
        if (hasImage && imageUri != null) {
            AsyncImage(
                model = imageUri,
                modifier = Modifier.fillMaxWidth(),
                contentDescription = "Selected image"
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                imagePicker.launch("image/*")
            }) {
                Text(text = "Выбрать изображение")
            }
            Button(modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    currentUri = ComposeFileProvider.getImageUri(context)

                    val permissionCheckResult = ContextCompat.checkSelfPermission(
                        context, android.Manifest.permission.CAMERA
                    )
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(currentUri!!)
                    } else {
                        permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }
                }
            ) {
                Text(text = "Сделать снимок")
            }
        }
    }
}

class ComposeFileProvider : FileProvider(R.xml.file_paths) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "selected image ",
                ".jpg",
                directory
            )
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    Ex2Theme {
        Main()
    }

}