package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.entity.FileEntity;
import com.proyIntUdeA.proyectoIntegradorI.response.ResponseFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileService {

    //Va a permitir almacenar el archivo en la BD
    FileEntity store(MultipartFile file) throws IOException;

    //Descargar archivo, excepcion archivo no encoentrado
    Optional<FileEntity> getFile(UUID id) throws FileNotFoundException;

    //Permite consultar lista de archivos cargados en la base de datos
    List<ResponseFile> getAllFiles();
}
