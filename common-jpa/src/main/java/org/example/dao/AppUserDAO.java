package org.example.dao;

import org.example.entity.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByTelegramUserId(Long id);
}
