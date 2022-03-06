package com.misoweather.misoweatherservice.mapping.service;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMapping;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMappingRepository;
import com.misoweather.misoweatherservice.domain.survey.Answer;
import com.misoweather.misoweatherservice.domain.survey.Survey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("MappingSurveyService에서 비즈니스 로직 없는 JPA 활용 부분을 테스트한다.")
public class MappingSurveyServiceJpaTest {
    @Autowired
    private MemberSurveyMappingRepository memberSurveyMappingRepository;
    @Autowired
    private TestEntityManager entityManager;
    private MappingSurveyService mappingSurveyService;

    @BeforeEach
    void setUp() {
        this.mappingSurveyService = new MappingSurveyService(memberSurveyMappingRepository);
    }

    @Test
    @DisplayName("성공: <Member> 객체로 <MemberSurveyMapping> 찾아 반환한다.")
    void saveMemberSurveyMapping() {
        // given
        Member givenMember = Member.builder()
                .socialType("kakao")
                .socialId("99999")
                .defaultRegion(1L)
                .nickname("홍길동")
                .emoji(":)")
                .build();
        entityManager.persist(givenMember);
        Answer givenAnswer = entityManager.find(Answer.class, 1L);
        Survey givenSurvey = entityManager.find(Survey.class, 1L);

        MemberSurveyMapping givenMemberSurveyMapping = MemberSurveyMapping.builder()
                .member(givenMember)
                .answer(givenAnswer)
                .survey(givenSurvey)
                .shortBigScale("서울")
                .build();

        // when
        MemberSurveyMapping actual = mappingSurveyService.saveMemberSurveyMapping(givenMemberSurveyMapping);

        // then
        assertThat(actual.getMember(), is(givenMember));
        assertThat(actual.getSurvey(), is(givenSurvey));
        assertThat(actual.getAnswer(), is(givenAnswer));
        assertThat(actual.getShortBigScale(), is("서울"));
    }

    @Test
    @DisplayName("성공: <Member> 객체로 <MemberSurveyMapping> 삭제한다. ")
    void deleteMemberSurvey() {
        // given
        Member givenMember = Member.builder()
                .socialType("kakao")
                .socialId("99999")
                .defaultRegion(1L)
                .nickname("홍길동")
                .emoji(":)")
                .build();
        entityManager.persist(givenMember);
        Answer givenAnswer = entityManager.find(Answer.class, 1L);
        Survey givenSurvey = entityManager.find(Survey.class, 1L);

        MemberSurveyMapping givenMemberSurveyMapping = MemberSurveyMapping.builder()
                .member(givenMember)
                .answer(givenAnswer)
                .survey(givenSurvey)
                .shortBigScale("서울")
                .build();
        entityManager.persist(givenMemberSurveyMapping);

        // when
        mappingSurveyService.deleteMemberSurvey(givenMember);
        List<MemberSurveyMapping> actual = memberSurveyMappingRepository.findByMemberAndSurvey(givenMember, givenSurvey);

        // then
        assertThat(actual, is(List.of()));
    }


    @Test
    @DisplayName("성공: 최근 (Long)days 만큼의 서베이를 찾아 반환한다.")
    void getRecentSurveyListFor() {
        // given
        Member givenMember = Member.builder()
                .socialType("kakao")
                .socialId("99999")
                .defaultRegion(1L)
                .nickname("홍길동")
                .emoji(":)")
                .build();
        entityManager.persist(givenMember);
        Answer givenAnswer = entityManager.find(Answer.class, 1L);
        Survey givenSurvey = entityManager.find(Survey.class, 1L);

        MemberSurveyMapping givenMemberSurveyMapping = MemberSurveyMapping.builder()
                .member(givenMember)
                .answer(givenAnswer)
                .survey(givenSurvey)
                .shortBigScale("서울")
                .build();
        entityManager.persist(givenMemberSurveyMapping);
        // when
        List<MemberSurveyMapping> actual = mappingSurveyService.getRecentSurveyListFor(1L)
                .stream().sorted(Comparator.comparing(MemberSurveyMapping::getCreatedAt).reversed()).collect(Collectors.toList());

        // then
        assertThat(actual.get(0).getSurvey().getId(), is(givenSurvey.getId()));
        assertThat(actual.get(0).getAnswer(), is(givenAnswer));
        assertThat(actual.get(0).getMember(), is(givenMember));
        assertThat(actual.get(0).getShortBigScale(), is("서울"));

        entityManager.remove(givenMemberSurveyMapping);
        entityManager.remove(givenMember);
        entityManager.clear();
    }
}
