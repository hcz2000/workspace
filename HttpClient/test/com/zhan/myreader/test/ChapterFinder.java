package com.zhan.myreader.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.zhan.myreader.callback.ResultCallback;
import com.zhan.myreader.greendao.entity.Book;
import com.zhan.myreader.greendao.entity.Chapter;
import com.zhan.myreader.webapi.BookApi;

public class ChapterFinder {
	private CountDownLatch2 latch;
	
	public ChapterFinder() {
		latch= new CountDownLatch2(1);
	}
	
	private class MyCallback implements ResultCallback{
		ArrayList result=new ArrayList();
		String errMsg;
		int code;
		
		
		@Override
		public void onFinish(Object o, int code) {
			this.result.addAll((ArrayList)o);
			this.code=code;
			latch.countDown();
		}

		@Override
		public void onError(Exception e) {
			System.out.println(e);
			this.code=-1;
			this.errMsg=e.getLocalizedMessage();
			latch.countDown();
		}
		
	}
	
	public  List<Chapter> getChapters(Book book) {
		MyCallback myCallback=new MyCallback();
		BookApi.getBookChapters(book, myCallback);

		try {
			for(;;) {
				System.out.println("Wating for latch...");
				if(!latch.await(5,TimeUnit.SECONDS))
					break;
				latch.reset();
				System.out.println("latch released");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(myCallback.code!=-1) {
			return (List<Chapter>)myCallback.result;
		}else {
			return null;
		}
	}

}
