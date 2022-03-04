package com.misoweather.misoweatherservice.mapping.repository;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMappingRepository;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.global.constants.RegionEnum;
import com.misoweather.misoweatherservice.mapping.service.MappingRegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MappingRegionRepositoryTest {
    @Autowired
    private MemberRegionMappingRepository memberRegionRepository;

    @Autowired
    private EntityManager entityManager;

    private MappingRegionService mappingRegionService;

    @BeforeEach
    void setUp(){
        this.mappingRegionService = new MappingRegionService(memberRegionRepository);
    }

    @Test
    @DisplayName("성공: <Member> 객체로 <MemberRegionMapping> 리스트 찾아 반환한다.")
    void getMemberRegionMappingList(){
        // given
        Member givenMember = Member.builder()
                .socialType("kakao")
                .socialId("99999")
                .defaultRegion(1L)
                .nickname("홍길동")
                .emoji(":)")
                .build();
        entityManager.persist(givenMember);
        Region givenRegion = entityManager.find(Region.class, 1L);

        MemberRegionMapping givenMemberRegionMapping = MemberRegionMapping.builder()
                .member(givenMember)
                .region(givenRegion)
                .regionStatus(RegionEnum.DEFAULT)
                .build();
        entityManager.persist(givenMemberRegionMapping);

        // when
        List<MemberRegionMapping> actual = mappingRegionService.getMemberRegionMappingList(givenMember);

        // then
        assertThat(actual, is(List.of(givenMemberRegionMapping)));
    }

    @Test
    @DisplayName("성공: <MemberRegionMapping> 리스트가 존재한다면 모두 삭제한다.")
    void deleteMemberRegionTest(){
        // given
        Member givenMember = Member.builder()
                .socialType("kakao")
                .socialId("99999")
                .defaultRegion(1L)
                .nickname("홍길동")
                .emoji(":)")
                .build();
        entityManager.persist(givenMember);
        Region givenRegion = entityManager.find(Region.class, 1L);

        MemberRegionMapping givenMemberRegionMapping = MemberRegionMapping.builder()
                .member(givenMember)
                .region(givenRegion)
                .regionStatus(RegionEnum.DEFAULT)
                .build();
        entityManager.persist(givenMemberRegionMapping);

        // when
        mappingRegionService.deleteMemberRegion(givenMember);

        // then
        assertThat(memberRegionRepository.findMemberRegionMappingByMember(givenMember), is(List.of()));
    }

    @Test
    @DisplayName("성공: <MemberRegionMapping> 빌드하여 저장한다.")
    void buildMemberRegionMappingAndSave(){
        // given
        Member givenMember = Member.builder()
                .socialType("kakao")
                .socialId("99999")
                .defaultRegion(1L)
                .nickname("홍길동")
                .emoji(":)")
                .build();
        entityManager.persist(givenMember);
        Region givenRegion = entityManager.find(Region.class, 1L);

        MemberRegionMapping givenMemberRegionMapping = MemberRegionMapping.builder()
                .member(givenMember)
                .region(givenRegion)
                .regionStatus(RegionEnum.DEFAULT)
                .build();

        // when
        MemberRegionMapping actual = mappingRegionService.buildMemberRegionMappingAndSave(givenMember, givenRegion);

        // then
        assertThat(actual, is(givenMemberRegionMapping));
    }


    @Test
    @DisplayName("성공 테스트: <MemberRegionMapping>을 저장한다.")
    void saveMemberRegionMapping(){
        // given
        Member givenMember = Member.builder()
                .socialType("kakao")
                .socialId("99999")
                .defaultRegion(1L)
                .nickname("홍길동")
                .emoji(":)")
                .build();
        entityManager.persist(givenMember);
        Region givenRegion = entityManager.find(Region.class, 1L);

        MemberRegionMapping givenMemberRegionMapping = MemberRegionMapping.builder()
                .member(givenMember)
                .region(givenRegion)
                .regionStatus(RegionEnum.DEFAULT)
                .build();

        // when
        MemberRegionMapping actual = mappingRegionService.saveMemberRegionmapping(givenMemberRegionMapping);

        // then
        assertThat(actual, is(givenMemberRegionMapping));
    }
}
