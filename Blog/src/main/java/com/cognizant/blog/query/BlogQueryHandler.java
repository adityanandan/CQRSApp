package com.cognizant.blog.query;

import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognizant.blog.entity.MongoBlogs;
import com.cognizant.blog.repository.MongoBlogRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BlogQueryHandler {
	@Autowired MongoBlogRepository blogRepository;

	@QueryHandler
	public List<MongoBlogs> handle(GetAllBlogsQuery getAllBlogsQuery){			
			return blogRepository.findAll();
		
	}
	@QueryHandler
	public List<MongoBlogs> handle1(GetMyBlogsQuery getAllQuery){
		
			return blogRepository.findAllByUserid(getAllQuery.getUserid());
	}
	@QueryHandler
	public String handle2(DeleteBlogQuery deleteBlogQuery){
		log.info("DELETE");
		blogRepository.deleteAllByBlogname(deleteBlogQuery.getBlog().getBlogname());
			return "success";
		
	}
	@QueryHandler
	public List<MongoBlogs> handle3(FindAllByCategoryQuery findAllByCategoryQuery){
		
			return blogRepository.findAllByCategory(findAllByCategoryQuery.getCategory());
			
		
	}
	@QueryHandler
	public List<MongoBlogs> handle4(FindAllByCategoryAndRangeQuery findAllByCategoryAndRangeQuery){
		
		return blogRepository.findAllByCategory(findAllByCategoryAndRangeQuery.getCategory(),
					findAllByCategoryAndRangeQuery.getFromdate(),findAllByCategoryAndRangeQuery.getTodate());
			
		
	}
}
