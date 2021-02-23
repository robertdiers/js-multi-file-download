package com.example.multidownload;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest")
public class DownloadRestController {

    private static final int BUFFER_SIZE = 1024;

    @GetMapping("image/{filename}")
    public StreamingResponseBody downloadImage(HttpServletResponse response, @PathVariable String filename) {

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

    @GetMapping("file/{filename}")
    public StreamingResponseBody downloadFile(HttpServletResponse response, @PathVariable String filename) {
        try {
            System.out.println("requested: " + filename + " - we will generate");

            response.setContentType("application/octet-stream");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + "\"");

            //create example file
            RandomAccessFile f = new RandomAccessFile(filename, "rw");
            f.setLength(500 * 1024 * 1024);

            return outputStream -> {
                int bytesRead;
                byte[] buffer = new byte[BUFFER_SIZE];
                InputStream inputStream = Channels.newInputStream(f.getChannel());
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
