package syh.controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import syh.pojo.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
@RestController
public class CommonController {
    @Value("${reji.file-sava-path}")
    private String fileSavePath;
    @PostMapping("/common/upload")
    public Result uploadFile(MultipartFile file) throws IOException {
        String filename=file.getOriginalFilename();
        filename= UUID.randomUUID()+filename.substring(filename.lastIndexOf('.'));
        File savaFile = new File(fileSavePath + "/" + filename);
        file.transferTo(savaFile);
        return Result.success(filename);
    }
    @GetMapping("/common/download")
    public Result downloadFile(String name, HttpServletResponse response) throws IOException {
//        response.setContentType("");
        String filename=fileSavePath+"/"+name;
        FileInputStream fileInputStream = new FileInputStream(filename);
        ServletOutputStream outputStream = response.getOutputStream();
        byte [] buffer = new byte[1024];
        int len=0;
        while ((len=fileInputStream.read(buffer))!=-1)
            outputStream.write(buffer,0,len);
        fileInputStream.close();
        outputStream.close();
        return Result.success("下载成功");
    }
}
