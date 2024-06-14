package com.cognizant.blog.query;

import com.cognizant.blog.entity.Blogs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteBlogQuery {
	private Blogs blog;
}
