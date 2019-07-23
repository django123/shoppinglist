package shopping_list.Repository;

import com.shopping_list.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    @Query("select t from Task as t join t.shopping ts where ts.shopId =: id order by t.taskId desc ")
    List<Task>findAllByShoppingOrderByTaskIdDesc(@Param("id") Long shopId);
}
