package com.jt.vo;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class EasyUIList {

	private Integer total;
	private List<?> rows;
	
	public EasyUIList() {}
	public EasyUIList(Integer total , List<?> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}
	
}
