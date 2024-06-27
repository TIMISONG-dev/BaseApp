package timisongdev.baseapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timisongdev.baseapp.ui.theme.BaseAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaseAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "BaseApp",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class UserCredentials(
    val email: String = "",
    val password: String = "",
    val uid: String = "",
    val name: String = ""
)

suspend fun getUserCredentials(uid: String): UserCredentials? {
    val database = Firebase.database

    val emailRef = database.getReference("$uid/email")
    val passwordRef = database.getReference("$uid/password")
    val uidRef = database.getReference("$uid/uid")
    val nameRef = database.getReference("$uid/nick")

    val emailSnapshot = emailRef.get().await()
    val passwordSnapshot = passwordRef.get().await()
    val uidSnapshot = uidRef.get().await()
    val nameSnapshot = nameRef.get().await()

    val email = emailSnapshot.getValue(String::class.java) ?: return null
    val password = passwordSnapshot.getValue(String::class.java) ?: return null
    val uid = uidSnapshot.getValue(String::class.java) ?: return null
    val name = nameSnapshot.getValue(String::class.java) ?: return null

    return UserCredentials(email, password, uid, name)
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val database = Firebase.database
    val context = LocalContext.current
    val auth: FirebaseAuth = Firebase.auth
    val userPreferences = DataStorage(context)
    val scope = rememberCoroutineScope()

    // Collect email and password from DataStore
    val savedEmail by userPreferences.email.collectAsState(initial = "")
    val savedPassword by userPreferences.pass.collectAsState(initial = "")

    var email by remember { mutableStateOf(savedEmail ?: "") }
    var password by remember { mutableStateOf(savedPassword ?: "") }
    var checkpass by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    var mode by remember { mutableStateOf("Login") }
    val colors = MaterialTheme.colorScheme
    var loginButton by remember { mutableStateOf(colors.primaryContainer) }
    var regButton by remember { mutableStateOf(colors.background) }
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            mode,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        TextField(
            value = email,
            onValueChange = { newText -> email = newText },
            label = {
                Text("Email")
            },
            modifier = Modifier.padding(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = "Email")}
        )

        if (mode == "Registration") {
            TextField(
                value = nickname,
                onValueChange = { newText -> nickname = newText },
                label = {
                    Text("Nickname")
                },
                modifier = Modifier.padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = "Nickname")}
            )
        }

        TextField(
            value = password,
            onValueChange = { newText -> password = newText },
            label = {
                Text("Password")
            },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.padding(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "Password")},
            trailingIcon = {
                val image = if (passwordVisibility) {
                    R.drawable.ic_visibility
                } else {
                    R.drawable.ic_visibility_off
                }
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        painter = painterResource(id = image),
                        contentDescription = ""
                    )
                }
            }
        )

        if (mode == "Registration") {
            TextField(
                value = checkpass,
                onValueChange = { newText -> checkpass = newText },
                label = {
                    Text("Verification password")
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "Verification password")},
                trailingIcon = {
                    val image = if (passwordVisibility) {
                        R.drawable.ic_visibility
                    } else {
                        R.drawable.ic_visibility_off
                    }
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            painter = painterResource(id = image),
                            contentDescription = ""
                        )
                    }
                }
            )
        }

        Button(onClick = {
            if (checkpass != password && mode == "Registration") {
                Toast.makeText(context,"You entered the verification password incorrectly", Toast.LENGTH_SHORT).show()

            } else {
                if (nickname == "" && mode == "Registration") {
                    Toast.makeText(context, "You didn't write Nickname", Toast.LENGTH_SHORT).show()
                } else {
                    // If no errors
                    if (mode == "Registration") {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                Toast.makeText(context, "Hello Reg", Toast.LENGTH_SHORT).show()

                                val user = task.result?.user
                                val uid = user?.uid

                                val getEmail = database.getReference("$uid/email")
                                val getPassword = database.getReference("$uid/password")
                                val getNickname = database.getReference("$uid/nick")
                                val getUid = database.getReference("$uid/key")
                                getEmail.setValue(email)
                                getPassword.setValue(password)
                                getNickname.setValue(nickname)
                                getUid.setValue(uid)

                                // Save email and password to DataStore
                                scope.launch {
                                    userPreferences.saveEmail(email, context)
                                    userPreferences.savePass(password, context)
                                    userPreferences.saveName(name, context)
                                    userPreferences.saveAuth(true, context)
                                    if (uid != null) {
                                        userPreferences.saveUid(uid, context)
                                    }
                                }
                            }
                    } else {
                            val user = auth.currentUser
                            val uid = user?.uid

                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {

                                        Toast.makeText(context, "Hello Login", Toast.LENGTH_SHORT).show()
                                        context.startActivity(Intent(context, MenuActivity::class.java))

                                        scope.launch{
                                            val credentials = uid?.let { getUserCredentials(it) }
                                            if (credentials != null) {
                                                email = credentials.email
                                            }
                                            if (credentials != null) {
                                                password = credentials.password
                                            }

                                            // Save email and password to DataStore
                                            userPreferences.saveEmail(email, context)
                                            userPreferences.savePass(password, context)
                                            userPreferences.saveName(name, context)
                                            userPreferences.saveAuth(true, context)
                                            if (uid != null) {
                                                userPreferences.saveUid(uid, context)
                                            }
                                        }
                                    } else {
                                        Toast.makeText(context, task.exception?.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                                    }
                                }
                    }
                }
            }
        }) {
            Text("ENTER")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column (
            Modifier.wrapContentWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(color = regButton)
                    .padding(8.dp)
                    .clickable {
                        mode = "Registration"
                        regButton = colors.primaryContainer
                        loginButton = colors.background

                    }
            ) {
                Text("Registration")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(color = loginButton)
                    .padding(8.dp)
                    .clickable {
                        mode = "Login"
                        regButton = colors.background
                        loginButton = colors.primaryContainer
                    }
            ) {
                Text("Login")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BaseAppTheme {
        Greeting("Android")
    }
}