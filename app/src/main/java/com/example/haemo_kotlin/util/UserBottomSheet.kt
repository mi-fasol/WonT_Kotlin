package com.example.haemo_kotlin.util

import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.user.UserResponseModel
import com.example.haemo_kotlin.viewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserBottomSheet(
    user: UserResponseModel,
    navController: NavController,
    onCloseUnit: () -> Unit
) {
    val conf = LocalConfiguration.current
    val screenWidth = conf.screenWidthDp

    ModalBottomSheet(
        onDismissRequest = { onCloseUnit() },
        sheetState = androidx.compose.material3.rememberModalBottomSheetState(),
        containerColor = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 30.dp)
        ) {
            Image(
                painter = painterResource(id = userProfileList[user.userImage]),
                contentDescription = null,
                modifier = Modifier
                    .size((screenWidth / 3).dp)
                    .padding(15.dp)
            )
            Text(
                user.nickname,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Text(user.major, fontSize = 12.sp, modifier = Modifier.padding(bottom = 20.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height((screenWidth/9).dp)
                    .background(Color(0xfff5f5f5), RoundedCornerShape(15.dp))
                    .padding(bottom = 30.dp),
            ) {
                Text("하이용", fontSize = 13.sp, modifier = Modifier.fillMaxHeight())
            }
            Row(
                Modifier
                    .fillMaxWidth().padding(top = 15.dp, bottom = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.mainColor),
                            RoundedCornerShape(20.dp)
                        )
                        .weight(3f)
                        .padding(end = 10.dp)
                        .clickable {
                            navController.navigate(NavigationRoutes.ChatScreen.createRoute(user.uId))
                        }
                        .height((screenWidth / 9.5).dp)
                ) {
                    Text("채팅하기", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            Color(0xffF65A64),
                            RoundedCornerShape(20.dp)
                        )
                        .weight(1f)
                        .clickable {

                        }
                        .height((screenWidth / 9.5).dp)
                ) {
                    Text("신고하기", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}