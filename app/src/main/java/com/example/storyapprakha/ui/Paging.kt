package com.example.storyapprakha.ui


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapprakha.data.network.responses.ListStoryItem
import com.example.storyapprakha.data.network.retrofit.Service
import com.example.storyapprakha.data.user.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class Paging(private val apiService: Service, userPreference: UserPreference) : PagingSource<Int, ListStoryItem>() {

    private val token = runBlocking {
        userPreference.getToken().map {
            it
        }.first()
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAll("Bearer $token" ,page, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}