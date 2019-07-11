package com.jt.service;

import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.jt.pojo.Cart;

@Service
public interface CartService {

	List<Cart> findCartList(Long userId);

	void updateCartNum(Cart cart);

	void saveCart(Cart cart);

	void deleteCart(Cart cart);


}
