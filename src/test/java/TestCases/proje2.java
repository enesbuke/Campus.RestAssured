package TestCases;

import Utilities.Utility;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class proje2 extends Utility {
    String ID="";
    String BankName="";
    String BankIban="";
    @Test
    public void BankAcoount() {
        String randomBankName = rndProd.name().firstName();
        String randomIban = rndProd.number().digits(11);
        BankName = randomBankName;
        BankIban = randomIban;
        Map<String, Object> BankNew = new HashMap<>();
        BankNew.put("name", randomBankName);
        BankNew.put("Iban", randomIban);
        BankNew.put("İntegrationCode", "team8");
        BankNew.put("currency", "EUR");
        BankNew.put("Active", true);
        BankNew.put("SclId", "646cbb07acf2ee0d37c6d984");
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
  @Test(dependsOnMethods = "BankAcoount")
  public void BankAcoountNegative(){
      Map<String, Object> BankNew = new HashMap<>();
      BankNew.put("id",ID);
      BankNew.put("name",BankName);
      BankNew.put("Iban",BankIban);
      BankNew.put("İntegrationCode","gdg");
      BankNew.put("currency","EUR");
      BankNew.put("Active",true);
      BankNew.put("SclId","646cbb07acf2ee0d37c6d984");
      given()
              .spec(reqSpec)
              .body(BankNew)
              .when()
              .post("school-service/api/bank-accounts")
              .then()
              .contentType(ContentType.JSON)
              .statusCode(400);
  }
  @Test(dependsOnMethods = "BankAcoountNegative")
    public void UpdateBankAcoount(){
      Map<String, Object> BankNew = new HashMap<>();
      BankNew.put("id",ID);
      BankNew.put("name","sasodjsdj");
      BankNew.put("Iban",BankIban);
      BankNew.put("İntegrationCode","gdg");
      BankNew.put("currency","EUR");
      BankNew.put("Active",true);
      BankNew.put("SclId","646cbb07acf2ee0d37c6d984");
      given()
              .spec(reqSpec)
              .body(BankNew)
              .when()
              .put("school-service/api/bank-accounts")
              .then()
              .contentType(ContentType.JSON)
              .statusCode(200)
              .body("name",equalTo("sasodjsdj"));


  }
}
