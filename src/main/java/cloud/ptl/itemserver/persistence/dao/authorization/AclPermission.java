package cloud.ptl.itemserver.persistence.dao.authorization;

import lombok.Getter;

public enum AclPermission {
    EDITOR(1),
    VIEWER(0);

    @Getter
    private final Integer order;

    AclPermission(Integer order){
        this.order = order;
    }

    public Boolean sameOrLower(AclPermission aclPermission){
        return this.order <= aclPermission.getOrder();
    }
}
