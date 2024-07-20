package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entities.File;
import com.udacity.jwdnd.course1.cloudstorage.entities.FileResponse;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class FileService {
    private final FileMapper fileMapper;

    public List<String> getAllFiles(Integer userId){
        return fileMapper.getAllFiles(userId);
    }
    public boolean isFileExist(String fileName){
        return fileMapper.findByFileName(fileName);
    }
    public int uploadFile(MultipartFile file, Integer userId) throws IOException {
        return fileMapper.uploadFile(new File(null, file.getOriginalFilename(),file.getContentType(), String.valueOf(file.getSize()),userId,file.getBytes()));
    }
    public void deleteFile(String name){
        fileMapper.deleteFile(name);
    }
    public FileResponse downloadFile(String name){
        return fileMapper.getFileByName(name);
    }

}
