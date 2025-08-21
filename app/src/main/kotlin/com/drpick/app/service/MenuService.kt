package com.drpick.app.service

import com.drpick.app.entity.FoodCategory
import com.drpick.app.entity.Menu
import com.drpick.app.entity.PriceRange
import com.drpick.app.repository.MenuRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MenuService(private val menuRepository: MenuRepository) {
    /**
     * 전체 메뉴 조회
     */
    fun getAllMenus(): List<Menu> {
        return menuRepository.findAll()
    }

    /**
     * ID로 메뉴 조회
     */
    fun getMenuById(id: Long): Menu? {
        return menuRepository.findByIdOrNull(id)
    }

    /**
     * 카테고리별 메뉴 조회
     */
    fun getMenusByCategory(category: FoodCategory): List<Menu> {
        return menuRepository.findByCategory(category)
    }

    /**
     * 여러 카테고리의 메뉴 조회
     */
    fun getMenusByCategories(categories: List<FoodCategory>): List<Menu> {
        return menuRepository.findByCategoryIn(categories)
    }

    /**
     * 메뉴명으로 검색
     */
    fun searchMenusByName(name: String): List<Menu> {
        return menuRepository.findByNameContainingIgnoreCase(name)
    }

    /**
     * 매운맛 정도로 필터링
     */
    fun getMenusBySpicyLevel(maxSpicy: Int): List<Menu> {
        return menuRepository.findBySpicyLevelLessThanEqual(maxSpicy)
    }

    /**
     * 다이어트 친화적 메뉴 조회
     */
    fun getDietFriendlyMenus(): List<Menu> {
        return menuRepository.findByIsDietFreindlyTrue()
    }

    /**
     * 국물, 소스가 없는 메뉴 조회 (옷에 튀지 않는 메뉴)
     */
    fun getMenusWithoutLiquid(): List<Menu> {
        return menuRepository.findByHasLiquidOrSauceFalse()
    }

    /**
     * 가격대별 메뉴 조회
     */
    fun getMenusByPriceRange(priceRange: PriceRange): List<Menu> {
        return menuRepository.findByPriceRange(priceRange)
    }

    /**
     * 복합 조건으로 메뉴 필터링
     */
    fun getFilteredMenus(
        categories: List<FoodCategory>? = null,
        maxSpicy: Int? = null,
        dietFriendly: Boolean? = null,
        hasLiquidOrSauce: Boolean? = null,
        priceRange: PriceRange? = null
    ): List<Menu> {
        return menuRepository.findMenusByConditions(
            categories = categories,
            maxSpicy = maxSpicy,
            dietFriendly = dietFriendly,
            hasLiquidOrSauce = hasLiquidOrSauce,
            priceRange = priceRange
        )
    }

    /**
     * 상황별 추천 메뉴
     * @param situation 상황 (alone: 혼밥, date: 데이트, family: 가족, group: 회식/모임)
     * @param minScore 최소 점수 (기본값: 4)
     */
    fun getMenusForSituation(situation: String, minScore: Int = 4): List<Menu> {
        return when (situation.lowercase()) {
            "alone", "혼밥" -> menuRepository.findByAloneScoreGreaterThanEqual(minScore)
            "date", "데이트" -> menuRepository.findByDateScoreGreaterThanEqual(minScore)
            "family", "가족" -> menuRepository.findByFamilyScoreGreaterThanEqual(minScore)
            "group", "회식", "모임" -> menuRepository.findByGroupScoreGreaterThanEqual(minScore)
            else -> emptyList()
        }
    }

    /**
     * 날씨별 추천 메뉴
     * @param weather 날씨 상황
     * @param minScore 최소 점수 (기본값: 4)
     */
    fun getMenusForWeather(weather: String, minScore: Int = 4): List<Menu> {
        return when (weather.lowercase()) {
            // 온도 기반
            "hot", "더움", "더운날", "여름", "summer" ->
                menuRepository.findByHotWeatherScoreGreaterThanEqual(minScore)

            "cold", "추움", "추운날", "겨울", "winter" ->
                menuRepository.findByColdWeatherScoreGreaterThanEqual(minScore)

            // 기상 상황
            "rainy", "비", "비오는날", "장마" ->
                menuRepository.findByRainyWeatherScoreGreaterThanEqual(minScore)

            "snowy", "눈", "눈오는날" -> {
                // 눈오는 날: 추운날과 비슷하게 따뜻한 음식 선호
                menuRepository.findByColdWeatherScoreGreaterThanEqual(minScore)
            }

            // 계절 (날씨 제약 없음)
            "spring", "봄", "autumn", "fall", "가을" -> {
                // 봄/가을: 날씨 제약 없음
                emptyList()
            }

            else -> emptyList()
        }
    }

    /**
     * 랜덤 메뉴 조회
     */
    fun getRandomMenus(count: Int = 5): List<Menu> {
        return menuRepository.findRandomMenus(count)
    }

    /**
     * 맞춤 추천 메뉴 - Best 추천 (3개)
     *
     * 로직: 조건에 가장 잘 맞는 메뉴들을 찾아서 상위 3개만 추천
     */
    fun getRecommendedMenus(
        categories: List<FoodCategory>? = null,
        situation: String? = null,
        weather: String? = null,
        maxSpicy: Int? = null,
        dietFriendly: Boolean? = null,
        avoidLiquid: Boolean? = null,
        priceRange: PriceRange? = null,
    ): List<Menu> {

        // 1. 기본 필터링
        val basicMenus = getFilteredMenus(
            categories = categories,
            maxSpicy = maxSpicy,
            dietFriendly = dietFriendly,
            hasLiquidOrSauce = if (avoidLiquid == true) false else null,
            priceRange = priceRange
        )

        // 2. 상황별 점수가 높은 메뉴로 추가 필터링
        val menusWithSituationScore = if (!situation.isNullOrBlank()) {
            basicMenus.map { menu ->
                val score = when (situation.lowercase()) {
                    "alone", "혼밥" -> menu.aloneScore
                    "date", "데이트" -> menu.dateScore
                    "family", "가족" -> menu.familyScore
                    "group", "회식", "모임" -> menu.groupScore
                    else -> 3 // 기본 점수
                }
                menu to score
            }.filter { it.second >= 3 } // 3점 이상만
        } else {
            basicMenus.map { it to 3 } // 상황 조건 없으면 기본 점수
        }

        // 3. 날씨별 점수가 높은 메뉴로 추가 필터링
        val finalScores = if (!weather.isNullOrBlank()) {
            menusWithSituationScore.mapNotNull { (menu, situationScore) ->
                val weatherScore = when (weather.lowercase()) {
                    "hot", "더움", "더운날", "여름", "summer" -> menu.hotWeatherScore
                    "cold", "추움", "추운날", "겨울", "winter" -> menu.coldWeatherScore
                    "rainy", "비", "비오는날", "장마" -> menu.rainyWeatherScore
                    "snowy", "눈", "눈오는날" -> menu.coldWeatherScore
                    else -> 3 // 기본 점수
                }

                if (situationScore >= 3 && weatherScore >= 3) {
                    val totalScore = situationScore + weatherScore
                    menu to totalScore
                } else {
                    null
                }
            }
        } else {
            menusWithSituationScore.map { (menu, score) -> menu to score }
        }

        // 4. 점수 순으로 정렬 후 3위까지
        val sortedMenus = finalScores
            .sortedByDescending { it.second }
            .map { it.first }

        return when {
            sortedMenus.size >= 3 -> sortedMenus.take(3) // 정확히 3개
            else -> sortedMenus // 1개 또는 2개면 그대로 반환
        }
    }
}