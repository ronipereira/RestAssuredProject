import files.payload;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Test;

public class SumValidation {

    @Test
    public void sumOfCourses()
    {
        JsonPath js = new JsonPath(payload.CoursePrice());

        int count = js.getInt("courses.size()");
        int totalAmount = 0;

        for(int i = 0; i < count; i++)
        {
            int price = js.get("courses["+ i +"].price");
            int copies = js.get("courses["+ i +"].copies");
            int amount = price * copies;
            System.out.println(amount);
            totalAmount = totalAmount + amount;
        }

        System.out.println(totalAmount);

        int responseTotalAmount = js.get("dashboard.purchaseAmount");

        Assert.assertEquals(totalAmount, responseTotalAmount);
    }
}
