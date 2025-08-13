package dev.haguel.job_ingestion_service.event.impl;

import dev.haguel.job_ingestion_service.event.JobEventProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class KafkaJobEventProducerImplTest {
    private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("apache/kafka-native:3.9.1");
    @Container
    private static final KafkaContainer kafkaContainer = new KafkaContainer(
            KAFKA_IMAGE.asCompatibleSubstituteFor("apache/kafka"));


    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("kafka.topics.job-submitted", () -> "jobs.submitted");
    }

    @Value("${kafka.topics.job-submitted}")
    private String jobSubmittedTopic;

    @Autowired
    private JobEventProducer jobEventProducer;

    private KafkaConsumer<String, String> kafkaConsumer;

    @BeforeEach
    void setUp() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        kafkaConsumer = new KafkaConsumer<>(props);
    }

    @AfterEach
    void tearDown() {
        kafkaConsumer.close();
    }

    @Test
    void whenSendJobSubmittedEvent_thenMessageShouldBeConsumed() {
        // Arrange
        String expectedJobId = "job-id-123";
        kafkaConsumer.subscribe(Collections.singletonList(jobSubmittedTopic));

        // Act
        jobEventProducer.sendJobSubmittedEvent(expectedJobId);

        // Assert
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(10));

        assertThat(records.count()).isEqualTo(1);
        ConsumerRecord<String, String> received = records.iterator().next();
        assertThat(received.value()).isEqualTo(expectedJobId);

        kafkaConsumer.close();
    }
}