package com.catalog.eligibleads.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Meli implements Serializable {

	private static final long serialVersionUID = 6420510638751496520L;

	@Id
	private String id;

	@Column(name = "clientapelido")
	private String clienteApelido;

	@Column(name = "accesstoken")
	private String accessToken;
	private Boolean active;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClienteApelido() {
		return clienteApelido;
	}

	public void setClienteApelido(String clienteApelido) {
		this.clienteApelido = clienteApelido;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
