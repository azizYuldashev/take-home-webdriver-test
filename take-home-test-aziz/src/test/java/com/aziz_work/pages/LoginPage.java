package com.aziz_work.pages;

import com.aziz_work.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class LoginPage {

    public LoginPage(){
        PageFactory.initElements(Driver.getDriver(), this);
    }

    //page objects

    @FindBy(xpath = "//input[@name='username']")
    public WebElement usernameInputBox;

    @FindBy(xpath = "//input[@name='password']")
    public WebElement passwordInputBox;

    @FindBy(xpath = "//button[@class='radius']")
    public WebElement loginButton;

    @FindBy(xpath = "//div[@class='flash success']")
    public WebElement successMessage;

    @FindBy(xpath = "//div[@class='flash error']")
    public WebElement failedLoginMessage;

    //page methods
    public void verifySuccessMessage(){
        Assert.assertTrue(successMessage.getText().contains("You logged into a secure area!"));
    }
    public void verifyFailedLoginMessage(String text){
        Assert.assertTrue(failedLoginMessage.getText().contains(text));
    }



}
