package com.md.monitoringsystem.service;

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
    public String createUser(User user){
        /// check if the org is already exists
        String email = user.getEmail();
        String[] domain = email.split("@");
        System.out.println(domain[domain.length-1]);
        String orgName = domain[domain.length-1];
        int orgId = orgRepo.isExist(orgName);
        /// else create an user with admin role and add the organization details to the table
        if(orgId == -1){
            // add the organization to the table
            int createdOrgId = orgRepo.addOrg(orgName);


            // add user to user table
            int createdUser = userRepo.addUser(user,createdOrgId);


            // get role id for admin
            int roleId = roleRepo.getRoleId("ADMIN");


            // add role id and user id to the user role table
            userRoleRepo.insertUserRole(roleId,createdUser);
        }else{
            /// note : get the orgId in the filter and add that to the thread local or any universal store
            int createdUser = userRepo.addUser(user,10);
            int roleId = roleRepo.getRoleId(user.getRole().toString());
            userRoleRepo.insertUserRole(roleId,createdUser);
            String invitationlink = "http://localhost:8080/MonitoringSystem_war_exploded/user/activate?id="+createdUser;
            return invitationlink;
        }
        return "";
    }

    public void activateUser(int id) {
        userRepo.activateUser(id);
    }

    public void deleteUser(int id) {
        userRepo.deleteUser(id);
    }
}
