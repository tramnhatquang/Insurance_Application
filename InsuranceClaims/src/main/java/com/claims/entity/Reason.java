package com.claims.entity;

import javax.persistence.*;

@Entity
@Table(name="reasons")
public class Reason {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long reasonId;
	private String description;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
