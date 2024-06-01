package com.musinsa.coordinator.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.UniqueConstraint

/*
    Category 종류
        Tops(상의)
        Outerwear(아우터)
        Pants(바지)
        Sneakers(스니커즈)
        Bags(가방)
        Hats(모자)
        Socks(양말)
        Accessories(액세서리)
 */
@Entity
class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0
    @Column(unique = true)
    var name: String = ""
}