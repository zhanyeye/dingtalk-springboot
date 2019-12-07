package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhanyeye
 * @description 操作User实体类的接口
 * @date 12/7/2019
 */
@Repository
public interface UserRepository extends CustomizedRepository<User, Integer>, JpaRepository<User, Integer> {

}
