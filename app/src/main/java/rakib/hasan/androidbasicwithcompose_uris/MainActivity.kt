package rakib.hasan.androidbasicwithcompose_uris

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import rakib.hasan.androidbasicwithcompose_uris.ui.theme.AndroidBasicWithCompose_UrisTheme
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* ----- URI(Unique Resource Identifier) TYPE 1 - RESOURCE URI ----- */
        val myUri = Uri.parse("android.resource://$packageName/drawable/iron_man")
        val imageBytes = contentResolver.openInputStream(myUri).use {
            it?.readBytes()
        }
        println("Image size (Resource Uri) : ${imageBytes?.size}") // OUTPUT -> Image size (Resource Uri) : 2718313


        /* ----- URI(Unique Resource Identifier) TYPE 2 - FILE URI ----- */
        val myFile = File(filesDir, "iron_man.png")
        FileOutputStream(myFile).use {
            it.write(imageBytes)
        }
        println("Path (File Uri) : ${myFile.toUri()}") // OUTPUT -> File (File Uri) : file:///data/user/0/rakib.hasan.androidbasicwithcompose_uris/files/iron_man.png

        /* ----- URI(Unique Resource Identifier) TYPE 4 - DATA URI ----- */
        val dataUri = Uri.parse("data:text/plain;charset=UTF-8,Hello%20World")

        setContent {
            AndroidBasicWithCompose_UrisTheme {
                /* ----- URI(Unique Resource Identifier) TYPE 3 - CONTENT URI ----- */

                var myContentUri by remember { mutableStateOf("") }
                var myContentImageSize by remember { mutableStateOf("") }

                val pickImage = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = {contentUri ->
                        myContentUri = contentUri.toString()
                        println("Info (Content Uri) : $contentUri") // OUTPUT -> Info (Content Uri) : content://com.android.providers.media.documents/document/image%3A1000000795
                        val myContentImageBytes =
                            contentUri?.let { it ->
                                contentResolver.openInputStream(it).use {
                                    it?.readBytes()
                                }
                            }
                        myContentImageSize = myContentImageBytes?.size.toString()
                        println("Image size (Resource Uri) : $myContentImageSize")

                    }
                )
                Scaffold(
                    modifier= Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.shadow(2.dp),
                            title = { Text(text = "URI(Unique Resource Identifier)") }
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {

                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterHorizontally)
                                .background(Color.DarkGray)
                                .padding(vertical = 4.dp),
                            text = "TYPE 1 : Resource Uri",
                            textAlign = TextAlign.Center,
                            style = TextStyle(Color.White),
                            fontSize = 18.sp,
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .align(Alignment.Start),
                            text = "android.resource://$packageName/drawable/iron_man | Image Size = ${imageBytes?.size.toString()}",
                            textAlign = TextAlign.Justify
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterHorizontally)
                                .background(Color.DarkGray)
                                .padding(vertical = 4.dp),
                            text = "TYPE 2 : File Uri",
                            textAlign = TextAlign.Center,
                            style = TextStyle(Color.White),
                            fontSize = 18.sp
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .align(Alignment.Start),
                            text = myFile.toUri().toString(),
                            textAlign = TextAlign.Justify
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterHorizontally)
                                .background(Color.DarkGray)
                                .padding(vertical = 4.dp),
                            text = "TYPE 3 : Content Uri",
                            textAlign = TextAlign.Center,
                            style = TextStyle(Color.White),
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterHorizontally),
                            onClick = { pickImage.launch("image/*") }
                        ) {
                            Text(text = "Pick Image")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .align(Alignment.Start),
                            text = if (myContentImageSize.isEmpty()) "" else "${myContentUri.toString()} | Image Size = $myContentImageSize",
                            textAlign = TextAlign.Justify
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterHorizontally)
                                .background(Color.DarkGray)
                                .padding(vertical = 4.dp),
                            text = "TYPE 4 : Data Uri",
                            textAlign = TextAlign.Center,
                            style = TextStyle(Color.White),
                            fontSize = 18.sp
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .align(Alignment.Start),
                            text = dataUri.toString(),
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }
        }
    }
}