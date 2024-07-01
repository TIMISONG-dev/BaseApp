package timisongdev.baseapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import timisongdev.baseapp.ui.theme.BaseAppTheme

class MenuActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaseAppTheme {
                Scaffold {
                    val title = listOf(
                        "Джоггеры спортивные",
                        "Брюки классические",
                        "Брюки спортивные",
                        "Рубашка Macroc",
                        "Брюки чинос"
                    )
                    val desc = listOf(
                        "Брюки спортивные Jog's штаны джоггеры мужские с карманами, размер 36, хаки, зеленый",
                        "Брюки классические RB, размер 30, синий",
                        "Брюки спортивные Ohana market, размер 64, темно-серый",
                        "Рубашка Mocroc, размер L/41, белый",
                        "Брюки чинос O'STIN, размер 46-48, синий"
                    )
                    val cost = listOf(
                        "2797р",
                        "2654р",
                        "1322р",
                        "1101p",
                        "1996p"
                    )
                    val image = listOf(
                        "https://avatars.mds.yandex.net/get-mpic/1578067/2a00000190133b039991c78f8f3fe00f421d/600x800",
                        "https://avatars.mds.yandex.net/get-mpic/5307186/2a0000018e13df91b0064ddd6d010fa3dbe8/600x800",
                        "https://avatars.mds.yandex.net/get-mpic/7631660/img_id5030633211651354258.jpeg/600x800",
                        "https://avatars.mds.yandex.net/get-mpic/12420105/2a0000018d414357e712e978d96149ca156b/600x800",
                        "https://avatars.mds.yandex.net/get-mpic/11552111/2a0000018d55c4121d072ef088a5efea1d68/600x800"
                    )
                    Column {

                        var selectedItem by remember { mutableIntStateOf(0) }
                        val titles = listOf("Home", "Favorite", "Cart", "Settings")
                        val icons = listOf(Icons.Outlined.Home, Icons.Outlined.FavoriteBorder, Icons.Outlined.ShoppingCart, Icons.Outlined.Settings)
                        // Sorry for this code

                        Column (
                            Modifier.weight(1F)
                        ){
                            when(selectedItem){
                                0 -> MyGrid(title = title, desc = desc, cost = cost, image = image)
                                1 -> Fav()
                                2 -> Cart()
                                3 -> Settings()
                            }
                        }


                        NavigationBar {
                            titles.forEachIndexed { index, title ->
                                NavigationBarItem(
                                    icon = { Icon(icons[index], contentDescription = title) },
                                    label = { Text(title) },
                                    selected = selectedItem == index,
                                    onClick = {
                                        selectedItem = index
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Fav(){
    Column (
        Modifier.fillMaxHeight().fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Favorite fun",
            fontSize = 20.sp
        )
    }
}

@Composable
fun Cart(){
    Column (
        Modifier.fillMaxHeight().fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Cart fun",
            fontSize = 20.sp
        )
    }
}

@Composable
fun Settings(){
    Column (
        Modifier.fillMaxHeight().fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Settings fun",
            fontSize = 20.sp
        )
    }
}

@Composable
fun MyGrid(title: List<String>, desc: List<String>, cost: List<String>, image: List<String>) {
    Column {
        Row (
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(24.dp, 44.dp, 24.dp, 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Left
        ){
            Icon(
                Icons.Outlined.Person,
                contentDescription = ""
            )
            Text(
                text = "Hello, User",
                Modifier.padding(8.dp)
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxHeight(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(title.size) { index ->
                GridItem(
                    title = title[index],
                    desc = desc[index],
                    cost = cost[index],
                    image = image[index]
                )
            }
        }
    }
}

@Composable
fun GridItem(title: String, desc: String, cost: String, image: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                ambientColor = Color.Transparent,
                spotColor = Color.LightGray
            )
            .aspectRatio(0.6f),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column (
                Modifier.wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = rememberAsyncImagePainter(model = image),
                    contentDescription = "Image",
                    Modifier
                        .size(180.dp)
                        .scale(1.5f)
                )
                Column (
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp))
                        .background(Color.White)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = desc,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = cost,
                        color = Color(0xFF19941b),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Outlined.FavoriteBorder,
                            contentDescription = "",
                            Modifier
                                .size(24.dp)
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Icon(
                            Icons.Outlined.ShoppingCart,
                            contentDescription = "",
                            Modifier
                                .size(24.dp)
                        )
                    }
                }
            }
        }
    }
}