package com.jt.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class EasyUITree {
	private Long id;  //节点名称
	private String text;  //节点名称
	private String state; //节点状态
	public EasyUITree() {}
	public EasyUITree(Long id, String text, String state) {
		super();
		this.id = id;
		this.text = text;
		this.state = state;
	}
}
