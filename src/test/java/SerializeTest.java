import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojo.AddPlace.*;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SerializeTest {
    public static void main(String[] args) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        AddPlace addPlace = new AddPlace();
        addPlace.setAccuracy(50);
        addPlace.setAddress("29, side layout, cohen 09");
        addPlace.setLanguage("Portuguese-PT");
        addPlace.setPhone("(208)222-333-4444");
        addPlace.setWebsite("https://rahulshettyacademy.com");
        addPlace.setName("Frontline house");
        List<String> types = new ArrayList<String>();
        types.add("shoe park");
        types.add("shop");
        addPlace.setTypes(types);
        Location location = new Location();
        location.setLat(-38.383494);
        location.setLng(33.427362);
        addPlace.setLocation(location);

        Response response = given().queryParams("key", "qaclick123").log().all().
                body(addPlace).
                when().post("/maps/api/place/add/json").
                then().assertThat().statusCode(200).extract().response();

        String responseString = response.asString();
        System.out.println(responseString);

    }
}
