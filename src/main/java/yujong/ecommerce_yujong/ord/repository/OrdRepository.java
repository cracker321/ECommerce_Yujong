package yujong.ecommerce_yujong.ord.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yujong.ecommerce_yujong.ord.entity.Ord;

import java.util.List;

public interface OrdRepository  extends JpaRepository<Ord,Long> {
    Page<Ord> findByCustomer_CustomerId(Long customerId, Pageable pageable);
}
