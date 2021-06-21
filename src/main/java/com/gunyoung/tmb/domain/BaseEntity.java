package com.gunyoung.tmb.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

/**
 * 모든 Entity 객체들에 공통적으로 들어가는 필드 모아놓은 클래스
 * MappedSuperclass 어노테이션으로 인해 DB 테이블 생성 안됨
 * @author kimgun-yeong
 *
 */
@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
	
	@Version
	@Column
	private int version;
	
	@CreatedDate
	@Column(updatable= false)
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	private LocalDateTime modifiedAt;
}
