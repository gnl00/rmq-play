package com.demo.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/4/18
 */
@Slf4j
@Service
@RocketMQMessageListener(consumerGroup = "${rmq-play.group-test}", topic = "${rmq-play.topic-test}", selectorExpression = "${rmq-play.tag-test}")
public class TestListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        log.info("[TestListener] onMessage: {}", message);
    }
}
