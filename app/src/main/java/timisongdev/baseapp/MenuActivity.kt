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
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
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
                Scaffold{
                    val title = listOf(
                        "Джоггеры спортивные",
                        "Брюки классические",
                        "Брюки спортивные"
                    )
                    val desc = listOf(
                        "Брюки спортивные Jog's штаны джоггеры мужские с карманами, размер 36, хаки, зеленый",
                        "Брюки классические RB, размер 30, синий",
                        "Брюки спортивные Ohana market, размер 64, темно-серый"
                    )
                    val cost = listOf(
                        "2797р",
                        "2654р",
                        "1322р"
                    )
                    val image = listOf(
                        "https://avatars.mds.yandex.net/get-mpic/1578067/2a00000190133b039991c78f8f3fe00f421d/600x800",
                        "https://avatars.mds.yandex.net/get-mpic/5307186/2a0000018e13df91b0064ddd6d010fa3dbe8/600x800",
                        "https://avatars.mds.yandex.net/get-mpic/7631660/img_id5030633211651354258.jpeg/600x800"
                    )
                    MyGrid(title, desc, cost, image)
                }
            }
        }
    }
}

@Composable
fun MyGrid(title: List<String>, desc: List<String>, cost: List<String>, image: List<String>) {
    Column {
        Row (
            Modifier.fillMaxWidth().wrapContentHeight()
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
                ambientColor = Color.LightGray,
                spotColor = Color.LightGray
                )
            .aspectRatio(0.7f),
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
                    contentDescription = "",
                    Modifier
                        .size(180.dp)
                        .scale(1.3f)
                )
                Column (
                    Modifier
                        .fillMaxWidth()
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
                }
            }
        }
    }
}