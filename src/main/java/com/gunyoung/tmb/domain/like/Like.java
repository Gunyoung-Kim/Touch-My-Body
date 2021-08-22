package com.gunyoung.tmb.domain.like;

import javax.persistence.MappedSuperclass;

import com.gunyoung.tmb.domain.BaseEntity;

/**
 * 좋아요 관련 엔티티들의 부모 클래스 <br>
 * 추후 좋아요 엔티티들에 공통적으로 추가해야되는 사항에 대하여 처리하기 위해 추가
 * @author kimgun-yeong
 *
 */
@MappedSuperclass
public abstract class Like extends BaseEntity{

}
