package yujong.ecommerce_yujong.ord.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yujong.ecommerce_yujong.ord.entity.Ord;

public interface OrdRepository  extends JpaRepository<Ord,Long> {
}
