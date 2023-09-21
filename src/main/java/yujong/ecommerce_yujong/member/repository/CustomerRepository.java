package yujong.ecommerce_yujong.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yujong.ecommerce_yujong.member.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {


}
