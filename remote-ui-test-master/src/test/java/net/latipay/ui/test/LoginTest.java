package net.latipay.ui.test;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import net.latipay.ui.test.common.DataUtils;
import net.latipay.ui.test.common.WebDriverUtils;
import net.latipay.ui.test.domain.TestConstants;

/**
 * @author jasonlu 2:15:49 PM
 */
public class LoginTest {

    @Test
    public void testLoginSuccess() throws Exception {
        // 获取WebDriver
        WebDriver chrome = WebDriverUtils.getWebDriver("chrome");
        try {
            WebDriverUtils.get(chrome, "https://merchant-staging.latipay.net/login");
            WebElement email = chrome.findElement(By.id("email"));
            email.sendKeys(TestConstants.AUTOTEST_NZD_EMAIL);
            WebElement password = chrome.findElement(By.id("password"));
            password.sendKeys("1234567a");
            WebElement login = chrome.findElement(By.className("lat-login-submit"));
            login.click();
            WebDriverUtils.pageLoadWait(chrome, 10);
            Thread.sleep(10000);
            WebElement greeting = chrome.findElements(By.className("lat-greeting")).get(0);
            Assert.assertEquals("Hi, wNGlRKMHpc", greeting.getText());
        } finally {
            WebDriverUtils.returnWebDriver(chrome);
        }
    }

    @Test
    public void testResetPassword() throws Exception {
        WebDriver chrome = WebDriverUtils.getWebDriver("chrome");
        try {
            WebDriverUtils.get(chrome, "https://merchant-staging.latipay.net/login");
            WebElement forgotten = chrome.findElement(By.xpath("/html/body/div[@id='root']/div/span/div[@class='fullscreen light']/div[@class='container lat-main-container']/div[@class='row']/div[@class='col-sm-4 col-sm-offset-4 col-xs-10 col-xs-offset-1']/form/div[@class='form-group'][3]/a[@class='btn btn-primary btn-sm lat-links']"));
            forgotten.click();
            WebElement email = chrome.findElement(By.id("email"));
            email.sendKeys(TestConstants.AUTOTEST_AUD_EMAIL);
            WebElement reset = chrome.findElement(By.tagName("button"));
            reset.click();
            WebElement sendMail = chrome.findElements(By.className("s-alert-box-inner")).get(0);
            Assert.assertEquals("Reset password email sent", sendMail.getText());
            Thread.sleep(10000);
            String nonce = DataUtils.getRedisKey(TestConstants.FORGET_PWD_KEY, TestConstants.AUTOTEST_AUD_EMAIL);
            WebDriverUtils.get(chrome, String.format("https://merchant-staging.latipay.net/setpwd/%s", nonce));
            WebElement password = chrome.findElement(By.id("password"));
            password.sendKeys("1234567a");
            WebElement password2 = chrome.findElement(By.id("password2"));
            password2.sendKeys("1234567a");
            WebElement submit = chrome.findElement(By.tagName("button"));
            submit.click();
            WebElement success = chrome.findElements(By.className("s-alert-box-inner")).get(0);
            Assert.assertEquals("Password updated", success.getText());
        } finally {
            WebDriverUtils.returnWebDriver(chrome);
        }
    }

    @Test
    public void testSignUp() {
        WebDriver chrome = WebDriverUtils.getWebDriver("chrome");
        try {
            WebDriverUtils.get(chrome, "https://merchant-staging.latipay.net/login");
            WebElement signup = chrome.findElement(By.xpath("/html/body/div[@id='root']/div/span/div[@class='fullscreen light']/div[@class='container lat-main-container']/div[@class='row']/div[@class='col-sm-4 col-sm-offset-4 col-xs-10 col-xs-offset-1']/form/div[@class='form-group'][4]/a[@class='btn btn-primary btn-sm lat-links']"));
            signup.click();
            // TODO

        } finally {
            WebDriverUtils.returnWebDriver(chrome);
        }
    }

}
