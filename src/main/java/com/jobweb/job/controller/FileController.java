package com.jobweb.job.controller;

import com.jobweb.job.domain.dto.response.ApiResponse;
import com.jobweb.job.domain.dto.response.FileResponse;
import com.jobweb.job.enums.ErrorCode;
import com.jobweb.job.exception.AppException;
import com.jobweb.job.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    @Value("${upload-file.base-uri}")
    private String basePath;

    public FileController(FileService fileService){
        this.fileService = fileService;
    }

    @PostMapping
    public ApiResponse<FileResponse> uploadFile(@RequestParam("file")MultipartFile file,
                                                @RequestParam("folder") String folder)
            throws URISyntaxException, IOException {

        if(file.isEmpty() || file == null)
            throw new AppException(ErrorCode.FILE_EMPTY);

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid  = allowedExtensions.stream().anyMatch(item ->
                fileName.toLowerCase().endsWith(item));
        if(!isValid)
            throw new AppException(ErrorCode.INVALID_FILE);

        fileService.createUploadFolder(basePath + folder);
        String uploadFile = fileService.store(file, folder);
        FileResponse fileResponse = FileResponse.builder()
                .fileName(uploadFile)
                .uploadedAt(Instant.now())
                .build();

        return ApiResponse.<FileResponse>builder()
                .data(fileResponse)
                .build();
    }

}
