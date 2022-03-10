package com.misoweather.misoweatherservice.mapping.repository;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMapping;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMappingRepository;
import com.misoweather.misoweatherservice.domain.survey.Answer;
import com.misoweather.misoweatherservice.domain.survey.Survey;
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

    @Test
    @DisplayName("<Member>와 <Survey> 통해 ")
    void findByMemberAndSurvey(){
        // given
        Survey givenSurvey = entityManager.find(Survey.class, 1L);
        Answer givenAnswer = entityManager.find(Answer.class, 1L);
        Member givenMember = Member.builder()
                .socialType("kakao")
                .socialId("99999")
                .defaultRegion(1L)
                .nickname("홍길동")
                .emoji(":)")
                .build();
        entityManager.persist(givenMember);

        MemberSurveyMapping givenSurveyMapping = MemberSurveyMapping.builder()
                .member(givenMember)
                .survey(givenSurvey)
                .answer(givenAnswer)
                .shortBigScale("서울")
                .build();
        entityManager.persist(givenSurveyMapping);

        // when
        List<MemberSurveyMapping> actual = memberSurveyRepository.findByMemberAndSurvey(givenMember, givenSurvey);

        // then
        assertThat(actual.get(0).getMember(), is(givenMember));
        assertThat(actual.get(0).getSurvey(), is(givenSurvey));
        assertThat(actual.get(0).getAnswer(), is(givenAnswer));
    }

}
