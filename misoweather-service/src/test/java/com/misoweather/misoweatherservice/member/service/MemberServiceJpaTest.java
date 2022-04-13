package com.misoweather.misoweatherservice.member.service;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member.MemberRepository;
import com.misoweather.misoweatherservice.domain.nickname.AdjectiveRepository;
import com.misoweather.misoweatherservice.domain.nickname.AdverbRepository;
import com.misoweather.misoweatherservice.domain.nickname.EmojiRepository;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import com.misoweather.misoweatherservice.member.auth.JwtTokenProvider;
import com.misoweather.misoweatherservice.member.dto.SignUpRequestDto;
import com.misoweather.misoweatherservice.member.validator.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("MemberService JPA 활용 부분 테스트")
public class MemberServiceJpaTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AdjectiveRepository adjectiveRepository;
    @Autowired
    private AdverbRepository adverbRepository;
    @Autowired
    private EmojiRepository emojiRepository;
    @Autowired
    private TestEntityManager entityManager;
    @Mock
    private ValidatorFactory validatorFactory;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        this.memberService = new MemberService(memberRepository, adjectiveRepository, adverbRepository, emojiRepository, jwtTokenProvider, validatorFactory);
    }
}
