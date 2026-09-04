package com.anthill.app.ui.location

import com.anthill.app.data.dao.LocationDao
import com.anthill.app.data.model.Location
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*

class LocationViewModelTest {

    private val locationDao = mock(LocationDao::class.java)
    private val viewModel = LocationViewModel(locationDao)

    @Test
    fun `should set success state with root locations when loading root`() = runBlocking {
        val roots = listOf(Location(id = 1, locationCode = "WH", name = "Warehouse", locationType = "warehouse", createdBy = "admin", updatedBy = "admin", createdAt = 0, updatedAt = 0))
        `when`(locationDao.getRootLocations()).thenReturn(roots)
        
        viewModel.loadRootLocations()
        
        val state = viewModel.viewState.value
        assert(state is LocationViewState.Success)
        assertEquals(roots, (state as LocationViewState.Success).locations)
    }

    @Test
    fun `should set success state with children when loading children`() = runBlocking {
        val parent = Location(id = 1, locationCode = "WH", name = "Warehouse", locationType = "warehouse", createdBy = "admin", updatedBy = "admin", createdAt = 0, updatedAt = 0)
        val children = listOf(Location(id = 2, locationCode = "HallA", name = "Hall A", parentId = 1, locationType = "hall", createdBy = "admin", updatedBy = "admin", createdAt = 0, updatedAt = 0))
        `when`(locationDao.getChildren(1)).thenReturn(children)
        
        viewModel.loadChildren(parent)
        
        val state = viewModel.viewState.value
        assert(state is LocationViewState.Success)
        assertEquals(children, (state as LocationViewState.Success).locations)
        assertEquals(parent, (state as LocationViewState.Success).parent)
    }
}
