package cloud.ptl.itemserver.persistence.helper;

import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

public interface LongIndexed {
    Long getId();
}
