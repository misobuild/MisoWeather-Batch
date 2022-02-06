package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.auth.JwtTokenProvider;
import com.misoweather.misoweatherservice.auth.KakaoOAuth;
import com.misoweather.misoweatherservice.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.constants.RegionEnum;
import com.misoweather.misoweatherservice.domain.comment.Comment;
import com.misoweather.misoweatherservice.domain.comment.CommentRepository;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member.MemberRepository;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMappingRepository;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMapping;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMappingRepository;
import com.misoweather.misoweatherservice.domain.nickname.*;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.domain.region.RegionRepository;
import com.misoweather.misoweatherservice.dto.request.member.DeleteMemberRequestDto;
import com.misoweather.misoweatherservice.dto.request.member.LoginRequestDto;
import com.misoweather.misoweatherservice.dto.request.member.SignUpRequestDto;
import com.misoweather.misoweatherservice.dto.response.member.MemberInfoResponseDto;
import com.misoweather.misoweatherservice.dto.response.member.NicknameResponseDto;
import com.misoweather.misoweatherservice.exception.ApiCustomException;
import com.misoweather.misoweatherservice.utils.factory.ValidatorFactory;
import com.misoweather.misoweatherservice.utils.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    // TODO 필드 생성자 추천하지 않음
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final AdjectiveRepository adjectiveRepository;
    private final AdverbRepository adverbRepository;
    private final EmojiRepository emojiRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoOAuth kakaoOAuth;

    public Member getMember(String socialId, String socialType){
        Member member = memberRepository
                .findBySocialIdAndSocialType(socialId, socialType)
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));
        return member;
    }

    public NicknameResponseDto buildNickname() {
        // TODO RANDOM sql 사용하면 좋을 것 같다.
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

    @Transactional
    public Member registerMember(SignUpRequestDto signUpRequestDto, String socialToken) throws ParseException {
        // TODO 애플의 경우 RSA 체크가 빠져있다.
        Validator validator = ValidatorFactory
                .of(signUpRequestDto.getSocialId(), signUpRequestDto.getSocialType(), socialToken);
        if(!validator.valid()) throw new ApiCustomException(HttpStatusEnum.BAD_REQUEST);

        memberRepository.findBySocialIdAndSocialType(signUpRequestDto.getSocialId(), signUpRequestDto.getSocialType())
                .ifPresent(m -> { throw new ApiCustomException(HttpStatusEnum.CONFLICT); });
        memberRepository.findByNickname(signUpRequestDto.getNickname())
                .ifPresent(m -> { throw new ApiCustomException(HttpStatusEnum.CONFLICT); });

        // buildMember()
        Member member = Member.builder()
                .socialId(signUpRequestDto.getSocialId())
                .socialType(signUpRequestDto.getSocialType())
                .emoji(signUpRequestDto.getEmoji())
                .nickname(signUpRequestDto.getNickname())
                .build();

        // getDefaultRegion()
        Region defaultRegion = regionRepository.findById(signUpRequestDto.getDefaultRegionId())
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));

        // buildMemberRegionMapping()
        MemberRegionMapping memberRegionMapping = MemberRegionMapping.builder()
                .regionStatus(RegionEnum.DEFAULT)
                .member(member)
                .region(defaultRegion)
                .build();

        Member registeredMember = memberRepository.save(member);
        memberRegionMappingRepository.save(memberRegionMapping);

        return registeredMember;
    }

    public String reissue(LoginRequestDto loginRequestDto, String socialToken) {
        Validator validator = ValidatorFactory
                .of(loginRequestDto.getSocialId(), loginRequestDto.getSocialType(), socialToken);
        Boolean temp = validator.valid();
        if(temp.equals(Boolean.FALSE)) {
            throw new ApiCustomException(HttpStatusEnum.BAD_REQUEST);
        }

        Member member = memberRepository
                .findBySocialIdAndSocialType(loginRequestDto.getSocialId(), loginRequestDto.getSocialType())
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));

        return jwtTokenProvider
                .createToken(Long.toString(member.getMemberId()), member.getSocialId(), member.getSocialType());
    }

    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    // TODO 사용하지 않는 함수 정리
    /**
     * 사용하지 않는 함수 정리할 것
     *
     * @author yeon
    **/
//    public MemberInfoResponseDto getMemberInfo(Member member) {
//        List<MemberRegionMapping> memberRegionMappingList =
//                memberRegionMappingRepository.findMemberRegionMappingByMember(member);
//
//        MemberRegionMapping memberRegionMapping = memberRegionMappingList.stream()
//                .filter(item -> item.getRegionStatus().equals(RegionEnum.DEFAULT))
//                .findFirst()
//                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));
//
//        return MemberInfoResponseDto.builder()
//                .emoji(member.getEmoji())
//                .nickname(member.getNickname())
//                .regionId(memberRegionMapping.getRegion().getId())
//                .regionName(BigScaleEnum
//                        .getEnum(memberRegionMapping.getRegion().getBigScale()).toString())
//                .build();
//    }

    public MemberInfoResponseDto buildMemberInfoResponse(Member member, MemberRegionMapping memberRegionMapping){
                return MemberInfoResponseDto.builder()
                .emoji(member.getEmoji())
                .nickname(member.getNickname())
                .regionId(memberRegionMapping.getRegion().getId())
                .regionName(BigScaleEnum
                        .getEnum(memberRegionMapping.getRegion().getBigScale()).toString())
                .build();
    }

    public Long getRandomId(Long number) {
        int randomNumber = ThreadLocalRandom
                .current()
                .nextInt(1, Long.valueOf(number).intValue() + 1);

        return Long.valueOf(randomNumber);
    }


    // TODO Deprecated 할 것인지 말 것인지 정리하기
    public Boolean ifMemberExist(String socialId, String socialType) {
//        try {
//            Logger logger = LoggerFactory.getLogger(this.getClass());
//            for(String item : socialType.split(",")){
//                System.out.println(item);
//            }
//            logger.info("user: {}", socialType.split(",")[0]);
//            logger.info("authCode: {}", socialType.split(",")[1]);
//            logger.info("identityToken: {}", socialType.split(",")[2]);
//            logger.info("identityToken: {}", socialType.split(",")[3]);
//        } catch (Exception e) {
//            throw new ApiCustomException(HttpStatusEnum.CONFLICT);
//        }

        return memberRepository.findBySocialIdAndSocialType(socialId, socialType).isPresent();
    }

//    public <T extends JpaRepository> void findbyId(T repository) throws Throwable {
//        System.out.println(repository.findById(idCreator.getRandomId(repository.count()))
//                .orElseThrow(() -> new ApiException(HttpStatusEnum.NOT_FOUND)));
//    }
}
