package yujong.ecommerce_yujong.global.audit;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@Data
@MappedSuperclass //이거 확인하기!
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
