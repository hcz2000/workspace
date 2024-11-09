package com.jams.fund;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.context.ApplicationContext;

public class DbDemo{

	public static void main(String[] args) {
		ApplicationContext ctx=new AnnotationConfigApplicationContext(DatabaseConfig.class);
	}
}
