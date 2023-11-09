package Utilities;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.BeforeClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class Utility {
    public Faker rndProd = new Faker();
    public RequestSpecification reqSpec;

    @BeforeClass
    public void Setup() throws IOException {

        String path = "src/test/java/Utilities/LoginDataT.xlsx";
        FileInputStream inputStream = new FileInputStream(path);
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        String username = sheet.getRow(0).getCell(0).toString();
        String password = sheet.getRow(0).getCell(1).toString();
        inputStream.close();

        baseURI = "https://test.mersys.io/";


        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("username", username);
        loginMap.put("password", password);
        loginMap.put("rememberMe", "true");

        Cookies cookies =
                given()
                        .body(loginMap).contentType(ContentType.JSON)
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();

        reqSpec = new RequestSpecBuilder().
                addCookies(cookies).setContentType(ContentType.JSON)
                .build();

    }

}
