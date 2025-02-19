package org.jset.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.jboss.logging.Logger;
import org.jset.jooq.generated.tables.records.ClipsRecord;

import java.util.List;

@ApplicationScoped
public class RabbitMQProducer {
    private static final Logger LOG = Logger.getLogger(RabbitMQProducer.class);

    @Inject
    Vertx vertx;

    public void clipRequest(ClipsRecord clip) {
        RabbitMQOptions config = new RabbitMQOptions();
        config.setUri("amqp://rabbit_user:rabbit_password@localhost:5672");

        RabbitMQClient client = RabbitMQClient.create(vertx, config);
        var queueName = "dvr-" + clip.getId() + "-requests";
        var mapper = new ObjectMapper();
        byte[] rabbitBody = null;
        try {
            var rabbitRequest = new RabbitClipRequest(clip);
            rabbitBody = mapper.writeValueAsBytes(rabbitRequest);
        } catch (JsonProcessingException e) {
            LOG.errorf("failed to map clip. Clip details: %s. Error: %s", clip, e.getMessage());
            throw new WebApplicationException(e);
        }
        client.basicPublish(
                        "",
                        queueName,
                        Buffer.buffer(rabbitBody)
        ).onSuccess(v -> LOG.debug("Published clip id " + clip.getId() + " to queue " + queueName))
         .onFailure(err -> LOG.error("Error publishing clip " + clip.getId() + " to queue " + queueName, err));
    }
}

record RabbitClipRequest(
        @JsonProperty("start_time") long startTime,
        @JsonProperty("end_time") long endTime,
        @JsonProperty("is_hq") boolean isHQ,
        @JsonProperty("clip_id") int clipId,
        @JsonProperty("cameras") List<Integer> cameras
) {
    public RabbitClipRequest(ClipsRecord c) {
       this(c.getStartTime().toEpochSecond(), c.getEndTime().toEpochSecond(), c.getIsHq(), c.getId(), List.of(1));
    }
}