package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.FileVo;


@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {

	//定义本地磁盘路径
		@Value("${image.dirPath}")
		private String localPath;
		@Value("${image.urlPath}")
		private String urlPath;
	
	
	@Override
	public FileVo upload(MultipartFile uploadFile) {
		FileVo fileVo = new FileVo();
		//1 获取文件名称
		String fileName = uploadFile.getOriginalFilename();
		//2 文件名 统一小写
		fileName = fileName.toLowerCase();
		//3 正则 判断后缀
		if (!fileName.matches("^.+\\.(png|jpg|gif)$")) {
			fileVo.setError(1);
			return fileVo;
		}
		//4 判断是否恶意程序
		try {
			BufferedImage image = ImageIO.read(uploadFile.getInputStream());
			int width = image.getWidth();
			int height = image.getHeight();
			if (width == 0 || height == 0 ) {
				fileVo.setError(1);
				return fileVo;
			}
		//5 按时间生成文件夹
			String dateDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
			String localDir = localPath + dateDir;
			
			File dirFile = new File(localDir);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
		//6 防止文件名 重复
			String uuidName = UUID.randomUUID().toString().replace("-", "");
			String fileType = fileName.substring(fileName.lastIndexOf("."));
			String realName = uuidName + fileType;
		//7 文件上传
			File realFile = new File(localDir + "/" + realName);
			uploadFile.transferTo(realFile);
			fileVo.setHeight(height);
			fileVo.setWidth(width);
			String realurlPath = urlPath + dateDir + "/" +realName;
			fileVo.setUrl(realurlPath);
		} catch (IOException e) {
			e.printStackTrace();
			fileVo.setError(1);
			return fileVo;
		}
		return fileVo;
	}

}
