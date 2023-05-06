package com.uniovi.sdi2223entrega2test63.pageobjects;

import com.uniovi.sdi2223entrega2test63.util.SeleniumUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_HomeView extends PO_NavView {
    static public void checkWelcomeToPage(WebDriver driver, int language) {
        //Esperamos a que se cargue el saludo de bienvenida en Español
        SeleniumUtils.waitLoadElementsBy(driver, "text", "Bienvenido a MyWallapop",
                getTimeout());
    }

}
