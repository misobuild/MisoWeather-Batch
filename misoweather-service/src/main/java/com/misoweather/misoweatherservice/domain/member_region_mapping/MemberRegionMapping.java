package com.misoweather.misoweatherservice.domain.member_region_mapping;

import com.misoweather.misoweatherservice.constants.RegionEnum;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.region.Region;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Entity(name = "MEMBER_REGION_MAPPING_TB")
public class MemberRegionMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ")
    private Long seq;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "STATUS")
    private RegionEnum regionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "REGION_ID")
    private Region region;

    @Builder
    public MemberRegionMapping(RegionEnum regionStatus, Member member, Region region ) {
        this.regionStatus = regionStatus;
        this.member = member;
        this.region = region;
    }

    public MemberRegionMapping update(Region region){
        this.region = region;
        return this;
    }
}
