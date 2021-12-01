package org.zerock.guestbook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// @MappedSuperclass : 해당 어노테이션 적용된 클래스는 테이블로 생성되지 않는다.
// 이 클래스를 상속한 엔티티의 클래스로 db 테이블 생성됨
@MappedSuperclass

// AuditingEntityListener : jpa내부(즉, Persistence Context)에서 엔티티객체가 생성/변경되는것을 감지
@EntityListeners(value={AuditingEntityListener.class})

@Getter
abstract class BaseEntity {

    // @CreatedDate : 엔티티 생성시간 처리
    @CreatedDate
    @Column(name="regdate", updatable = false) // 엔티티객체 db 반영시 regdate 컬럼값은 변경되지 않음
    private LocalDateTime regDate;

    // @LastModifiedDate : 최종 수정시간 자동 처리
    @LastModifiedDate
    @Column(name="moddate")
    private LocalDateTime modDate;
}
