package TestCases;

import Utilities.Utility;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Attestation extends Utility {
    String AttestationsID = "";
    String AttestationsName = "";

    @Test
    public void createAttestations() {
        AttestationsName = rndProd.name().fullName();
        Map<String, String> attestations = new HashMap<>();
        attestations.put("name", AttestationsName);

        AttestationsID =
                given()
                        .spec(reqSpec)
                        .body(attestations)
                        .when()
                        .post("school-service/api/attestation")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createAttestations")
    public void createAttestationsNegative() {
        Map<String, String> attestations = new HashMap<>();
        attestations.put("name", AttestationsName);
        given()
                .spec(reqSpec)
                .body(attestations)
                .when()
                .post("school-service/api/attestation")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already exists"));
    }

    @Test(dependsOnMethods = "createAttestationsNegative")
    public void UpdateAttestations() {
        Map<String, Object> attestations = new HashMap<>();
        attestations.put("id", AttestationsID);
        attestations.put("name", "team8");
        attestations.put("translateName", new Object[1]);


        given()
                .spec(reqSpec)
                .body(attestations)
                .when()
                .put("school-service/api/attestation")
                .then()
                .log().body()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name",equalTo("team8"));
    }

    @Test(dependsOnMethods = "UpdateAttestations")
    public void deleteAttestations() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/attestation/" + AttestationsID)
                .then()
                .log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deleteAttestations")
    public void deleteAttestationsNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/attestation/" + AttestationsID)
                .then()
                .statusCode(400)
                .log().body()
        ;
    }
}

