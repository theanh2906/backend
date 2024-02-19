package com.example.backend.models;

import java.util.List;
import java.util.Map;

public abstract class TableModel {
    public abstract List<String> getHeader();

    public abstract Map<String, Object> getMap();
}
