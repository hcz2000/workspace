package com.zhan.myreader.test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.zhan.myreader.callback.ResultCallback;
import com.zhan.myreader.greendao.entity.Book;
import com.zhan.myreader.greendao.entity.Chapter;
import com.zhan.myreader.util.StringHelper;
import com.zhan.myreader.webapi.BookApi;

public class BookCatalogLoader {
	private CountDownLatch countDownLatch;
	
	public BookCatalogLoader() {
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
		
		BookCatalogLoader bookFinder=new BookCatalogLoader();
		//List<Book> books=bookFinder.getBooks("我的1991");
		List<Book> books=bookFinder.getBooks("121057");
		if(books!=null) {
				for(Book book: books) {
					System.out.println(book.getName());
					System.out.println(book.getDesc());
					System.out.println(book.getChapterUrl());
				}
				Book mybook=books.get(0);
			
			 List<Chapter> chapters=BookApi.getBookChapters(mybook);
				for(Chapter chapter: chapters) {
					System.out.println(chapter.getNumber()+"--"+chapter.getTitle()+"--"+chapter.getUrl());
				}
			}
	}
	
	

}
