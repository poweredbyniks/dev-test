//package com.musala.drones.config;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.codec.Decoder;
//import org.springframework.core.codec.Encoder;
//import org.springframework.http.codec.json.Jackson2JsonDecoder;
//import org.springframework.http.codec.json.Jackson2JsonEncoder;
//
//@Configuration
//public class JacksonConfig {
//
//    @Bean
//    @Primary
//    public ObjectMapper commonObjectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
////        objectMapper.configure(DeserializationFeature.B)
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        return objectMapper;
//    }
//
//    @Bean("commonExchangeDecoder")
//    public Decoder<?> commonExchangeDecoder() {
//        return new Jackson2JsonDecoder(commonObjectMapper());
//    }
//
//    @Bean("commonExchangeEncoder")
//    public Encoder<?> commonExchangeEncoder() {
//        return new Jackson2JsonEncoder(commonObjectMapper());
//    }
//}
