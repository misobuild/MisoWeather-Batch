package com.misoweather.misoweatherservice.member.service;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member.MemberRepository;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.nickname.*;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import com.misoweather.misoweatherservice.member.auth.JwtTokenProvider;
import com.misoweather.misoweatherservice.member.dto.MemberInfoResponseDto;
import com.misoweather.misoweatherservice.member.dto.NicknameResponseDto;
import com.misoweather.misoweatherservice.member.validator.Validator;
import com.misoweather.misoweatherservice.member.validator.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService 테스트")
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private AdjectiveRepository adjectiveRepository;
    @Mock
    private AdverbRepository adverbRepository;
    @Mock
    private EmojiRepository emojiRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private ValidatorFactory validatorFactory;
    @InjectMocks
    private MemberService memberService;


    @BeforeEach
    public void setUp() {
        this.memberService = new MemberService(memberRepository, adjectiveRepository, adverbRepository, emojiRepository, jwtTokenProvider, validatorFactory);
    }

    @Test
    @DisplayName("buildNickName() 테스트")
    void buildNicknameTest(){
        // given
        Adjective givenAdjective = spy(Adjective.class);
        doReturn("행복한").when(givenAdjective).getWord();
        Adverb givenAdverb = spy(Adverb.class);
        doReturn("빨간").when(givenAdverb).getWord();
        Emoji givenEmoji = spy(Emoji.class);
        doReturn("코뿔소").when(givenEmoji).getWord();
        doReturn("testEmoji").when(givenEmoji).getEmoji();

        given(adjectiveRepository.count()).willReturn(1L);
        given(adverbRepository.count()).willReturn(1L);
        given(emojiRepository.count()).willReturn(1L);
        given(adjectiveRepository.findById(anyLong())).willReturn(Optional.ofNullable(givenAdjective));
        given(adverbRepository.findById(anyLong())).willReturn(Optional.ofNullable(givenAdverb));
        given(emojiRepository.findById(anyLong())).willReturn(Optional.ofNullable(givenEmoji));

        // when
        NicknameResponseDto actual = memberService.buildNickname();

        // then
        assertThat(actual.getNickname(), is("행복한 빨간코뿔소"));
        assertThat(actual.getEmoji(), is("testEmoji"));
    }
}
