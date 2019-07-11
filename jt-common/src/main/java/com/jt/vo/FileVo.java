package com.jt.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class FileVo {

	/**
	 * 文件上传后的vo对象
	 *  {"error":0,
	 *  "url":"图片的保存路径",
	 *  "width":图片的宽度,
	 *  "height":图片的高度}
	 * 参数说明： 0代表是一张图片，如果是0，前台才可以解析并显示。1代表不是图片，
	 *	不显示如果不设置宽度和高度，则默认用图片原来的大小，所以不用设置
	 */
	private Integer error = 0;
	private String url;
	private Integer width;
	private Integer height;
	
	public FileVo() {}
	public FileVo(Integer error, String url, Integer width, Integer height) {
		super();
		this.error = error;
		this.url = url;
		this.width = width;
		this.height = height;
	}
}
