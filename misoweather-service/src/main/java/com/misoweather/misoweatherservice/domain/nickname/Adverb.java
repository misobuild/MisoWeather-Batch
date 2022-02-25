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
@Entity(name = "ADVERB_TB")
public class Adverb {
    @Id
    @Column(name = "ADVERB_ID")
    private Long id;

    @Column(name = "WORD", nullable = false)
    private String word;
}