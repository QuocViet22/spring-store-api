package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.*;
import com.spring.store.api.repository.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private LineItemRepository lineItemRepository;

    @Autowired
    private LineItemOrderRepository lineItemOrderRepository;

    //    create order for user by user_id
    @PostMapping("/orders/{userId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Order> createOrderByUserId(@PathVariable(value = "userId") Long userId, @RequestBody Order orderRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
        Order order = new Order();
        order.setUser(user);

        order.setCreatedBy(orderRequest.getCreatedBy());
        order.setCreatedDate(orderRequest.getCreatedDate());
        order.setModifiedBy(orderRequest.getModifiedBy());
        order.setModifiedDate(orderRequest.getModifiedDate());
        order.setStatus(orderRequest.getStatus());

        order.setVat(orderRequest.getVat());
        order.setSaleOff(orderRequest.getSaleOff());
        order.setTotalPrice(orderRequest.getTotalPrice());

        order.setAddress(orderRequest.getAddress());
        order.setName(orderRequest.getName());
        order.setPhoneNumber(orderRequest.getPhoneNumber());
        orderRepository.save(order);

        WishList wishList = wishListRepository.findByUserId(userId);
        List<LineItem> lineItems = lineItemRepository.findByWishListId(wishList.getId());
        for (int i = 0; i < lineItems.size(); i++) {
            LineItemOrder lineItemOrder = new LineItemOrder();
            lineItemOrder.setAmount(lineItems.get(i).getAmount());
            lineItemOrder.setProduct(lineItems.get(i).getProduct());
            lineItemOrder.setOrder(order);
            lineItemOrder.setStatus(lineItems.get(i).getStatus());
            lineItemOrder.setTotal(lineItems.get(i).getTotal());
            lineItemOrderRepository.save(lineItemOrder);
        }

//        orderRepository.save(order);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    //    retrieve list order of user by user_id
    @GetMapping("/orders/{userId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Order>> getListOrderByUserId(@PathVariable(value = "userId") Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Not found User with id = " + userId);
        }

        List<Order> orders = orderRepository.findByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    //    retrieve order by order_id
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Order> getOrderByOrderId(@PathVariable(value = "orderId") Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Order with id = " + orderId));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    //    retrieve list of all order
    @GetMapping("/orders")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getListOrder() {
        List<Order> orders = orderRepository.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    //    update order for manager by order_id to confirm
    @PutMapping("/orders/{orderId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateOrderByUserId(@PathVariable(value = "orderId") Long orderId, @RequestBody Order orderRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Order with id = " + orderId));
        order.setStatus(orderRequest.getStatus());
        orderRepository.save(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

//    //    cancel by id
//    @DeleteMapping("/lineItem/{id}")
//    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
//    public ResponseEntity<?> deleteLineItem(@PathVariable("id") long id) {
//        lineItemRepository.deleteById(id);
//        return ResponseEntity.ok().body(new MessageResponse("Line item has been deleted successfully!"));
//    }

}