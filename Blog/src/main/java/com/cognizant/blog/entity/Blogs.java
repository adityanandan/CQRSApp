package com.cognizant.blog.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;


@Entity
@Table(	name = "blog")
@Data
public class Blogs {
	
	@Id
	@NotNull(message = "Blog Name cannot be blank#######")
	@Size(min=20, message="Minimum 20 Characters")
	private String blogname;
	private String userid;
	@Size(min=20, message="Minimum 20 Characters")
	private String category;
	@NotNull(message = "Author Name cannot be blank#######")
	@Size(min=3, message="Minimum 3 Characters")
	private String authorname;
	@NotNull(message = "Article cannot be blank#######")
	@Size(min=10, message="Minimum 10 Characters")
	private String article;
	private Timestamp timestamp;
	
}