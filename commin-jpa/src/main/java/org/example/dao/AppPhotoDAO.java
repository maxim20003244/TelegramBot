package org.example.dao;

import org.example.entity.AppDocument;
import org.example.entity.AppPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppPhotoDAO extends JpaRepository<AppPhoto , Long> {
}
