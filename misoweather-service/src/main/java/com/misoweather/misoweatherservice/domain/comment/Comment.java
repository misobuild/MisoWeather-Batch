package com.misoweather.misoweatherservice.domain.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.misoweather.misoweatherservice.domain.Timestamped;
import com.misoweather.misoweatherservice.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Entity(name = "COMMENT_TB")
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(nullable = false, name = "CONTENT", columnDefinition = "varchar(45)")
    private String content;

    @Column(nullable = false, name = "BIGSCALE" , columnDefinition = "varchar(10)")
    private String bigScale;

    @Column(nullable = false, name = "NICKNAME" , columnDefinition = "varchar(45)")
    private String nickname;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "MEMBER_ID")
    private Member member;

    @Column(nullable = false, name = "DELETED")
    private Boolean deleted;

    @Column(nullable = false, name = "EMOJI")
    private String emoji;

    @Builder
    public Comment(String content, String bigScale, String nickname, Member member, Boolean deleted, String emoji) {
        this.content = content;
        this.bigScale = bigScale;
        this.nickname = nickname;
        this.member = member;
        this.deleted = deleted;
        this.emoji = emoji;
    }

    @Builder
    Comment(Long id, String content, String bigScale, String nickname, Member member, Boolean deleted, String emoji) {
        this.id = id;
        this.content = content;
        this.bigScale = bigScale;
        this.nickname = nickname;
        this.member = member;
        this.deleted = deleted;
        this.emoji = emoji;
    }
}
