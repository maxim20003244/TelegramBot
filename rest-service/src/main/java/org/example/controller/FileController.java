package org.example.controller;

import lombok.extern.log4j.Log4j;
import org.example.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping("/file")
@Log4j
@RestController
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    @RequestMapping(method = RequestMethod.GET, value = "/get-photo")
    public void getPhoto (@RequestParam("id") String id, HttpServletResponse response){
        var photo = fileService.getPhoto(id);
        if(photo == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ;
        }
        response.setContentType(MediaType.IMAGE_JPEG.toString());
        response.setHeader("Content-Disposition", "attachment; ");
        response.setStatus(HttpServletResponse.SC_OK);


        var binaryContent = photo.getBinaryContent();
       /* var fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if(fileSystemResource == null){

            return;
        }*/
        try {
            var out = response.getOutputStream();
        } catch (IOException e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.GET, value = "/get-doc")
    public void getDoc (@RequestParam("id") String id, HttpServletResponse response) {
        var doc = fileService.getDocument(id);
        if (doc == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.setContentType(MediaType.parseMediaType(doc.getMimeType()).toString());
        response.setHeader("Content-Disposition", "attachment ; filename= " + doc.getDocName());
        response.setStatus(HttpServletResponse.SC_OK);

        var binaryContent = doc.getBinaryContent();

        try {
            var out = response.getOutputStream();
        } catch (IOException e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
