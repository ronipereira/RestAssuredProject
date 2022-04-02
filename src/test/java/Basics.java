import files.ReusableMethods;
import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class Basics {
    public static void main(String[] args) throws IOException {
        RestAssured.baseURI="https://rahulshettyacademy.com";

        //Add place using payload from method payload.AddPlace()
//        String response =
//        given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
//                .body(payload.AddPlace())
//        .when().post("maps/api/place/add/json")
//        .then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
//                .header("server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();

        //Add place using payload from json file
        String response =
                given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
                        .body(new String(Files.readAllBytes(Paths.get("src/test/java/jsonPayload/addPlace.json"))))
                        .when().post("maps/api/place/add/json")
                        .then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
                        .header("server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();

        JsonPath js = ReusableMethods.rawToJson(response);//for parsing json

        String place_id = js.getString("place_id");//extract only value present in place_id path

        //Update place with new address and verify if new new place is present in response

        String newAddress = "Summer Walk, Africa";

        given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
                .body(payload.UpdatePlace(place_id, newAddress))
                .when().put("maps/api/place/update/json")
                .then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));

        //Get place to ensure that place was updated

        String getPlaceResponse = given().log().all().queryParam("place_id", place_id)
                .queryParam("key", "qaclick123")
                .when().get("maps/api/place/get/json")
                .then().log().all().statusCode(200).extract().response().asString();

        JsonPath js1 = ReusableMethods.rawToJson(getPlaceResponse);

        String actualAddress = js1.getString("address");

        Assert.assertEquals(newAddress, actualAddress);
    }
}