package com.jt.quartz;

import java.util.Calendar;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderMapper;
import com.jt.pojo.Order;

public class OrderQuartz extends QuartzJobBean {
	@Autowired
	private OrderMapper orderMapper;

	/**
	 * 当定时任务执行时  执行job具体操作
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// 获取格林威治时间
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -15);  //进行时间运算  超时15min
		Date timeOut = calendar.getTime();  //获取超时时间
		
		//需要修改超时时间
		Order order = new Order();
		order.setStatus(6).setUpdated(new Date());
		
		UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("status", 1)
		             .lt("created", timeOut); //.lt 表示小于
		orderMapper.update(order, updateWrapper);
	}

}
