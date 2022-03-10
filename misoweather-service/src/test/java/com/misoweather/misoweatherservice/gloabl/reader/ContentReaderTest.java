package com.misoweather.misoweatherservice.gloabl.reader;

import com.misoweather.misoweatherservice.global.reader.ContentReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DisplayName("Global: ContentReader 테스트")
public class ContentReaderTest {
    private ContentReader contentReader;

    @BeforeEach
    void setUp(){
        this.contentReader = new ContentReader();
    }
    @Test
    @DisplayName("ContentReader 테스트")
    void check(){
        // given
        String givenString = "안녕하세요.\r아버지를 아버지라고 부르지 못하고..";

        // when
        String actual = contentReader.check(givenString);

        // then
        assertThat(actual, is("안녕하세요. 아버지를 아버지라고 부르지 못하고.."));

    }

}
