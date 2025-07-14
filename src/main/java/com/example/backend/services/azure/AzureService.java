package com.example.backend.services.azure;

import com.microsoft.graph.serviceclient.GraphServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AzureService {
    public AzureService() {

    }

    public void getUserInfo() {
        client.me().authentication().get();
    }
    @Autowired
    private GraphServiceClient client;
}
