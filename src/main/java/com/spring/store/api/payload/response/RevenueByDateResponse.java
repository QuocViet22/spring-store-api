package com.spring.store.api.payload.response;

import com.spring.store.api.projection.IRevenueByDateResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevenueByDateResponse {
    private int revenue;
    private List<IRevenueByDateResponse> iRevenueByDateResponses;
}
