package com.misoweather.misoweatherservice.region.service;

import com.misoweather.misoweatherservice.config.JpaAuditingConfiguration;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.domain.region.RegionRepository;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import com.misoweather.misoweatherservice.region.reader.RegionReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfiguration.class)
@DisplayName("RegionService에서 비즈니스 로직 없는 JPA 활용 부분을 테스트한다.")
public class RegionServiceJpaTest {
    @Autowired
    private RegionRepository regionRepository;
    private RegionReader regionReader;
    @Autowired
    private TestEntityManager entityManager;
    private RegionService regionService;

    @BeforeEach
    void setUp() {
        this.regionReader = new RegionReader();
        this.regionService = new RegionService(regionRepository, regionReader);
    }

    @Test
    @DisplayName("getRegion() 테스트")
    void getRegion(){
        // given
        Region givenRegion = entityManager.find(Region.class, 1L);

        // when
        Region actual = regionService.getRegion(1L);

        // then
        assertThat(actual.getId(), is(givenRegion.getId()));
    }

    @Test
    @DisplayName("getRegion() 테스트")
    void getRegionFail(){
        // when, then
        assertThatThrownBy(() -> regionService.getRegion(99999L))
                .isInstanceOf(ApiCustomException.class)
                .hasMessageContaining(HttpStatusEnum.NOT_FOUND.getMessage());
    }

}
