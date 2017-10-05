package net.latipay.ui.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import net.latipay.ui.test.common.WebDriverUtils;
import net.latipay.ui.test.domain.TestConstants;

public class SpotPayTest_NZD {

	   @Test
	    public void testLoginSuccess() throws Exception {
	        // 获取WebDriver
	        WebDriver chrome = WebDriverUtils.getWebDriver("chrome");
	       try {
	        //login SpotPay success
	            WebDriverUtils.get(chrome, "https://spotpay-staging.latipay.net/login");
	            WebElement email = chrome.findElement(By.id("email"));
	            email.sendKeys(TestConstants.AUTOTEST_NZD_EMAIL);
	            WebElement password = chrome.findElement(By.id("password"));
	            password.sendKeys("1234567a");
	             WebElement login = chrome.findElement(By.className("bpbpHD")); // Spotpay 中很多class name的命名不是很明了
	            login.click();
	            WebDriverUtils.pageLoadWait(chrome, 10);
	            Thread.sleep(10000);

	            
	            //Amount tab setting
	            WebElement amount_tab = chrome.findElements(By.className("cqTnUx")).get(0);
	            Assert.assertEquals("Amount to be paid", amount_tab.getText());
	            Select s1=new Select(chrome.findElement(By.id("wallet_id")));
	            Assert.assertEquals("NZD-UiIDOdPwJh", s1.getFirstSelectedOption().getText());//检查默认选项检查
	            WebElement amount_cur = chrome.findElements(By.xpath(".//*[@id='root']/div/div[2]/div/div[2]/form/div[2]/label/span")).get(0);
	            Assert.assertEquals("Transaction amount in NZD", amount_cur.getText());  
	  
	            WebElement amount = chrome.findElement(By.id("amount"));
	            amount.sendKeys("0.02");
	            WebElement refer = chrome.findElement(By.id("merchant_reference"));
	            refer.sendKeys("Spotpay_reference_NZD");
	            WebElement next1 = chrome.findElement(By.className("bpbpHD"));
	            next1.click();
	            WebDriverUtils.pageLoadWait(chrome, 10);
	            Thread.sleep(10000);

	            
	            //payment method selection _ wechat  
	            WebElement payment_tab = chrome.findElements(By.className("cqTnUx")).get(0);
	            Assert.assertEquals("Payment method", payment_tab.getText());
	            WebElement wechat = chrome.findElement(By.xpath(".//*[@id='root']/div/div[2]/div/div[2]/form/div[2]/button"));  // 或者使用xpath定位
	            wechat.click();
	            WebDriverUtils.pageLoadWait(chrome, 10);
	            Thread.sleep(10000);
	            
	            // QR scan page _wechat
	            WebElement w_qr_tab = chrome.findElements(By.className("cqTnUx")).get(0);
	            Assert.assertEquals("QR code to scan", w_qr_tab.getText());
	            WebElement w_pay_aud = chrome.findElements(By.className("bDHqcA")).get(0);  
	            Assert.assertEquals("$0.02 NZD", w_pay_aud.getText());
	            WebElement w_pay_cny = chrome.findElements(By.className("dsFqHj")).get(0);  // 另外这块如何根据汇率实时计算？ ？？如果是比较大的金额， 汇率计算后会有误差
	            Assert.assertEquals("CNY\n¥ 0.09", w_pay_cny.getText());  //获取值是换行显示的 
	           // ??? image 如何比较， wechat pay的图标??
	            
	            WebElement pre_s2 = chrome.findElement(By.partialLinkText("Previous Step"));
	            pre_s2.click();    //回退到前一页
	            WebDriverUtils.pageLoadWait(chrome, 10);
	            Thread.sleep(10000);
	            
	            //重现选择渠道 payment method selection _ Alipay  
	            payment_tab = chrome.findElements(By.className("cqTnUx")).get(0);
	            Assert.assertEquals("Payment method", payment_tab.getText());
	            WebElement alipay = chrome.findElement(By.xpath(".//*[@id='root']/div/div[2]/div/div[2]/form/div[1]/button"));
	            alipay.click();
	            WebDriverUtils.pageLoadWait(chrome, 10);
	            Thread.sleep(10000);
	            
	            // QR scan page _ Alipay
	            WebElement a_qr_tab = chrome.findElements(By.className("cqTnUx")).get(0);
	            Assert.assertEquals("QR code to scan", a_qr_tab.getText());
	            WebElement a_pay_aud = chrome.findElements(By.className("bDHqcA")).get(0); 
	            Assert.assertEquals("$0.02 NZD", a_pay_aud.getText());
	            WebElement a_pay_cny = chrome.findElements(By.className("dsFqHj")).get(0);  
	            Assert.assertEquals("CNY\n¥ 0.10", a_pay_cny.getText()); //获取值是换行显示的 
	           
	           //切换到Merchant端平台， 登陆
	            WebElement tranx_hist = chrome.findElement(By.partialLinkText("Transaction history"));
	            tranx_hist.click();
	            WebDriverUtils.pageLoadWait(chrome, 10);
	            Thread.sleep(10000);
	            Assert.assertEquals("Latipay Merchant Portal", chrome.getTitle()); // 比较打开页面的Title是否正确
	            WebElement m_email = chrome.findElement(By.id("email"));
	            m_email.sendKeys(TestConstants.AUTOTEST_NZD_EMAIL);
	            WebElement m_password = chrome.findElement(By.id("password"));
	            m_password.sendKeys("1234567a");
	            WebElement m_login = chrome.findElement(By.className("lat-login-submit"));
	            m_login.click();
	            WebDriverUtils.pageLoadWait(chrome, 10);
	            Thread.sleep(10000);
	            
	            //展开Processing 列表， 查看产生的新订单 (应该是两条)
	            WebElement show = chrome.findElement(By.xpath(".//*[@id='root']/div/span/div/div[2]/div/div[2]/div[1]/div/a"));
	            show.click();
	            
	            Date date = new Date();
	            WebElement tranx_date = chrome.findElement(By.xpath(".//*[@id='root']/div/span/div/div[2]/div/div[2]/div[2]/table/tbody/tr[1]/td[1]"));
	        	SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM",Locale.ENGLISH );
	        	String date_s = sdf.format(date);
	            Assert.assertEquals(date_s, tranx_date.getText());
	            
	            WebElement tranx_type = chrome.findElement(By.xpath(".//*[@id='root']/div/span/div/div[2]/div/div[2]/div[2]/table/tbody/tr[1]/td[2]"));
	            Assert.assertEquals("SpotPay", tranx_type.getText());      
	            // 如何比较获取的部分信息相同？？   transaction  ID 的比较， 包含选择的支付渠道 (如果可以判断了， 则检查两种渠道的transaction记录)
	            WebElement tranx_wal = chrome.findElement(By.xpath(".//*[@id='root']/div/span/div/div[2]/div/div[2]/div[2]/table/tbody/tr[1]/td[4]"));
	            Assert.assertEquals("NZD-UiIDOdPwJh", tranx_wal.getText());  
	            WebElement Tranx_Amount = chrome.findElement(By.xpath(".//*[@id='root']/div/span/div/div[2]/div/div[2]/div[2]/table/tbody/tr[1]/td[5]"));
	            Assert.assertEquals("$0.02 NZD", Tranx_Amount.getText());  
	            
	    } 
	       catch(Exception e) {
	    	e.printStackTrace();
	    }
	       finally {        
	            WebDriverUtils.returnWebDriver(chrome);
	    }

	    }
}
