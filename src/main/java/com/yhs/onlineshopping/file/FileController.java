package com.yhs.onlineshopping.file;

import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileController {
    public List<String> upLoader(MultipartFile file, HttpServletRequest request) throws Exception {
        List<String> fileObject = new ArrayList();
        if (file == null || file.isEmpty()) {
            throw new Exception("未选择需上传的日志文件");
        }
        String c = System.getProperty("user.dir");

        String filePath = c +"\\src\\main\\resources\\static\\images";
//      String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/images/";
        System.out.println(filePath);
        File fileUpload = new File(filePath);
        if (!fileUpload.exists()) {
            fileUpload.mkdirs();

        }

        fileUpload = new File(filePath, file.getOriginalFilename());
        String newfilePath = filePath +"/"+file.getOriginalFilename();
        if (fileUpload.exists()) {
            throw new Exception("上传的文件已存在");
        }

        try {
            file.transferTo(fileUpload);
        } catch (Exception e) {
            throw new Exception("上传文件到服务器失败：" + e.toString());
        }
        fileObject.add(newfilePath);
        fileObject.add(file.getOriginalFilename());
        return fileObject;
    }

    public void downloader(String name, HttpServletResponse response) throws Exception {
        String c = System.getProperty("user.dir");
        name = c +"\\src\\main\\resources\\static\\images\\"+name;
        File file = new File( name);

        if (!file.exists()) {
            throw new Exception(name + "文件不存在");
        }
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + name);

        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();

            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (fis != null) {
                fis.close();
            }
        }

    }
}
