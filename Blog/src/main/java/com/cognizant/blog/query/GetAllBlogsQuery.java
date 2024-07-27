package com.cognizant.blog.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class GetAllBlogsQuery {

	@Override
	public String toString() {
		return "GetAllBlogsQuery [getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	
}
