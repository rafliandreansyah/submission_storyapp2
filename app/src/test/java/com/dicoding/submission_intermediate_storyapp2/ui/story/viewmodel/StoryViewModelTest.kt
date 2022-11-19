package com.dicoding.submission_intermediate_storyapp2.ui.story.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.submission_intermediate_storyapp2.data.StoryRepository
import com.dicoding.submission_intermediate_storyapp2.model.ResponseDetailStory
import com.dicoding.submission_intermediate_storyapp2.model.ResponseGeneral
import com.dicoding.submission_intermediate_storyapp2.model.ResponseListStory
import com.dicoding.submission_intermediate_storyapp2.model.Story
import com.dicoding.submission_intermediate_storyapp2.ui.story.adapter.StoryAdapter
import com.dicoding.submission_intermediate_storyapp2.util.*
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
import java.io.File

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
    @Mock
    private lateinit var file: File
    companion object {
        private const val desc = "desc"
        private const val EXISTING_ID_DETAIL_STORY = "KDI"
        private const val NOT_FOUND_ID_DETAIL_STORY = "NOT FOUND"
    }


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

    @Test
    fun `When create story return Result Success`() {
        val dataDummy = generateSuccessDummyCreateStory()
        val expectedValues = MutableLiveData<Result<ResponseGeneral>>()
        expectedValues.value = Result.Success(dataDummy)

        `when`(storyRepository.createStory(desc, file)).thenReturn(expectedValues)

        val actualValues = storyViewModel.createStory(desc, file).getOrAwaitValue()
        Mockito.verify(storyRepository).createStory(desc, file)

        assertNotNull(actualValues)
        assertTrue(actualValues is Result.Success)
        assertEquals((expectedValues.value as Result.Success).data, actualValues.data)
    }

    @Test
    fun `When create story then return Result Error`() {
        val dataDummy = generateErrorDummyCreateStory()
        val expectedValues = MutableLiveData<Result<ResponseGeneral>>()
        expectedValues.value = Result.Error(data = dataDummy, code = 400)

        `when`(storyRepository.createStory(desc, file)).thenReturn(expectedValues)

        val actualValues = storyViewModel.createStory(desc, file).getOrAwaitValue()
        Mockito.verify(storyRepository).createStory(desc, file)

        assertNotNull(actualValues)
        assertTrue(actualValues is Result.Error)
        assertEquals((expectedValues.value as Result.Error).data?.message, actualValues.data?.message)
    }

    @Test
    fun `When get list story with lat lon return Result Success`(){
        val dataDummy = generateSuccessDummyListStoryLocation()
        val expectedValues = MutableLiveData<Result<ResponseListStory>>()
        expectedValues.value = Result.Success(dataDummy)

        `when`(storyRepository.getListStoryLocation()).thenReturn(expectedValues)

        val actualValues = storyViewModel.getListStoryLocation().getOrAwaitValue()
        Mockito.verify(storyRepository).getListStoryLocation()

        assertNotNull(actualValues)
        assertTrue(actualValues is Result.Success)
        assertNotNull(actualValues.data?.listStory?.get(0)?.lat)
        assertNotNull(actualValues.data?.listStory?.get(0)?.lon)
        assertEquals((expectedValues.value as Result.Success).data?.listStory?.size, actualValues.data?.listStory?.size)
        assertEquals((expectedValues.value as Result.Success).data?.listStory?.get(0), actualValues.data?.listStory?.get(0))
    }

    @Test
    fun `When get list story with lat lon return Result Error`() {
        val dataDummy = generateErrorDummyListStoryLocation()
        val expectedValues = MutableLiveData<Result<ResponseListStory>>()
        expectedValues.value = Result.Error(data = dataDummy, code = null)

        `when`(storyRepository.getListStoryLocation()).thenReturn(expectedValues)

        val actualValues = storyViewModel.getListStoryLocation().getOrAwaitValue()
        Mockito.verify(storyRepository).getListStoryLocation()

        assertNotNull(actualValues)
        assertTrue(actualValues is Result.Error)
        assertNull(actualValues.data?.listStory?.get(0)?.lat)
        assertNull(actualValues.data?.listStory?.get(0)?.lon)
    }

    @Test
    fun `When get detail story return Result Success`() {
        val dataDummy = generateSuccessDummyDetailStory()
        val expectedValues = MutableLiveData<Result<ResponseDetailStory>>()
        expectedValues.value = Result.Success(dataDummy)

        `when`(storyRepository.getDetailStory(EXISTING_ID_DETAIL_STORY)).thenReturn(expectedValues)
        val actualValues = storyViewModel.getDetailStory(EXISTING_ID_DETAIL_STORY).getOrAwaitValue()
        Mockito.verify(storyRepository).getDetailStory(EXISTING_ID_DETAIL_STORY)

        assertNotNull(actualValues)
        assertTrue(actualValues is Result.Success)
        assertEquals((expectedValues.value as Result.Success).data?.story, actualValues.data?.story)
    }

    @Test
    fun `When get detail story return Result Error`(){
        val dataDummy = generateErrorDummyDetailStory()
        val expectedValues = MutableLiveData<Result<ResponseDetailStory>>()
        expectedValues.value = Result.Error(data = dataDummy, code = 401)

        `when`(storyRepository.getDetailStory(NOT_FOUND_ID_DETAIL_STORY)).thenReturn(expectedValues)
        val actualValues = storyViewModel.getDetailStory(NOT_FOUND_ID_DETAIL_STORY).getOrAwaitValue()
        Mockito.verify(storyRepository).getDetailStory(NOT_FOUND_ID_DETAIL_STORY)

        assertNotNull(actualValues)
        assertTrue(actualValues is Result.Error)
        assertEquals((expectedValues.value as Result.Error).data?.message, actualValues.data?.message)
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