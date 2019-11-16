package io.ryanluoxu.xapp.mapper

import feign.Param
import groovy.transform.CompileStatic
import io.ryanluoxu.xapp.entity.UserInfo
import io.ryanluoxu.xapp.util.JsonTypeHandler
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

/**
 * @author luoxu on 11/19
 */
@CompileStatic
interface UserInfoMapper {
    @Insert('''
    <script>
        INSERT INTO `user_info` (
            `user_id`
            ,`user_name`
            ,`account_ids`
            )
        VALUES (
            #{userId}
            ,#{userName}
            ,#{accountIds, typeHandler=io.ryanluoxu.xapp.util.JsonTypeHandler}
            )
    </script>
    ''')
    void add(UserInfo userInfo)

    @Select('''
    <script>
        SELECT * FROM `user_info`
        WHERE `user_id` = #{userId}
    </script>
    ''')
    @Results([
            @Result(property = 'accountIds', column = 'account_ids', typeHandler = JsonTypeHandler)
    ])
    UserInfo getUserInfo(@Param("userId") String userId)

    @Update('''
    <script>
        UPDATE `user_info`
        SET `user_name` = #{userName}
            ,`account_ids` = #{accountIds, typeHandler=io.ryanluoxu.xapp.util.JsonTypeHandler}
        WHERE `user_id` = #{userId}
    </script>
    ''')
    void updateUserInfo(UserInfo userInfo)
}
