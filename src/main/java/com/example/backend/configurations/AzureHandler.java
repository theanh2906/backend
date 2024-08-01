package com.example.backend.configurations;

import com.example.backend.models.UserModel;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.graph.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AzureHandler {
    @Autowired
    private Hello hello;

    @FunctionName("useful-tools")
    public HttpResponseMessage execute(
            @HttpTrigger(name = "request", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<UserModel>> request, ExecutionContext context) {
        UserModel user = request.getBody()
                .filter(u -> u.getName() != null)
                .orElseGet(() -> new UserModel(request.getQueryParameters().getOrDefault("name", "world")));
        context.getLogger().info("Greeting user name: " + user.getName());
        return request.createResponseBuilder(HttpStatus.OK)
                .body(hello.apply(user))
                .header("Content-Type", "application/json")
                .build();
    }
}
