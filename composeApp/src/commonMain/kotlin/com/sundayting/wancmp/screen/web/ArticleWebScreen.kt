package com.sundayting.wancmp.screen.web

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.toUri
import com.multiplatform.webview.request.RequestInterceptor
import com.multiplatform.webview.request.WebRequest
import com.multiplatform.webview.request.WebRequestInterceptResult
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import org.jetbrains.compose.resources.stringResource
import wancmp.composeapp.generated.resources.Res
import wancmp.composeapp.generated.resources.action_jump
import wancmp.composeapp.generated.resources.action_need_browser

data class ArticleWebScreen(
    val url: String
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val state = rememberWebViewState(url)
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()

        val snackbarHostState = remember { SnackbarHostState() }

        var rejectUrl by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            snapshotFlow { state.lastLoadedUrl }.collect {
                rejectUrl = null
            }
        }

        if (rejectUrl != null) {
            val actionString = stringResource(Res.string.action_need_browser)
            val actionJump = stringResource(Res.string.action_jump)
            LaunchedEffect(Unit) {
                val result = snackbarHostState.showSnackbar(
                    message = actionString,
                    withDismissAction = true,
                    actionLabel = actionJump,
                    duration = SnackbarDuration.Indefinite
                )
                when (result) {
                    SnackbarResult.Dismissed -> {
                        rejectUrl = null
                    }

                    SnackbarResult.ActionPerformed -> {
                        rejectUrl = null
                    }
                }
            }
        }

        Scaffold(
            topBar = {
                Surface(
                    shadowElevation = 2.dp
                ) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                state.pageTitle.orEmpty(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        expandedHeight = 46.dp,
                        navigationIcon = {
                            IconButton(onClick = {
                                navigator.pop()
                            }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }

            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) {
            val webViewNavigator = rememberWebViewNavigator(
                coroutineScope = scope,
                requestInterceptor = remember {
                    object : RequestInterceptor {
                        override fun onInterceptUrlRequest(
                            request: WebRequest,
                            navigator: WebViewNavigator
                        ): WebRequestInterceptResult {
                            val uri = request.url.toUri()
                            if (uri.scheme == "http" || uri.scheme == "https") {
                                return WebRequestInterceptResult.Allow
                            }
                            rejectUrl = request.url
                            return WebRequestInterceptResult.Reject
                        }

                    }
                }
            )
            Box(
                Modifier.fillMaxSize().padding(it)
            ) {
                WebView(
                    state,
                    navigator = webViewNavigator,
                    modifier = Modifier.fillMaxSize(),
                )
                val progress by remember {
                    derivedStateOf {
                        val loadingState = state.loadingState
                        if (loadingState is LoadingState.Loading) {
                            loadingState.progress
                        } else {
                            null
                        }
                    }
                }
                if (progress != null) {
                    LinearProgressIndicator(
                        progress = { progress ?: 0f },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

    }

}