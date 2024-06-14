package com.cognizant.blog.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognizant.blog.entity.MongoBlogs;

@Repository
public interface MongoBlogRepository extends MongoRepository<MongoBlogs, Long> {
	
	  Optional<MongoBlogs> findByBlogname(String blogName);
	  
	  List<MongoBlogs> findAllByCategory(String category);
	  
	  @Query("{'timestamp':{$gt:?1,$lt:?2}}")
	  List<MongoBlogs>
	  findAllByCategory(
	  String category, Date fromdate, Date todate);
	  
	  Optional<MongoBlogs> findByBlognameAndUserid(String blogName, String userid);
	  
	  List<MongoBlogs> findAllByUserid(String userid);
	  
	  @Query(delete = true)
	  void deleteAllByBlogname(String blogName);
	 
}
