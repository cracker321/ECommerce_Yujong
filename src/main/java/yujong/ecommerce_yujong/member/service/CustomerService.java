package yujong.ecommerce_yujong.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.global.exception.BusinessLogicException;
import yujong.ecommerce_yujong.global.exception.ExceptionCode;
import yujong.ecommerce_yujong.member.entity.Customer;
import yujong.ecommerce_yujong.member.repository.CustomerRepository;

import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerService {


    private final CustomerRepository customerRepository;
    private final MemberService memberService;



    public Customer findVerifiedCustomer(long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        Customer findCustomer = optionalCustomer
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return findCustomer;
    }

    @Transactional(readOnly = true)
    public Customer findCustomer(long customerId) {
        Customer customer = findVerifiedCustomer(customerId);
        return customer;
    }



    public Customer updateCustomer(long customerId, Customer customer) {
        return customerRepository.save(findCustomer(customerId));
    }
}

