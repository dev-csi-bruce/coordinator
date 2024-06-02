package com.musinsa.coordinator.model

import com.musinsa.coordinator.dto.admin.BrandDTO
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Brand(
    name: String
) {
    fun toDto(): BrandDTO {
        return BrandDTO(
            id = id,
            name = name
        )
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0
    @Column(unique = true)
    var name: String = name
        protected set
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(50) default 'ACTIVE'")
    var state: BrandState = BrandState.ACTIVE
        protected set

    fun updateState(state: BrandState): Brand {
        this.state = state
        return this
    }

    fun updateName(name: String): Brand {
        this.name = name
        return this
    }
}

enum class BrandState {
    ACTIVE, INACTIVE
}
