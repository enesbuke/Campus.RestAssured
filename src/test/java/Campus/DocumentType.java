package Campus;

import Utilities.Utility;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class DocumentType extends Utility {
    String documentId = "";
    String name;
    List<String> stages = new ArrayList<>();
    Map<String, Object> dtKeys = new HashMap<>();


    @Test
    public void createDocumentType() {

        name = rndProd.name().fullName().toUpperCase();
        stages.add("EXAMINATION");


        dtKeys.put("name", name);
        dtKeys.put("attachmentStages", stages);
        dtKeys.put("schoolId", "6390f3207a3bcb6a7ac977f9");


        documentId = given()
                .spec(reqSpec)
                .body(dtKeys)

                .when()
                .post("school-service/api/attachments/create")

                .then()
                .statusCode(201)

                .log().body()
                .extract().path("id")
        ;
        System.out.println("documentIdC = " + documentId);
    }

    @Test(dependsOnMethods = "createDocumentType")
    public void createDocumentTypeNegative() {


        dtKeys.put("name", " ");
        dtKeys.put("attachmentStages", stages);
        dtKeys.put("schoolId", "6390f3207a3bcb6a7ac977f9");


        given()
                .spec(reqSpec)
                .body(dtKeys)

                .when()
                .post("school-service/api/attachments/create")

                .then()
                .statusCode(400)

                .log().body()
        ;
    }

    @Test(dependsOnMethods = "createDocumentTypeNegative")
    public void updateDocumentType() {

        dtKeys.put("id", documentId);
        dtKeys.put("name", name);
        dtKeys.put("attachmentStages", stages);
        dtKeys.put("schoolId", "6390f3207a3bcb6a7ac977f9");

        given()
                .spec(reqSpec).body(dtKeys)
                .when()
                .put("school-service/api/attachments")
                .then()
                .statusCode(200)
                .body("id", equalTo(documentId))
        ;
        System.out.println("documentIdUpdate = " + documentId);
    }

    @Test(dependsOnMethods = "updateDocumentType")
    public void deleteDocumentType() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/attachments/" + documentId)
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteDocumentType")
    public void deleteDocumentTypeNegative() {

        given()
                .spec(reqSpec)
                .when()
                .delete("/school-service/api/attachments/" + documentId)
                .then()
                .statusCode(400)
        ;
    }
}

