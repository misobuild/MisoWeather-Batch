package com.misoweather.misoweatherservice.domain.member_survey_mapping;

import com.misoweather.misoweatherservice.domain.Timestamped;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.survey.Answer;
import com.misoweather.misoweatherservice.domain.survey.Survey;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Entity(name = "MEMBER_SURVEY_MAPPING_TB")
public class MemberSurveyMapping extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ")
    private Long seq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "MEMBER_ID")
    private Member member;

    // TODO fetch join 사용하여 n+1 문제 해결해야
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "ANSWER_ID")
    private Answer answer;

    // TODO fetch join 사용하여 n+1 문제 해결해야
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "SURVEY_ID")
    private Survey survey;

    @Column(nullable = false, name = "SHORT_BIGSCALE", columnDefinition = "varchar(5)")
    private String shortBigScale;

    @Builder
    public MemberSurveyMapping(Member member, Answer answer, Survey survey, String shortBigScale) {
        this.member = member;
        this.survey = survey;
        this.answer = answer;
        this.shortBigScale = shortBigScale;
    }
}
