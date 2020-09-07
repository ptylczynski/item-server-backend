package cloud.ptl.itemserver.persistence.dao.authorization;

public enum CompoundPermission {
    EDITOR(
            Permission.READ,
            Permission.CREATE,
            Permission.UPDATE,
            Permission.DELETE
    ),

    VIEWER(
            Permission.READ
    );

    private final Permission[] permissions;

    CompoundPermission(Permission... permissions){
        this.permissions = permissions;
    }

    public Permission[] getPermissions(){
        return this.permissions;
    }
}
