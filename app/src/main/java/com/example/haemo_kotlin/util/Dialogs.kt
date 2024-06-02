package com.example.haemo_kotlin.util

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.acceptation.AcceptationResponseModel
import com.example.haemo_kotlin.model.retrofit.user.UserResponseModel
import com.example.haemo_kotlin.viewModel.board.AcceptationViewModel
import com.example.haemo_kotlin.viewModel.board.PostViewModel
import com.example.haemo_kotlin.viewModel.user.InquiryViewModel

@Composable
fun YesOrNoDialog(
    content: String,
    mainColor: Int,
    onClickCancel: () -> Unit,
    onClickConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = { onClickCancel() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        val conf = LocalConfiguration.current
        val screenHeight = conf.screenHeightDp
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height((screenHeight / 4.5).dp)
                .padding(horizontal = 20.dp)
        ) {
            Column {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(4f)
                ) {
                    Text(content, fontSize = 16.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        onClick = { onClickCancel() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xfff4f4f4),
                            contentColor = Color(0xff969696)
                        )
                    ) {
                        Text("아니오")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        onClick = { onClickConfirm() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = mainColor),
                            contentColor = Color.White
                        )
                    ) {
                        Text("예")
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmDialog(
    content: String,
    mainColor: Int,
    onClickConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        val conf = LocalConfiguration.current
        val screenHeight = conf.screenHeightDp
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height((screenHeight / 4.5).dp)
                .padding(horizontal = 20.dp)
        ) {
            Column {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(4f)
                ) {
                    Text(content, fontSize = 16.sp)
                }
                Button(
                    onClick = { onClickConfirm() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = mainColor),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                ) {
                    Text("확인")
                }
            }
        }
    }
}

@Composable
fun InquiryTypeDialog(
    inquiryViewModel: InquiryViewModel,
    onClickCancel: () -> Unit,
    onClicked: () -> Unit
) {
    Dialog(
        onDismissRequest = { onClickCancel() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Column {
                Text(
                    text = "문의 유형을 선택해 주세요.",
                    color = colorResource(id = R.color.mainGreyColor).copy(0.5f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 20.dp)
                        .fillMaxWidth()
                )
                inquiryTypeList.forEach { type ->
                    Box(
                        Modifier
                            .clickable {
                                inquiryViewModel.inquiryType.value = type
                                onClicked()
                            }
                            .fillMaxWidth()
                    ) {
                        Text(
                            type, color = colorResource(id = R.color.inquiryScreenTitleTextColor),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 7.dp, horizontal = 20.dp)
                        )
                    }
                    if (inquiryTypeList.indexOf(type) == 3) {
                        Spacer(Modifier.height(5.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun PostManagementDialog(
    onClickCancel: () -> Unit,
    onClicked: () -> Unit
) {
    Dialog(
        onDismissRequest = { onClickCancel() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(horizontal = 10.dp)
        ) {
            Column(

                Modifier.padding(vertical = 15.dp)
            ) {
                postManagementList.forEach { type ->
                    Box(
                        Modifier
                            .clickable {
                                when (type) {
                                    "임시 마감" -> {
                                        Log.d("미란", "임시 마감")
                                    }

                                    "수정하기" -> Log.d("미란", "수정하기")
                                    else -> onClicked()
                                }
                            }
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            type, color = colorResource(id = R.color.inquiryScreenTitleTextColor),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AttendUserDialog(
    attendList: List<AcceptationResponseModel>,
    attendees: List<UserResponseModel>,
    mainColor: Int,
    acceptationViewModel: AcceptationViewModel,
    onClickCancel: () -> Unit
) {
    val conf = LocalConfiguration.current
    val screenWidth = conf.screenWidthDp

    Dialog(
        onDismissRequest = { onClickCancel() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Box(
            modifier = Modifier
                .background(Color.Unspecified)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn() {
                items(attendList.size) {
                    val iconColor =
                        if (attendList[it].acceptation) colorResource(mainColor) else colorResource(
                            id = R.color.attendFalseColor
                        )
                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(30.dp))
                            .clickable {
                                acceptationViewModel.allowUserToJoin(
                                    attendList[it].pId,
                                    attendList[it].uId
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Image(
                                painterResource(
                                    id = userMyPageImageList[attendees[it].userImage]
                                ),
                                contentDescription = null,
                                modifier = Modifier.size((screenWidth / 8).dp)
                            )
                            Text(
                                attendees[it].nickname,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = colorResource(id = R.color.mainTextColor)
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.accept_user_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 15.dp)
                                    .size((screenWidth / 27).dp),
                                tint = iconColor
                            )
                        }
                    }
                    if(attendList.size -1 != it){
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}