package com.respire.startapp

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.respire.startapp.di.ContextModule
import com.respire.startapp.di.DataModule
import com.respire.startapp.repositories.EntityRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class EntityRepositoryTest {
    @Inject
    lateinit var entityRepository: EntityRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val app = InstrumentationRegistry.getTargetContext().applicationContext as App
        val component = DaggerTestAppComponent.builder()
            .contextModule(ContextModule(app))
            .dataModule(DataModule(app))
            .build()
            .inject(this)
    }

    @Test
    fun testRepo() {
        Assert.assertNotEquals(entityRepository, null)
    }
}