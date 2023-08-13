package com.misoweather.misoweatherservice.kafka;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.region.Region;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberEventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberEventProducer.class);

    private final KafkaTemplate<String, MemberEvent> kafkaTemplate;

    public void sendMessage(Member member, Region defaultRegion){
        MemberEvent memberEvent = MemberEvent.builder()
                .memberId(member.getMemberId())
                .socialType(member.getSocialType())
                .socialId(member.getSocialId())
                .nickname(member.getNickname())
                .emoji(member.getEmoji())
                .defaultRegion(defaultRegion.getId())
                .build();

        LOGGER.info(String.format("Member Event => %s", memberEvent.toString()));
        kafkaTemplate.send("misoweather-weather", memberEvent);
    }

    public void sendMessageTest(MemberEvent memberEvent){
        LOGGER.info(String.format("Member Event => %s", memberEvent.toString()));
        kafkaTemplate.send("misoweather-weather", memberEvent);
    }
}
