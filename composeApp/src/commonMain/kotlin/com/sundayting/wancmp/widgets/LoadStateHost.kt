package com.sundayting.wancmp.widgets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

abstract class LoadStateHost {

    var isRefreshing by mutableStateOf(false)
        protected set
    var isLoadingMore by mutableStateOf(false)
        protected set

    var isEmpty by mutableStateOf(false)
        protected set

    var isError by mutableStateOf(false)
        protected set

    fun startLoad(isRefresh: Boolean) {
        isRefreshing = isRefresh
        isLoadingMore = !isRefresh
    }

    var canLoadMore by mutableStateOf(true)
        protected set

    fun changeCanLoadMore(canLoadMore: Boolean) {
        this.canLoadMore = canLoadMore
    }

    fun changeError(isError: Boolean) {
        this.isError = isError
    }

    fun changeEmpty(isEmpty: Boolean) {
        this.isEmpty = isEmpty
    }

    fun beforeLoad() {
        endLoad()
        isEmpty = false
        isError = false
    }

    fun endLoad() {
        isLoadingMore = false
        isRefreshing = false
    }
}

/**
 * 加载之前的校验
 * @param tryRefresh 本次加载是不是刷新
 */
fun LoadStateHost.checkBeforeLoad(
    tryRefresh: Boolean,
    beforeLoad: () -> Unit = this::beforeLoad,
    cancelLastJob: (() -> Unit)? = null
): Boolean {
    //如果当前在刷新，那就不要在做任何加载处理了
    if (isRefreshing) {
        return false
    }

    //如果尝试刷新，那就尝试取消上一次的工作（加载更多）
    if (tryRefresh) {
        cancelLastJob?.invoke()
    }
    //如果尝试加载更多，当前也在加载更多 或者 不能加载更多了，那就不要在做任何加载处理了
    else {
        if (isLoadingMore || !canLoadMore) {
            return false
        }
    }
    beforeLoad()
    startLoad(tryRefresh)
    return true
}
