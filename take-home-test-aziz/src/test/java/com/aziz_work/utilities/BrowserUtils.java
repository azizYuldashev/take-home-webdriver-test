package com.aziz_work.utilities;

import org.openqa.selenium.WebElement;

public class BrowserUtils {

    public static void enterText(WebElement inputBox, String text){
        try {
            inputBox.sendKeys(text);
        }catch (Exception e){
            System.out.println("Inout box not found");
        }
    }

    public static void clickElement(WebElement element){
        try {
            element.click();
        }catch (Exception e){
            System.out.println("Element not found");
        }
    }

}
