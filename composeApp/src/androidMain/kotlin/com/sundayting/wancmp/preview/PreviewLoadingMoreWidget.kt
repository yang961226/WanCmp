package com.sundayting.wancmp.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sundayting.wancmp.widgets.LoadingMoreState
import com.sundayting.wancmp.widgets.LoadingMoreWidget

@Composable
@Preview
fun PreviewLoadingMoreWidget() {

    Column{
        LoadingMoreWidget(
            state = LoadingMoreState.CantLoadMore,
            modifier = Modifier.fillMaxWidth()
        )
        LoadingMoreWidget(
            state = LoadingMoreState.LoadingMore,
            modifier = Modifier.fillMaxWidth()
        )
    }

}