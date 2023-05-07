package com.uniovi.sdi2223entrega2test63;

import com.uniovi.sdi2223entrega2test63.pageobjects.*;
import com.uniovi.sdi2223entrega2test63.util.MongoDB;
import com.uniovi.sdi2223entrega2test63.util.SeleniumUtils;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class
Sdi2223Entrega2TestApplicationTests {
    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";

    static String Geckodriver= "C:\\Users\\Daniel Alonso\\Desktop\\test\\PL-SDI-Sesion5-material\\geckodriver-v0.30.0-win64.exe";
    //static String Geckodriver = "E:\\ADRIAN\\Uniovi\\Curso 3\\SDI\\drivers\\geckodriver.exe";
    //static String Geckodriver = "C:\\Users\\larry\\Desktop\\UNI\\SDI\\PL-SDI-Sesio╠ün5-material\\geckodriver-v0.30.0-win64.exe";


    //static String Geckodriver = "C:\\Dev\\tools\\selenium\\geckodriver-v0.30.0-win64.exe";
    //static String PathFirefox = "/Applications/Firefox.app/Contents/MacOS/firefox-bin";
    //static String Geckodriver = "/Users/USUARIO/selenium/geckodriver-v0.30.0-macos";
    static WebDriver driver = getDriver(PathFirefox, Geckodriver);
    static String URL = "http://localhost:8080";

    public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckodriver);
        driver = new FirefoxDriver();
        return driver;
    }

    @BeforeEach
    public void setUp() {
        MongoDB m = new MongoDB();
        m.resetMongo();
        driver.navigate().to(URL);
    }

    //Después de cada prueba se borran las cookies del navegador
    @AfterEach
    public void tearDown() {
        driver.manage().deleteAllCookies();
    }

    //Antes de la primera prueba
    @BeforeAll
    static public void begin() {
    }

    //Al finalizar la última prueba
    @AfterAll
    static public void end() {
        //Cerramos el navegador al finalizar las pruebas
        driver.quit();
    }

    /*
     * ###################
     * Pruebas de registro de usuario
     * ###################
     */

    /**
     * Registro de Usuario con datos válidos
     */
    @Test
    @Order( 1 )
    void P1() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_SignUpView.fillForm(driver, "newuser1", "20", "newuser1@email.com",
                "10/12/2002", "newpass1", "newpass1");
        //Comprobamos que entramos en la sección privada y nos nuestra el texto a buscar
        List<WebElement> result = PO_View.checkElementBy(driver, "text", "Mis ofertas");
        Assertions.assertEquals("Mis ofertas", result.get(0).getText());
    }

    /**
     * Registro de Usuario con datos inválidos (campos vacíos)
     */
    @Test
    @Order(2)
    void P2() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Pulsamos el botón para enviar el formulario sin haberlo rellenado
        By boton = By.className("btn");
        driver.findElement(boton).click();
        List<WebElement> result = PO_SignUpView.checkElementBy(driver, "text", "Es obligatorio rellenar todos los campos." );
        Assertions.assertEquals(1, result.size());
    }


    /**
     * Registro de Usuario con datos inválidos (repetición de contraseña inválida)
     */
    @Test
    @Order(3)
    void P3() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_SignUpView.fillForm(driver, "newuser2", "20", "newuser2@email.com",
                "10/12/2002", "newpass2", "newpass1");
        //Comprobamos que entramos en la sección privada y nos nuestra el texto a buscar
        List<WebElement> result = PO_View.checkElementBy(driver, "text", "Las contraseñas no coinciden.");
        Assertions.assertEquals(1, result.size());
    }

    /**
     * Registro de Usuario con datos inválidos (email existente).
     */
    @Test
    @Order(4)
    void P4() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_SignUpView.fillForm(driver, "newuser3", "20", "user01@email.com",
                "10/12/2002", "newpass3", "newpass3");
        //Comprobamos que entramos en la sección privada y nos nuestra el texto a buscar
        List<WebElement> result = PO_View.checkElementBy(driver, "text", "El email ya está en uso.");
        Assertions.assertEquals(1, result.size());
    }

    /*
     * ###################
     * Pruebas de Login
     * ###################
     */

    /**
     * Inicio de sesión con datos válidos (administrador)
     */
    @Test
    @Order(5)
    void P5() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        //Comprobamos que entramos en la pagina privada del administrador
        List<WebElement> result = PO_View.checkElementBy(driver, "text", "Usuarios:");
        Assertions.assertEquals("Usuarios:", result.get(0).getText());
    }

    /**
     * Inicio de sesión con datos válidos (usuario estándar)
     */
    @Test
    @Order(6)
    void P6() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Comprobamos que entramos en la pagina privada del usuario
        List<WebElement> result = PO_View.checkElementBy(driver, "text", "Mis ofertas");
        Assertions.assertEquals("Mis ofertas", result.get(0).getText());
    }

    /**
     * Inicio de sesión con datos inválidos (usuario estándar, email existente, pero contraseña
     * incorrecta).
     */
    @Test
    @Order(7)
    void P7() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user02");
        List<WebElement> result = PO_LoginView.checkElementBy(driver, "text", "Email o contraseña inválidos.");
        Assertions.assertEquals(1, result.size());
    }

    /**
     * Inicio de sesión con datos inválidos (campo email o contraseña vacíos)
     */
    @Test
    @Order(8)
    void P8() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Pulsamos el botón para enviar el formulario sin haberlo rellenado
        By boton = By.className("btn");
        driver.findElement(boton).click();
        List<WebElement> result = PO_LoginView.checkElementBy(driver, "text", "Es obligatorio rellenar todos los campos.");
        Assertions.assertEquals(1, result.size());
    }

    /*
     * ###################
     * Pruebas de Logout
     * ###################
     */

    /**
     * Hacer click en la opción de salir de sesión y comprobar que se redirige a la página de inicio
     * de sesión (Login)
     */
    @Test
    @Order( 9 )
    void P9() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Comprobamos que entramos en la pagina privada del usuario
        List<WebElement> result = PO_View.checkElementBy(driver, "text", "Mis ofertas");
        Assertions.assertEquals(1, result.size());
        //Ahora nos desconectamos comprobamas que aparece el menu de login
        PO_UserPrivateView.logout(driver);
        List<WebElement> resultlogin = PO_View.checkElementBy(driver, "free", "/html/body/div/h2");
        Assertions.assertEquals("Iniciar Sesión", resultlogin.get(0).getText());
    }

    /**
     * Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado
     */
    @Test
    @Order( 10 )
    public void P10() {
        // Comprueba que no existe el dropdown con la informacion del usuario,
        // incluyendo el boton de logout
        SeleniumUtils.textIsNotPresentOnPage( driver, "Cerrar sesión");
    }

    /*
     + ###################
     * Pruebas de listado de usuarios
     * ###################
     */

    /**
     * Mostrar el listado de usuarios. Comprobar que se muestran todos los que existen en el
     * sistema, contabilizando al menos el número de usuarios.
     */
    @Test
    @Order( 11 )
    void P11() {
        // Nos logueamos
        PO_UserPrivateView.loginToPrivateView( driver, "admin@email.com", "admin" );
        //Contamos el número de filas
        List<WebElement> usersList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());
        //int expectedUsers=17; // Si se ejecutan todos
        int expectedUsers=16; // Si se ejecuta individualmente
        int users = usersList.size();

        for(int i=2; i<=4; i++){
            PO_AdminPrivateView.navigateToUsersPage(driver, i);
            usersList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                    PO_View.getTimeout());
            users+= usersList.size();
        }
        Assertions.assertEquals(expectedUsers, users);
    }

    /*
     + ###################
     * Pruebas de borrado de usuarios
     * ###################
     */

    /**
     *  Ir a la lista de usuarios, borrar el primer usuario de la lista, comprobar que la lista se actualiza
     * y dicho usuario desaparece.
     */
    @Test
    @Order( 12 )
    void P12() {
        // Nos logueamos
        PO_UserPrivateView.loginToPrivateView( driver, "admin@email.com", "admin" );
        // Capturamos el checkbox y lo seleccionamos
        WebElement checkbox = driver.findElement(By.xpath("(//input[@type='checkbox'])[1]"));
        checkbox.click();
        // Pulsamos el botón de eliminar
        By save = By.cssSelector("button[type=submit]");
        driver.findElement(save).click();

        SeleniumUtils.textIsNotPresentOnPage(driver,"user01@email,com");
        //SeleniumUtils.textIsNotPresentOnPage(driver,"newuser1@email,com");
    }

    /**
     *  Ir a la lista de usuarios, borrar el último usuario de la lista, comprobar que la lista se actualiza
     * y dicho usuario desaparece.
     */
    @Test
    @Order( 13 )
    void P13() {
        // Nos logueamos
        PO_UserPrivateView.loginToPrivateView( driver, "admin@email.com", "admin" );
        //Navegamos a la ultima pagina
        PO_AdminPrivateView.navigateToUsersPage(driver, 4);
        // Capturamos los checkbox y seleccionamos el ultimo
        List<WebElement> checkboxes = driver.findElements(By.xpath("//input[@type='checkbox']"));
        WebElement lastCheckbox = checkboxes.get(checkboxes.size() - 1);
        lastCheckbox.click();
        // Pulsamos el botón de eliminar
        By save = By.cssSelector("button[type=submit]");
        driver.findElement(save).click();

        SeleniumUtils.textIsNotPresentOnPage(driver,"user15@email,com");
        //SeleniumUtils.textIsNotPresentOnPage(driver,"newuser1@email,com");
    }

    /**
     *  Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se actualiza y dichos
     * usuarios desaparecen.
     */
    @Test
    @Order( 14 )
    void P14() {
        // Nos logueamos
        PO_UserPrivateView.loginToPrivateView( driver, "admin@email.com", "admin" );
        // Capturamos los checkbox y los seleccionamos
        List<WebElement> checkboxes = driver.findElements(By.xpath("//input[@type='checkbox']"));
        for (WebElement checkbox : checkboxes) {
            if (checkbox.getAttribute("value").equals("user02@email.com") ||
                    checkbox.getAttribute("value").equals("user03@email.com") ||
                        checkbox.getAttribute("value").equals("user04@email.com")) {
                checkbox.click();
            }
        }
        // Pulsamos el botón de eliminar
        By save = By.cssSelector("button[type=submit]");
        driver.findElement(save).click();

        SeleniumUtils.textIsNotPresentOnPage(driver,"user02@email,com");
        SeleniumUtils.textIsNotPresentOnPage(driver,"user03@email,com");
        SeleniumUtils.textIsNotPresentOnPage(driver,"user04@email,com");
    }

    /*
     + ###################
     * Pruebas de crear ofertas
     * ###################
     */

    /**
     * Ir al formulario de alta de oferta, rellenarla con datos válidos y pulsar el botón Submit.
     * Comprobar que la oferta sale en el listado de ofertas de dicho usuario.
     */
    @Test
    @Order( 16 )
    void P16() {
        // Nos logueamos
        PO_UserPrivateView.loginToPrivateView( driver, "user01@email.com", "user01" );

        // Vamos a la pagina de crear oferta
        PO_UserPrivateView.navigateToNewOfferForm(driver);

        // Rellenamos el formulario de oferta
        String checkText = "Coche";
        PO_UserPrivateView.fillFormAddOffer(driver, checkText, "7800", "Un buen coche con pocos " +
                "kilometros", false);

        // Comprobamos que la oferta aparece en la lista
        List<WebElement> elements = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, elements.get(0).getText());

        //Ahora nos desconectamos y comprobamos que aparece el menú de iniciar sesion
        PO_UserPrivateView.logout( driver );

    }

    /**
     * Ir al formulario de alta de oferta, rellenarla con datos inválidos (campo título vacío y precio
     * en negativo) y pulsar el botón Submit. Comprobar que se muestra el mensaje de campo inválido.
     */
    @Test
    @Order( 17 )
    void P17() {
        // Nos logueamos
        PO_UserPrivateView.loginToPrivateView( driver, "user01@email.com", "user01" );

        // Vamos a la pagina de crear oferta
        PO_UserPrivateView.navigateToNewOfferForm(driver);
        // Rellenamos el formulario de oferta
        PO_UserPrivateView.fillFormAddOffer(driver, "", "-2100", "Un buen coche con pocos " +
                "kilometros", false);

        // Seguimos en la pagina de crear oferta
        List<WebElement> result = PO_UserPrivateView.checkElementBy(driver, "text", "Crear Oferta");
        Assertions.assertEquals("Crear Oferta" , result.get(0).getText());

        // Aparecen los errores
        result = PO_UserPrivateView.checkElementBy(driver, "text", "Debes incluir, al menos, el título y el precio.");
        Assertions.assertEquals(1, result.size());

    }

    /*
     + ###################
     * Pruebas de listar ofertas creadas
     * ###################
     */

    /**
     * Mostrar el listado de ofertas para dicho usuario y comprobar que se muestran todas las que
     * existen para este usuario.
     */
    @Test
    @Order( 18 )
    void P18() {
        // Nos logueamos
        PO_UserPrivateView.loginToPrivateView( driver, "user02@email.com", "user02" );

        // Vamos a la lista de ofertas del usuario
        PO_UserPrivateView.navigateToMyOffers(driver);

        // Lista de ofertas generadas al iniciar la aplicación
        List<String> offerTitles = new ArrayList<String>() {
            {
                add( "Silla" );
                add( "Cuadro pequeño" );
            }
        };

        // Comprobamos que las ofertas aparecen en la lista
        for (String title : offerTitles) {
            List<WebElement> elements = PO_View.checkElementBy(driver, "text", title);
            Assertions.assertEquals(title, elements.get(0).getText());
        }

    }
    
    /*
     * ###################
     * Pruebas de eliminar ofertas
     * ###################
     */

    /**
     * Ir a la lista de ofertas, borrar la primera oferta de la lista, comprobar que la lista se actualiza
     * y que la oferta desaparece.
     */
    @Test
    @Order( 19 )
    void P19() {
        // Nos logueamos
        PO_UserPrivateView.loginToPrivateView( driver, "user02@email.com", "user02" );

        // Vamos a la lista de ofertas del usuario
        PO_UserPrivateView.navigateToMyOffers(driver);

        // Guardamos el título de la primera oferta de la lista
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//table/tbody/tr[1]/td[1]");
        String title = elements.get(0).getText();

        // Hacemos click en eliminar la primera
        elements = PO_UserPrivateView.checkElementBy(driver, "free", "//table/tbody/tr[1]/td[7]/a");
        elements.get(0).click();

        // Comprobamos que ya no podemos encontrar el titulo de la oferta en la página
        SeleniumUtils.textIsNotPresentOnPage( driver, title );
    }

    /**
     * Inicia sesión, entra al listado de ofertas publicadas por el usuario,
     * borra la ultima y comprueba que ya no aparece.
     */
    @Test
    @Order( 20 )
    void P20() {
        // Nos logueamos
        PO_UserPrivateView.loginToPrivateView( driver, "user02@email.com", "user02" );

        // Vamos a la lista de ofertas del usuario
        PO_UserPrivateView.navigateToMyOffers(driver);

        // Guardamos el título de la ultima oferta de la lista
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//table/tbody/tr[last()]/td[1]");
        String title = elements.get(0).getText();

        // Hacemos click en eliminar la ultima
        elements = PO_UserPrivateView.checkElementBy(driver, "free", "//table/tbody/tr[last()]/td[7]/a");
        elements.get(0).click();

        // Comprobamos que ya no podemos encontrar el titulo de la oferta en la página
        SeleniumUtils.textIsNotPresentOnPage( driver, title );
    }

    /**
     * Ir a la lista de ofertas, borrar una oferta propia que ha sido vendida, comprobar que la
     * oferta no se borra.
     */
    @Test
    @Order( 22 )
    void P22() {
        // Nos logueamos
        PO_UserPrivateView.loginToPrivateView( driver, "user02@email.com",
                "user02" );

        // Vamos a la lista de ofertas del usuario
        PO_UserPrivateView.navigateToMyOffers(driver);

        // Guardamos la primera oferta que aparezca como vendida
        WebElement offer = PO_View.checkElementBy(driver, "free",
                "//table/tbody/tr[td[contains(text(), 'Vendida')]]").get(0);
        String title = offer.findElement(By.xpath("td")).getText();

        // Hacemos click en eliminar
        By delete = By.xpath("td/a[contains(text(), 'Eliminar')]");
        offer.findElement(delete).click();

        // Comprobamos que el titulo esta presente y que aparece el mensaje de error
        SeleniumUtils.textIsPresentOnPage( driver, title );
        SeleniumUtils.textIsPresentOnPage( driver, "Esta oferta ya fue vendida. No puedes eliminarla." );

    }

    /*
     + ###################
     * Pruebas de buscar ofertas
     * ###################
     */

    /**
     * Hacer una búsqueda con el campo vacío y comprobar que se muestra la página que
     * corresponde con el listado de las ofertas existentes en el sistema.
     */
    @Test
    @Order( 23 )
    void P23() {
        // Nos logueamos
        PO_UserPrivateView.loginToPrivateView( driver, "user01@email.com",
                "user01" );

        // Vamos a la lista de buscar ofertas
        PO_UserPrivateView.navigateToSearchOffers(driver);

        // Comprobamos que aparecen las 5 ofertas que deben aparecer por página
        List<WebElement> markList = SeleniumUtils.waitLoadElementsBy(driver,
                "free", "//*[@id=\"offersList\"]/tbody/tr",
                PO_View.getTimeout());
        Assertions.assertEquals(5, markList.size());

    }

    /**
     * Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar que se
     * muestra la página que corresponde, con la lista de ofertas vacía.
     */
    @Test
    @Order( 24 )
    void P24() {
        // Nos logueamos
        PO_UserPrivateView.loginToPrivateView( driver, "user01@email.com",
                "user01" );

        // Vamos a la lista de buscar ofertas
        PO_UserPrivateView.navigateToSearchOffers(driver);
        WebElement searchField = driver.findElement( By.name("search"));
        searchField.click();
        searchField.clear();
        searchField.sendKeys("asdfasd");
        By search = By.className("btn");
        driver.findElement(search).click();

        // Comprobamos que aparece el mensaje de lista vacia
        SeleniumUtils.textIsPresentOnPage(driver, "No hay ofertas");
    }

    /**
     * Hacer una búsqueda escribiendo en el campo un texto en minúscula o mayúscula y
     * comprobar que se muestra la página que corresponde, con la lista de ofertas que contengan dicho
     * texto, independientemente que el título esté almacenado en minúsculas o mayúscula.
     */
    @Test
    @Order( 25 )
    void P25() {
        // Nos logueamos
        PO_UserPrivateView.loginToPrivateView( driver, "user01@email.com",
                "user01" );

        // Vamos a la lista de buscar ofertas
        PO_UserPrivateView.navigateToSearchOffers(driver);
        WebElement searchField = driver.findElement( By.name("search"));
        searchField.click();
        searchField.clear();
        searchField.sendKeys("ALF");
        By search = By.className("btn");
        driver.findElement(search).click();

        // Comprobamos que aparecen las 5 ofertas que deben aparecer por página
        List<WebElement> markList = SeleniumUtils.waitLoadElementsBy(driver,
                "free", "//*[@id=\"offersList\"]/tbody/tr",
                PO_View.getTimeout());
        Assertions.assertEquals(2, markList.size());
    }

    /*
     + ###################
     * Pruebas de comprar ofertas
     * ###################
     */

    /**
     * Sobre una búsqueda determinada (a elección de desarrollador), comprar una oferta que
     * deja un saldo positivo en el contador del comprobador. Y comprobar que el contador se actualiza
     * correctamente en la vista del comprador
     */
    @Test
    @Order( 26 )
    void P26() {
        // Nos logueamos con un usuario que tiene 100€ en su cartera
        PO_UserPrivateView.loginToPrivateView( driver, "user01@email.com",
                "user01" );

        // Vamos a la lista de buscar ofertas
        PO_UserPrivateView.navigateToSearchOffers(driver);
        WebElement searchField = driver.findElement( By.name("search"));
        searchField.click();
        searchField.clear();
        searchField.sendKeys("café"); // Cuesta 50€
        By search = By.className("btn");
        driver.findElement(search).click();

        // Guardamos el dinero del usuario antes de comprar
        double initialMoney = PO_UserPrivateView.getCurrentUserWallet(driver);

        // Hacemos click en comprar
        double price = Double.parseDouble(PO_UserPrivateView
                .checkElementBy(driver, "free", "//*[@id=\"offersList\"]/tbody/tr/td[4]").get(0)
                .getText().replaceAll("[^0-9.]", ""));

        List<WebElement> result = PO_UserPrivateView.checkElementBy(driver, "free",
                "//*[@id=\"offersList\"]/tbody/tr/td[5]/a" );
        result.get( 0 ).click();

        // Comprobamos que se ha restado el dinero de la cartera del usuario
        Assertions.assertEquals(initialMoney - price,
                PO_UserPrivateView.getCurrentUserWallet( driver ));

        SeleniumUtils.textIsPresentOnPage(driver, "Oferta comprada");
    }

    /**
     * Sobre una búsqueda determinada (a elección de desarrollador), comprar una oferta que
     * deja un saldo 0 en el contador del comprobador. Y comprobar que el contador se actualiza
     * correctamente en la vista del comprador.
     */
    @Test
    @Order( 27 )
    void P27() {
        // Nos logueamos con un usuario que tiene 100€ en su cartera
        PO_UserPrivateView.loginToPrivateView( driver, "user03@email.com",
                "user03" );

        // Vamos a la lista de buscar ofertas
        PO_UserPrivateView.navigateToSearchOffers(driver);
        WebElement searchField = driver.findElement( By.name("search"));
        searchField.click();
        searchField.clear();
        searchField.sendKeys("Espejo"); // Cuesta 100€
        By search = By.className("btn");
        driver.findElement(search).click();

        // Click en comprar
        List<WebElement> result = PO_UserPrivateView.checkElementBy(driver, "free",
                "//*[@id=\"offersList\"]/tbody/tr/td[5]/a" );
        result.get( 0 ).click();

        // Comprobamos que se ha restado el dinero de la cartera del usuario
        Assertions.assertEquals(0,
                PO_UserPrivateView.getCurrentUserWallet( driver ));

        SeleniumUtils.textIsPresentOnPage(driver, "Oferta comprada");
    }

    /**
     * Sobre una búsqueda determinada (a elección de desarrollador), intentar comprar una oferta
     * que esté por encima de saldo disponible del comprador. Y comprobar que se muestra el mensaje
     * de saldo no suficiente.
     */
    @Test
    @Order( 28 )
    void P28() {
        // Nos logueamos con un usuario que tiene 100€ en su cartera
        PO_UserPrivateView.loginToPrivateView( driver, "user01@email.com",
                "user01" );

        // Vamos a la lista de buscar ofertas
        PO_UserPrivateView.navigateToSearchOffers(driver);
        WebElement searchField = driver.findElement( By.name("search"));
        searchField.click();
        searchField.clear();
        searchField.sendKeys("persa"); // Cuesta 50€
        By search = By.className("btn");
        driver.findElement(search).click();

        // Guardamos el dinero del usuario antes de comprar
        double initialMoney = PO_UserPrivateView.getCurrentUserWallet(driver);

        List<WebElement> result = PO_UserPrivateView.checkElementBy(driver, "free",
                "//*[@id=\"offersList\"]/tbody/tr/td[5]/a" );
        result.get( 0 ).click();

        // Comprobamos que no ha cambiado el dinero de la cartera del usuario
        Assertions.assertEquals(initialMoney,
                PO_UserPrivateView.getCurrentUserWallet( driver ));

        SeleniumUtils.textIsPresentOnPage(driver, "No tienes suficiente dinero.");
    }

    /*
     * ###################
     * Pruebas de listado de ofertas compradas
     * ###################
     */
     
     /**
     * Ir a la opción de ofertas compradas del usuario y mostrar la lista. Comprobar que aparecen
     * las ofertas que deben aparecer.
     */
    @Test
    @Order( 29 )
    void P29() {
        // Nos logueamos con un usuario que ha comprado una oferta, en este
        // caso, una television vendida por user02@email.com
        PO_UserPrivateView.loginToPrivateView( driver, "user09@email.com",
                "user09" );

        // Vamos a la lista de ofertas compradas
        PO_UserPrivateView.navigateToBoughtOffers(driver);

        // Comprobamos que la oferta de la televisión esta en la lista
        List<WebElement> elements = PO_UserPrivateView.checkElementBy( driver,
                "free", "//*[text()='Mesa']");
        Assertions.assertTrue( elements.size() >= 1 );
    }

    /*
     * ###################
     * Pruebas de destacar ofertas
     * ###################
     */

    /**
     * Al crear una oferta, marcar dicha oferta como destacada y a continuación comprobar: i)
     * que aparece en el listado de ofertas destacadas para los usuarios y que el saldo del usuario se
     * actualiza adecuadamente en la vista del ofertante (comprobar saldo antes y después, que deberá
     * diferir en 20€).
     */
    @Test
    @Order( 30 )
    void P30() {
        // Nos logueamos con un usuario que tenga mas de 20€ en su cartera
        PO_UserPrivateView.loginToPrivateView( driver, "user04@email.com",
                "user04" );
        // Vamos a la pagina de crear oferta
        PO_UserPrivateView.navigateToNewOfferForm(driver);
        String offerName = "Nueva oferta 1";
        double initialMoney = PO_UserPrivateView.getCurrentUserWallet(driver);
        PO_UserPrivateView.fillFormAddOffer(driver, offerName, "40", "esa oferta aparecera en la pestaña de destacados", true);
        PO_UserPrivateView.navigateToMyOffers(driver);

        // Comprobamos que la oferta aparece como destacada en la lista de ofertas del usuario
        WebElement offer = PO_View.checkElementBy(driver, "free",
                "//table/tbody/tr[td[contains(text(), '"+offerName+"')]]").get(0);
        List<WebElement> featured = offer.findElements(By.xpath("//*[contains(text(), 'Oferta destacada')]"));
        Assertions.assertEquals(1, featured.size());
        // Comprobamos que se han restado los 20 € de la cartera del usuario
        Assertions.assertEquals(initialMoney - 20, PO_UserPrivateView.getCurrentUserWallet(driver));

        // Comprobamos que la oferta aparece en la seccion de destacadas
        PO_UserPrivateView.navigateToSearchOffers(driver);
        List<WebElement> result = PO_UserPrivateView.checkElementBy(driver, "free",
                "//*[@id=\"featured\"]/tbody/tr[td[contains(text(), '"+offerName+"')]]" );
        Assertions.assertEquals(1, result.size());
    }

    /**
     *  Sobre el listado de ofertas de un usuario con más de 20 euros de saldo, pinchar en el enlace
     * Destacada y a continuación comprobar: i) que aparece en el listado de ofertas destacadas para los
     * usuarios y que el saldo del usuario se actualiza adecuadamente en la vista del ofertante (comprobar
     * saldo antes y después, que deberá diferir en 20€ ).
     */
    @Test
    @Order( 31 )
    void P31() {
        // Nos logueamos con un usuario que tenga mas de 20€ en su cartera
        PO_UserPrivateView.loginToPrivateView( driver, "user04@email.com",
                "user04" );

        // Escogemos una oferta no destacada de la lista de ofertas del usuario y pulsamos en destacar
        double initialMoney = PO_UserPrivateView.getCurrentUserWallet(driver);
        WebElement offer = PO_View.checkElementBy(driver, "free",
                "//table/tbody/tr[td[a[contains(text(), 'Destacar')]]]").get(0);
        String offerName = offer.findElement(By.xpath("td")).getText();
        WebElement featureBtn = offer.findElement(By.xpath("//*[contains(text(), 'Destacar')]"));
        featureBtn.click();

        // Comprobamos que se han restado los 20 € de la cartera del usuario
        Assertions.assertEquals(initialMoney - 20, PO_UserPrivateView.getCurrentUserWallet(driver));

        // Comprobamos que la oferta aparece en la seccion de destacadas
        PO_UserPrivateView.navigateToSearchOffers(driver);
        List<WebElement> result = PO_UserPrivateView.checkElementBy(driver, "free",
                "//*[@id=\"featured\"]/tbody/tr[td[contains(text(), '"+offerName+"')]]" );
        Assertions.assertEquals(1, result.size());
    }

    /**
     * Sobre el listado de ofertas de un usuario con menos de 20 euros de saldo, pinchar en el
     * enlace Destacada y a continuación comprobar que se muestra el mensaje de saldo no suficiente
     */
    @Test
    @Order( 32 )
    void P32() {
        // Nos logueamos con un usuario que tenga mas de 20€ en su cartera
        PO_UserPrivateView.loginToPrivateView( driver, "user07@email.com",
                "user07" );

        // Escogemos una oferta no destacada de la lista de ofertas del usuario y pulsamos en destacar
        double initialMoney = PO_UserPrivateView.getCurrentUserWallet(driver);
        WebElement offer = PO_View.checkElementBy(driver, "free",
                "//table/tbody/tr[td[a[contains(text(), 'Destacar')]]]").get(0);
        String offerName = offer.findElement(By.xpath("td")).getText();
        WebElement featureBtn = offer.findElement(By.xpath("//*[contains(text(), 'Destacar')]"));
        featureBtn.click();

        // Comprobamos que no se ha restado dinero de la cartera del usuario
        Assertions.assertEquals(initialMoney, PO_UserPrivateView.getCurrentUserWallet(driver));

        // Comprobamos que aparece el error
        SeleniumUtils.textIsPresentOnPage(driver, "No tienes suficiente dinero.");
    }
    
    /*
     + ###################
     * Pruebas de seguridad y auditoría
     * ###################
     */
    
    /**
     *  Intentar acceder sin estar autenticado a la opción de listado de usuarios. Se deberá volver
     * al formulario de login.
     */
    @Test
    @Order( 33 )
    void P33() {
        driver.get("http://localhost:8080/users");
        SeleniumUtils.textIsPresentOnPage(driver,"Iniciar Sesión");
    }

    /**
     *  Intentar acceder sin estar autenticado a la opción de listado de usuarios. Se deberá volver
     * al formulario de login.
     */
    @Test
    @Order( 34 )
    void P34() {
        driver.get("http://localhost:8080/api/conversations");
        SeleniumUtils.textIsPresentOnPage(driver,"\"authorized\":false");
    }

    /**
     *   Estando autenticado como usuario estándar intentar acceder a una opción disponible solo
     * para usuarios administradores. Se deberá indicar un
     * mensaje de acción prohibida
     */
    @Test
    @Order( 35 )
    void P35() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Intentamos navegar a la sección de logs
        driver.get("http://localhost:8080/logs");
        // Como no esta permitido se redirige a la seccion de inicio del tipo de usuario, en este caso, mis ofertas
        List<WebElement> result = PO_SignUpView.checkElementBy(driver, "text", "Mis ofertas" );
        Assertions.assertEquals(1, result.size());
    }

    /**
     *  Estando autenticado como usuario administrador visualizar todos los logs generados en
     * una serie de interacciones. Esta prueba deberá generar al menos dos interacciones de cada tipo y
     * comprobar que el listado incluye los logs correspondientes.
     */
    @Test
    @Order( 36 )
    void P36() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_SignUpView.fillForm(driver, "user20", "20", "user20@email.com",
                "10/12/2002", "user20", "user20");
        //Hacemos logout
        PO_UserPrivateView.logout(driver);
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_SignUpView.fillForm(driver, "user21", "21", "user21@email.com",
                "10/12/2002", "user21", "user21");
        //Hacemos logout
        PO_UserPrivateView.logout(driver);
        //Rellenamos el formulario de logeo de forma incorrecta dos veces
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user02");
        PO_LoginView.fillLoginForm(driver, "user02@email.com", "user01");
        //Rellenamos el formulario de logeo de forma correcta
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Hacemos logout
        PO_UserPrivateView.logout(driver);
        // Nos logueamos como administrador
        PO_UserPrivateView.loginToPrivateView( driver, "admin@email.com", "admin" );
        //Navegamos a la pagina de logs
        PO_AdminPrivateView.navigateToLogs(driver);
        List<WebElement> logsList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());
        int expectedLogs=30;
        int logs=logsList.size();
        for(int i=2; i<=3; i++){
            PO_AdminPrivateView.navigateToLogsPage(driver, i);
            logsList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                    PO_View.getTimeout());
            logs+= logsList.size();
        }
        Assertions.assertEquals(expectedLogs, logs);
    }

    /**
     *   Estando autenticado como usuario administrador, ir a visualización de logs, pulsar el
     * botón/enlace borrar logs y comprobar que se eliminan los logs de la base de datos.
     */
    @Test
    @Order( 37 )
    void P37() {
        // Nos logueamos
        PO_UserPrivateView.loginToPrivateView( driver, "admin@email.com", "admin" );
        //Navegamos a la pagina de logs
        PO_AdminPrivateView.navigateToLogs(driver);
        // Pulsamos el botón de eliminar
        By save = By.cssSelector("button[type=submit]");
        driver.findElement(save).click();

        List<WebElement> logsList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());

        Assertions.assertEquals(1, logsList.size());
    }

    /* Ejemplos de pruebas de llamada a una API-REST */
    /* ---- Probamos a obtener lista de canciones sin token ---- */
    /*
    @Test
    @Order(37)
    public void PR11() {
        final String RestAssuredURL = "http://localhost:8081/api/v1.0/songs";
        Response response = RestAssured.get(RestAssuredURL);
        Assertions.assertEquals(403, response.getStatusCode());
    }
    */

    /*
     * ###################
     * Pruebas de login RestAPI
     * ###################
     */

    /**
     * Inicio de sesión con datos válidos.
     */
    @Test
    @Order( 38 )
    public void P38() {
        final String RestAssuredURL = URL + "/api/users/login";
        //2. Preparamos el parámetro en formato JSON
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "user01@email.com");
        requestParams.put("password", "user01");
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());
        //3. Hacemos la petición
        Response response = request.post(RestAssuredURL);
        //4. Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, response.getStatusCode());
        ResponseBody body = response.getBody();
        Assertions.assertTrue(body.asString().contains("token"));
    }

    /**
     * Inicio de sesión con datos inválidos (email existente, pero contraseña incorrecta).
     */
    @Test
    @Order( 39 )
    public void P39() {
        final String RestAssuredURL = URL + "/api/users/login";
        //2. Preparamos el parámetro en formato JSON
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "user01@email.com");
        requestParams.put("password", "user02");
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());
        //3. Hacemos la petición
        Response response = request.post(RestAssuredURL);
        //4. Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(400, response.getStatusCode());
        ResponseBody body = response.getBody();
        Assertions.assertTrue(body.asString().contains("Email o contraseña incorrectos"));
    }

    /**
     * Inicio de sesión con datos inválidos (campo email o contraseña vacíos).
     */
    @Test
    @Order( 40 )
    public void P40() {
        final String RestAssuredURL = URL + "/api/users/login";
        //2. Preparamos el parámetro en formato JSON
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "");
        requestParams.put("password", "");
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());
        //3. Hacemos la petición
        Response response = request.post(RestAssuredURL);
        //4. Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(400, response.getStatusCode());
        ResponseBody body = response.getBody();
        Assertions.assertTrue(body.asString().contains("Falta email o contraseña"));
    }

    /**
     * Enviar mensaje a una oferta.
     */
    @Test
    @Order(42)
    void P42() {

        //1. Nos registramos exitosamente  (S1)
        final String RestAssuredURL1 = URL + "/api/users/login";
        RequestSpecification request1 = RestAssured.given();
        JSONObject requestParams1 = new JSONObject();
        requestParams1.put("email", "user01@email.com");
        requestParams1.put("password", "user01");
        request1.header("Content-Type", "application/json");
        request1.body(requestParams1.toJSONString());
        Response response1 = request1.post(RestAssuredURL1);
        String token = response1.jsonPath().getString("data.token");


        //2. Escribimos un nuevo mensaje a una oferta (S3)
        final String RestAssuredURL2 = URL + "/api/new/conversation/message";
        RequestSpecification request2 = RestAssured.given();
        JSONObject requestParams2 = new JSONObject();
        requestParams2.put("offerID", "IDfalso");
        requestParams2.put("offerTitle", "Titulo");
        requestParams2.put("offerSeller", "user02@email.com");
        requestParams2.put("content", "Hola");
        request2.header("Content-Type", "application/json");
        request2.header("token", token);
        request2.body(requestParams2.toJSONString());
        Response response2 = request2.post(RestAssuredURL2);
        String idConver = response2.getBody().jsonPath().getString("messages.conversation");


        //3. Comprobamos que el mensaje se ha registrado y se muestra (S4)
        final String RestAssuredURL3 = URL + "/api/conversations/" + idConver;
        RequestSpecification request3 = RestAssured.given();
        request3.header("Content-Type", "application/json");
        request3.header("token", token);
        Response response3 = request3.get(RestAssuredURL3);


        Assertions.assertEquals(200, response3.getStatusCode());
        ResponseBody body = response3.getBody();
        Assertions.assertEquals("Hola",body.jsonPath().getList("messages.content").get(0));


    }

    @Test
    @Order(43)
    void P43() {
        MongoDB m = new MongoDB();
        m.resetMongo();
        //1. Nos registramos exitosamente con un ususario sin conversaciones (S1)
        final String RestAssuredURL1 = URL + "/api/users/login";
        RequestSpecification request1 = RestAssured.given();
        JSONObject requestParams1 = new JSONObject();
        requestParams1.put("email", "user04@email.com");
        requestParams1.put("password", "user04");
        request1.header("Content-Type", "application/json");
        request1.body(requestParams1.toJSONString());
        Response response1 = request1.post(RestAssuredURL1);
        String token = response1.jsonPath().getString("data.token");

        //2. Escribimos un nuevo mensaje a una oferta nuestra (S3)
        final String RestAssuredURL2 = URL + "/api/new/conversation/message";
        RequestSpecification request2 = RestAssured.given();
        JSONObject requestParams2 = new JSONObject();
        requestParams2.put("offerID", "IDfalso");
        requestParams2.put("offerTitle", "Titulo");
        requestParams2.put("offerSeller", "user04@email.com");
        requestParams2.put("content", "Hola");
        request2.header("Content-Type", "application/json");
        request2.header("token", token);
        request2.body(requestParams2.toJSONString());
        Response response2 = request2.post(RestAssuredURL2);

        Assertions.assertEquals(400, response2.getStatusCode());

        //3. Comprobamos que el mensaje no se ha registrado ni se ha creado la conversacion (S4)
        final String RestAssuredURL3 = URL + "/api/conversations";
        RequestSpecification request3 = RestAssured.given();
        request3.header("Content-Type", "application/json");
        request3.header("token", token);
        Response response3 = request3.post(RestAssuredURL3);
        Assertions.assertEquals(200, response3.getStatusCode());
        int nConver = response3.getBody().jsonPath().getList("conversations").size();
        Assertions.assertEquals(0, nConver);

    }

    @Test
    @Order(44)
    void P44() {
        //1. Nos registramos exitosamente  (S1)
        final String RestAssuredURL1 = URL + "/api/users/login";
        RequestSpecification request1 = RestAssured.given();
        JSONObject requestParams1 = new JSONObject();
        requestParams1.put("email", "user01@email.com");
        requestParams1.put("password", "user01");
        request1.header("Content-Type", "application/json");
        request1.body(requestParams1.toJSONString());
        Response response1 = request1.post(RestAssuredURL1);
        String token = response1.jsonPath().getString("data.token");


        //2. Escribimos un nuevo mensaje a una oferta (S3)
        final String RestAssuredURL2 = URL + "/api/new/conversation/message";
        RequestSpecification request2 = RestAssured.given();
        JSONObject requestParams2 = new JSONObject();
        requestParams2.put("offerID", "IDfalso");
        requestParams2.put("offerTitle", "Titulo");
        requestParams2.put("offerSeller", "user02@email.com");
        requestParams2.put("content", "Hola");
        request2.header("Content-Type", "application/json");
        request2.header("token", token);
        request2.body(requestParams2.toJSONString());
        Response response2 = request2.post(RestAssuredURL2);
        String idConver = response2.getBody().jsonPath().getString("messages.conversation");

        //3. Comprobamos que el mensaje se ha registrado (S4)
        final String RestAssuredURL3 = URL + "/api/conversations/" + idConver;
        RequestSpecification request3 = RestAssured.given();
        request3.header("Content-Type", "application/json");
        request3.header("token", token);
        Response response3 = request3.get(RestAssuredURL3);

        Assertions.assertEquals(200, response3.getStatusCode());
        ResponseBody body = response3.getBody();
        Assertions.assertEquals("Hola",body.jsonPath().getList("messages.content").get(0));



    }

    @Test
    @Order(45)
    void P45() {
        MongoDB m = new MongoDB();
        m.resetMongo();
        //Inicialmente en el setUp se crean 3 conversaciones, como se puede ver, tan solo en 2 de estas participa el user01
        //1. Nos registramos exitosamente  (S1)
        final String RestAssuredURL1 = URL + "/api/users/login";
        RequestSpecification request1 = RestAssured.given();
        JSONObject requestParams1 = new JSONObject();
        requestParams1.put("email", "user01@email.com");
        requestParams1.put("password", "user01");
        request1.header("Content-Type", "application/json");
        request1.body(requestParams1.toJSONString());
        Response response1 = request1.post(RestAssuredURL1);
        String token = response1.jsonPath().getString("data.token");

        //2. Comprobamos que tan solo tenga dos conversaciones  (S5)
        final String RestAssuredURL2 = URL + "/api/conversations";
        RequestSpecification request2 = RestAssured.given();
        request2.header("Content-Type", "application/json");
        request2.header("token", token);
        Response response2 = request2.post(RestAssuredURL2);
        Assertions.assertEquals(200, response2.getStatusCode());
        int nConver = response2.getBody().jsonPath().getList("conversations").size();
        Assertions.assertEquals(2, nConver);

    }

    @Test
    @Order(46)
    void P46() {
        MongoDB m = new MongoDB();
        m.resetMongo();
        //Inicialmente en el setUp de la base de datos se crean 3 conversaciones, como se puede ver, tan solo en 2 de estas participa el user01
        //1. Nos registramos exitosamente  (S1)

        final String RestAssuredURL1 = URL + "/api/users/login";
        RequestSpecification request1 = RestAssured.given();
        JSONObject requestParams1 = new JSONObject();
        requestParams1.put("email", "user01@email.com");
        requestParams1.put("password", "user01");
        request1.header("Content-Type", "application/json");
        request1.body(requestParams1.toJSONString());
        Response response1 = request1.post(RestAssuredURL1);
        String token = response1.jsonPath().getString("data.token");

        //2. Escribimos un nuevo mensaje a una oferta (S3) para conseguir un ID valido de manera sencilla
        final String RestAssuredURL2 = URL + "/api/new/conversation/message";
        RequestSpecification request2 = RestAssured.given();
        JSONObject requestParams2 = new JSONObject();
        requestParams2.put("offerID", "IDfalso");
        requestParams2.put("offerTitle", "Titulo");
        requestParams2.put("offerSeller", "user06@email.com");
        requestParams2.put("content", "Hola");
        request2.header("Content-Type", "application/json");
        request2.header("token", token);
        request2.body(requestParams2.toJSONString());
        Response response2 = request2.post(RestAssuredURL2);
        String idConver = response2.getBody().jsonPath().getString("messages.conversation");

        //3. Comprobamos que haya 3 conversaciones (S5)
        final String RestAssuredURL3 = URL + "/api/conversations";
        RequestSpecification request3 = RestAssured.given();
        request3.header("Content-Type", "application/json");
        request3.header("token", token);
        Response response3 = request3.post(RestAssuredURL3);
        Assertions.assertEquals(200, response3.getStatusCode());
        int nConver = response3.getBody().jsonPath().getList("conversations").size();
        Assertions.assertEquals(3, nConver);


        //4. Eliminamos la ultima conversacion creada quedandonos solo con las dos iniciales (S6)
        final String RestAssuredURL4 = URL + "/api/conversations/" + idConver;
        RequestSpecification request4 = RestAssured.given();
        request4.header("Content-Type", "application/json");
        request4.header("token", token);
        Response response4 = request4.delete(RestAssuredURL4);

        //5. Comprobamos que solo haya 2 conversaciones (S5)
        final String RestAssuredURL5 = URL + "/api/conversations";
        RequestSpecification request5 = RestAssured.given();
        request5.header("Content-Type", "application/json");
        request5.header("token", token);
        Response response5 = request5.post(RestAssuredURL5);
        Assertions.assertEquals(200, response5.getStatusCode());
        int nConver2 = response5.getBody().jsonPath().getList("conversations").size();
        System.out.println(response5.getBody().jsonPath().getList("conversations").toString());
        Assertions.assertEquals(2, nConver2);


    }

    @Test
    @Order(48)
    void P48() {
        //Vamos al formulario de logueo.
        driver.navigate().to(URL+"/apiclient");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Comprobamos que entramos en la pagina privada del usuario
        List<WebElement> result = PO_View.checkElementBy(driver, "text", "Ofertas disponibles");
        Assertions.assertEquals("Ofertas disponibles", result.get(0).getText());
    }

    /**
     * Inicio de sesión con datos inválidos (usuario estándar, email existente, pero contraseña
     * incorrecta).
     */
    @Test
    @Order(49)
    void P49() throws InterruptedException {
        //Vamos al formulario de logueo.
        driver.navigate().to(URL+"/apiclient");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user02");
        List<WebElement> result = PO_LoginView.checkElementBy(driver, "text", "Email o contraseña inválidos.");
        Assertions.assertTrue(result.size() > 0);
    }


    @Test
    @Order(52)
    void P52() {
        MongoDB m = new MongoDB();
        m.resetMongo();
        //Vamos al formulario de logueo.
        driver.navigate().to(URL+"/apiclient");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //Eligimos una oferta con la que no hayamos entablado conversacion
        String xpath = "/html/body/div[1]/div/table/tbody/tr[5]/td[6]/a";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        List<WebElement> webElements = driver.findElements(By.xpath(xpath));
        webElements.get(0).click();

        //Escribimos el mensaje
        xpath = "/html/body/div[1]/div/h1";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        PO_FirstNewMessage.fillNewMessage(driver, "Nuevo mensaje");

        //Comprobamos que esta en "Mis Conversaciones"
        xpath = "/html/body/div[1]/div/table/tbody/tr/td[3]/a[1]";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        webElements = driver.findElements(By.xpath(xpath));
        webElements.get(2).click();

        //Comprobamos que esta en los mensajes de la conversacion
        xpath = "/html/body/div[1]/div/table/tbody/tr/td[2]";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        webElements = driver.findElements(By.xpath(xpath));
        Assertions.assertEquals("Nuevo mensaje", webElements.get(0).getText());


    }

    @Test
    @Order(53)
    void P53() {
        MongoDB m = new MongoDB();
        m.resetMongo();
        //Vamos al formulario de logueo.
        driver.navigate().to(URL+"/apiclient");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //Eligimos una oferta con la que no hayamos entablado conversacion
        String xpath = "/html/body/div[1]/div/table/tbody/tr[5]/td[6]/a";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        List<WebElement> webElements = driver.findElements(By.xpath(xpath));
        webElements.get(0).click();

        //Escribimos el mensaje
        xpath = "/html/body/div[1]/div/h1";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        PO_FirstNewMessage.fillNewMessage(driver, "Nuevo mensaje");

        //Comprobamos que esta en "Mis Conversaciones"
        xpath = "/html/body/div[1]/div/table/tbody/tr/td[3]/a[1]";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        webElements = driver.findElements(By.xpath(xpath));
        webElements.get(2).click();

        //Escribimos un nuevo mensaje
        xpath = "/html/body/div[1]/div/h2";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        PO_NewMessage.fillMessage(driver, "Segundo mensaje");

        //Comprobamos que esta en los mensajes de la conversacion
        xpath = "/html/body/div[1]/div/table/tbody/tr[2]/td[2]";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        webElements = driver.findElements(By.xpath(xpath));
        Assertions.assertEquals("Segundo mensaje", webElements.get(0).getText());


    }

    @Test
    @Order(54)
    void P54() {
        MongoDB m = new MongoDB();
        m.resetMongo(); //Partimos de la base de que el user01 tiene dos conversaciones abiertas (cargadas de la base de datos de pruebas)

        driver.navigate().to(URL+"/apiclient");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //Eligimos una oferta con la que no hayamos entablado conversacion
        String xpath = "/html/body/div[1]/div/table/tbody/tr[5]/td[6]/a";
        PO_UserPrivateView.checkElementBy(driver, "free", xpath);
        List<WebElement> webElements = driver.findElements(By.xpath(xpath));
        webElements.get(0).click();

        //Escribimos el mensaje
        xpath = "/html/body/div[1]/div/h1";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        PO_FirstNewMessage.fillNewMessage(driver, "Nuevo mensaje");

        //Comprobamos que en "Mis Conversaciones" haya 3 conversaciones en total
        xpath = "/html/body/div[1]/div/table/tbody/tr";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        webElements = driver.findElements(By.xpath(xpath));
        Assertions.assertEquals(3,webElements.size());
    }


    @Test
    @Order(55)
    void P55() {
        MongoDB m = new MongoDB();
        m.resetMongo(); //Partimos de la base de que el user01 tiene 2 conversaciones abiertas (cargadas de la base de datos de pruebas)

        driver.navigate().to(URL+"/apiclient");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //Entramos en mis conversaciones
        String xpath = "/html/body/nav/div/div[2]/ul[1]/li[2]/a";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        List<WebElement> webElements = driver.findElements(By.xpath(xpath));
        webElements.get(0).click();

        //Hay dos conversaciones, y eliminamos la primera de estas
        xpath = "/html/body/div[1]/div/table/tbody/tr[1]/td[3]/a[2]";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        webElements = driver.findElements(By.xpath(xpath));
        webElements.get(0).click();

        //Comprobamos que en "Mis Conversaciones" haya 1 conversacion en total
        xpath = "/html/body/div[1]/div/table/tbody/tr";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        webElements = driver.findElements(By.xpath(xpath));
        Assertions.assertEquals(1,webElements.size());
    }

    @Test
    @Order(56)
    void P56() {
        MongoDB m = new MongoDB();
        m.resetMongo(); //Partimos de la base de que el user01 tiene 2 conversaciones abiertas (cargadas de la base de datos de pruebas)

        driver.navigate().to(URL+"/apiclient");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //Entramos en mis conversaciones
        String xpath = "/html/body/nav/div/div[2]/ul[1]/li[2]/a";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        List<WebElement> webElements = driver.findElements(By.xpath(xpath));
        webElements.get(0).click();

        //Hay dos conversaciones, y eliminamos la ultima de estas
        xpath = "/html/body/div[1]/div/table/tbody/tr[2]/td[3]/a[2]";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        webElements = driver.findElements(By.xpath(xpath));
        webElements.get(0).click();

        //Comprobamos que en "Mis Conversaciones" haya 1 conversacion en total
        xpath = "/html/body/div[1]/div/table/tbody/tr";
        SeleniumUtils.waitLoadElementsByXpath(driver,xpath,PO_View.getTimeout());
        webElements = driver.findElements(By.xpath(xpath));
        Assertions.assertEquals(1,webElements.size());
    }
    /*
     + ###################
     * Pruebas de listado de ofertas en RestAPI
     * ###################
     */

    /**
     *  Mostrar el listado de ofertas para dicho usuario y comprobar que se muestran todas las que
     * existen para este usuario.
     */
    @Test
    @Order( 41 )
    public void P41() {
        final String RestAssuredURL = URL + "/api/users/login";
        //2. Preparamos el parámetro en formato JSON
        RequestSpecification request1 = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "user01@email.com");
        requestParams.put("password", "user01");
        request1.header("Content-Type", "application/json");
        request1.body(requestParams.toJSONString());
        //3. Hacemos la petición
        Response response1 = request1.post(RestAssuredURL);
        //4. Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, response1.getStatusCode());
        String token = response1.jsonPath().getString("data.token");

        //5. Accedemos a la url del listado de ofertas
        final String RestAssuredURL2 = URL + "/api/offers";
        RequestSpecification request2 = RestAssured.given();
        request2.header("Content-Type", "application/json");
        request2.header("token",token);
        Response response2 = request2.get(RestAssuredURL2);
        //6. Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, response2.getStatusCode());
        int offers = response2.getBody().jsonPath().getList("offers").size();
        Assertions.assertEquals(11,offers);
    }

     /*
     * ###################
     * Pruebas de login cliente JQuery
     * ###################
     */

    /**
     * Inicio de sesión con datos válidos
     */


    /**
     * Inicio de sesión con datos inválidos (campo email o contraseña vacíos)
     */
    @Test
    @Order(50)
    void P50() {
        //Vamos al formulario de logueo.
        driver.navigate().to(URL+"/apiclient");
        //Pulsamos el botón para enviar el formulario sin haberlo rellenado
        By boton = By.className("btn");
        driver.findElement(boton).click();
        List<WebElement> result = PO_LoginView.checkElementBy(driver, "text", "Email o contraseña inválidos.");
        Assertions.assertTrue(result.size() > 0);
    }
    
    /*
     + ###################
     * Pruebas de listado de ofertas en cliente JQuery
     * ###################
     */

    /**
     *  Mostrar el listado de ofertas disponibles y comprobar que se muestran todas las que existen,
     * menos las del usuario identificado
     */
    @Test
    @Order( 51 )
    public void P51() {
        //Vamos al formulario de logueo.
        driver.navigate().to(URL+"/apiclient/index.html");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Comprobamos que se muestran todas las ofertas
        List<WebElement> offersList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());
        int expectedOffers=11;
        Assertions.assertEquals(expectedOffers, offersList.size());
    }

}
