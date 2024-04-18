package com.xcoder.tasklist.config;

import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphqlConfig {

    @Bean
    public GraphQLScalarType localDateTimeScalar() {
        return GraphQLScalarType.newScalar()
            .name("LocalDateTime")
            .description("Local Date Time scalar")
            .coercing(new LocalDateTimeCoercing())
            .build();
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(GraphQLScalarType localDateTimeScalar) {
        return wiringBuilder -> wiringBuilder
            .scalar(localDateTimeScalar)
            .build();
    }
}
