package com.example.localmarketplace.DI

import com.example.localmarketplace.data.repository.ListingRepositoryImpl
import com.example.localmarketplace.data.repository.UserRepositoryImpl
import com.example.localmarketplace.data.repository.WishlistRepositoryImpl
import com.example.localmarketplace.domain.ListingRepository
import com.example.localmarketplace.domain.UserRepository
import com.example.localmarketplace.domain.WishlistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindListingRepository(
        listingRepositoryImpl: ListingRepositoryImpl
    ): ListingRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindWishlistRepository(
        wishlistRepositoryImpl: WishlistRepositoryImpl
    ): WishlistRepository
}
