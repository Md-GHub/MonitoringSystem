package com.md.monitoringsystem.service;

import com.md.monitoringsystem.exception.ErrorInCreatingUser;
import com.md.monitoringsystem.model.User;
import com.md.monitoringsystem.repo.OrgRepo;
import com.md.monitoringsystem.repo.RoleRepo;
import com.md.monitoringsystem.repo.UserRepo;
import com.md.monitoringsystem.repo.UserRoleRepo;

public class UserService {
    private UserRepo userRepo = UserRepo.get();
    private OrgRepo orgRepo = OrgRepo.get();
    private RoleRepo roleRepo = RoleRepo.get();
    private UserRoleRepo userRoleRepo = UserRoleRepo.get();
    public int createAdmin(User user){
        String email = user.getEmail();
        String[] domain = email.split("@");
        String orgName = domain[domain.length-1];
        int createdOrgId;
        int createdUser;
        int roleId;
        try{
            // add the organization to the table
            createdOrgId = orgRepo.addOrg(orgName);
            // add user to user table
            createdUser = userRepo.addUser(user,createdOrgId,true);
            // get role id for admin
            roleId = roleRepo.getRoleId(user.getRole().toString());
            // add role id and user id to the user role table
            userRoleRepo.insertUserRole(roleId,createdUser);
        }catch(ErrorInCreatingUser e) {
            throw new ErrorInCreatingUser("Error creating user");
        }
        return createdUser;
    }
    public int createUser(User user){
        String email = user.getEmail();
        String[] domain = email.split("@");
        String orgName = domain[domain.length-1];
        int orgId;
        int createdUser;
        int roleId;
        try{
            // add the organization to the table
            orgId = orgRepo.getOrgId(orgName.split(".")[0]);
            // add user to user table
            createdUser = userRepo.addUser(user,orgId,false);
            // get role id for admin
            roleId = roleRepo.getRoleId(user.getRole().toString());
            // add role id and user id to the user role table
            userRoleRepo.insertUserRole(roleId,createdUser);
        }catch(ErrorInCreatingUser e) {
            throw new ErrorInCreatingUser("Error creating user");
        }
        return createdUser;
    }

    public void activateUser(int id) {
        userRepo.activateUser(id);
    }

    public void deleteUser(int id) {
        userRepo.deleteUser(id);
    }
}
