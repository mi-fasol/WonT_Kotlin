package com.example.haemo_kotlin.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.user.UserResponseModel

@Composable
fun ErrorScreen(text: String) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painterResource(id = R.drawable.error_image),
                contentDescription = null,
                modifier = Modifier.size((screenWidth / 4).dp)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = text,
                color = Color(0xffc2c2c2),
                fontSize = 12.5.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterInfo(type: String, value: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
        ) {
            androidx.compose.material3.Text(type, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xff82C0EA),
                    unfocusedBorderColor = Color.LightGray,
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(15.dp)
            )
        }
    }
}

@Composable
fun PostUserInfo(user: UserResponseModel, date: String) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    painter = painterResource(id = userMyPageImageList[user.userImage]),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size((screenHeight / 18).dp)
                )
            }
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = user.nickname,
                    color = Color(0xff3f3f3f),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.5.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${user.major} / ", fontSize = 8.5.sp, color = Color(0xff3f3f3f))
                    val iconColor =
                        if (user.gender == "남자") Color(0xff82c0e2) else Color(0xffFF9B9B)
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(8.5.dp)
                    )
                    Text(text = " / $date", fontSize = 8.5.sp, color = Color(0xff3f3f3f))
                }
            }
        }
    }
}

@Composable
fun SendReply(type: String, postType: Int, value: String, onValueChange: (String) -> Unit) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
//                decorationBox = { ... },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(10f)
                    .height((screenWidth / 9).dp)
                    .background(Color(0xffededed), RoundedCornerShape(25.dp))
                    .padding(10.dp)
            )
            FilledIconButton(
                onClick = { /*TODO*/ },
                shape = IconButtonDefaults.filledShape,
                colors = IconButtonColors(
                    containerColor = colorResource(id = R.color.mainColor),
                    contentColor = Color.White,
                    disabledContainerColor = colorResource(id = R.color.mainColor),
                    disabledContentColor = Color.White
                ),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.send_comment_icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size((screenWidth / 20).dp)
                )
            }
        }
    }
}