package com.aliny.palmpet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aliny.palmpet.ui.screens.*
import com.aliny.palmpet.ui.theme.AzulFontes
import com.aliny.palmpet.ui.theme.CinzaContainersClaro
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.aliny.palmpet.viewmodel.PetViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private fun navigateToLogin() {
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        setContent {
            PalmPetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (currentUser != null) {
                        val navController = rememberNavController()
                        val userViewModel: UserViewModel = viewModel()
                        val drawerState = rememberDrawerState(DrawerValue.Closed)
                        val scope = rememberCoroutineScope()

                        ModalNavigationDrawer(
                            drawerState = drawerState,
                            drawerContent = {
                                DrawerContent(navController, { scope.launch { drawerState.close() } })
                            }
                        ) {
                            Scaffold(
                                topBar = {
                                    TopAppBar(
                                        navigationIcon = {
                                            IconButton(
                                                onClick = { scope.launch { drawerState.open() } },
                                                modifier = Modifier
                                                    .padding(start = 12.dp, top = 5.dp)
                                            ) {
                                                val userPhoto: ImageBitmap? = null
                                                if (userPhoto != null) {
                                                    Image(
                                                        bitmap = userPhoto,
                                                        contentDescription = "Perfil",
                                                        modifier = Modifier
                                                            .size(40.dp)
                                                            .clip(CircleShape),
                                                        contentScale = ContentScale.Crop
                                                    )
                                                } else {
                                                    Icon(
                                                        imageVector = Icons.Filled.Person,
                                                        contentDescription = "Perfil",
                                                        modifier = Modifier
                                                            .size(40.dp)
                                                            .clip(CircleShape),
                                                        tint = AzulFontes
                                                    )
                                                }
                                            }
                                        },
                                        title = {},
                                        modifier = Modifier
                                            .height(56.dp),
                                        colors = TopAppBarDefaults.topAppBarColors(
                                            containerColor = Color.White
                                        )
                                    )
                                },
                                bottomBar = { BottomNavigationBar(navController) }
                            ) {
                                NavigationHost(navController = navController, userViewModel = userViewModel, modifier = Modifier.padding(it))
                            }
                        }

                    } else {
                        navigateToLogin()
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation(
        modifier = Modifier.navigationBarsPadding(),
        backgroundColor = CinzaContainersClaro,
        contentColor = AzulFontes
    ) {
        val items = listOf(
            Screen.Home,
            Screen.Calendar,
            Screen.Search,
            Screen.Notifications
        )

        items.forEach { screen ->
            BottomNavigationItem(
                selected = false,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        contentDescription = screen.route,
                        imageVector = screen.icon,
                        tint = AzulFontes,
                        modifier = Modifier
                            .padding(top = 9.dp, bottom = 3.dp)
                            .size(30.dp)
                    )
                }
            )
        }
    }
}

@Composable
fun DrawerContent(navController: NavHostController, onCloseDrawer: () -> Unit) {
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }

    //função para exibir o diálogo de confirmação
    fun showLogoutConfirmation() {
        showLogoutDialog = true
    }

    //função para confirmar o logout
    fun confirmLogout() {
        showLogoutDialog = false
        logout(context)
        onCloseDrawer()
    }

    //função para cancelar o logout
    fun cancelLogout() {
        showLogoutDialog = false
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirmar Logout") },
            text = { Text("Você tem certeza que deseja sair?") },
            confirmButton = {
                TextButton(onClick = { confirmLogout() }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { cancelLogout() }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
            .background(Color.White)
    ) {
        Text(
            text = "Menu",
            modifier = Modifier
                .padding(start = 20.dp, top = 30.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        DrawerItem("Perfil", Icons.Filled.Person) {
            navController.navigate("UserProfile")
            onCloseDrawer()
        }
        DrawerItem("Configurações", Icons.Filled.Settings) {
            navController.navigate("Settings")
            onCloseDrawer()
        }
        DrawerItem("Sair", Icons.Filled.ExitToApp) {
            onCloseDrawer()
            showLogoutConfirmation()
        }
    }
}


fun logout(context: Context){
    FirebaseAuth.getInstance().signOut()
    val intent = Intent(context, Login::class.java)
    context.startActivity(intent)
    (context as Activity).finish()
}

@Composable
fun DrawerItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)
        .padding(16.dp)) {
        Icon(icon, contentDescription = title, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title)
    }
}

@Composable
fun NavigationHost(navController: NavHostController, userViewModel: UserViewModel, modifier: Modifier) {
    NavHost(navController = navController, startDestination = Screen.Home.route, modifier = modifier) {
        composable(Screen.Home.route) { Home(userViewModel = userViewModel) }
        composable(Screen.Calendar.route) { Calendar() }
        composable(Screen.Notifications.route) { Notifications() }
        composable(Screen.Search.route) { Search() }
        composable("UserProfile") { UserProfile() }
        composable("Settings") { Settings() }
    }
}

sealed class Screen(val route: String, val icon: ImageVector) {
    object Home : Screen("Home", Icons.Outlined.Home)
    object Calendar : Screen("Calendar", Icons.Outlined.DateRange)
    object Notifications : Screen("Notifications", Icons.Outlined.Notifications)
    object Search : Screen("Search", Icons.Outlined.Search)
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TelaPreview() {
    PalmPetTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            val navController = rememberNavController()
            val userViewModel: UserViewModel = viewModel()
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    DrawerContent(navController, { scope.launch { drawerState.close() } })
                }
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    val userPhoto: ImageBitmap? = null
                                    if (userPhoto != null) {
                                        Image(
                                            bitmap = userPhoto,
                                            contentDescription = "Perfil",
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Filled.Person,
                                            contentDescription = "Perfil",
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape),
                                            tint = AzulFontes
                                        )
                                    }
                                }
                            },
                            title = {},
                            modifier = Modifier.padding(start = 13.dp)
                        )
                    },
                    bottomBar = { BottomNavigationBar(navController) }
                ) {
                    NavigationHost(navController = navController, userViewModel = userViewModel, modifier = Modifier.padding(it))
                }
            }
        }
    }
}