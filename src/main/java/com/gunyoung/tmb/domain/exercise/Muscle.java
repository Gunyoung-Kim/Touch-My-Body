package com.gunyoung.tmb.domain.exercise;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.gunyoung.tmb.domain.BaseEntity;
import com.gunyoung.tmb.enums.TargetType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 근육의 종류를 나타내는 Entity
 * @author kimgun-yeong
 *
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Muscle extends BaseEntity{
	
	/**
	 * id 값
	 */
	@Id
	@Column(name = "muscle_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 근육의 이름
	 */
	@NotNull
	@NotEmpty
	private String name;
	
	/**
	 * 근육의 대분류에서의 종류
	 */
	@Enumerated(EnumType.STRING)
	@NotNull
	private TargetType category;
}
