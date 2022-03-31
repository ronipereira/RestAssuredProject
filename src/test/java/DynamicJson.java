import static io.restassured.RestAssured.*;

import files.ReusableMethods;
import files.payload;
import io.restassured.path.json.JsonPath;
import org.junit.Test;

public class DynamicJson {

    @Test
    public void addBook()
    {
        baseURI = "https://rahulshettyacademy.com";

        String response = given().header("Content-Type", "application/json").
                body(payload.AddBook()).
                when()
                .post("/Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath js = ReusableMethods.rawToJson(response);

        String id = js.get("ID");
        System.out.println(id);

    }
}
