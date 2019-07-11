package com.jt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;

@Service
public class ItemServiceImpl implements ItemService{

	/**
	 * 该操作是前台service请求jt-manage中的数据
	 * 需要使用httpClient技术
	 * 
	 * 自己理解: 后台：jt-manage 中的 controller -->service ...  将结果返回到前台的controller 的是 JSON串或者html页面
	 * 		     然后  前台 通过 json 转化 对象 再 呈现 页面
	 */
	
	@Autowired
	private HttpClientService httpClientService;
	
	@Override
	public Item findItemById(Long itemId) {
		String url = "http://manage.jt.com/web/item/findItemById/" + itemId;
		String resultJSON = httpClientService.doGet(url);
		//json ==> 对象
		Item item = ObjectMapperUtil.toObject(resultJSON, Item.class);
		return item;
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		String url = "http://manage.jt.com/web/item/findItemDescById/" + itemId;
		String resultJSON = httpClientService.doGet(url);
		//将json转化为对象
		return ObjectMapperUtil.toObject(resultJSON, ItemDesc.class);
	}

	
}





















