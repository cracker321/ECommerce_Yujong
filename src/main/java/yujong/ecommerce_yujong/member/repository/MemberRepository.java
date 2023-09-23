package yujong.ecommerce_yujong.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yujong.ecommerce_yujong.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /* 존재하는 이메일인지 확인해야 하기 때문 */
    Optional<Member> findByEmail(String email);

}
