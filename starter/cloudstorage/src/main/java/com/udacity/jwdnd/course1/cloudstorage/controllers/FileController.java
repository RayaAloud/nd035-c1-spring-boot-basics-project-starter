package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.entities.FileResponse;
import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;


@Controller
@AllArgsConstructor
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUser(authentication.getName());
            if (file.isEmpty()) {
                redirectAttributes.addAttribute("operationFailed", "No file is uploaded");
                return "redirect:/home";
            }
            if (fileService.isFileExist(file.getOriginalFilename())) {
                redirectAttributes.addAttribute("operationFailed", "File is exists!");
                return "redirect:/home";
            }
            fileService.uploadFile(file, user.getUserId());
            redirectAttributes.addAttribute("operationSuccess", "File is uploaded successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("operationFailed", "Something went wrong!");
        }
        return "redirect:/home";
    }

    @GetMapping("/delete-file/{name}")
    public String deleteFile(@PathVariable String name, RedirectAttributes redirectAttributes) {
        try {
            fileService.deleteFile(name);
            redirectAttributes.addFlashAttribute("operationSuccess", "File is deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("operationFailed", "Something went wrong!");
        }
        return "redirect:/home";
    }

    @GetMapping("/download-file/{name}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String name) {
        FileResponse fileResponse = fileService.downloadFile(name);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment:filename=\"" + fileResponse.getFileName() + "\"")
                .body(new ByteArrayResource(fileResponse.getStoredData()));

    }
}
