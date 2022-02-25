package com.misoweather.misoweatherservice.domain.member;

import com.misoweather.misoweatherservice.domain.Timestamped;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Entity(name = "MEMBER_TB")
public class Member extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Column(name = "SOCIAL_ID", nullable = false)
    private String socialId;

    @Column(name = "SOCIAL_TYPE", nullable = false)
    private String socialType;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "MEMBER_EMOJI")
    private String emoji;

    @Builder
    public Member(String socialId, String socialType, String nickname, String emoji, Long defaultRegion) {
        this.socialId = socialId;
        this.socialType = socialType;
        this.nickname = nickname;
        this.emoji = emoji;
    }
}
