package de.telran.gartenshop.controller;

import de.telran.gartenshop.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orderItems")
public class OrderItemController {
    private final OrderItemService orderItemService;
}
