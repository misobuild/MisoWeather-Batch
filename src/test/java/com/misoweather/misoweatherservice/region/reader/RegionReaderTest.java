package com.misoweather.misoweatherservice.region.reader;

import com.misoweather.misoweatherservice.domain.region.Region;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.Mockito.spy;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("MappingRegionService 테스트")
public class RegionReaderTest {
    @InjectMocks
    private RegionReader regionReader;

    @BeforeEach
    void setUp(){
        this.regionReader = new RegionReader();
    }

    @Test
    @DisplayName("<MidScale> 리스트를 받으면 중복 값을 삭제한다.")
    void filterMidScaleList(){
        // given
        Region givenRegionOne = spy(Region.class);
        Region givenRegionTwo = spy(Region.class);
        given(givenRegionOne.getMidScale()).willReturn("노원구");
        given(givenRegionTwo.getMidScale()).willReturn("노원구");
        List<Region> givenRawMidScaleList = List.of(givenRegionOne, givenRegionTwo);

        // when
        List<Region> actual = regionReader.filterMidScaleList(givenRawMidScaleList);

        // then
        assertThat(actual.size(), is(1));
        assertThat(actual.get(0).getMidScale(), is("노원구"));
    }
}
