package com.cognizant.blog.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.blog.common.AppConstants;
import com.cognizant.blog.query.FindAllByCategoryAndRangeQuery;
import com.cognizant.blog.query.FindAllByCategoryQuery;
import com.cognizant.blog.repository.BlogRepository;
import com.cognizant.blog.service.KafKaProducerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController 
@RequestMapping("/api/v1.0/blogsite")
public class BlogSearchController extends ErrorController {
	@Autowired
	BlogRepository blogRepository;
	@Autowired KafKaProducerService kafkaProducerService;
	@Autowired QueryGateway queryGateway;
	/**
	 * Search By Category
	 * 
	 * @param category
	 * @return
	 */
	@GetMapping("blogs/info/{category}")
	public ResponseEntity<Object> searchByCategory(@PathVariable String category) {
		log.debug("inside searchbyCategory");
		FindAllByCategoryQuery findAllByCategoryQuery = new FindAllByCategoryQuery();
		findAllByCategoryQuery.setCategory(category);
		CompletableFuture<List<Object>> allBlogs = queryGateway.query(findAllByCategoryQuery, ResponseTypes.multipleInstancesOf(Object.class));
		List<Object> blogs = allBlogs.join();
		kafkaProducerService.searchBlogs(blogs,AppConstants.TOPIC_SEARCH_BLOG_CAT);
		if (blogs.isEmpty()) {
			return new ResponseEntity<Object>("No Blogs Found", HttpStatus.OK);
		}
		return new ResponseEntity<Object>(blogs, HttpStatus.OK);
	}

	/**
	 * Search By Category and Duration Range
	 * 
	 * @param category
	 * @param fromdate
	 * @param todate
	 * @return
	 */
	@GetMapping("blogs/get/{category}/{fromdate}/{todate}")
	public ResponseEntity<Object> searchByCategoryAndRange(@PathVariable String category,
			@PathVariable String fromdate, @PathVariable String todate) {
		LocalDate toDate = LocalDate.parse(todate).plusDays(1);
		FindAllByCategoryAndRangeQuery findAllByCategoryAndRangeQuery = new FindAllByCategoryAndRangeQuery();
		findAllByCategoryAndRangeQuery.setCategory(category);
		findAllByCategoryAndRangeQuery.setFromdate(Date.valueOf(fromdate));
		findAllByCategoryAndRangeQuery.setTodate(Date.valueOf(toDate));
		CompletableFuture<List<Object>> allBlogs =  queryGateway.query(findAllByCategoryAndRangeQuery, ResponseTypes.multipleInstancesOf(Object.class));
		List<Object> blogs = allBlogs.join();
		kafkaProducerService.searchBlogs(blogs,AppConstants.TOPIC_SEARCH_BLOG_CAT_RANGE);														
		if (blogs.isEmpty()) {
			return new ResponseEntity<Object>("No Blogs Found", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(blogs, HttpStatus.OK);
		}

	}

}
