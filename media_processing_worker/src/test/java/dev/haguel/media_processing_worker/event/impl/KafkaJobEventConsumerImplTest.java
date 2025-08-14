package dev.haguel.media_processing_worker.event.impl;

import dev.haguel.media_processing_worker.event.JobEventConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
class KafkaJobEventConsumerImplTest {
    private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("apache/kafka-native:3.9.1");
    @Container
    private static final KafkaContainer kafkaContainer = new KafkaContainer(
            KAFKA_IMAGE.asCompatibleSubstituteFor("apache/kafka"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("kafka.topics.job-submitted", () -> "jobs.submitted");
        registry.add("spring.kafka.consumer.group-id", () -> "chromaflow");
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
    }

    @Value("${kafka.topics.job-submitted}")
    private String jobSubmittedTopic;

    @MockitoSpyBean
    private KafkaJobEventConsumerImpl jobEventConsumer;

    private KafkaProducer<String, String> kafkaProducer;

    @BeforeEach
    void setUp() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        kafkaProducer = new KafkaProducer<>(props);
    }

    @AfterEach
    void tearDown() {
        kafkaProducer.close();
    }

    @Test
    void whenJovEventProduced_thenShouldBeConsumed() {
        // Arrange
        String expectedJobId = "job-id-123";

        // Act
        kafkaProducer.send(new ProducerRecord<>(jobSubmittedTopic, expectedJobId));

        // Assert
        verify(jobEventConsumer, timeout(5000)).consumeJobId(expectedJobId);
    }
}