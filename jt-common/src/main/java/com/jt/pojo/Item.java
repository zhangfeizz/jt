package com.jt.pojo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * pojo中的包装类型的原因： 比如 int--integer
 * 		integer 可以接收 null 、 1 、2、...
 * 		int     如果参数为null空  则 默认值为0;
 * 
 * 	因现在使用mapper工具做面向对象的数据库操作   比如 mybatis-plus
 * 	mapper工具中要求操作的是不为null的数据		
 * 
 * @author 张飞
 * 
 * 企业开发的价格数据类型
 *  很少使用double dubbo
 *	理由：
 *		1 double数据库总占用的空间较大
 *		2 double的计算精度问题
 *		3 使用double的计算速度慢于long
 *	实际开发的使用：
 *		一般会将数据扩大100倍，用户展现时缩小100倍。
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true) //表示JSON转化时忽略未知属性
@TableName("tb_item")
@Data
@EqualsAndHashCode(callSuper=true)  //为了@Data报警告
@Accessors(chain=true)
public class Item extends BasePojo{
	private static final long serialVersionUID = -113421486821062779L;
	@TableId(type=IdType.AUTO)
	private Long id;				//商品id
	private String title;			//商品标题
	private String sellPoint;		//商品卖点信息
	private Long   price;			//商品价格 Long > dubbo
	private Integer num;			//商品数量
	private String barcode;			//条形码
	private String image;			//商品图片信息   1.jpg,2.jpg,3.jpg
	private Long   cid;				//表示商品的分类id
	private Integer status;			//1正常，2下架
	
	//为了满足页面调用需求,添加get方法
	public String[] getImages(){
		
		return image.split(",");
	}
}
