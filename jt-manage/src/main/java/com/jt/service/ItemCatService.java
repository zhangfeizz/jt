package com.jt.service;

import java.util.List;

import com.jt.vo.EasyUITree;

public interface ItemCatService {

	String findItemCatNameById(Long itemCatId);

	List<EasyUITree> findItemCatList(Long parentId);
	
	List<EasyUITree> findItemCatCacheList(Long parentId);
}
