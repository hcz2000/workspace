package com.jams.myapp.dao;

import java.util.List;

import com.jams.myapp.entity.Message;

public interface MessageDao {  
    public List<Message> findMyMessage(String receiver); 
}  
