package com.uniovi.sdi2223entrega2test63.pageobjects;

import org.openqa.selenium.WebDriver;

public class PO_AdminPrivateView extends PO_NavView {

    public static void logout(WebDriver driver) {
        PO_AdminPrivateView.clickOption(driver, "logout", "free",
                "/html/body/div/h2");
    }
}
