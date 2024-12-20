package com.jams.finance;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.jams.finance.core.DatabaseConfig;


public class App {
	private static AnnotationConfigApplicationContext ctx = 
			new AnnotationConfigApplicationContext(DatabaseConfig.class,Task.class);


	public static Object getBean(String name) throws BeansException {
		return ctx.getBean(name);
	}
	
    public static void main(String[] args) {
    	Task task=new Task();
    	task.process();
    	
        Object monitor = new Object();
        synchronized (monitor) {
            try {
                monitor.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
