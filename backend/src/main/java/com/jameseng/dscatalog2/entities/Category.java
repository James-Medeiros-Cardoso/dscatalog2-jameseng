package com.jameseng.dscatalog2.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity // javax.persistence - será uma entidade no Banco de Dados
@Table(name = "tb_category") // tb_category = será o nome da tabela no Banco de dad
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id // javax.persistence
	@GeneratedValue(strategy = GenerationType.IDENTITY) // id será autoincrementavel no banco de dados
	private Long id;
	private String name;

	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") // entende que será o UTC (GMT zero)
	private Instant createdAt; // Instante = armazena dia e hora

	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") // entende que será o UTC (GMT zero)
	private Instant updatedAt; // Instante = armazena dia e hora

	@ManyToMany(mappedBy = "categories") // já mapeado na entidade Product
	private Set<Product> products = new HashSet<>();

	public Category() {
	}

	public Category(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	@PrePersist // sempre que chamar o save do Jpa, vai executar o prePersist
	public void prePersist() {
		createdAt = Instant.now();
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	@PreUpdate // sempre que der um save para atualizar Jpa, vai executar o preUpdate
	public void preUpdate() {
		updatedAt = Instant.now();
	}

	public Set<Product> getProducts() {
		return products;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}