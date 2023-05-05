package com.uniovi.sdi2223entrega2test63.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_UserPrivateView extends PO_NavView {

    public static void logout(WebDriver driver) {
        PO_UserPrivateView.clickOption(driver, "logout", "free",
                "/html/body/div/h2");
    }
}
