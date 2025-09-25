package es.ull.project.configuration;

import java.util.Arrays;

import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import es.ull.project.adapter.mongodb.reader.WorkFlowReadingConverter;
import es.ull.project.adapter.mongodb.reader.ExecutionReadingConverter;
import es.ull.project.adapter.mongodb.reader.OperationReadingConverter;
import es.ull.project.adapter.mongodb.repository.WorkFlowMongoRepository;
import es.ull.project.adapter.mongodb.repository.ExecutionMongoRepository;
import es.ull.project.adapter.mongodb.repository.OperationMongoRepository;
import es.ull.project.adapter.mongodb.writter.WorkFlowWritingConverter;
import es.ull.project.adapter.mongodb.writter.ExecutionWritingConverter;
import es.ull.project.adapter.mongodb.writter.OperationWritingConverter;
import es.ull.project.application.repository.WorkFlowRepository;
import es.ull.project.application.repository.ExecutionRepository;
import es.ull.project.application.repository.OperationRepository;

@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String uri;
    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(this.uri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    @Override
    public MappingMongoConverter mappingMongoConverter(
            MongoDatabaseFactory databaseFactory,
            MongoCustomConversions customConversions,
            MongoMappingContext mappingContext) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        converter.setCustomConversions(customConversions());
        converter.afterPropertiesSet();
        return converter;
    }

    @Bean
    @Override
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
                Arrays.asList(
                        new WorkFlowWritingConverter(this),
                        new OperationWritingConverter(this),
                        new ExecutionWritingConverter(this),
                        new WorkFlowReadingConverter(this),
                        new OperationReadingConverter(this),
                        new ExecutionReadingConverter(this)));
    }

    @Bean
    public WorkFlowRepository workFlowRepository() {
        return new WorkFlowMongoRepository();
    }

    @Bean
    public OperationRepository operationRepository() {
        return new OperationMongoRepository();
    }

    @Bean
    public ExecutionRepository executionRepository() {
        return new ExecutionMongoRepository();
    }
}