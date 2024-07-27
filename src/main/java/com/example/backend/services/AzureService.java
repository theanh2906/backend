package com.example.backend.services;

import com.microsoft.graph.models.User;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AzureService {
    @Autowired
    private GraphServiceClient client;

    public AzureService() {

    }

    public void getUserInfo() {
        User me = client.me().get();
        System.out.printf("Hello %s, your ID is %s%n", me.getDisplayName(), me.getId());
    }
}
