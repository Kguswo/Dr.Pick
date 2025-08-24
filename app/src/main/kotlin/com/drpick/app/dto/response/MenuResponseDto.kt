package com.drpick.app.dto.response

import com.drpick.app.entity.FoodCategory
import com.drpick.app.entity.Menu
import com.drpick.app.entity.PriceRange

class MenuResponseDto(
    val id: Long,
    val name: String,
    val category: FoodCategory,
    val categoryDisplay: String,
    val description: String?,
    val calories: Int?,
    val spicyLevel: Int,
    val hasLiquidOrSauce: Boolean,
    val isDietFriendly: Boolean,
    val priceRange: PriceRange,
    val priceRangeDisplay: String,
    val scores: MenuScoreDto
) {

    companion object {
        fun from(menu: Menu): MenuResponseDto {
            return MenuResponseDto(
                id = menu.id,
                name = menu.name,
                category = menu.category,
                categoryDisplay = menu.category.displayName,
                description = menu.description,
                calories = menu.calories,
                spicyLevel = menu.spicyLevel,
                hasLiquidOrSauce = menu.hasLiquidOrSauce,
                isDietFriendly = menu.isDietFriendly,
                priceRange = menu.priceRange,
                priceRangeDisplay = menu.priceRange.displayName,
                scores = MenuScoreDto(
                    alone = menu.aloneScore,
                    date = menu.dateScore,
                    family = menu.familyScore,
                    group = menu.groupScore,
                    hotWeather = menu.hotWeatherScore,
                    coldWeather = menu.coldWeatherScore,
                    rainyWeather = menu.rainyWeatherScore
                )
            )
        }
    }
}

data class MenuScoreDto(
    val alone: Int,
    val date: Int,
    val family: Int,
    val group: Int,
    val hotWeather: Int,
    val coldWeather: Int,
    val rainyWeather: Int
)