package com.restful.booker.info;

import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Title;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasKey;

@RunWith(SerenityRunner.class)
public class BookingCrudTest extends TestBase {

    public static String firstname = "Avi" + TestUtils.getRandomValue();
    public static String lastname = "Patel" + TestUtils.getRandomValue();
    public static Integer totalprice = 321;
    public static Boolean depositpaid = false;
    public static String additionalneeds = "Breakfast";
    public static int bookingID;

    public static String username = "admin";
    public static String password = "password123";
    public static String token;

    @Steps
    BookingSteps steps;

    @Title("This will create auth token")
    @Test
    public void T1() {
        ValidatableResponse response = steps.createToken(username, password);

        response.log().all().statusCode(200);

        HashMap<Object, Object> tokenMap = response.log().all().extract().path("");

        Assert.assertThat(tokenMap, hasKey("token"));
        String jsonString = response.extract().asString();
        token = JsonPath.from(jsonString).get("token");

        System.out.println(token);
    }

    @Title("This will Create a booking")
    @Test
    public void T2() {

        HashMap<Object, Object> bookingsDatesData = new HashMap<>();
        bookingsDatesData.put("checkin", "2023-06-06");
        bookingsDatesData.put("checkout", "2023-07-07");

        ValidatableResponse response = steps.createBooking(firstname, lastname, totalprice,
                depositpaid, bookingsDatesData, additionalneeds);

        response.log().all().statusCode(200);
        bookingID = response.log().all().extract().path("bookingid");

        HashMap<Object, Object> bookingMap = response.log().all().extract().path("");
        Assert.assertThat(bookingMap, anything(firstname));
        System.out.println(token);
    }

    @Title("This will read booking")
    @Test
    public void T3() {

        ValidatableResponse response = steps.readBooking(bookingID);
        response.log().all().statusCode(200);

    }

    @Title("This will Update booking")
    @Test
    public void T4() {
        HashMap<Object, Object> bookingsDatesData = new HashMap<>();
        bookingsDatesData.put("checkin", "2024-08-08");
        bookingsDatesData.put("checkout", "2024-09-09");

        ValidatableResponse response = steps.updateBooking(bookingID, firstname, lastname,
                totalprice, depositpaid, bookingsDatesData, additionalneeds);

        response.log().all().statusCode(200);

        HashMap<Object, Object> bookingMap = response.log().all().extract().path("");
        Assert.assertThat(bookingMap, anything(firstname));
        System.out.println(token);
    }


    @Title("This will Deleted with BookingId")
    @Test
    public void T5() {

        ValidatableResponse response = steps.deleteBooking(bookingID);
        response.log().all().statusCode(201);


    }
}
