package shopping_list.Repository;

import com.shopping_list.entities.UserAndRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by EDOUGA on 10/06/2019.
 */

@Repository
public interface UserAndRoleRepository extends JpaRepository<UserAndRole,Long> {

    UserAndRole findByUserId(Long userId);
}
