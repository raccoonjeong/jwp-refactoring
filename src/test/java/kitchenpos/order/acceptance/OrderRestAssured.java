package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.dto.OrderRequest;
import org.springframework.http.MediaType;

public class OrderRestAssured {

    public static ExtractableResponse<Response> 주문_등록되어_있음(OrderRequest orderRequest) {
        return 주문_생성_요청(orderRequest);
    }

    public static ExtractableResponse<Response> 주문_생성_요청(OrderRequest orderRequest) {
        return RestAssured
                .given().log().all()
                .body(orderRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Long orderId, OrderRequest orderRequest) {
        return RestAssured
                .given().log().all()
                .body(orderRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/orders/{orderId}/order-status", orderId)
                .then().log().all()
                .extract();
    }
}
