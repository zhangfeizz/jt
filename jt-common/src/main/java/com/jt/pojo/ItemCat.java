package com.jt.pojo;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@TableName("tb_item_cat")
@Data
@EqualsAndHashCode(callSuper=true)  //为了@Data报警告
@Accessors(chain=true)
public class ItemCat extends BasePojo {
	private static final long serialVersionUID = 5006550716849464495L;
	@TableId(type=IdType.AUTO)
	private Long id;			//bigint(20)类目ID
	private Long parentId;	    //bigint(20)父类目ID=0时，代表的是一级的类目
	private String name;		//varchar(50)类目名称
	private Integer status;		// int(1)状态。可选值:1(正常),2(删除)
	private Integer sortOrder; 	//int(4)排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
	private Boolean isParent; 	//tinyint(1)该类目是否为父类目，1为true，0为false


	
}
