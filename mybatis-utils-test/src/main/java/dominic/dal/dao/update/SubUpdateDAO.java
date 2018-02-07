package dominic.dal.dao.update;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;


@Repository
public interface SubUpdateDAO {

    @Update("update `order` set `status`=#{status} where sub_id=#{subId}")
    int updateStatusById(@Param("subId") Long subId, @Param("status") Integer status);
}
