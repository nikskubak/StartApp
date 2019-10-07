package com.respire.startapp

import android.content.Context
import com.respire.startapp.di.ContextModule
import com.respire.startapp.di.DaggerApplicationComponent
import com.respire.startapp.di.DataModule
import com.respire.startapp.repositories.EntityRepositoryImpl
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import javax.inject.Inject


public class EntityRepositoryTest {
    @Inject
    lateinit var entityRepositoryImpl: EntityRepositoryImpl

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val component = DaggerApplicationComponent.builder()
            .contextModule(ContextModule(mock(App::class.java)))
            .dataModule(DataModule(mock(Context::class.java)))
            .build()
    }

    @Test
    @Throws(Exception::class)
    fun testRepo() {
        Assert.assertNotEquals(entityRepositoryImpl, null)
    }
}