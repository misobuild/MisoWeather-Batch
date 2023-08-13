package com.misoweather.misoweatherservice.service

import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMappingRepository
import com.misoweather.misoweatherservice.global.constants.RegionEnum
import com.misoweather.misoweatherservice.global.exception.ApiCustomException
import com.misoweather.misoweatherservice.mapping.service.MappingRegionService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

// lombok을 kotlin이 활용하지 못하는 문제 발생
class MappingRegionServiceKtTest : BehaviorSpec(
        {
            val memberRegionMappingRepository = mockk<MemberRegionMappingRepository>()
            val mappingRegionService = MappingRegionService(memberRegionMappingRepository)

            Given("사용자-지역 기록이 있는 경우") {
                val memberRegionMapping = MemberRegionMapping.builder().regionStatus(RegionEnum.DEFAULT).build();
                val memberRegionMappingList = listOf(memberRegionMapping)

                When("기록의 지역 상태가 기본 상태인 첫번째 기록을 가져오면") {
                    val actual = mappingRegionService.filterMemberRegionMappingList(memberRegionMappingList)

                    Then("사용자의 기본 상태 지역을 가져올 수 있다.") {
                        actual.regionStatus shouldBe RegionEnum.DEFAULT
                    }
                }
            }

            Given("사용자-지역 리스트가 비어있을 때"){
                val memberRegionMappingEmptyList = emptyList<MemberRegionMapping>()

                When("현재 날짜 조건에 부합하는 필터링을 하면"){
                    val actual = shouldThrow<ApiCustomException> {
                        mappingRegionService.filterMemberRegionMappingList(memberRegionMappingEmptyList)
                    }
                    Then("NOT_FOUND 커스텀 에러가 발생한다."){
                        actual.message shouldBe "NOT_FOUND"
                    }
                }
            }
        }
)