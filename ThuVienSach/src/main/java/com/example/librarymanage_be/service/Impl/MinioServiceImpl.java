package com.example.librarymanage_be.service.Impl;

import com.example.librarymanage_be.properties.MinioProperties;
import com.example.librarymanage_be.service.MinioService;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;
    @Override
    public String uploadFile(String bucket, InputStream inputStream,long size, String fileName, String contentType) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
           minioClient.putObject(
                   PutObjectArgs.builder()
                           .bucket(bucket)   //Nơi chứa bucket
                           .object(fileName)    //key object
                           .stream(inputStream,size,-1)
                           .contentType(contentType)
                           .build()
           );
           return minioClient.getPresignedObjectUrl(
                   GetPresignedObjectUrlArgs.builder()
                           .method(Method.GET)
                           .bucket(bucket)
                           .object(fileName)
                           .expiry(30, TimeUnit.MINUTES).build()
           );
    }
}
