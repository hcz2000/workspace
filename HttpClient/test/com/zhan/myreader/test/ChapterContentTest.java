package com.zhan.myreader.test;
import com.zhan.myreader.callback.ResultCallback;
import com.zhan.myreader.greendao.entity.Chapter;
import com.zhan.myreader.webapi.BookApi;

public class ChapterContentTest  {

	public static void main(String[] args) {
		MyCallback myCallback=new MyCallback();
		Chapter chapter=new Chapter();
		chapter.setUrl("https://m.23sk.net/files/article/html/1/1288/26824549.html");
		
		BookApi.getChapterContent(chapter,myCallback);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(myCallback.getContent());
	
	}
	
	static class MyCallback implements ResultCallback {
		private String content;

		@Override
		public void onFinish(Object content, int code) {
			this.content=(String)content;
		}
		@Override
		public void onError(Exception e) {
			e.printStackTrace();
		}
			
		public String getContent(){
			return content;
		}

	}

}
