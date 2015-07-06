package com.sample.spring.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sample.spring.security.UserAuthentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(name = "UK_LOGIN", columnNames = "user_login"))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "empresasprodutos")
@ToString(exclude = "empresasprodutos")

public class User implements UserAuthentication {

	@GeneratedValue
	@Id
	@Column(name = "user_id")
	private Long id;

	@Column(name = "user_login", nullable = false, updatable = false)
	private String login;

	@Column(name = "user_name", nullable = false)
	private String name;

	@Column(name = "user_password", nullable = false)
	private String password;

	@Column(name = "user_admin")
	private Boolean admin;
	
	@OneToMany(mappedBy = "user")
	private Set<EmpresaProduto> empresasprodutos = new HashSet<EmpresaProduto>();

}
