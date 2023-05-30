package com.zhan.myreader.test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.zhan.myreader.callback.ResultCallback;
import com.zhan.myreader.greendao.entity.Book;
import com.zhan.myreader.greendao.entity.Chapter;
import com.zhan.myreader.util.StringHelper;
import com.zhan.myreader.webapi.BookApi;

public class BookFinder {
	private CountDownLatch countDownLatch;
	
	public BookFinder() {
		countDownLatch= new CountDownLatch(1);
	}
	
	private class MyCallback implements ResultCallback{
		Object result;
		String errMsg;
		int code;
		
		
		@Override
		public void onFinish(Object o, int code) {
			this.result=o;
			this.code=code;
			countDownLatch.countDown();
		}

		@Override
		public void onError(Exception e) {
			System.out.println(e);
			this.code=-1;
			this.errMsg=e.getLocalizedMessage();
			countDownLatch.countDown();
		}
		
	}
	
	public List<Book> getBooks(String key) {
		MyCallback myCallback=new MyCallback();
		BookApi.search(StringHelper.encode(key),myCallback);
		try {
			System.out.println("Wating for latch...");
			countDownLatch.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if(myCallback.code!=-1) {
			return (List<Book>)myCallback.result;
		}else {
			return null;
		}
	}

	public static void main(String[] args) {
		
		BookFinder bookFinder=new BookFinder();
		List<Book> books=bookFinder.getBooks("ÎÒµÄ1991");
		if(books!=null) {
				for(Book book: books) {
					System.out.println(book.getName());
					System.out.println(book.getDesc());
					System.out.println(book.getChapterUrl());
				}
				Book mybook=books.get(0);
			
				ChapterFinder chapterFinder=new ChapterFinder();
				List<Chapter> chapters=chapterFinder.getChapters(mybook);
				for(Chapter chapter: chapters) {
					System.out.println(chapter.getNumber()+"--"+chapter.getTitle()+"--"+chapter.getUrl());
				}
			}
	}
	
	

}
