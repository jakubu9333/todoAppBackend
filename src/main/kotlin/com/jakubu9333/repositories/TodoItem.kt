package com.jakubu9333.repositories

import com.jakubu9333.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class TodoItem(@Serializable(with = UUIDSerializer::class) val id: UUID, val item: String)
