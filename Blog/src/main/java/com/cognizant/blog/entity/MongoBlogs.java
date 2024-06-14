package com.cognizant.blog.entity;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Document(collection = "blog")
@Getter
@Setter
public class MongoBlogs {//not a spring bean
	
	@Id
	private String blogname;
	private String userid;
	private String category;
	
	private String authorname;
	
	private String article;
	private Date timestamp;
	
}