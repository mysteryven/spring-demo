package com.mystery.blog.service;

import com.mystery.blog.dao.BlogDao;
import com.mystery.blog.entity.Blog;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class BlogService {
    private final BlogDao blogDao;

    @Inject
    public BlogService(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    public List<Blog> getBlogs(Integer pageNo, Integer pageSize, Integer userId) {
        return blogDao.getBlogs(pageNo, pageSize, userId);
    }

    public void insertBlog(Blog blog) {
        blogDao.insertBlog(blog);
    }

    public void updateBlog(String id, Blog blog) {
        blogDao.updateBlog(blog);
    }

    public Object getBlogTotal() {
        return blogDao.getBlogTotal();
    }
}
