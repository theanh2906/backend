package com.example.backend.configurations;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.graph.models.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AzureHandler {
    @FunctionName("useful-tools")
    public HttpResponseMessage execute(
            @HttpTrigger(name = "request", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<User>> request, ExecutionContext context) {
        User user = request.getBody()
                .filter(u -> u.getDisplayName() != null)
                .orElseGet(User::new);
        context.getLogger().info("Greeting user name: " + user.getDisplayName());
        return request.createResponseBuilder(HttpStatus.OK)
                .body("Hello")
                .header("Content-Type", "application/json")
                .build();
    }
}
