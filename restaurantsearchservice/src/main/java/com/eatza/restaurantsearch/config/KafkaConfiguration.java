package com.eatza.restaurantsearch.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer2;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.eatza.restaurantsearch.dto.ReviewDTO;

@Configuration
@EnableKafka
public class KafkaConfiguration {

	@Bean
	public ConsumerFactory<String, ReviewDTO> consumerFactory() {
		Map<String, Object> configs = new HashMap<>();

		JsonDeserializer<ReviewDTO> deserializer = new JsonDeserializer<>(ReviewDTO.class);
		deserializer.setRemoveTypeHeaders(false);
		deserializer.addTrustedPackages("*");
		deserializer.setUseTypeMapperForKey(true);

		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, "review_notification");
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
		configs.put(ErrorHandlingDeserializer2.KEY_DESERIALIZER_CLASS, deserializer);
		configs.put(ErrorHandlingDeserializer2.VALUE_DESERIALIZER_CLASS, deserializer.getClass());
		configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(),
				new ErrorHandlingDeserializer2<ReviewDTO>(deserializer));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, ReviewDTO> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, ReviewDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}
}
