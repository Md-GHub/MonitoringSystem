package com.md.monitoringsystem.utils;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptUtil {

    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static void main(String[] args) {
        System.out.println(new BcryptUtil().hashPassword("123456"));
        System.out.println(new BcryptUtil().verifyPassword("123456", "$2a$10$gi5YD28WdsahKVSSrc1UHeOZXZ5ELM9KnFR7qtDeft1vDk0mqfs1a"));
    }
}

