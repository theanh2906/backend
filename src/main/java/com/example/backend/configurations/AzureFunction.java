//package com.example.backend.configurations;
//
//import com.microsoft.azure.functions.ExecutionContext;
//import com.microsoft.azure.functions.HttpMethod;
//import com.microsoft.azure.functions.HttpRequestMessage;
//import com.microsoft.azure.functions.annotation.AuthorizationLevel;
//import com.microsoft.azure.functions.annotation.FunctionName;
//import com.microsoft.azure.functions.annotation.HttpTrigger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.function.context.FunctionCatalog;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//import java.util.function.Function;
//
//
//@Component
//public class AzureFunction {
//    @Autowired
//    private Function<String, String> uppercase;
//    @Autowired private FunctionCatalog functionCatalog;
//
//    @FunctionName("spring")
//    public String plainBean(
//            @HttpTrigger(name = "req", authLevel = AuthorizationLevel.ANONYMOUS, methods = HttpMethod.GET) HttpRequestMessage<Optional<String>> request,
//            ExecutionContext context) {
//        context.getLogger().info(request.getBody().get());
//        return this.uppercase.apply(request.getBody().get());
//    }
//
//    @FunctionName("scf")
//    public String springCloudFunction(
//            @HttpTrigger(name = "req", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
//            ExecutionContext context) {
//
//        // Use SCF composition. Composed functions are not just spring beans but SCF such.
//        Function composed = this.functionCatalog.lookup("reverse|uppercase");
//
//        return (String) composed.apply(request.getBody().get());
//    }
//}