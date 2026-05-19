package asia.hfh.code.mapper;

import asia.hfh.code.entity.TaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xiaoHe
 * @since 2025-12-07
 */
@Mapper
public interface TaskMapper extends BaseMapper<TaskEntity> {

    TaskEntity getTaskDetail(Long id);

    /**
     * 手写分页：offset 从 0 开始
     */
    List<TaskEntity> selectTaskPage(@Param("offset") long offset,
                                    @Param("size") int size);

    long countTask();

}
