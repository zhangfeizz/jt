package com.jt.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.FileVo;

@Controller
public class FileController {

	@Autowired
	private FileService fileService;
	
	@RequestMapping("/file")
	public String fileImage(MultipartFile fileImage) throws IllegalStateException, IOException {
		//1 获取文件名称
		String fileName = fileImage.getOriginalFilename();
		
		//2 指定存储的目录 判断文件是否存在
		String filePath = "E:/JavaZiLiao/jingtao/jt-upload";
		File dirFile = new File(filePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		
		//3 上传
		String realName = "E:/JavaZiLiao/jingtao/jt-upload/"+fileName;
		fileImage.transferTo(new File(realName));
		
		return "redirect:/file.jsp";
	}
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public FileVo uploadFile(MultipartFile uploadFile) {
		return fileService.upload(uploadFile);
	}
}
