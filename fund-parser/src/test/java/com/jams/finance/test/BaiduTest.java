package com.jams.finance.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;


public class BaiduTest {
	@Test
    public void searchTest() {
    	
		FirefoxOptions opts=new FirefoxOptions();
		opts.setCapability("pageLoadStrategy", "none");
		FirefoxDriver driver=new FirefoxDriver(opts);
		driver.get("https://www.baidu.com/");
		driver.manage().window().maximize();
		driver.findElement(By.cssSelector("#kw")).sendKeys("selenium");
		driver.findElement(By.cssSelector("#kw")).sendKeys(Keys.CONTROL,"a");
		driver.findElement(By.cssSelector("#kw")).sendKeys(Keys.CONTROL,"c");
		driver.findElement(By.cssSelector("#kw")).sendKeys(Keys.CONTROL,"v");
		driver.findElement(By.cssSelector("#su")).click();
   
    }
}
