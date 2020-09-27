package cn.xie.sb_fileupload;

import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
public class FileUpLoadController {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("/yyyy/MM/dd/");

    @PostMapping("/upload")
    public String upload(MultipartFile file, HttpServletRequest req){
        String format = simpleDateFormat.format(new Date());
        String realPath = req.getServletContext().getRealPath("/static/img") + format;
        File folder = new File(realPath);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));

        try {
            file.transferTo(new File(folder,newName));
            System.out.println(realPath);
            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/img" + format + newName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  "error";
    }
}
