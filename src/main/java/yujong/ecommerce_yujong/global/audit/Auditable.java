package yujong.ecommerce_yujong.global.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {
    /* 생성 시간 */
    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdAt;

    /* 수정 시간 */
    @LastModifiedDate
    private LocalDate modifiedAt;
}
