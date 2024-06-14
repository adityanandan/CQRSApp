package com.cognizant.blog.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.blog.command.CreateBlogCommand;
import com.cognizant.blog.common.AppConstants;
import com.cognizant.blog.entity.Blogs;
import com.cognizant.blog.query.DeleteBlogQuery;
import com.cognizant.blog.query.GetMyBlogsQuery;
import com.cognizant.blog.repository.BlogRepository;
import com.cognizant.blog.service.KafKaProducerService;
import com.cognizant.blog.utils.JwtUtils;

import lombok.extern.slf4j.Slf4j;;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/blogsite/user/blogs")
public class BlogController extends ErrorController {
	@Autowired
	BlogRepository blogRepository;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired KafKaProducerService kafkaProducerService;
	@Autowired CommandGateway commandGateway;
	
	@Autowired QueryGateway queryGateway;
	
	/**
	 * Creating new Blog
	 * 
	 * @param blog
	 * @param userDetails
	 * @return
	 */
	@PostMapping("/add")
	public ResponseEntity<Object> addBlog(@Valid @RequestBody Blogs blog, HttpServletRequest http) {
		log.debug("inside addBlog");
		String jwtToken =""+http.getHeader("Authorization");
		if(jwtUtils.validateJwtToken(jwtToken)) {
			Optional<Blogs> blog1 = blogRepository.findByBlogname(blog.getBlogname());
			if (!blog1.isPresent()) {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				blog.setTimestamp(timestamp);
				blog.setUserid(jwtUtils.getUserIdFromJwtToken(jwtToken));		
				CreateBlogCommand createBlogCommand = CreateBlogCommand.builder()
						.blogId(UUID.randomUUID().toString())
						.blogname(blog.getBlogname())
						.authorname(blog.getAuthorname())
						.article(blog.getArticle())
						.category(blog.getCategory())
						.timestamp(blog.getTimestamp())
						.userid(blog.getUserid())
						.build();
				commandGateway.sendAndWait(createBlogCommand);
				kafkaProducerService.addDelBlog(blog, AppConstants.TOPIC_ADD_BLOG);
				return new ResponseEntity<Object>("Blog added Success", HttpStatus.OK);
			}
			return ResponseEntity.badRequest().body("Blog Name already Exist");
		}
		return new ResponseEntity<Object>("UnAuthorised",HttpStatus.UNAUTHORIZED);

	}
	/**
	 * Delete User Blog
	 * 
	 * @param blogName
	 * @param userDetails
	 * @return
	 */
	@DeleteMapping("/delete/{blogName}")
	public ResponseEntity<Object> deleteBlog(@PathVariable String blogName, HttpServletRequest http) {
		
		String jwtToken =""+http.getHeader("Authorization");
		if(jwtUtils.validateJwtToken(jwtToken)) {
			String userid= jwtUtils.getUserIdFromJwtToken(jwtToken);
			Optional<Blogs> blog = blogRepository.findByBlognameAndUserid(blogName,userid);
			if (blog.isPresent()) {
				
				DeleteBlogQuery deleteBlogQuery = new DeleteBlogQuery();
				deleteBlogQuery.setBlog(blog.get());
				blogRepository.delete(blog.get());
				queryGateway.query(deleteBlogQuery,String.class);
				kafkaProducerService.addDelBlog(blog.get(), AppConstants.TOPIC_ADD_BLOG);
				return new ResponseEntity<Object>("Blog Deleted Success", HttpStatus.OK);
			}
			return new ResponseEntity<Object>("Blog already deleted / not exists", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>("UnAuthorised", HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Get Specific user Blogs
	 * 
	 * @param userDetails
	 * @return
	 */
	@GetMapping("/getMyBlogs")
	public ResponseEntity<Object> getMyBlogs(HttpServletRequest http) {
		String jwtToken =""+http.getHeader("Authorization");
		if(jwtUtils.validateJwtToken(jwtToken)) {
			String userid= jwtUtils.getUserIdFromJwtToken(jwtToken);
			if(userid==null) {
				return new ResponseEntity<Object>("UserID is Null for this request", HttpStatus.BAD_REQUEST);
			}
			GetMyBlogsQuery getMyBlogsQuery = new GetMyBlogsQuery();
			getMyBlogsQuery.setUserid(userid);
			CompletableFuture<List<Object>> allBlogs = queryGateway.query(getMyBlogsQuery, ResponseTypes.multipleInstancesOf(Object.class));
			List<Object> blogs = allBlogs.join();
			kafkaProducerService.findBlogs(blogs, AppConstants.TOPIC_FINDMY_BLOGS);
			return new ResponseEntity<Object>(blogs, HttpStatus.OK);
		}
		return new ResponseEntity<Object>("UnAuthorised", HttpStatus.UNAUTHORIZED);
	}

	/**
	 * List All Blogs
	 * 
	 * @return
	 */
	@GetMapping("/getall")
	public ResponseEntity<Object> getAllBlogs() {
		GetMyBlogsQuery getMyBlogsQuery = new GetMyBlogsQuery();
		getMyBlogsQuery.setUserid(null);
		CompletableFuture<List<Object>> allBlogs = queryGateway.query(getMyBlogsQuery, ResponseTypes.multipleInstancesOf(Object.class));
		List<Object> blogs = allBlogs.join();
		kafkaProducerService.findBlogs(blogs, AppConstants.TOPIC_FINDMY_BLOGS);
		return new ResponseEntity<Object>(blogs, HttpStatus.OK);
	}
}
