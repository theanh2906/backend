package com.example.backend.configurations;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.spring.cloud.autoconfigure.implementation.context.AzureTokenCredentialAutoConfiguration;
import com.example.backend.shared.Constant;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.QueueOutput;
import com.microsoft.graph.core.authentication.AzureIdentityAuthenticationProvider;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class AzureConfiguration {
    @Bean
    public AzureIdentityAuthenticationProvider authenticationProvider() {
        return new AzureIdentityAuthenticationProvider(tokenCredential(), null, Constant.Azure.SCOPES);
    }

    @Bean
    public GraphServiceClient graphServiceClient() {
        return new GraphServiceClient(authenticationProvider());
    }

    @Bean
    public TokenCredential tokenCredential() {
        return new ClientSecretCredentialBuilder()
                .clientId(Constant.Azure.CLIENT_ID)
                .clientSecret(Constant.Azure.CLIENT_SECRET)
                .tenantId(Constant.Azure.TENANT_ID)
                .build();
    }

    @FunctionName("benna")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            @QueueOutput(name = "msg", queueName = "outqueue",
                    connection = "AzureWebJobsStorage") OutputBinding<String> msg,
            final ExecutionContext context){
        return null;
    }
}
