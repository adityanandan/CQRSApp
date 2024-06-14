package com.cognizant.blog.command;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogCreatedEvent {
	private String blogId;
	private String blogname;
	private String userid;
	private String category;
	private String authorname;
	private String article;
	private Timestamp timestamp;
}
