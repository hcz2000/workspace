package com.jams.myapp;

import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.jams.myapp.dao.MessageDao;
import com.jams.myapp.entity.Message;
import com.jams.myapp.util.SqlSessionFactoryUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class MessageTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MessageTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( MessageTest.class );
    }

  
    public void testFindMyMessage()
    {
    	  SqlSession sqlSession=SqlSessionFactoryUtil.openSession();  
    	  MessageDao messageDao = sqlSession.getMapper(MessageDao.class);  
          List<Message> messageList = messageDao.findMyMessage("hcz13825089890");
          assertNotNull(messageList);
          assertEquals(messageList.size(), 3);
          for(Iterator<Message> itr=messageList.iterator();itr.hasNext();) {
        	  Message message=itr.next();
        	  System.out.println(message.getCreateTime()+":"+message.getSender()+":"+message.getMessage());
          }
    }
}
