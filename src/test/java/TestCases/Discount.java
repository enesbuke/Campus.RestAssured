package TestCases;

import Utilities.Utility;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class Discount extends Utility {
    Map<String, String> newDiscount = new HashMap<>();
    String discountID;


    @Test
    public void createDiscount() {


        newDiscount.put("description", "study2");
        newDiscount.put("code", "2");
        newDiscount.put("active", "true");
        newDiscount.put("priority", "20");
        newDiscount.put("tenantId", "646cb816433c0f46e7d44cb0");

        discountID =
                given()
                        .spec(reqSpec)
                        .body(newDiscount)
                        .when()
                        .post("school-service/api/discounts")
                        .then()
                        .contentType(ContentType.JSON)
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");

    }

        @Test(dependsOnMethods = "createDiscount")
                public void editDiscount() {

            newDiscount.put("id", discountID);
            newDiscount.put("description", "study3");
            newDiscount.put("code", "2");
            newDiscount.put("active", "true");
            newDiscount.put("priority", "20");

            given()
                    .spec(reqSpec)
                    .body(newDiscount)
                    .when()
                    .put("school-service/api/discounts")
                    .then()
                    .log().body()
                    .statusCode(200);

        }




      @Test(dependsOnMethods ="editDiscount")

      public void deleteDiscount(){


        given()
                .spec(reqSpec)
                .when()
                .delete("/school-service/api/discounts/"+discountID)
                .then()
                .statusCode(200);
      }


    }








