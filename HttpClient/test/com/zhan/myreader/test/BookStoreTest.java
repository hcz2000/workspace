package com.zhan.myreader.test;

import java.util.ArrayList;
import java.util.List;

import com.zhan.myreader.common.URLCONST;
import com.zhan.myreader.entity.bookstore.Catalog;
import com.zhan.myreader.greendao.entity.Book;
import com.zhan.myreader.webapi.BookStoreApi;

public class BookStoreTest {
	public static void main(String[] args) {
		BookStoreApi.getBookTypeList(URLCONST.nameSpace_tianlai+"/sort.html", (Object o, int code) -> {
			List<Catalog> mBookTypes = (List<Catalog>) o;
			Catalog type = mBookTypes.get(0);
			getBookList(type);
		});
	}
	
	static private  void getBookList(Catalog type) {
		BookStoreApi.getBookRankList(type.getUrl(), (Object o, int code) -> {
			List<Book>bookList = (ArrayList<Book>) o;
			for(Book book: bookList) {
				System.out.println(book.getName()+":"+book.getChapterUrl()+":"+book.getAuthor());
				System.out.println("  "+book.getDesc());
			}
		});
	}
}