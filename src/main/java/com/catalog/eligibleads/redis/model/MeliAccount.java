package com.catalog.eligibleads.redis.model;

import java.io.Serializable;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "meliAccount", timeToLive = 86400)
public class MeliAccount implements Serializable {

	private static final long serialVersionUID = -6418544145690642792L;

	@Id
	@Indexed
	private String id;

	private String nameAccount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNameAccount() {
		return nameAccount;
	}

	public void setNameAccount(String nameAccount) {
		this.nameAccount = nameAccount;
	}

}
