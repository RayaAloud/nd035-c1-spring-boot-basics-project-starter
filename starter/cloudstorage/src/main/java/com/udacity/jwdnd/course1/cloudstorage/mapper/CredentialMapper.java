package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entities.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE userId=#{userId}")
    List<Credential> getAllCredentials(Integer userId);
    @Insert("INSERT INTO CREDENTIALS (url, username, key , password ,userId) VALUES(#{url}, #{userName}, #{key}, #{password} , #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insertCredential(Credential credential);
    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{userName} , key = #{key} , password = #{password} , userId=#{userId} WHERE credentialId = #{credentialId}")
    void updateCredential(Credential credential);
    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    void deleteCredential(Integer credentialId);
    @Select("SELECT key FROM CREDENTIALS WHERE credentialid = #{credentialId} ")
    String getKeyByCredentialId(String credentialId);
}
