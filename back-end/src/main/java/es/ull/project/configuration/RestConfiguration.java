package es.ull.project.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.Module;

import es.ull.project.adapter.rest.request.WorkFlowImportRequestBody;
import es.ull.project.adapter.rest.request.WorkFlowPostRequestBody;
import es.ull.project.adapter.rest.request.WorkFlowPutRequestBody;
import es.ull.project.adapter.rest.deserialization.OperationPostRequestBodyDeserializer;
import es.ull.project.adapter.rest.deserialization.OperationPutRequestBodyDeserializer;
import es.ull.project.adapter.rest.deserialization.WorkFlowExecuteRequestBodyDeserializer;
import es.ull.project.adapter.rest.deserialization.WorkFlowImportRequestBodyDeserializer;
import es.ull.project.adapter.rest.deserialization.WorkFlowPostRequestBodyDeserializer;
import es.ull.project.adapter.rest.deserialization.WorkFlowPutRequestBodyDeserializer;
import es.ull.project.adapter.rest.request.OperationPostRequestBody;
import es.ull.project.adapter.rest.request.OperationPutRequestBody;
import es.ull.project.adapter.rest.request.WorkFlowExecuteRequestBody;
import es.ull.project.adapter.rest.response.WorkFlowResponseBody;
import es.ull.project.adapter.rest.response.OperationResponseBody;
import es.ull.project.adapter.rest.response.WorkFlowExecuteResponseBody;
import es.ull.project.adapter.rest.serialization.OperationResponseBodySerializer;
import es.ull.project.adapter.rest.serialization.WorkFlowExecuteResponseBodySerializer;
import es.ull.project.adapter.rest.serialization.WorkFlowResponseBodySerializer;
import es.ull.utils.rest.exception.UllExceptionHandler;


@Configuration
public class RestConfiguration {
    @Bean
    public Module dynamoDemoEntityDeserializer() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(
                WorkFlowPostRequestBody.class,
                new WorkFlowPostRequestBodyDeserializer());
        module.addDeserializer(
                WorkFlowImportRequestBody.class,
                new WorkFlowImportRequestBodyDeserializer());
        module.addDeserializer(
                WorkFlowPutRequestBody.class,
                new WorkFlowPutRequestBodyDeserializer());
        module.addDeserializer(
                OperationPostRequestBody.class,
                new OperationPostRequestBodyDeserializer());
        module.addDeserializer(
                OperationPutRequestBody.class,
                new OperationPutRequestBodyDeserializer());
        module.addDeserializer(
                WorkFlowExecuteRequestBody.class,
                new WorkFlowExecuteRequestBodyDeserializer());

        module.addSerializer(
                WorkFlowResponseBody.class,
                new WorkFlowResponseBodySerializer());
        module.addSerializer(
                OperationResponseBody.class,
                new OperationResponseBodySerializer());
        module.addSerializer(
                WorkFlowExecuteResponseBody.class,
                new WorkFlowExecuteResponseBodySerializer());

        return module;
    }

    @Bean
    public ResponseEntityExceptionHandler responseEntityExceptionHandler() {
        return new UllExceptionHandler();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost") // frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false);
            }
        };
    }

}
