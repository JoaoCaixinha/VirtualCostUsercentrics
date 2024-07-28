package com.example.app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.app.VirtualCostCounterViewModel.VirtualCostCounterEvent
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class VirtualCostCounterViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: VirtualCostCounterViewModel

    @Mock
    private lateinit var virtualCostCounterEventObserver: Observer<VirtualCostCounterViewModel.VirtualCostCounterEvent>

    @Mock
    private lateinit var isFirstTimeObserver: Observer<Boolean>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = VirtualCostCounterViewModel()
    }

    @Test
    fun `test on application starts it fetch store info if first layer was accepted`() {
        viewModel.virtualCostCounterEvents.observeForever(virtualCostCounterEventObserver)
        assertEquals(VirtualCostCounterEvent.AsUserAsFirstTimeEvent, viewModel.virtualCostCounterEvents.value)
        verify(virtualCostCounterEventObserver).onChanged(VirtualCostCounterEvent.AsUserAsFirstTimeEvent)
    }

    @Test
    fun `test isFirstTime setter method is updating variable`() {
        viewModel.isFirstTime.observeForever(isFirstTimeObserver)

        viewModel.setUserAsFirstTime(false)

        assertEquals(false, viewModel.isFirstTime.value)
        verify(isFirstTimeObserver).onChanged(false)
    }

    @Test
    fun `test if isFirstTime application display first layer banner`() {
        viewModel.virtualCostCounterEvents.observeForever(virtualCostCounterEventObserver)
        viewModel.setUserAsFirstTime(true)
        viewModel.onShowBannerPressed()

        assertEquals(VirtualCostCounterEvent.ShowFirstLayer, viewModel.virtualCostCounterEvents.value)
        verify(virtualCostCounterEventObserver).onChanged(VirtualCostCounterEvent.ShowFirstLayer)
    }

    @Test
    fun `test if not isFirstTime application display second layer banner`() {
        viewModel.virtualCostCounterEvents.observeForever(virtualCostCounterEventObserver)
        viewModel.setUserAsFirstTime(false)
        viewModel.onShowBannerPressed()

        assertEquals(VirtualCostCounterEvent.ShowSecondLayer, viewModel.virtualCostCounterEvents.value)
        verify(virtualCostCounterEventObserver).onChanged(VirtualCostCounterEvent.ShowSecondLayer)
    }
}