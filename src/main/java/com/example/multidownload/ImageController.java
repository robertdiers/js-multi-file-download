package com.example.multidownload;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@RestController
@RequestMapping("/rest")
public class ImageController {

    private static final int BUFFER_SIZE = 1024;

    @GetMapping("download/{filename}")
    public StreamingResponseBody downloadFile(HttpServletResponse response, @PathVariable String filename) {

        System.out.println("requested: "+filename + " - but we will use the included image instead");

        response.setContentType("image/jpeg");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + "\"");

        return outputStream -> {
            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            InputStream inputStream = getClass().getResourceAsStream("/bild.jpg");
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        };
    }

}
