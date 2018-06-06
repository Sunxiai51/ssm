package com.sunveee.template.ssm.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sunveee.template.ssm.util.LogUtil;

/**
 * 文件上传下载
 * 
 * @author 51
 * @version $Id: FileController.java, v 0.1 2018年6月6日 下午2:53:35 51 Exp $
 */
@Controller
@RequestMapping("/file")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @RequestMapping(value = "/operate")
    public String toFileList(Model model) {
        LogUtil.debug(logger, "访问文件操作Controller");

        return "file/file-operate";
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/upload")
    public String upload(MultipartFile file, HttpServletRequest request) {
        LogUtil.info(logger, "收到文件上传请求");

        String path = request.getSession().getServletContext().getRealPath("upload");
        String fileName = file.getOriginalFilename();
        LogUtil.info(logger, "文件上传请求参数:path={0},fileName={1}", path, fileName);

        File dir = new File(path, fileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            //MultipartFile自带的解析方法
            file.transferTo(dir);
        } catch (IllegalStateException | IOException e) {
            LogUtil.error(e, logger, "文件上传异常");
            return "failed";
        }
        return "success";
    }

    /**
     * 文件下载
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    @PostMapping("/download")
    public void download(String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        LogUtil.info(logger, "收到文件下载请求,fileName={0}", fileName);

        //模拟文件，myfile.txt为需要下载的文件
        String filePath = request.getSession().getServletContext().getRealPath("upload") + "/" + fileName;
        //获取输入流
        InputStream bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
        //假如以中文名下载的话
        String filename = "下载文件.txt";
        //转码，免得文件名中文乱码
        filename = URLEncoder.encode(filename, "UTF-8");
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while ((len = bis.read()) != -1) {
            out.write(len);
            out.flush();
        }
        out.close();
    }

}