package com.example.backend.configurations;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.example.backend.shared.Constant;
import io.awspring.cloud.messaging.config.SimpleMessageListenerContainerFactory;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
//@EnableSqs
public class AmazonConfiguration {
    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxConnections(30);
        clientConfiguration.setCacheResponseMetadata(false);
        clientConfiguration.setResponseMetadataCacheSize(0);
        clientConfiguration.setMaxErrorRetry(3);
        return getBuilder(clientConfiguration).build();
    }

    private AmazonSQSAsyncClientBuilder getBuilder(ClientConfiguration configuration) {
        return AmazonSQSAsyncClientBuilder
                .standard()
                .withClientConfiguration(configuration)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(Constant.AWSConfig.AWS_SQS_API, Constant.AWSConfig.AWS_SQS_REGION))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials()));
    }

    @Bean
    public AWSCredentials awsCredentials() {
        return new BasicAWSCredentials(Constant.AWSConfig.AWS_CREDENTIAL_ACCESS_KEY, Constant.AWSConfig.AWS_CREDENTIAL_SECRET_KEY);
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @Bean
    public AmazonS3 s3Client() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials()))
                .withRegion(Constant.AWSConfig.AWS_S3_REGION).build();
    }

    @Bean
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSQSAsync) {
        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
        factory.setAmazonSqs(amazonSQSAsync);
        factory.setWaitTimeOut(10);
        factory.setTaskExecutor(createListenerThreadPool());
        factory.setMaxNumberOfMessages(5);
        return factory;
    }

    private ThreadPoolTaskExecutor createListenerThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(0);
        executor.setKeepAliveSeconds(60);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();
        executor.setThreadNamePrefix("webhook-listener-");
        return executor;
    }
}
