package com.misoweather.misoweatherservice.domain.member_region_mapping;

import com.misoweather.misoweatherservice.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRegionMappingRepository extends JpaRepository<MemberRegionMapping, Long> {
    List<MemberRegionMapping> findMemberRegionMappingByMember(Member member);
}