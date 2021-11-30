package com.nutrymaco.gateway.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.quarkus.jackson.ObjectMapperCustomizer;

/**
 * ObjectMapperConfiguration
 */
@ApplicationScoped
public class ObjectMapperConfiguration {

	@Singleton
	@Produces
	public ObjectMapperCustomizer objectMapperCustomizer() {
		return new ObjectMapperCustomizer(){

			@Override
			public void customize(ObjectMapper objectMapper) {
				
				objectMapper.registerModule(new JavaTimeModule());
				objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
				
			}
			
		};
	}
	
}
