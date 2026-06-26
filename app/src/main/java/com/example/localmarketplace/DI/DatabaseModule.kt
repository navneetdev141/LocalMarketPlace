package com.example.localmarketplace.DI

import android.content.Context
import androidx.room.Room
import com.example.localmarketplace.data.ListingDao
import com.example.localmarketplace.data.local.ListingDatabase
import com.example.localmarketplace.data.local.WishlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ListingDatabase {
        return Room.databaseBuilder(
            context,
            ListingDatabase::class.java,
            "listing_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideListingDao(db: ListingDatabase): ListingDao {
        return db.listingDao()
    }
    @Provides
    fun provideWishlistDao(
        database: ListingDatabase
    ): WishlistDao {
        return database.wishlistDao()
    }

}
