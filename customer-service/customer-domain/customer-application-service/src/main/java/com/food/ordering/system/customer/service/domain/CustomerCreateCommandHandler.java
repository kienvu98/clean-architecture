package com.food.ordering.system.customer.service.domain;

import com.food.ordering.system.customer.service.domain.mapper.CustomerDataMapper;
import com.food.ordering.system.customer.service.domain.ports.output.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerCreateCommandHandler {
    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;
    private final CustomerDataMapper customerDataMapper;

    public CustomerCreateCommandHandler(CustomerDomainService customerDomainService,
                                        CustomerRepository customerRepository,
                                        CustomerDataMapper customerDataMapper) {
        this.customerDomainService = customerDomainService;
        this.customerRepository = customerRepository;
        this.customerDataMapper = customerDataMapper;
    }

}
