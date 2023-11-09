package TestCases;

import Utilities.Utility;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BankAccount extends Utility {
    String ID = "";
    String bankName = "";
    String bankIban = "";

    @Test
    public void BankAccount() {
        bankName = rndProd.name().firstName();
        bankIban = rndProd.number().digits(11);

        Map<String, Object> BankNew = new HashMap<>();
        BankNew.put("name", bankName);
        BankNew.put("iban", bankIban);
        BankNew.put("integrationCode", "team8");
        BankNew.put("currency", "EUR");
        BankNew.put("active", true);
        BankNew.put("schoolId", "646cbb07acf2ee0d37c6d984");
        ID =
                given()
                        .spec(reqSpec)
                        .body(BankNew)
                        .when()
                        .post("school-service/api/bank-accounts")
                        .then()
                        .contentType(ContentType.JSON)
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "BankAccount")
    public void BankAccountNegative() {

        Map<String, Object> BankNew = new HashMap<>();
        BankNew.put("id", ID);
        BankNew.put("name", bankName);
        BankNew.put("iban", bankIban);
        BankNew.put("integrationCode", "gdg");
        BankNew.put("currency", "EUR");
        BankNew.put("active", true);
        BankNew.put("schoolId", "646cbb07acf2ee0d37c6d984");
        given()
                .spec(reqSpec)
                .body(BankNew)
                .when()
                .post("school-service/api/bank-accounts")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(400);
    }

    @Test(dependsOnMethods = "BankAccountNegative")
    public void UpdateBankAccount() {
        Map<String, Object> BankNew = new HashMap<>();
        BankNew.put("id", ID);
        BankNew.put("name", bankName);
        BankNew.put("iban", bankIban);
        BankNew.put("integrationCode", "gdg");
        BankNew.put("currency", "EUR");
        BankNew.put("active", false);
        BankNew.put("schoolId", "646cbb07acf2ee0d37c6d984");
        given()
                .spec(reqSpec)
                .body(BankNew)
                .when()
                .put("school-service/api/bank-accounts")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo(bankName));
    }

    @Test(dependsOnMethods = "UpdateBankAccount")
    public void deleteBankAccount() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/bank-accounts/" + ID)
                .then()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteBankAccount")
    public void deleteDepartmentsNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/bank-accounts/" + ID)
                .then()
                .statusCode(400)
                .log().body();
    }

}
