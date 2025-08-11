package com.drpick.app.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "menus")
class Menu(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0,

	@Column(nullable = false)
	val name: String,

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	val category: FoodCategory,

	@Column(length = 500)
	val description: String? = null,

	val calories: Int? = null,

	@Column(name = "spicy_level")
	val spicyLevel: Int = 0,  // 0: 안매움, 1-5: 매운 정도

	@Column(name = "has_liquid_or_sauce")
	val hasLiquidOrSauce: Boolean = false,  // 국물/소스 여부 (흰옷 고려)

	@Column(name = "is_diet_frienly")
	val isDietFrienly: Boolean = false,  // 다이어트 적합성

	@Enumerated(EnumType.STRING)
	@Column(name = "price_range")
	val priceRange: PriceRange = PriceRange.UNDER_10K,

	// 상황별 적합성 점수 (1-5점)
	@Column(name = "alone_score")
	val aloneScore: Int = 3,  // 혼밥 적합도

	@Column(name = "date_score")
	val dateScore: Int = 3,   // 데이트 적합도

	@Column(name = "family_score")
	val familyScore: Int = 3, // 가족식사 적합도

	@Column(name = "group_score")
	val groupScore: Int = 3,  // 회식/모임 적합도

	// 날씨별 적합성 점수 (1-5점)
	@Column(name = "hot_weather_score")
	val hotWeatherScore: Int = 3,   // 더운날 적합도

	@Column(name = "cold_weather_score")
	val coldWeatherScore: Int = 3   // 추운날 적합도
) {
	override fun toString(): String {
		return "Menu(id=$id, name='$name', category=$category)"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		other as Menu
		return id == other.id
	}

	override fun hashCode(): Int {
		return id.hashCode()
	}
}

enum class FoodCategory(val displayName: String) {
	KOREAN("한식"),
	WESTERN("양식"),
	JAPANESE("일식"),
	CHINESE("중식"),
	SNACK("간식"),
	CAFE("카페/디저트"),
	BUFFET("뷔페"),
	ASIAN("아시안"),
	OTHER("기타")
}

enum class PriceRange(val displayName: String, val minPrice: Int, val maxPrice: Int) {
	UNDER_10K("1만원 이하", 0, 10000),
	UNDER_20K("1~2만원", 10000, 20000),
	UNDER_30K("2~3만원", 20000, 30000),
	OVER_30K("3만원 이상", 30000, Integer.MAX_VALUE),

}