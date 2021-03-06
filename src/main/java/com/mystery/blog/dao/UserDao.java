package com.mystery.blog.dao;

import com.mystery.blog.entity.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.Instant;
import java.util.HashMap;

@Service
public class UserDao {
    private final SqlSession sqlSession;

    @Inject
    public UserDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public User findUserByUsername(String username) {
        return sqlSession.selectOne("selectUser", username);
    }

    public void saveUser(String username, String password) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("createdAt", Instant.now());
        map.put("modifiedAt",Instant.now());

        sqlSession.insert("insertUser", map);
    }

    public int getUserId(String username) {
        User user = findUserByUsername(username);

        return user.getId();
    }
}
