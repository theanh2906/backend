package com.example.backend.configurations;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.example.backend.shared.Constant;
import com.microsoft.graph.core.authentication.AzureIdentityAuthenticationProvider;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class AzureConfiguration {
    @Bean
    public GraphServiceClient graphServiceClient() {
        return new GraphServiceClient(authenticationProvider());
    }

    @Bean
    public AzureIdentityAuthenticationProvider authenticationProvider() {
        return new AzureIdentityAuthenticationProvider(tokenCredential(), null, Constant.Azure.SCOPES);
    }

    @Bean
    public TokenCredential tokenCredential() {
        return new ClientSecretCredentialBuilder()
                .clientId(Constant.Azure.CLIENT_ID)
                .clientSecret(Constant.Azure.CLIENT_SECRET)
                .tenantId(Constant.Azure.TENANT_ID)
                .build();
    }

    @Bean
    public Function<String, String> reverse() {
        return payload -> new StringBuilder(payload).reverse().toString();
    }

    @Bean
    public Function<String, String> uppercase() {
        return String::toUpperCase;
    }
}
