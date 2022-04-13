package com.ymbok.kohelper.component

import androidx.paging.PagingSource
import androidx.paging.PagingState

open class KoPagingSource<T : Any>(private val pageSize:Int) : PagingSource<Int, T>() {


    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {

        return try {
            val page = params.key ?: 1
            val loadSize = params.loadSize

            var list = getData(page,loadSize)

            return  LoadResult.Page(
                    data = list,
                    prevKey = getPrevKey(page),
                    nextKey = getNextKey(list.size,loadSize,page,pageSize)

            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }

    }

    open suspend fun getData(page:Int, loadSize:Int):List<T>{
        return ArrayList()
    }

    fun getPrevKey(page:Int):Int?{
        var prevKey: Int?
        if(page == 1){
            prevKey = null
        }else{
            prevKey = page - 1
        }
        return prevKey
    }

    fun getNextKey(size:Int,loadSize:Int,page:Int,pageSize:Int):Int?{
        var nextKey: Int?
        if(page == 1){
            nextKey = if (size < pageSize) null else loadSize/pageSize  + 1
        }else{
            nextKey = if (size < pageSize) null else page + 1
        }
        return  nextKey
    }
}