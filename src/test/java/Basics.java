import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class Basics {
    public static void main(String[] args){
        RestAssured.baseURI="https://rahulshettyacademy.com";

        //Add place
        String response =
        given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
                .body(payload.AddPlace())
        .when().post("maps/api/place/add/json")
        .then().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();

        JsonPath js = new JsonPath(response);//for parsing json

        String place = js.getString("place_id");//extract only value present in place_id path

        //update place with new address and verify if new new place is present in response
    }
}