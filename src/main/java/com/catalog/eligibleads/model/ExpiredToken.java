package com.catalog.eligibleads.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ExpiredToken implements Serializable {

	private static final long serialVersionUID = -4294763361170932681L;

	@Id
	private String id;
	private String apelido;

	@Column(name = "dataaviso")
	private Date dataAviso;

	@Column(name = "dataexpiracao")
	private Date dataExpiracao;

	@Column(name = "tokenativo")
	private boolean tokenAtivo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public Date getDataAviso() {
		return dataAviso;
	}

	public void setDataAviso(Date dataAviso) {
		this.dataAviso = dataAviso;
	}

	public Date getDataExpiracao() {
		return dataExpiracao;
	}

	public void setDataExpiracao(Date dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}

	public boolean isTokenAtivo() {
		return tokenAtivo;
	}

	public void setTokenAtivo(boolean tokenAtivo) {
		this.tokenAtivo = tokenAtivo;
	}

}
