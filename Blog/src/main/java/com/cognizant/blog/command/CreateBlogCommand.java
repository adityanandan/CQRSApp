package com.cognizant.blog.command;

import java.sql.Timestamp;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBlogCommand {
	
	@TargetAggregateIdentifier
	private String blogId;
	private String blogname;
	private String userid;
	private String category;
	private String authorname;
	private String article;
	private Timestamp timestamp;
}
