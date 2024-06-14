package com.cognizant.blog.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognizant.blog.entity.Blogs;

@Repository
public interface BlogRepository extends JpaRepository<Blogs, Long> {
	Optional<Blogs> findByBlogname(String blogName);

	List<Blogs> findAllByCategory(String category);

	List<Blogs> findAllByCategoryAndTimestampGreaterThanEqualAndTimestampLessThanEqual(String category, Date fromdate,
			Date todate);

	Optional<Blogs> findByBlognameAndUserid(String blogName, String userid);

	List<Blogs> findAllByUserid(String userid);
}
