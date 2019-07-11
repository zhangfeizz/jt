package com.jt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.service.CartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Reference(timeout=3000,check=false)
	private CartService cartService;

	//购物车列表数据展现
	@RequestMapping("/show")
	public String show (Model model) {   //model 是 域  ，是从 购物车 页面 .jsp 中的 ${cartList} 查看的
		// 获取当前用户信息
		Long userId = UserThreadLocal.get().getId();
		List<Cart> cartList = cartService.findCartList(userId);
		model.addAttribute("cartList",cartList);
		return "cart";
	}

	//实现购物车数量的修改 
	//使用restFul风格有多个参数时，并且和pojo属性一致，则可以使用pojo对象接收
	@RequestMapping("update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateNum(Cart cart) {
		try {
			@SuppressWarnings("unused")
			Long userId = UserThreadLocal.get().getId();
			cartService.updateCartNum(cart);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}

	//实现购物车新增
	@RequestMapping("/add/{itemId}")
	public String saveCart(Cart cart) {
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cartService.saveCart(cart);
		return "redirect:/cart/show.html";
	}	

	//实习购物车删除
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(@PathVariable Long itemId) {
		Long userId = UserThreadLocal.get().getId();
		Cart cart = new Cart();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		cartService.deleteCart(cart);
		return "redirect:/cart/show.html";
	}

}











