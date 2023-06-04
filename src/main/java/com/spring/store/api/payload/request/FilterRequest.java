package com.spring.store.api.payload.request;

import com.spring.store.api.models.QueryOperator;
import lombok.Data;

import java.util.List;

@Data
public class FilterRequest {
    private String field;
    private QueryOperator operator;
    private String value;
    private List<String> values;
}
