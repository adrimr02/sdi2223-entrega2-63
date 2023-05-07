package com.uniovi.sdi2223entrega2test63.pageobjects;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_UserPrivateView extends PO_NavView {

    static public void fillFormAddOffer(WebDriver driver, String namep,
                                        String pricep, String descriptionp, boolean featuredp) {
        //Rellenamos el campo de nombre
        WebElement title = driver.findElement( By.name("title"));
        title.click();
        title.clear();
        title.sendKeys(namep);
        //Rellenamos el campo de precio
        WebElement price = driver.findElement( By.name("price"));
        price.click();
        price.clear();
        price.sendKeys(pricep);
        WebElement details = driver.findElement(By.name("description"));
        details.click();
        details.clear();
        details.sendKeys(descriptionp);
        WebElement feature = driver.findElement(By.name("featured"));
        if (featuredp)
            feature.click();

        By save = By.cssSelector("button[type=submit]");
        driver.findElement(save).click();
    }

    public static void logout(WebDriver driver) {
        PO_UserPrivateView.clickOption(driver, "logout", "free",
                "/html/body/div/h2");
    }

    static public void loginToPrivateView(WebDriver driver, String email,
                                          String password) {
        // Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class",
                "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, email, password);

        PO_View.checkElementBy(driver, "text", email);
    }

    public static void navigateToMyOffers(WebDriver driver) {
        PO_View.checkElementBy(driver, "@href", "offers/my-offers").get(0).click();
        List<WebElement> result = PO_View.checkElementBy(driver, "free", "//h2[text()=\"Mis ofertas\"]");
        Assertions.assertEquals(1, result.size());
    }

    public static void navigateToSearchOffers(WebDriver driver) {
        PO_View.checkElementBy(driver, "@href", "shop").get(0).click();
        List<WebElement> result = PO_View.checkElementBy(driver, "free", "//h2[text()=\"Ofertas disponibles\"]");
        Assertions.assertEquals(1, result.size());
    }

    public static void navigateToBoughtOffers(WebDriver driver) {
        PO_View.checkElementBy(driver, "@href", "offers/bought").get(0).click();
        List<WebElement> result = PO_View.checkElementBy(driver, "free", "//h2[text()=\"Ofertas Compradas\"]");
        Assertions.assertEquals(1, result.size());
    }

    public static void navigateToNewOfferForm(WebDriver driver) {
        navigateToMyOffers(driver);
        PO_HomeView.clickOption(driver, "offers/new", "text",
                "Crear Oferta");

        List<WebElement> result = PO_View.checkElementBy(driver, "text", "Crear Oferta");
        Assertions.assertEquals(1, result.size());
    }

    public static double getCurrentUserWallet(WebDriver driver) {
        By element = By.xpath(
                "//*[@id=\"myNavbar\"]/ul[2]/li[1]/p[1]" );
        String money = driver.findElement(element).getText();
        String numString = money.replaceAll("[^\\d.]", "");
        return Double.parseDouble(numString);
    }


}
