package com.spring.store.api.controllers;

import com.spring.store.api.payload.request.RevenueByDateRequest;
import com.spring.store.api.payload.request.RevenueByMonthRequest;
import com.spring.store.api.payload.response.RevenueByDateResponse;
import com.spring.store.api.payload.response.RevenueByMonthResponse;
import com.spring.store.api.projection.IRevenueByDateResponse;
import com.spring.store.api.projection.IRevenueByMonthResponse;
import com.spring.store.api.repository.RevenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class RevenueController {
    @Autowired
    RevenueRepository revenueRepository;

    //    retrieve revenue by date
    @GetMapping("/revenue/date")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public RevenueByDateResponse getRevenueByDate(@RequestBody RevenueByDateRequest revenueByDateRequest) {
        List<IRevenueByDateResponse> iRevenueByDateResponses = revenueRepository.viewRevenueByDate(revenueByDateRequest.getModifiedDate());
        RevenueByDateResponse revenueByDateResponse = new RevenueByDateResponse();
        revenueByDateResponse.setIRevenueByDateResponses(iRevenueByDateResponses);
        int revenue = 0;
        for (int i = 0; i < iRevenueByDateResponses.size(); i++) {
            revenue = revenue + iRevenueByDateResponses.get(i).getTotalPrice();
        }
        revenueByDateResponse.setRevenue(revenue);
        return revenueByDateResponse;
    }

    @GetMapping("/revenue/month")
//    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public RevenueByMonthResponse getRevenueByMonth(@RequestBody RevenueByMonthRequest revenueByMonthRequest) {
        List<IRevenueByMonthResponse> iRevenueByMonthResponses = revenueRepository.viewRevenueByMonth(revenueByMonthRequest.getYear());
        RevenueByMonthResponse revenueByMonthResponse = new RevenueByMonthResponse();
        revenueByMonthResponse.setIRevenueByMonthResponses(iRevenueByMonthResponses);
        int revenueByMonth = 0;
        for (int i = 0; i < iRevenueByMonthResponses.size(); i++) {
            revenueByMonth = revenueByMonth + iRevenueByMonthResponses.get(i).getTotalPrice();
        }
        revenueByMonthResponse.setRevenueByMonth(revenueByMonth);
        return revenueByMonthResponse;
    }
}
