package com.cognizant.blog.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.cognizant.blog.common.AppConstants;
import com.cognizant.blog.entity.Blogs;
import com.cognizant.blog.entity.MongoBlogs;
import com.cognizant.blog.repository.MongoBlogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafKaConsumerService 
{
	@Autowired MongoBlogRepository mongoRepo;
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@KafkaListener(topics = AppConstants.TOPIC_ADD_BLOG, groupId = AppConstants.GROUP_ID)
	public void consume1(String message) {
		try {
            Blogs blog = objectMapper.readValue(message, Blogs.class);
            System.out.println("Received Blog: " + blog.toString());
            log.info(String.format(AppConstants.TOPIC_ADD_BLOG+"%s", blog));
    		MongoBlogs mongoBlogs = new MongoBlogs();
    		BeanUtils.copyProperties(blog, mongoBlogs);
    		mongoRepo.save(mongoBlogs);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		
	}
	
	@KafkaListener(topics = AppConstants.TOPIC_DELETE_BLOG, groupId = AppConstants.GROUP_ID)
	public void consume2(String message) {
		try {
            Blogs blog = objectMapper.readValue(message, Blogs.class);
            // Process the Blogs object as needed
            System.out.println("Received Blog: " + blog.toString());
            log.info(String.format(AppConstants.TOPIC_DELETE_BLOG+"%s", blog));
    		Optional<MongoBlogs> mongoBlog = mongoRepo.findByBlogname(blog.getBlogname());
    		mongoRepo.delete(mongoBlog.get());
        } catch (Exception e) {
            // Handle deserialization exception
            e.printStackTrace();
        }
		
	}
	
	@KafkaListener(topics = AppConstants.TOPIC_FINDALL_BLOGS, groupId = AppConstants.GROUP_ID)
	public void consume3(String message) {
		try {
            Blogs blog = objectMapper.readValue(message, Blogs.class);
            log.info(String.format(AppConstants.TOPIC_FINDALL_BLOGS+"%s", blog));
            // Process the Blogs object as needed
            System.out.println("Received Blog: " + blog.toString());
        } catch (Exception e) {
            // Handle deserialization exception
            e.printStackTrace();
        }
		
	}
	
	@KafkaListener(topics = AppConstants.TOPIC_FINDMY_BLOGS, groupId = AppConstants.GROUP_ID)
	public void consume4(String message) {
		try {
            Blogs blog = objectMapper.readValue(message, Blogs.class);
            log.info(String.format(AppConstants.TOPIC_FINDMY_BLOGS+"%s", blog));
            // Process the Blogs object as needed
            System.out.println("Received Blog: " + blog.toString());
        } catch (Exception e) {
            // Handle deserialization exception
            e.printStackTrace();
        }
		
	}
	
	@KafkaListener(topics = AppConstants.TOPIC_SEARCH_BLOG_CAT, groupId = AppConstants.GROUP_ID)
	public void consume5(String message) {
		try {
            Blogs blog = objectMapper.readValue(message, Blogs.class);
            log.info(String.format(AppConstants.TOPIC_SEARCH_BLOG_CAT+"%s", blog));
            // Process the Blogs object as needed
            System.out.println("Received Blog: " + blog.toString());
        } catch (Exception e) {
            // Handle deserialization exception
            e.printStackTrace();
        }
		
	}
	
	@KafkaListener(topics = AppConstants.TOPIC_SEARCH_BLOG_CAT_RANGE, groupId = AppConstants.GROUP_ID)
	public void consume6(String message) {
		try {
            Blogs blog = objectMapper.readValue(message, Blogs.class);
            log.info(String.format(AppConstants.TOPIC_SEARCH_BLOG_CAT_RANGE+"%s", blog));
            // Process the Blogs object as needed
            System.out.println("Received Blog: " + blog.toString());
        } catch (Exception e) {
            // Handle deserialization exception
            e.printStackTrace();
        }
	}
}
