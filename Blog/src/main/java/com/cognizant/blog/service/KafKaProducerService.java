package com.cognizant.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.cognizant.blog.entity.Blogs;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafKaProducerService 
{
	
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public void addDelBlog(Blogs blog,String topic) 
	{
		try {
            String blogJson = objectMapper.writeValueAsString(blog);
            kafkaTemplate.send(topic, blogJson);
        } catch (Exception e) {
            // Handle serialization exception
            e.printStackTrace();
        }
	}
	
	public void findBlogs(List<Object> blogs,String topic) 
	{
		 for (Object blog : blogs) {
			 try {
	                String blogJson = objectMapper.writeValueAsString(blog);
	                kafkaTemplate.send(topic, blogJson);
	            } catch (Exception e) {
	                // Handle serialization exception
	                e.printStackTrace();
	            }
	        }	
	}

	public void searchBlogs(List<Object> blogs, String topic) {
		 for (Object blog : blogs) {
			 try {
	                String blogJson = objectMapper.writeValueAsString(blog);
	                kafkaTemplate.send(topic, blogJson);
	            } catch (Exception e) {
	                // Handle serialization exception
	                e.printStackTrace();
	            }
	        }		
	}
	
}
