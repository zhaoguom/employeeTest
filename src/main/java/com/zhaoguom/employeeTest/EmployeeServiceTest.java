package com.zhaoguom.employeeTest;
import com.zhaoguom.employeeTest.dataobject.EmployeeDO;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.ResponseAwareMatcher.*;
import io.restassured.response.Response;
import static org.hamcrest.Matcher.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class EmployeeServiceTest {

    @BeforeClass
    public void beforeClass(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8089;
        RestAssured.basePath = "api";
    }

    @DataProvider(name = "createEmployeeData")
    public final Object[][] getMspOrDirectOrganizationInfo() {
        return new Object[][] {
                {"Ma", "Zhaoguo", "zhaoguo.ma@gmail.com"},
        };
    }
    @Test(dataProvider = "createEmployeeData")
    public void testCreateEmployee(String firstName, String lastName, String email){
        EmployeeDO employeeDO= new EmployeeDO();
        String prefix = RandomStringUtils.randomAlphabetic(8);
        email = prefix + email;
        firstName = prefix + firstName;
        lastName = prefix + lastName;
        employeeDO.setFirstName(firstName);
        employeeDO.setLastName(lastName);
        employeeDO.setEmail(email);
        Response response = given().header("Content-Type", "application/json")
                .body(JSON.toJSONString(employeeDO)).when()
                .post("users");

        assertEquals(email,response.then().extract().path("data.email"));
        assertEquals(firstName,response.then().extract().path("data.firstName"));
        assertEquals(lastName,response.then().extract().path("data.lastName"));
        Integer employeeID = (Integer)response.then().extract().path("data.id");

        response = given().header("Content-Type", "application/json")
                .body(JSON.toJSONString(employeeDO)).when()
                .get("users/" + employeeID.toString());
        assertEquals(email,response.then().extract().path("data.email"));
        assertEquals(firstName,response.then().extract().path("data.firstName"));
        assertEquals(lastName,response.then().extract().path("data.lastName"));
        Reporter.log("this is a log");
    }
}
