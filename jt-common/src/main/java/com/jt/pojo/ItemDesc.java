package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper=true)  //为了@Data报警告
@Accessors(chain=true)
@TableName("tb_item_desc")
public class ItemDesc extends BasePojo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1016593866051306734L;
	@TableId  //只表示主键
	private Long itemId;
	private String itemDesc;
	
}
