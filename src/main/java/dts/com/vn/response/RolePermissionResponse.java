package dts.com.vn.response;

import dts.com.vn.entities.MapRolePermission;
import dts.com.vn.entities.Permission;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RolePermissionResponse {

	private Long roleId;

	private List<Permission> listPermission;

	public RolePermissionResponse() {
	}

	public RolePermissionResponse(Long roleId, List<MapRolePermission> listInput) {
		super();
		this.roleId = roleId;
		this.listPermission = new ArrayList<>();
		listInput.forEach((element) -> {
			listPermission.add(element.getPermission());
		});
	}
}
