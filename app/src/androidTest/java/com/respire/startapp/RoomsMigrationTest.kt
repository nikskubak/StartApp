package com.respire.startapp

import android.util.Log
import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.respire.startapp.data.sources.database.AppDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomsMigrationTest {
    public val TEST_DB = "migration-test"

    @Rule
    @JvmField
    var helper: MigrationTestHelper  = MigrationTestHelper (
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrateAll() {
        val database = helper.createDatabase(TEST_DB, 1)
        Log.e("db", " " + database.version)
        helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)
        Log.e("db", " " + database.version)
        helper.closeWhenFinished(database)
    }

    var MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE `Entity` ADD COLUMN `test_column` INTEGER NOT NULL DEFAULT 0"
            )
        }
    }
}