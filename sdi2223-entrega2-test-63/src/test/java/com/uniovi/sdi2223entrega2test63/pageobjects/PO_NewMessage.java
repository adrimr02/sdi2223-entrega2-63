package com.uniovi.sdi2223entrega2test63.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_NewMessage extends PO_NavView {

    static public void fillMessage(WebDriver driver, String message) {
        WebElement msg = driver.findElement(By.name("newMsg"));
        msg.click();
        msg.clear();
        msg.sendKeys(message);
        //Pulsar el boton de Alta.
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }
}
