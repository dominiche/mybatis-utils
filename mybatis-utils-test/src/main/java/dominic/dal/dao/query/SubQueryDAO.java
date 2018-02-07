package dominic.dal.dao.query;

import dominic.dal.entity.Sub;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


@Repository
public interface SubQueryDAO {

    @Select("select * from `order` where sub_id=#{subId}")
    Sub queryById(@Param("subId") Long subId);
}
