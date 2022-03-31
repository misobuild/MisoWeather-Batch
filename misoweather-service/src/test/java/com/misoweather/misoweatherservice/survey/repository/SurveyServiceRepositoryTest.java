package com.misoweather.misoweatherservice.survey.repository;

import com.misoweather.misoweatherservice.config.JpaAuditingConfiguration;
import com.misoweather.misoweatherservice.domain.survey.AnswerRepository;
import com.misoweather.misoweatherservice.domain.survey.SurveyRepository;
import com.misoweather.misoweatherservice.survey.service.SurveyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfiguration.class)
@DisplayName("SurveyService에서 비즈니스 로직 없는 JPA 활용 부분을 테스트한다. 테스트")
public class SurveyServiceRepositoryTest {
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private TestEntityManager entityManager;

    private SurveyService mappingSurveyService;

    @BeforeEach
    void setUp() {
        this.mappingSurveyService = new SurveyService(surveyRepository, answerRepository);
    }

    @Test
    @DisplayName("")
    void getAnswer(){

    }
}
