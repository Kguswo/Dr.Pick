package com.drpick.app.repository

import com.drpick.app.entity.FoodCategory
import com.drpick.app.entity.Menu
import com.drpick.app.entity.PriceRange
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MenuRepository : JpaRepository<Menu, Long> {

	// 카테고리별 조회
	fun findByCategory(category: FoodCategory): List<Menu>

	// 여러 카테고리로 조회
	fun findByCategoryIn(category: List<FoodCategory>): List<Menu>

	// 매운맛 정도 필터링
	fun findBySpicyLevelLessThanEqual(maxSpicy: Int): List<Menu>

	// 다이어트 친화적 메뉴
	fun findByIsDietFreindlyTrue(): List<Menu>

	// 국물/소스 여부로 필터링 (밝은 옷 고려)
	fun findByHasLiquidOrSauceFalse(): List<Menu>

	// 가격대별 조회
	fun findByPriceRange(priceRange: PriceRange): List<Menu>

	// 메뉴명으로 검색 (부분 일치)
	fun findByNameContainingIgnoreCase(name: String): List<Menu>

	// 복합 조건 검색용 커스텀 쿼리
	@Query(
		"""
		SELECT m FROM Menu m
		WHERE (:categories IS NULL OR m.category IN :categories)
		AND (:maxSpicy IS NULL OR m.spicyLevel <= :maxSpicy)
		AND (:dietFriendly IS NULL OR m.isDietFrienly = :dietFriendly)
		AND (:hasLiquidOrSauce IS NULL OR m.hasLiquidOrSauce = :hasLiquidOrSauce)
		AND (:priceRange IS NULL OR m.priceRange = :priceRange)
	"""
	)
	fun findMenusByConditions(
		@Param("categories") categories: List<FoodCategory>?,
		@Param("maxSpicy") maxSpicy: Int?,
		@Param("dietFriendly") dietFriendly: Boolean?,
		@Param("hasLiquidOrSauce") hasLiquidOrSauce: Boolean?,
		@Param("priceRange") priceRange: PriceRange?,
	): List<Menu>

	// 추천 알고리즘용 : 상황별 점수가 높은 메뉴들
	fun findByAloneScoreGreaterThanEqual(minScore: Int): List<Menu>
	fun findByDateScoreGreaterThanEqual(minScore: Int): List<Menu>
	fun findByFamilyScoreGreaterThanEqual(minScore: Int): List<Menu>
	fun findByGroupScoreGreaterThanEqual(minScore: Int): List<Menu>

	// 날씨별 적합한 메뉴들
	fun findByHotWeatherScoreGreaterThanEqual(minScore: Int): List<Menu>
	fun findByColdWeatherScoreGreaterThanEqual(minScore: Int): List<Menu>

	// 랜덤 메뉴 선택 (초기 테스트용)
	@Query("SELECT m FROM Menu m ORDER BY RANDOM() LIMIT :count")
	fun findRandomMenus(@Param("count") count: Int): List<Menu>
	
}