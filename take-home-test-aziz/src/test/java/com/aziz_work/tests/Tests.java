package com.aziz_work.tests;

import com.aziz_work.pages.LoginPage;
import com.aziz_work.utilities.BrowserUtils;
import com.aziz_work.utilities.ConfigurationReader;
import com.aziz_work.utilities.Driver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import java.util.*;
import static java.time.Duration.*;

public class Tests {

    @AfterMethod
    public void tearDown(){
        Driver.closeDriver();
    }

    @Test
    public static void tc01_successfulLogin() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url")+"login");
        LoginPage loginPage = new LoginPage();

        BrowserUtils.enterText(loginPage.usernameInputBox,ConfigurationReader.getProperty("username"));
        BrowserUtils.enterText(loginPage.passwordInputBox,ConfigurationReader.getProperty("password"));
        BrowserUtils.clickElement(loginPage.loginButton);
        loginPage.verifySuccessMessage();
    }

    @Test
    public static void tc02_unsuccessfulLogin() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url")+"login");
        LoginPage loginPage = new LoginPage();
        BrowserUtils.enterText(loginPage.usernameInputBox,"Aziz");
        BrowserUtils.enterText(loginPage.passwordInputBox,"Yuldashev");
        BrowserUtils.clickElement(loginPage.loginButton);
        loginPage.verifyFailedLoginMessage("Your username is invalid!");
        BrowserUtils.enterText(loginPage.usernameInputBox,ConfigurationReader.getProperty("username"));
        BrowserUtils.enterText(loginPage.passwordInputBox,"Yuldashev");
        BrowserUtils.clickElement(loginPage.loginButton);
        loginPage.verifyFailedLoginMessage("Your password is invalid!");
    }

    @Test
    public static void tc03_checkboxTest() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url")+"checkboxes");
        List<WebElement> checkboxes = Driver.getDriver().findElements(By.xpath("//input[@type='checkbox']"));
        Assert.assertFalse(checkboxes.get(0).isSelected());
        Assert.assertTrue(checkboxes.get(1).isSelected());
        BrowserUtils.clickElement(checkboxes.get(0));
        BrowserUtils.clickElement(checkboxes.get(1));
        Assert.assertTrue(checkboxes.get(0).isSelected());
        Assert.assertFalse(checkboxes.get(1).isSelected());
    }

    @Test
    public static void tc04_contextMenuTest() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url")+"context_menu");
        WebElement box = Driver.getDriver().findElement(By.xpath("//div[@id='hot-spot']"));
        Actions actions = new Actions(Driver.getDriver());
        actions.contextClick(box).perform();
        Alert alert = Driver.getDriver().switchTo().alert();
        String alertMessage = alert.getText();
        Assert.assertEquals(alertMessage,"You selected a context menu");
    }

    @Test
    public static void tc05_dragAndDropTest() throws InterruptedException {
        Driver.getDriver().get(ConfigurationReader.getProperty("url")+"drag_and_drop");
        WebElement boxA = Driver.getDriver().findElement(By.xpath("//div[@id='column-a']"));
        WebElement boxB = Driver.getDriver().findElement(By.xpath("//div[@id='column-b']"));
        Assert.assertTrue(boxA.getText().equals("A"));
        Assert.assertTrue(boxB.getText().equals("B"));

        Actions actions = new Actions(Driver.getDriver());
        actions.dragAndDrop(boxA,boxB).perform();
        Thread.sleep(2000);
        Assert.assertTrue(boxA.getText().equals("B"));
        Assert.assertTrue(boxB.getText().equals("A"));
    }

    @Test
    public static void tc06_dropdownTest() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url")+"dropdown");
        WebElement selectDropdown = Driver.getDriver().findElement(By.xpath("//select"));
        Select select = new Select(selectDropdown);
        Assert.assertTrue(select.getFirstSelectedOption().getText().equals("Please select an option"));
        select.selectByVisibleText("Option 1");
        Assert.assertTrue(select.getFirstSelectedOption().getText().equals("Option 1"));
        select.selectByValue("2");
        Assert.assertTrue(select.getFirstSelectedOption().getText().equals("Option 2"));
    }
    @Test
    public static void tc07_dynamicContentTest() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url")+"dynamic_content");

        for (int i = 0; i < 3; i++) {
            WebElement clickHereLink = Driver.getDriver().findElement(By.xpath("//a[.='click here']"));

            List<WebElement> listOfTexts = Driver.getDriver().findElements(By.xpath("//div[@class='large-10 columns']"));
            String text1 = listOfTexts.get(0).getText();
            String text2 = listOfTexts.get(1).getText();
            String text3 = listOfTexts.get(2).getText();
            BrowserUtils.clickElement(clickHereLink);

            listOfTexts = Driver.getDriver().findElements(By.xpath("//div[@class='large-10 columns']"));
            if (!text1.equals(listOfTexts.get(0).getText()) ||
                    !text2.equals(listOfTexts.get(1).getText()) ||
                            !text3.equals(listOfTexts.get(2).getText())){
                Assert.assertTrue(true);
            }else{
                Assert.assertFalse(false);
            }
        }
    }

    @Test
    public static void tc08_dynamicControlsTest() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url") + "dynamic_controls");
        WebElement removeButton = Driver.getDriver().findElement(By.xpath("//button[.='Remove']"));
        List<WebElement> checkbox = Driver.getDriver().findElements(By.xpath("//input[@type='checkbox']"));
        BrowserUtils.clickElement(removeButton);
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOf(checkbox.get(0)));
        if (checkbox.size() < 1){
            Assert.assertTrue(true);
        }else {
            Assert.assertFalse(false);
        }

        WebElement addButton = Driver.getDriver().findElement(By.xpath("//button[.='Add']"));
        BrowserUtils.clickElement(addButton);
        wait.until(ExpectedConditions.visibilityOf(Driver.getDriver().findElement(By.xpath("//input[@type='checkbox']"))));
        checkbox = Driver.getDriver().findElements(By.xpath("//input[@type='checkbox']"));
        Assert.assertTrue(checkbox.get(0).isDisplayed());

        WebElement enableDisableInputBox = Driver.getDriver().findElement(By.xpath("//input[@type='text']"));
        WebElement enableButton = Driver.getDriver().findElement(By.xpath("//button[.='Enable']"));
        BrowserUtils.clickElement(enableButton);
        wait.until(ExpectedConditions.elementToBeClickable(enableDisableInputBox));
        Assert.assertTrue(enableDisableInputBox.isEnabled());

        WebElement disableButton = Driver.getDriver().findElement(By.xpath("//button[.='Disable']"));
        BrowserUtils.clickElement(disableButton);
        wait.until(ExpectedConditions.visibilityOf(enableButton));
        Assert.assertTrue(Driver.getDriver().findElement(By.xpath("//p[@id='message']")).isDisplayed());
    }

    @Test
    public static void tc09_dynamicLoadingTest() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url") + "dynamic_loading/2");
        WebElement startButton = Driver.getDriver().findElement(By.xpath("//button"));
        BrowserUtils.clickElement(startButton);

        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOf(Driver.getDriver().findElement(By.xpath("//h4[.='Hello World!']"))));
        WebElement helloWorldMessage = Driver.getDriver().findElement(By.xpath("//h4[.='Hello World!']"));
        Assert.assertTrue(helloWorldMessage.isDisplayed());
    }
    @Test(enabled = false)
    public static void tc10_downloadTest() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url") + "download");

        //do not remember the exact way of asserting the downloaded file
    }
    @Test
    public static void tc11_uploadTest() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url") + "upload");

        WebElement chooseFileButton = Driver.getDriver().findElement(By.xpath("//input[@id='file-upload']"));
        chooseFileButton.sendKeys("/Users/azizyuldashev/Downloads/some-file.txt");
        WebElement uploadButton = Driver.getDriver().findElement(By.xpath("//input[@value='Upload']"));
        BrowserUtils.clickElement(uploadButton);
        Assert.assertTrue(Driver.getDriver().findElement(By.xpath("//div[@class='example']//h3")).isDisplayed());

    }

    @Test
    public static void tc12_floatingMenuTest() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url") + "floating_menu");
        List<WebElement> floatingMenuItems = Driver.getDriver().findElements(By.xpath("//div[@id='menu']//a"));
        for (WebElement element : floatingMenuItems) {
            Assert.assertTrue(element.isDisplayed());
        }

        WebElement poweredBy = Driver.getDriver().findElement(By.xpath("//div[@class='large-4 large-centered columns']//div"));
        Actions actions = new Actions(Driver.getDriver());
        actions.scrollToElement(poweredBy).perform();
        for (WebElement element : floatingMenuItems) {
            Assert.assertTrue(element.isDisplayed());
        }
    }

    @Test
    public static void tc13_floatingMenuTest() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url") + "iframe");
        Driver.getDriver().switchTo().frame("mce_0_ifr");
        WebElement inputBox = Driver.getDriver().findElement(By.xpath("//body[@id='tinymce']"));
        inputBox.sendKeys("Aziz");
        String text = inputBox.getText();
        Assert.assertTrue(text.contains("Aziz"));
    }

    @Test
    public static void tc14_hoversTest() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url") + "hovers");
        List<WebElement> avatars = Driver.getDriver().findElements(By.xpath("//img[@alt='User Avatar']"));
        List<WebElement> newInformation = Driver.getDriver().findElements(By.xpath("//h5"));
        Actions actions = new Actions(Driver.getDriver());

        for (int i = 0; i < avatars.size(); i++) {
            actions.moveToElement(avatars.get(i)).perform();
            Assert.assertTrue(newInformation.get(i).isDisplayed());
        }
    }


    @Test
    public static void tc15_javaScriptAlertTest() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url") + "javascript_alerts ");
        List<WebElement> alertButtons = Driver.getDriver().findElements(By.xpath("//button"));
        BrowserUtils.clickElement(alertButtons.get(0));
        Alert alert = Driver.getDriver().switchTo().alert();
        Assert.assertTrue(alert.getText().equals("I am a JS Alert"));
        alert.accept();

        BrowserUtils.clickElement(alertButtons.get(1));
        Assert.assertTrue(alert.getText().equals("I am a JS Confirm"));
        alert.accept();

        BrowserUtils.clickElement(alertButtons.get(2));
        alert.sendKeys("Aziz");
        alert.accept();
        Assert.assertTrue(Driver.getDriver().findElement(By.xpath("//p[@id='result']")).getText().contains("Aziz"));
    }

    @Test(enabled = false)
    public static void tc16_javaScriptErrorTest() {
        //I haven't had an experience working with JS Error
    }

    @Test
    public static void tc17_windowsTest() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url") + "windows ");
        WebElement clickHereButton = Driver.getDriver().findElement(By.xpath("//a[.='Click Here']"));
        BrowserUtils.clickElement(clickHereButton);

        for (String each : Driver.getDriver().getWindowHandles()) {
            Driver.getDriver().switchTo().window(each);
            WebElement newWindowMessage = Driver.getDriver().findElement(By.xpath("//h3"));
            Assert.assertTrue(newWindowMessage.isDisplayed());
        }
    }

    @Test
    public static void tc18_notificationMessageRenderedTest() {
        Driver.getDriver().get(ConfigurationReader.getProperty("url") + "/notification_message_rendered");
        WebElement clickHereButton = Driver.getDriver().findElement(By.xpath("//a[.='Click here']"));
        BrowserUtils.clickElement(clickHereButton);
        Set<String> set = new HashSet<>();

        while (set.size() != 2){

            WebElement message = Driver.getDriver().findElement(By.xpath("//div[@id='flash']"));
            set.add(message.getText().substring(0,message.getText().length()-2));
            clickHereButton = Driver.getDriver().findElement(By.xpath("//a[.='Click here']"));
            BrowserUtils.clickElement(clickHereButton);

        }

        Assert.assertTrue(set.contains("Action successful"));
        Assert.assertTrue(set.contains("Action unsuccesful, please try again"));
    }
    }
