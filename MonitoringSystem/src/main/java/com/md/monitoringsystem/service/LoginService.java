package com.md.monitoringsystem.service;

import com.md.monitoringsystem.exception.UserNotInvited;
import com.md.monitoringsystem.exception.UsernamePasswordError;
import com.md.monitoringsystem.model.User;
import com.md.monitoringsystem.repo.UserRepo;
import com.md.monitoringsystem.utils.BcryptUtil;

public class LoginService {
    private UserRepo userRepo = UserRepo.get();
    private BcryptUtil bcryptUtil = new BcryptUtil();
    public void loginUser(User user) throws UsernamePasswordError, UserNotInvited {
        User userFromDb = userRepo.getUserDetails(user.getEmail());
        if( !user.getUserName().equals(userFromDb.getUserName()) || !bcryptUtil.verifyPassword(user.getPassword(),userFromDb.getPassword())){
            throw new UsernamePasswordError("Username or Password Incorrect");
        }
    }
}
