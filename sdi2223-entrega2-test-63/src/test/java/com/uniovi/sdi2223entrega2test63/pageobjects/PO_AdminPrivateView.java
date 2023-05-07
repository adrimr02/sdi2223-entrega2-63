package com.uniovi.sdi2223entrega2test63.pageobjects;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_AdminPrivateView extends PO_NavView {

    public static void logout(WebDriver driver) {
        PO_AdminPrivateView.clickOption(driver, "logout", "free",
                "/html/body/div/h2");
    }

    public static void navigateToUsersPage(WebDriver driver, int page) {
        driver.get("http://localhost:8080/users/?page="+page);
    }

    public static void navigateToLogs(WebDriver driver){
        driver.get("http://localhost:8080/logs");
    }

    public static void navigateToLogsPage(WebDriver driver, int page) {
        driver.get("http://localhost:8080/logs/?page="+page);
    }
}
