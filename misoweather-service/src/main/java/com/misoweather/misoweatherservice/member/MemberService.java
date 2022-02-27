package com.misoweather.misoweatherservice.member;

import com.misoweather.misoweatherservice.global.auth.JwtTokenProvider;
import com.misoweather.misoweatherservice.global.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member.MemberRepository;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.nickname.*;
import com.misoweather.misoweatherservice.member.dto.SignUpRequestDto;
import com.misoweather.misoweatherservice.member.dto.MemberInfoResponseDto;
import com.misoweather.misoweatherservice.member.dto.NicknameResponseDto;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import com.misoweather.misoweatherservice.global.utils.factory.ValidatorFactory;
import com.misoweather.misoweatherservice.global.utils.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AdjectiveRepository adjectiveRepository;
    private final AdverbRepository adverbRepository;
    private final EmojiRepository emojiRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Member getMember(String socialId, String socialType) {
        Member member = memberRepository
                .findBySocialIdAndSocialType(socialId, socialType)
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));
        return member;
    }

    public NicknameResponseDto buildNickname() {
        Adjective adjective = adjectiveRepository.findById(getRandomId(adjectiveRepository.count()))
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));
        Adverb adverb = adverbRepository.findById(getRandomId(adverbRepository.count()))
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));
        Emoji emoji = emojiRepository.findById(getRandomId(emojiRepository.count()))
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));

        return NicknameResponseDto.builder()
                .nickname(adjective.getWord() + " " + adverb.getWord() + emoji.getWord())
                .emoji(emoji.getEmoji())
                .build();
    }

    public Member buildMemberAndSave(SignUpRequestDto signUpRequestDto) {
        Member member = Member.builder()
                .socialId(signUpRequestDto.getSocialId())
                .socialType(signUpRequestDto.getSocialType())
                .emoji(signUpRequestDto.getEmoji())
                .nickname(signUpRequestDto.getNickname())
                .build();
        return memberRepository.save(member);
    }

    public MemberInfoResponseDto buildMemberInfoResponse(Member member, MemberRegionMapping memberRegionMapping) {
        return MemberInfoResponseDto.builder()
                .emoji(member.getEmoji())
                .nickname(member.getNickname())
                .regionId(memberRegionMapping.getRegion().getId())
                .regionName(BigScaleEnum.getEnum(memberRegionMapping.getRegion().getBigScale()).toString())
                .build();
    }

    public Long getRandomId(Long number) {
        int randomNumber = ThreadLocalRandom
                .current()
                .nextInt(1, Long.valueOf(number).intValue() + 1);
        return Long.valueOf(randomNumber);
    }

    public void checkToken(String socialId, String socialType, String socialToken) {
        Validator validator = ValidatorFactory.of(socialId, socialType, socialToken);
        if (!validator.valid()) throw new ApiCustomException(HttpStatusEnum.BAD_REQUEST);
    }

    public void checkExistence(String socialId, String socialType, String nickname) {
        memberRepository.findBySocialIdAndSocialType(socialId, socialType)
                .ifPresent(m -> { throw new ApiCustomException(HttpStatusEnum.CONFLICT); });
        memberRepository.findByNickname(nickname)
                .ifPresent(m -> { throw new ApiCustomException(HttpStatusEnum.CONFLICT); });
    }

    public String createToken(Member member){
        return jwtTokenProvider
                .createToken(Long.toString(member.getMemberId()), member.getSocialId(), member.getSocialType());
    }

    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }
}
