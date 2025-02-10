package de.telran.gartenshop.service;

import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.OrderItemRepository;
import de.telran.gartenshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final Mappers mappers;
}
