package com.sundayting.wancmp.function.loginAndRegister

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import wancmp.composeapp.generated.resources.Res
import wancmp.composeapp.generated.resources.account_title
import wancmp.composeapp.generated.resources.input_account_text_field_hint
import wancmp.composeapp.generated.resources.login_button_text
import wancmp.composeapp.generated.resources.login_title
import wancmp.composeapp.generated.resources.password_again_title
import wancmp.composeapp.generated.resources.password_text_again_field_hint
import wancmp.composeapp.generated.resources.password_text_field_hint
import wancmp.composeapp.generated.resources.password_title
import wancmp.composeapp.generated.resources.register_button_text
import wancmp.composeapp.generated.resources.register_title
import wancmp.composeapp.generated.resources.remember_me

object LoginAndRegister {

    @Composable
    fun Content(
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier.fillMaxWidth()
        ) {
            val scope = rememberCoroutineScope()
            val pagerState = rememberPagerState { 2 }

            val icons = remember {
                listOf(
                    Icons.Filled.PersonAdd,
                    Icons.AutoMirrored.Filled.Login
                )
            }

            val contentDescriptions = listOf(
                stringResource(Res.string.register_title), // 仍然为可访问性提供描述
                stringResource(Res.string.login_title)
            )

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                val selectedIndex = pagerState.currentPage
                icons.forEachIndexed { index, icon ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = 2
                        ),
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        selected = index == selectedIndex,
                        icon = {

                        },
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = contentDescriptions[index]
                                )

                                Spacer(Modifier.width(5.dp))

                                Text(
                                    contentDescriptions[index]
                                )
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth().height(360.dp),
                userScrollEnabled = false,
                verticalAlignment = Alignment.Top,
            ) { page ->
                when (page) {
                    1 -> Column(Modifier.fillMaxWidth().padding(horizontal = 30.dp)) {
                        var account by remember { mutableStateOf("") }
                        var password by remember { mutableStateOf("") }
                        OutlinedTextField(
                            value = account,
                            onValueChange = {
                                account = it.take(30).trim()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(stringResource(Res.string.input_account_text_field_hint))
                            },
                            label = {
                                Text(stringResource(Res.string.account_title))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = null
                                )
                            },
                            singleLine = true
                        )
                        Spacer(Modifier.height(8.dp)) // 增加一些间距

                        var isPasswordVisible by remember { mutableStateOf(false) }

                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it.take(20).trim()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(stringResource(Res.string.password_title))
                            },
                            placeholder = {
                                Text(stringResource(Res.string.password_text_field_hint))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = null
                                )
                            },
                            suffix = {
                                IconButton(onClick = {
                                    isPasswordVisible = !isPasswordVisible
                                }) {
                                    Icon(
                                        imageVector = if (isPasswordVisible)
                                            Icons.Filled.Visibility
                                        else
                                            Icons.Filled.VisibilityOff,
                                        contentDescription = null
                                    )
                                }
                            },
                            singleLine = true,
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        )

                        Spacer(Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            var checked by remember { mutableStateOf(false) }
                            Checkbox(
                                checked,
                                onCheckedChange = { checked = !checked }
                            )
                            Text(
                                stringResource(Res.string.remember_me),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }

                        Spacer(Modifier.fillMaxHeight().weight(1f))
                        Button(
                            onClick = {

                            },
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(
                                vertical = 16.dp, horizontal = 100.dp
                            )
                        ) {
                            Text(
                                stringResource(Res.string.register_button_text),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Spacer(Modifier.height(30.dp))
                    }

                    0 -> Column(Modifier.fillMaxWidth().padding(horizontal = 30.dp)) {
                        var account by remember { mutableStateOf("") }
                        var password by remember { mutableStateOf("") }
                        var passwordAgain by remember { mutableStateOf("") }
                        OutlinedTextField(
                            value = account,
                            onValueChange = {
                                account = it.take(30).trim()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(stringResource(Res.string.input_account_text_field_hint))

                            },
                            label = {
                                Text(stringResource(Res.string.account_title))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = null
                                )
                            },
                            singleLine = true
                        )
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it.take(20).trim()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(stringResource(Res.string.password_title))
                            },
                            placeholder = {
                                Text(stringResource(Res.string.password_text_field_hint))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = null
                                )
                            },

                            singleLine = true
                        )

                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = passwordAgain,
                            onValueChange = {
                                passwordAgain = it.take(20).trim()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(stringResource(Res.string.password_again_title))
                            },
                            placeholder = {
                                Text(stringResource(Res.string.password_text_again_field_hint))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = null
                                )
                            },

                            singleLine = true
                        )
                        Spacer(Modifier.fillMaxHeight().weight(1f))
                        Button(
                            onClick = {

                            },
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(
                                vertical = 16.dp, horizontal = 100.dp
                            )
                        ) {
                            Text(
                                stringResource(Res.string.login_button_text),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Spacer(Modifier.height(30.dp))
                    }
                }

            }
        }
    }

}