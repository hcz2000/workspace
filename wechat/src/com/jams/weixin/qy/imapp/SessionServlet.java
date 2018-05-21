package com.jams.weixin.qy.imapp;
 
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;


 
/**
 * 核心请求处理类
 * 
 * @author HCZ
 * 
 */
public class SessionServlet extends HttpServlet {
    private static final long serialVersionUID = 4440739483644821986L;

    /**
     * 确认请求来自微信服务器
     * @throws IOException 
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
         
        // 微信加密签名 
        String sVerifyMsgSig = request.getParameter("msg_signature");
        // 时间戳
        String sVerifyTimeStamp = request.getParameter("timestamp");
        // 随机数
        String sVerifyNonce = request.getParameter("nonce");
        // 随机字符串
        String sVerifyEchoStr = request.getParameter("echostr");
        String sEchoStr; //需要返回的明文
        PrintWriter out = response.getWriter();  
        WXBizMsgCrypt wxcpt;
        try {
            wxcpt = new WXBizMsgCrypt(Constants.TOKEN, Constants.ENCODINGAESKEY,Constants.CORPID);
            sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp,sVerifyNonce, sVerifyEchoStr);
            // 验证URL成功，将sEchoStr返回
            out.print(sEchoStr);  
        } catch (AesException e1) {
            e1.printStackTrace();
        }
    }
    
   
 
    /**
     * 处理微信服务器发来的消息
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");  
        response.setCharacterEncoding("UTF-8");  
           // 微信加密签名  
        String signature = request.getParameter("msg_signature");  
          // 时间戳  
        String timestamp = request.getParameter("timestamp");  
          // 随机数  
        String nonce = request.getParameter("nonce");  
               
        InputStream is = request.getInputStream(); 
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
        String buffer = null; 
        StringBuffer sb = new StringBuffer(); 
        while ((buffer = br.readLine()) != null) { 
                sb.append(buffer); 
        } 
        String notifyMessage = sb.toString(); 
            
        try {
        	WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(Constants.TOKEN, Constants.ENCODINGAESKEY,Constants.CORPID);
			String msg = wxcpt.DecryptMsg(signature, timestamp, nonce, notifyMessage);
			System.out.println(msg);
			Map<String, Object> message = parseXml(msg);
			PrintWriter out = response.getWriter();
			if("chat".equals(message.get("AgentType"))){
				out.print(message.get("PackageId"));
			}
		    out.close();
			process(message);
		} catch (AesException e){
			e.printStackTrace();
		}     
    }
    
    /*
    <xml>
    	<AgentType><![CDATA[chat]]></AgentType>
    	<ToUserName><![CDATA[wx8042b22041aa4f39]]></ToUserName>
    	<ItemCount>1</ItemCount>
    	<PackageId>429496738357941800</PackageId>
    	<Item>
    		<FromUserName><![CDATA[hcz13825089890]]></FromUserName>
    		<CreateTime>1465560657</CreateTime>
    		<MsgType><![CDATA[text]]></MsgType>
    		<Content><![CDATA[古]]></Content>
    		<MsgId>2542659985437398667</MsgId>
    		<Receiver>
    			<Type>group</Type>
    			<Id>9223372036864775810</Id>
    		</Receiver>
    	</Item>
    </xml>
    */
    
    private void process(Map<String,Object> messageMap){
    	int count=Integer.parseInt((String)messageMap.get("ItemCount"));
    	System.out.println(messageMap);
    	for(int i=0;i<count;i++){
    		Map<String,Object> itemMap=(Map<String,Object>)messageMap.get("Item"+i);
        	String msgType =(String)itemMap.get("MsgType");
        	String fromUserName=(String)itemMap.get("FromUserName");;
            Map<String,String> receiver=(Map<String,String>)itemMap.get("Receiver");
            if (msgType.equals("text")) {
                String content=(String)itemMap.get("Content");
                System.out.println("From "+fromUserName +" to "+receiver.get("Type")+" "+receiver.get("Id")+" ,Message content: "+content);
            }else if(msgType.equals("image")||msgType.equals("voice")||msgType.equals("video")){
            	String mediaId=(String)itemMap.get("MediaId");
            	System.out.println("From "+fromUserName +" to "+receiver.get("Type")+" "+receiver.get("Id")+" ,Media id: "+mediaId);
            }else if (msgType.equals("location")) {
               String locationX=(String)itemMap.get("Location_X");
               String locationY=(String)itemMap.get("Location_Y");
               String label=(String)itemMap.get("Label");
               System.out.println("From "+fromUserName +" to "+receiver.get("Type")+" "+receiver.get("Id")+" ,location: "+label+"("+locationX+","+locationY+")");
           }else if (msgType.equals("event")) {
        	   String event=(String)itemMap.get("Event");
        	   if("create_chat".equals(event)){
        		   Map<String,String> chatInfo=(Map<String,String>)itemMap.get("ChatInfo");
        		   String chatId=chatInfo.get("ChatId");
        		   String name=chatInfo.get("Name");
        		   String owner=chatInfo.get("Owner");
        		   String userList=chatInfo.get("UserList");
        		   System.out.println("Chat created: id "+chatId+" name "+name+" owner "+owner+" userlist "+userList);
        	   }else if("quit_chat".equals(event)){
        		   String chatId=(String)itemMap.get("ChatId");
        		   System.out.println("User "+fromUserName+" quit chat "+chatId);
        	   }else if("update_chat".equals(event)){
        		   String chatId=(String)itemMap.get("ChatId");
        		   String name=(String)itemMap.get("Name");
        		   String owner=(String)itemMap.get("Owner");
        		   String addUserList=(String)itemMap.get("AddUserList");
        		   String delUserList=(String)itemMap.get("DelUserList");
        		   System.out.println("Chat update: id "+chatId+" name "+name+" owner "+owner+" addUserlist "+addUserList+" delUserList "+delUserList);
        	   }
        	   
           }else {
        	   System.out.println(itemMap);
           }
        }
    }
    
    /*{FromUserName=hcz13825089890, 
    	ChatInfo={Name=HCZ;User1;陈国秋, Owner=hcz13825089890, ChatId=9223372036864775811, UserList=hcz13825089890|User1|cgq}, 
    	Event=create_chat, 
    	CreateTime=1465617245, 
    	MsgType=event}
     */
    private  Map<String, Object> parseXml(String  xml) {
        Map<String, Object> map = new HashMap<String, Object>();
      
        // 读取输入流
        Document document = null;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        // 得到xml根元素
        Element root = document.getRootElement();
        
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();
        // 遍历所有子节点
        for (Element e : elementList) {
        	String name;
        	int i=0;
        	if("Item".equals(e.getName())){
        		name=e.getName()+i;
        		i++;
        	}else
        		name=e.getName();
        	map.put(name, parseNode(e));
        	/*
        	
        	Object value=e.getText();
        	if(!e.elements().isEmpty()){
        		List<Element> subList = e.elements();
        		Map<String,String> subMap=new HashMap<String,String>();
        		for (Element se : subList) {
        			subMap.put(se.getName(), se.getText());
        		}
        		value=subMap;
        	}	
        	map.put(name,value);
        	*/
        }
        return map;
    }
    
    Object parseNode(Element node){
    	
    	if(node.elements().isEmpty())
    		return node.getText();
    	else{
    		Map<String, Object> map = new HashMap<String, Object>();
    		List<Element> elementList = node.elements();
    		for (Element e : elementList) {
    			map.put(e.getName(), parseNode(e));
    		}
    		return map;
    	}
    }
}