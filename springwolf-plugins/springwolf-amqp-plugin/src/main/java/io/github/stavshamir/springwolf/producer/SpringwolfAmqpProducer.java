// SPDX-License-Identifier: Apache-2.0
package io.github.stavshamir.springwolf.producer;

import com.asyncapi.v2._6_0.model.channel.ChannelItem;
import com.asyncapi.v2._6_0.model.channel.operation.Operation;
import com.asyncapi.v2.binding.channel.amqp.AMQPChannelBinding;
import com.asyncapi.v2.binding.operation.amqp.AMQPOperationBinding;
import io.github.stavshamir.springwolf.asyncapi.AsyncApiService;
import io.github.stavshamir.springwolf.asyncapi.types.AsyncAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
public class SpringwolfAmqpProducer {

    private final AsyncApiService asyncApiService;
    private final Optional<RabbitTemplate> rabbitTemplate;

    public boolean isEnabled() {
        return rabbitTemplate.isPresent();
    }

    public SpringwolfAmqpProducer(AsyncApiService asyncApiService, List<RabbitTemplate> rabbitTemplates) {
        this.asyncApiService = asyncApiService;
        this.rabbitTemplate = rabbitTemplates.isEmpty() ? Optional.empty() : Optional.of(rabbitTemplates.get(0));
    }

    public void send(String channelName, Object payload) {
        AsyncAPI asyncAPI = asyncApiService.getAsyncAPI();
        ChannelItem channelItem = asyncAPI.getChannels().get(channelName);

        String exchange = getExchangeName(channelItem);
        String routingKey = getRoutingKey(channelItem);
        if (routingKey.isEmpty() && exchange.isEmpty()) {
            routingKey = channelName;
        }

        if (rabbitTemplate.isPresent()) {
            rabbitTemplate.get().convertAndSend(exchange, routingKey, payload);
        } else {
            log.warn("AMQP producer is not configured");
        }
    }

    private String getExchangeName(ChannelItem channelItem) {
        String exchange = "";
        if (channelItem.getBindings() != null && channelItem.getBindings().containsKey("amqp")) {
            AMQPChannelBinding channelBinding =
                    (AMQPChannelBinding) channelItem.getBindings().get("amqp");
            if (channelBinding.getExchange() != null
                    && channelBinding.getExchange().getName() != null) {
                exchange = channelBinding.getExchange().getName();
            }
        }

        return exchange;
    }

    private String getRoutingKey(ChannelItem channelItem) {
        String routingKey = "";
        Operation operation =
                channelItem.getSubscribe() != null ? channelItem.getSubscribe() : channelItem.getPublish();
        if (operation != null
                && operation.getBindings() != null
                && operation.getBindings().containsKey("amqp")) {
            AMQPOperationBinding operationBinding =
                    (AMQPOperationBinding) operation.getBindings().get("amqp");
            if (!CollectionUtils.isEmpty(operationBinding.getCc())) {
                routingKey = operationBinding.getCc().get(0);
            }
        }

        return routingKey;
    }
}
