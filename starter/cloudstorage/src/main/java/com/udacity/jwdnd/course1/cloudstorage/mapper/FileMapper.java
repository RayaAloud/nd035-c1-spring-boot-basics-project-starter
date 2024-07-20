package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entities.File;
import com.udacity.jwdnd.course1.cloudstorage.entities.FileResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT fileName FROM FILES where userId=#{userId}")
    List<String> getAllFiles(Integer userId);
    @Select("SELECT EXISTS (SELECT 1 FROM FILES WHERE fileName = #{fileName})")
    boolean findByFileName(String fileName);
    @Insert("INSERT INTO FILES (filename, contenttype,filesize, userid ,filedata) VALUES(#{fileName}, #{contentType}, #{fileSize},#{userId},#{blob})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int uploadFile(File file);
    @Delete("DELETE FROM FILES WHERE filename=#{name}")
    void deleteFile(String name);
    @ResultType(FileResponse.class)
    @Select("SELECT filedata as storedData,filename,contenttype FROM FILES WHERE filename = #{name}")
    FileResponse getFileByName(String name);
}
