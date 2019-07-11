package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private ItemCatMapper itemCatMapper;
	
	private Jedis jedis;

	@Override
	public String findItemCatNameById(Long itemCatId) {
		ItemCat itemCat = itemCatMapper.selectById(itemCatId);
		return itemCat.getName();
	}

	@Override
	public List<EasyUITree> findItemCatList(Long parentId) {
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent_id", parentId);
		List<ItemCat> itemCatList = itemCatMapper.selectList(queryWrapper);

		//需要返回VOList集合信息，则遍历itemItemList
		List<EasyUITree> treeList = new ArrayList<>();
		for (ItemCat itemCat : itemCatList) {
			EasyUITree uiTree = new EasyUITree();
			uiTree.setId(itemCat.getId()).setText(itemCat.getName());
			String state = itemCat.getIsParent() ? "closed" : "opend" ;
			uiTree.setState(state);
			treeList.add(uiTree);
		}
		return treeList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EasyUITree> findItemCatCacheList(Long parentId) {
		List<EasyUITree> treeList = new ArrayList<>();
		String key = "ttem_cat_"+parentId;
		String result = jedis.get(key);
		if (StringUtils.isEmpty(result)) {
			//缓存中没有数据 查询数据库
			treeList = findItemCatList(parentId);
			//将数据 保存到 缓存中
			String json = ObjectMapperUtil.toJson(treeList);
			jedis.set(key, json);
			System.out.println("查询数据库");
		} else {
			treeList = ObjectMapperUtil.toObject(result, treeList.getClass());
			System.out.println("查询redis缓存");
		}
		return treeList;
	}

	
}














