package com.jams.myapp;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.jams.myapp.dao.UserDao;
import com.jams.myapp.entity.User;
import com.jams.myapp.util.SqlSessionFactoryUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class UserTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public UserTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( UserTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testFindUserById()
    {
    	  SqlSession sqlSession=SqlSessionFactoryUtil.openSession();  
          UserDao userDao = sqlSession.getMapper(UserDao.class);  
          String id = "admin";  
          User user = userDao.findUserById(id);
          assertNotNull(user);
          assertEquals(user.getPassword(), "123456");
         
    }
    
    public void testListAllUsers()
    {
    	  SqlSession sqlSession=SqlSessionFactoryUtil.openSession();  
          UserDao userDao = sqlSession.getMapper(UserDao.class);  
          List<User> userlist = userDao.listAllUsers();
          assertNotNull(userlist);
          assertEquals(userlist.size(), 3);
         
    }
}
