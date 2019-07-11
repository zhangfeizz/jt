package com.jt.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUIList;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;
	
	@Override
	public EasyUIList findItemByPage(Integer page, Integer rows) {
		int total = itemMapper.selectCount(null);
		int start = (page - 1) * rows;
		List<Item> itemList = itemMapper.findItemListByPage(start, rows);
		
		return new EasyUIList(total, itemList);
	}

	@Override
	@Transactional
	public void saveItem(Item item, ItemDesc itemDesc) {
		item.setStatus(1);
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		itemMapper.insert(item);
		
		itemDesc.setItemId(item.getId());
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}

	/**
	 * 说明:该操作是一个批量更新操作
	 *sql:
	 *	update tb_item_cat 
	 *	set status = #{status},
	 *		updated = #{updated}
	 *	where id in (id1,id2,id3.....)
	 *实现A.面向对象的方式操作
	 *实现B.自己写sql. 当做作业
	 */
	@Override
	public void updateStatus(Long[] ids, Integer status) {
		Item item = new Item();
		item.setStatus(status);
		item.setUpdated(new Date());
		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();
		List<Long> idList = Arrays.asList(ids);
		updateWrapper.in("id", idList);
		itemMapper.update(item, updateWrapper);
	}

	@Override
	@Transactional
	public void updateItem(Item item, ItemDesc itemDesc) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		
		itemDesc.setItemId(item.getId());
		itemDesc.setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}

	@Override
	@Transactional
	public void deleteItems(Long[] ids) {
		List<Long> idList = Arrays.asList(ids);
		itemMapper.deleteBatchIds(idList);
		itemDescMapper.deleteBatchIds(idList);
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		return itemDescMapper.selectById(itemId);
	}

	@Override
	public Item findItemById(Long itemId) {
		return itemMapper.selectById(itemId);
	}

}
