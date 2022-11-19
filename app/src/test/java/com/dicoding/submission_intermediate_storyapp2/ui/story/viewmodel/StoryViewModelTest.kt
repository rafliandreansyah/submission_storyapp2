package com.dicoding.submission_intermediate_storyapp2.ui.story.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.submission_intermediate_storyapp2.data.StoryRepository
import com.dicoding.submission_intermediate_storyapp2.model.Story
import com.dicoding.submission_intermediate_storyapp2.ui.story.adapter.StoryAdapter
import com.dicoding.submission_intermediate_storyapp2.util.MainDispatcherRule
import com.dicoding.submission_intermediate_storyapp2.util.generateSuccessDummyListStoryResponse
import com.dicoding.submission_intermediate_storyapp2.util.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoryViewModel

    @Before
    fun setUp() {
        storyViewModel = StoryViewModel(storyRepository)
    }

    @Test
    fun `When get stories data not null and return data story`() = runTest {
        val dataDummy = generateSuccessDummyListStoryResponse()
        val dataPaging = StoryPagingSource.snapshot(dataDummy)

        val expectedValues = MutableLiveData<PagingData<Story>>()
        expectedValues.value = dataPaging

        `when`(storyRepository.getListStory()).thenReturn(expectedValues)

        val actualValues: PagingData<Story> = storyViewModel.getListStory().getOrAwaitValue()
        Mockito.verify(storyRepository).getListStory()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualValues)

        assertNotNull(differ.snapshot())
        assertEquals(dataDummy.size, differ.snapshot().size)
        assertEquals(dataDummy, differ.snapshot())
        assertEquals(dataDummy[0], differ.snapshot()[0])

    }

    @Test
    fun `When get stories return data empty`() = runTest {
        val dummyResponseWithData = generateSuccessDummyListStoryResponse()
        val dummyResponse = arrayListOf<Story>()
        val dataPaging = StoryPagingSource.snapshot(dummyResponse)

        val expectedValues = MutableLiveData<PagingData<Story>>()
        expectedValues.value = dataPaging

        `when`(storyRepository.getListStory()).thenReturn(expectedValues)

        val actualValues = storyRepository.getListStory().getOrAwaitValue()
        Mockito.verify(storyRepository).getListStory()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualValues)
        assertTrue(differ.snapshot().isEmpty())
        assertNotEquals(dummyResponseWithData, differ.snapshot())
    }
}


class StoryPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }

    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}