package com.ledgerhub.model.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 2, nullable = false, unique = true)
	private String iso2;

	@Column(length = 3, nullable = false, unique = true)
	private String iso3;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(name = "numeric_code", length = 3)
	private String numericCode;

	@Column(name = "phone_code", length = 10)
	private String phoneCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "currency_id", foreignKey = @ForeignKey(name = "fk_countries_currency_id"))
	private SystemLookup currency;
}
