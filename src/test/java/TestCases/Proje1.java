package TestCases;
import Utilities.Utility;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class Proje1 extends Utility {
    String AttestationsID="";
    String AttestationsName="";
@Test
    public void Attestations(){
    AttestationsName=rndProd.name().fullName();
    Map<String,String> attestations=new HashMap<>();
    attestations.put("name",AttestationsName);

    AttestationsID=
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
@Test(dependsOnMethods = "Attestations")
    public void  AttestationsNegative(){
    Map<String,String> attestations=new HashMap<>();
    attestations.put("name",AttestationsName);
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
@Test(dependsOnMethods ="AttestationsNegative")
    public void UpdateAttestations(){
    Map<String,String> attestations=new HashMap<>();
    attestations.put("id",AttestationsID);
    attestations.put("name","team8");
    given()
            .spec(reqSpec)
            .body(attestations)
            .when()
            .put("school-service/api/attestation")
            .then()
            .log().body()
            .contentType(ContentType.JSON)
            .statusCode(200);
}
@Test(dependsOnMethods = "UpdateAttestations")
    public void UpdateAttestationsNegative(){
    Map<String,String> attestations=new HashMap<>();
    attestations.put("name","team8");
    given()
            .spec(reqSpec)
            .body(attestations)
            .when()
            .put("school-service/api/attestation")
            .then()
            .log().body()
            .contentType(ContentType.JSON)
            .statusCode(400)
            .body("message",containsString("already exists"));
}
}
