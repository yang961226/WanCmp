package com.sundayting.wancmp.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import wancmp.composeapp.generated.resources.Res
import wancmp.composeapp.generated.resources.can_not_load_more

@Composable
fun LoadingMoreWidget(
    modifier: Modifier = Modifier,
    state: LoadingMoreState
) {

    Surface(
        modifier.height(50.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            when (state) {
                LoadingMoreState.NotLoading -> {}
                LoadingMoreState.LoadingMore -> CircularProgressIndicator(
                    modifier = Modifier.size(30.dp)
                )

                LoadingMoreState.CantLoadMore -> Text(
                    stringResource(Res.string.can_not_load_more),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }

}

enum class LoadingMoreState {
    NotLoading,
    LoadingMore,
    CantLoadMore
}

@Composable
fun LoadStateHost.toLoadingMoreState(): LoadingMoreState {
    return if (isLoadingMore) {
        LoadingMoreState.LoadingMore
    } else if (!canLoadMore) {
        LoadingMoreState.CantLoadMore
    } else {
        LoadingMoreState.NotLoading
    }
}