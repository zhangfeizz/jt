package com.jt.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

@RestController
@RequestMapping("/web/item")
public class WebItemController {

	@Autowired
	private ItemService itemService;

	/**
	 * 模拟用户请求
	 * http://manage.jt.com/web/item/findItemById/XXXId
	 */
	@RequestMapping("/findItemById/{itemId}")
	public Item findItemById(@PathVariable Long itemId) { 
		//@PathVariable是spring3.0的一个新功能：接收请求路径中占位符的值
		return itemService.findItemById(itemId);
	}

	//获取itemDesc数据
	@RequestMapping("/findItemDescById/{itemDesc}") 
	public ItemDesc findItemDescById(@PathVariable Long itemId) {
		return itemService.findItemDescById(itemId);
	}

}
