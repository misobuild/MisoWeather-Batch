package com.misoweather.misoweatherservice.mapping.repository;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMappingRepository;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMapping;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMappingRepository;
import com.misoweather.misoweatherservice.domain.survey.Answer;
import com.misoweather.misoweatherservice.domain.survey.Survey;
import com.misoweather.misoweatherservice.mapping.service.MappingSurveyService;
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

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MappingSurveyRepositoryTest {
    @Autowired
    private MemberSurveyMappingRepository memberSurveyRepository;

    @Autowired
    private EntityManager entityManager;

    private MappingSurveyService mappingSurveyService;

    @BeforeEach
    void setUp(){
        this.mappingSurveyService = new MappingSurveyService(memberSurveyRepository);
    }

    @Test
    @DisplayName("성공: <Member> 객체로 <MemberRegionMapping> 리스트 찾아 반환한다.")
    void saveMemberSurveyMapping(){
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
}
