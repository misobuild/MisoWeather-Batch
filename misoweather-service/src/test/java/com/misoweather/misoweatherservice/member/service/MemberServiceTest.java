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


    @Test
    @DisplayName("buildNickName() 테스트")
    void buildNicknameTestFailFirstBranch(){
        // given
        given(adjectiveRepository.count()).willReturn(1L);
        given(adjectiveRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> memberService.buildNickname())
                .isInstanceOf(ApiCustomException.class)
                .hasMessageContaining(HttpStatusEnum.NOT_FOUND.getMessage());
    }


    @Test
    @DisplayName("buildNickName() 테스트")
    void buildNicknameTestFailSecondBranch(){
        // given
        Adjective givenAdjective = spy(Adjective.class);

        given(adjectiveRepository.count()).willReturn(1L);
        given(adverbRepository.count()).willReturn(1L);
        given(adjectiveRepository.findById(anyLong())).willReturn(Optional.ofNullable(givenAdjective));
        given(adverbRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> memberService.buildNickname())
                .isInstanceOf(ApiCustomException.class)
                .hasMessageContaining(HttpStatusEnum.NOT_FOUND.getMessage());
    }


    @Test
    @DisplayName("buildNickName() 테스트")
    void buildNicknameTestFailThirdBranch(){
        // given
        Adjective givenAdjective = spy(Adjective.class);
        Adverb givenAdverb = spy(Adverb.class);

        given(adjectiveRepository.count()).willReturn(1L);
        given(adverbRepository.count()).willReturn(1L);
        given(emojiRepository.count()).willReturn(1L);
        given(adjectiveRepository.findById(anyLong())).willReturn(Optional.ofNullable(givenAdjective));
        given(adverbRepository.findById(anyLong())).willReturn(Optional.of(givenAdverb));
        given(emojiRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> memberService.buildNickname())
                .isInstanceOf(ApiCustomException.class)
                .hasMessageContaining(HttpStatusEnum.NOT_FOUND.getMessage());
    }


    @Test
    @DisplayName("buildMemberInfoResponse() 테스트")
    void buildMemberInfoResponseTest(){
        // given
        Region spyRegion = spy(Region.class);
        doReturn(9999L).when(spyRegion).getId();
        doReturn("경기도").when(spyRegion).getBigScale();
        Member givenMember = Member.builder().emoji("testEmoji").nickname("testNickname").build();
        MemberRegionMapping givenMapping = MemberRegionMapping.builder().region(spyRegion).build();

        // when
        MemberInfoResponseDto actual = memberService.buildMemberInfoResponse(givenMember, givenMapping);

        // then
        assertThat(actual.getEmoji(), is("testEmoji"));
        assertThat(actual.getNickname(), is("testNickname"));
        assertThat(actual.getRegionId(), is(9999L));
        assertThat(actual.getRegionName(), is("경기"));
    }


    @Test
    @DisplayName("getRandomId() 테스트")
    void getRandomIdTest(){
        // given
        Long givenNumber = 9999L;
        boolean same = false;

        // when
        Long actual = memberService.getRandomId(givenNumber);
        if(actual == givenNumber) same = true;

        // then
        assertThat(same, is(false));
    }


    // valida
    @Test
    @DisplayName("checkToken() throws BAD_REQUEST 테스트")
    void checkTokenFail(){
        // given
        Validator givenValidator = spy(Validator.class);
        doReturn(false).when(givenValidator).valid();
        given(validatorFactory.of(any(), any(), any())).willReturn(givenValidator);

        // when, then
        assertThatThrownBy(() -> memberService.checkToken("testID", "kakao", "testNickname"))
                .isInstanceOf(ApiCustomException.class)
                .hasMessageContaining(HttpStatusEnum.BAD_REQUEST.getMessage());
    }

    @Test
    @DisplayName("checkToken() doesNotThrow 테스트")
    void checkToken(){
        // given
        Validator givenValidator = spy(Validator.class);
        doReturn(true).when(givenValidator).valid();
        given(validatorFactory.of(any(), any(), any())).willReturn(givenValidator);

        // when, then
        assertDoesNotThrow(() -> memberService.checkToken("testID", "kakao", "testNickname"));
    }


    @Test
    @DisplayName("checkExistence() 테스트 - 첫번째 CONFLICT 발생")
    void checkExistenceFirstException(){
        //given
        Member givenMember = Member.builder().build();
        given(memberRepository.findBySocialIdAndSocialType(any(), any())).willReturn(Optional.of(givenMember));

        // when, then
        assertThatThrownBy(() -> memberService.checkExistence("testID", "kakao", "testNickname"))
                .isInstanceOf(ApiCustomException.class)
                .hasMessageContaining(HttpStatusEnum.CONFLICT.getMessage());
    }

    @Test
    @DisplayName("checkExistence() 테스트")
    void checkExistenceSecondException(){
        //given
        Member givenMember = Member.builder().build();
        given(memberRepository.findBySocialIdAndSocialType(any(), any())).willReturn(Optional.empty());
        given(memberRepository.findByNickname(any())).willReturn(Optional.of(givenMember));

        // when, then
        assertThatThrownBy(() -> memberService.checkExistence("testID", "kakao", "testNickname"))
                .isInstanceOf(ApiCustomException.class)
                .hasMessageContaining(HttpStatusEnum.CONFLICT.getMessage());
    }

    @Test
    @DisplayName("checkExistence() 테스트")
    void checkExistenceSuccess(){
        //given
        given(memberRepository.findBySocialIdAndSocialType(any(), any())).willReturn(Optional.empty());
        given(memberRepository.findByNickname(any())).willReturn(Optional.empty());

        // when, then
        assertDoesNotThrow(() -> memberService.checkExistence("testID", "testType", "testName"));
    }

    @Test
    @DisplayName("createToken() 테스트")
    void createTokenTest(){
        // given
        Member spyMember = spy(Member.class);
        doReturn(99999L).when(spyMember).getMemberId();
        doReturn("testSocialID").when(spyMember).getSocialId();
        doReturn("testSocialType").when(spyMember).getSocialType();

        given(jwtTokenProvider.createToken("99999", "testSocialID", "testSocialType")).willReturn("testSuccess");

        // when
        String actual = memberService.createToken(spyMember);

        // then
        assertThat(actual, is("testSuccess"));
    }
}
