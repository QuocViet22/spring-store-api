package com.spring.store.api.specifications;

import com.spring.store.api.models.Product;
import com.spring.store.api.payload.request.FilterRequest;
import com.spring.store.api.repository.ProductInforRepository;
import com.spring.store.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
public class ProductSpecification {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductInforRepository productInforRepository;

    public List<Product> getQueryResult(List<FilterRequest> filters) {
        if (filters.size() > 0) {
            return productRepository.findAll(getSpecificationFromFilters(filters));
        } else {
            return productRepository.findAll();
        }
    }

    private Specification<Product> getSpecificationFromFilters(List<FilterRequest> filter) {
        Specification<Product> specification =
                where(createSpecification(filter.remove(0)));
        for (FilterRequest input : filter) {
            specification = specification.and(createSpecification(input));
        }
        return specification;
    }

    private Specification<Product> createSpecification(FilterRequest input) {
        switch (input.getOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(input.getField()),
                                castToRequiredType(root.get(input.getField()).getJavaType(),
                                        input.getValue()));
            case NOT_EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(input.getField()),
                                castToRequiredType(root.get(input.getField()).getJavaType(),
                                        input.getValue()));
            case GREATER_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.gt(root.get(input.getField()),
                                (Number) castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValue()));
            case LESS_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lt(root.get(input.getField()),
                                (Number) castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValue()));
            case LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(input.getField()),
                                "%" + input.getValue() + "%");
            case IN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.in(root.get(input.getField()))
                                .value(castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValues()));
            default:
                throw new RuntimeException("Operation not supported yet");
        }
    }

    private Object castToRequiredType(Class fieldType, String value) {
        if (fieldType.isAssignableFrom(Double.class)) {
            return Double.valueOf(value);
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        } else if (Enum.class.isAssignableFrom(fieldType)) {
            return Enum.valueOf(fieldType, value);
        }
        return null;
    }

    private Object castToRequiredType(Class fieldType, List<String> value) {
        List<Object> lists = new ArrayList<>();
        for (String s : value) {
            lists.add(castToRequiredType(fieldType, s));
        }
        return lists;
    }
}
