package com.md.monitoringsystem.service;

import com.md.monitoringsystem.constant.Role;
import com.md.monitoringsystem.exception.ErrorInCreatingUser;
import com.md.monitoringsystem.exception.NoUserFounded;
import com.md.monitoringsystem.model.LoginCred;
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
            createdUser = userRepo.addAdmin(user,createdOrgId);
            // get role id for admin
            roleId = roleRepo.getRoleId(Role.ADMIN.toString());
            // add role id and user id to the user role table
            userRoleRepo.insertUserRole(roleId,createdUser);
        }catch(ErrorInCreatingUser e) {
            throw new ErrorInCreatingUser("Error creating user");
        }
        return createdUser;
    }

    public int createUser(User user) throws ErrorInCreatingUser{
        String email = user.getEmail();
        String[] domain = email.split("@");
        String orgName = domain[domain.length-1];
        int orgId;
        int createdUser;
        int roleId;
        if(userRepo.getUserDetails(email) != null){
            throw new ErrorInCreatingUser("Error creating user already exists");
        }
        // add the organization to the table
        orgId = orgRepo.getOrgId(orgName);
        // add user to user table
        createdUser = userRepo.addUser(user,orgId);
        // get role id for admin
        roleId = roleRepo.getRoleId(user.getRole().toString());
        // add role id and user id to the user role table
        userRoleRepo.insertUserRole(roleId,createdUser);
        return createdUser;
    }

    public void deleteUser(int id) throws NoUserFounded {
        userRepo.deleteUser(id);
    }

    public void activateUser(LoginCred emailPassword) {
        int id = userRepo.getUserId(emailPassword.getEmail());
        if(id>0){
            userRepo.addPassword(id,emailPassword.getPassword());
        }else{
            throw new NoUserFounded("No such user");
        }
    }

    public void deleteUser(String email) throws NoUserFounded {
        userRepo.deleteUserByEmail(email);
    }

}
