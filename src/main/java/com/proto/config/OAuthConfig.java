package com.proto.config;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Configures the OAuth security for fetching events from AppDirect
 * <p>
 * see http://codehustler.org/blog/spring-security-tutorial-2-legged-oauth-1-0/
 * https://jira.spring.io/browse/SECOAUTH-312
 */
@Configuration
public class OAuthConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(marshallingMessageConverter());
        restTemplate.setMessageConverters(converters);

        return restTemplate;
    }

    @Bean
    public OAuthConsumer oAuthConsumer(@Value("${oauth.key}") String oauthKey, @Value("${oauth.secret}") String oauthSecret) {
        OAuthConsumer consumer = new DefaultOAuthConsumer(oauthKey, oauthSecret);
        return consumer;
    }

    @Bean
    public MarshallingHttpMessageConverter marshallingMessageConverter() {
        return new MarshallingHttpMessageConverter(
                jaxb2Marshaller(),
                jaxb2Marshaller()
        );
    }

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.proto.model");

        return marshaller;
    }
}
