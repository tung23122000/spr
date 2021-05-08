package dts.com.vn.service;

import dts.com.vn.entities.MapRolePermission;
import dts.com.vn.repository.RoleRepository;
import dts.com.vn.response.RolePermissionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;


    public RolePermissionResponse getPermissionById(Long id) {
        List<MapRolePermission> list = roleRepository.getPermissionById(id);
        RolePermissionResponse rolePermissionResponse = new RolePermissionResponse(id, list);
        return rolePermissionResponse;
    }
}
