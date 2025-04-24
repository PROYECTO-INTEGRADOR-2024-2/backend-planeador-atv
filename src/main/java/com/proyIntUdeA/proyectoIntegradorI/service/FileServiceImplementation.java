package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.entity.FileEntity;
import com.proyIntUdeA.proyectoIntegradorI.repository.FileRepository;
import com.proyIntUdeA.proyectoIntegradorI.response.ResponseFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileServiceImplementation implements FileService {

    @Autowired
    private FileRepository fileRepository;
    @Override
    public FileEntity store(MultipartFile file, String userId) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileEntity fileEntity = FileEntity.builder()
                .name(fileName)
                .type(file.getContentType())
                .data(file.getBytes())
                .user_id(userId)
                .build();

        return fileRepository.save(fileEntity);
    }

    @Override
    public Optional<FileEntity> getFile(UUID id) throws FileNotFoundException {
        Optional<FileEntity> file = fileRepository.findById(id);
        if(file.isPresent()){
            return file;
        }
        throw new FileNotFoundException();
    }

    @Override
    public List<ResponseFile> getAllFiles() {
        List<ResponseFile> files = fileRepository.findAll()
                .stream()
                .map(dbFile ->{
                   String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                           .path("api/v1/fileManager/files/")
                           .path(dbFile.getId().toString())
                           .toUriString();
                   return ResponseFile.builder()
                           .name(dbFile.getName())
                           .url(fileDownloadUri)
                           .type(dbFile.getType())
                           .userId(dbFile.getUser_id())
                           .size(dbFile.getData().length).build();

                }).collect(Collectors.toList());

        return files;
    }
}
