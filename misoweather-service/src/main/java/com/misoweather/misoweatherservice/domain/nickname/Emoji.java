package com.misoweather.misoweatherservice.domain.nickname;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Entity(name = "EMOJI_TB")
public class Emoji {
    @Id
    @Column(name = "EMOJI_ID")
    private Long id;

    @Column(name = "EMOJI_NAME", nullable = false)
    private String word;

    @Column(name = "EMOJI", nullable = false)
    private String emoji;
}