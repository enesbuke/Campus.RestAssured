package TestCases;

import Utilities.Utility;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.github.javafaker.Faker;

import java.util.HashMap;
import java.util.Map;

public class Field extends Utility {
    Map<String, String> newField = new HashMap<>();

    String fieldName = " ";
    String fieldID = " ";

    @Test
    public void createField() {


        fieldName = "burak" + rndProd.number().digits(1);


        newField.put("name", fieldName);
        newField.put("code", "5");
        newField.put("type", "STRING");
        newField.put("systemField", "false");
        newField.put("systemFieldName", null);
        newField.put("schoolId", "646cbb07acf2ee0d37c6d984");

        fieldID = given()
                .spec(reqSpec)
                .body(newField)
                .when()
                .post("school-service/api/entity-field")
                .then()
                .contentType(ContentType.JSON)
                .log().body()
                .extract().path("id");


    }

    @Test(dependsOnMethods = "createField")
    public void editField() {
        fieldName = "ozturk" + rndProd.number().digits(1);

        newField.put("id", fieldID);
        newField.put("name", fieldName);
        newField.put("code", "5");
        newField.put("type", "STRING");
        newField.put("systemField", "false");
        newField.put("systemFieldName", null);
        newField.put("schoolId", "646cbb07acf2ee0d37c6d984");

        given()
                .spec(reqSpec)
                .body(newField)
                .when()
                .put("school-service/api/entity-field")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200);

    }

    @Test(dependsOnMethods = "editField")
    public void deleteField() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/entity-field/" + fieldID)
                .then()
                .statusCode(204);


    }

}











































