package com.misoweather.misoweatherservice.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberEvent {
    private Long memberId;
    private String socialId;
    private String socialType;
    private String nickname;
    private String emoji;
    private Long defaultRegion;
    @Builder
    public MemberEvent(Long memberId, String socialId, String socialType, String nickname, String emoji, Long defaultRegion) {
        this.memberId = memberId;
        this.socialId = socialId;
        this.socialType = socialType;
        this.nickname = nickname;
        this.emoji = emoji;
        this.defaultRegion = defaultRegion;
    }
}
