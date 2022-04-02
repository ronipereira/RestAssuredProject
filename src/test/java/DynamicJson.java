import static io.restassured.RestAssured.*;

import files.ReusableMethods;
import files.payload;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DynamicJson {

    @Test(dataProvider = "BooksData")
    public void addBook(String isbn, String aisle)
    {
        baseURI = "https://rahulshettyacademy.com";

        String response = given().header("Content-Type", "application/json").
                body(payload.AddBook(isbn, aisle)).
                when()
                .post("/Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath js = ReusableMethods.rawToJson(response);

        String id = js.get("ID");
        System.out.println(id);

    }

    @DataProvider(name = "BooksData")
    public Object[][] getData()
    {
        return new Object[][] {{"haruuf","9269"}, {"yshfp","9759"}, {"osgdjr","9658"}};
    }
}
