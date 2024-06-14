package com.cognizant.blog.query;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindAllByCategoryAndRangeQuery {
	private String category;
	private Date fromdate;
	private Date todate;
}
