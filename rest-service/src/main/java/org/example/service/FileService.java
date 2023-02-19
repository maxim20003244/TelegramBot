package org.example.service;

import org.example.entity.AppDocument;
import org.example.entity.AppPhoto;
import org.example.entity.BinaryContent;
import org.springframework.core.io.FileSystemResource;

public interface FileService {
    AppDocument getDocument(String id);

    AppPhoto getPhoto (String id);
    FileSystemResource getFileSystemResource (BinaryContent binaryContent);
}
