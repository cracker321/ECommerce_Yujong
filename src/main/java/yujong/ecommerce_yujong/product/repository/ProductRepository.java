package yujong.ecommerce_yujong.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yujong.ecommerce_yujong.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
