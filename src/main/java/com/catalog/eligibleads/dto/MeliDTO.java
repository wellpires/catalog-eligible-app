package com.catalog.eligibleads.dto;

public class MeliDTO {

	private String id;
	private String accessToken;
	private String nomeConta;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setNomeConta(String nomeConta) {
		this.nomeConta = nomeConta;
	}

	public String getNomeConta() {
		return nomeConta;
	}

}
