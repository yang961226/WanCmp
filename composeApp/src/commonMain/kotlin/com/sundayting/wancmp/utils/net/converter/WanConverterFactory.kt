package com.sundayting.wancmp.utils.net.converter

import com.sundayting.wancmp.utils.net.bean.NResult
import com.sundayting.wancmp.utils.net.bean.WanError
import com.sundayting.wancmp.utils.net.bean.WanNResult
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlin.coroutines.cancellation.CancellationException

class WanConverterFactory : Converter.Factory {

    override fun suspendResponseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit
    ): Converter.SuspendResponseConverter<HttpResponse, *>? {
        if (typeData.typeInfo.type == NResult::class) {
            return object : Converter.SuspendResponseConverter<HttpResponse, Any> {
                override suspend fun convert(result: KtorfitResult): Any {
                    fun Throwable.needSkip(): Boolean {
                        return when (this) {
                            is CancellationException -> true
                            else -> false
                        }
                    }
                    return when (result) {
                        is KtorfitResult.Success -> {
                            val body: Any =
                                result.response.body(typeData.typeArgs.first().typeInfo)
                            if (body is WanNResult<*>) {
                                if (body.errorCode != 0) {
                                    if (body.errorCode == -1001) {
//                                        eventManager.emitNeedLoginAgain()
                                    }
//                                    eventManager.emitToast(body.errorMsg)
                                    NResult.Error(WanError(body.errorCode, body.errorMsg))
                                } else {
                                    NResult.Success(body)
                                }
                            } else {
                                NResult.Success(body)
                            }
                        }

                        is KtorfitResult.Failure -> {
                            when (result.throwable) {
                                is WanError -> {
//                                    EventManager.getInstance()
//                                        .emitToast((result.throwable as WanError).errorMsg)
                                }

                                else -> {
                                    if (!result.throwable.needSkip()) {
//                                        EventManager.getInstance()
//                                            .emitToast("网络异常，请检查网络")
                                    }
                                }
                            }
                            NResult.Error(result.throwable)
                        }

                    }
                }

            }
        }
        return null
    }


}