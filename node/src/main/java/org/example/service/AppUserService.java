package org.example.service;

import org.example.entity.AppUser;

public interface AppUserService {
    String  registerUser(AppUser appUser);
    String sendEmail(AppUser appUser , String email);

}
