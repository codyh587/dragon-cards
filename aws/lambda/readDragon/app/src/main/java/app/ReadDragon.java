package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CompressionType;
import software.amazon.awssdk.services.s3.model.ExpressionType;
import software.amazon.awssdk.services.s3.model.InputSerialization;
import software.amazon.awssdk.services.s3.model.JSONInput;
import software.amazon.awssdk.services.s3.model.JSONOutput;
import software.amazon.awssdk.services.s3.model.OutputSerialization;
import software.amazon.awssdk.services.s3.model.SelectObjectContentEventStream;
import software.amazon.awssdk.services.s3.model.SelectObjectContentRequest;
import software.amazon.awssdk.services.s3.model.SelectObjectContentResponse;
import software.amazon.awssdk.services.s3.model.SelectObjectContentResponseHandler;
import software.amazon.awssdk.services.s3.model.selectobjectcontenteventstream.DefaultRecords;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ReadDragon implements RequestHandler<
    APIGatewayProxyRequestEvent,
    APIGatewayProxyResponseEvent
> {

    private static final SsmClient ssmClient = SsmClient.builder().build();
    private static final S3AsyncClient s3 = S3AsyncClient.builder().build();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
        APIGatewayProxyRequestEvent input,
        Context context
    ) {
        String dragonData = readDragonData(input);
        return generateResponse(dragonData);
    }

    protected static String readDragonData(APIGatewayProxyRequestEvent request) {
        Map<String, String> queryParams = request.getQueryStringParameters();
        String bucket = getBucket();
        String key = getKey();
        String query = getQuery(queryParams);
        JsonArray results = new JsonArray();
        TestHandler testHandler = new TestHandler();

        CompletableFuture<Void> selection = queryS3(
            s3,
            bucket,
            key,
            query,
            testHandler
        );

        selection.join();

        for (SelectObjectContentEventStream events : testHandler.events) {
            if (events instanceof DefaultRecords) {
                DefaultRecords defaultRecords = (DefaultRecords) events;
                String payload = defaultRecords.payload().asUtf8String();
                Scanner scanner = new Scanner(payload);
                while (scanner.hasNextLine()) {
                    JsonElement element = JsonParser.parseString(scanner.nextLine());
                    results.add(element);
                }
                scanner.close();
            }
        }

        return results.toString();
    }

    private static String getBucket() {
        GetParameterRequest parameterRequest = GetParameterRequest
                .builder()
                .name("dragon_data_bucket_name")
                .withDecryption(false)
                .build();

        GetParameterResponse parameterResponse = ssmClient.getParameter(parameterRequest);
        return parameterResponse.parameter().value();
    }

    private static String getKey() {
        GetParameterRequest parameterRequest = GetParameterRequest
                .builder()
                .name("dragon_data_file_name")
                .withDecryption(false)
                .build();

        GetParameterResponse parameterResponse = ssmClient.getParameter(parameterRequest);
        return parameterResponse.parameter().value();
    }

    private static String getQuery(Map<String, String> queryParams) {
        if (queryParams != null) {
            System.out.println("Query contains params");

            if (queryParams.containsKey("family")) {
                System.out.println("Query for attribute \"family\"");
                return "select * from S3Object[*][*] s where s.family = '"
                    + queryParams.get("family") + "'";
            }

            else if (queryParams.containsKey("dragonName")) {
                return "select * from S3Object[*][*] s where s.name = '"
                    + queryParams.get("dragonName") + "'";
            }
        }

        return "select * from s3object[*][*] s";
    }

    private static CompletableFuture<Void> queryS3(
        S3AsyncClient s3,
        String bucket,
        String key,
        String query,
        SelectObjectContentResponseHandler handler
    ) {
        InputSerialization inputSerialization = InputSerialization
            .builder()
            .json(JSONInput.builder().type("Document").build())
            .compressionType(CompressionType.NONE)
            .build();

        OutputSerialization outputSerialization = OutputSerialization
            .builder()
            .json(JSONOutput.builder().build())
            .build();

        SelectObjectContentRequest request = SelectObjectContentRequest.builder()
            .bucket(bucket)
            .key(key)
            .expression(query)
            .expressionType(ExpressionType.SQL)
            .inputSerialization(inputSerialization)
            .outputSerialization(outputSerialization)
            .build();

        return s3.selectObjectContent(request, handler);
    }

    private static APIGatewayProxyResponseEvent generateResponse(String dragonData) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        Map<String, String> headers = new HashMap<String, String>();

        headers.put("access-control-allow-origin", "*");

        response.setStatusCode(200);
        response.setBody(dragonData);
        response.setHeaders(headers);
        return response;
    }

    private static class TestHandler implements SelectObjectContentResponseHandler {
        private List<SelectObjectContentEventStream> events = new ArrayList<>();

        @Override
        public void onEventStream(SdkPublisher<SelectObjectContentEventStream> publisher) {
            publisher.subscribe(events::add);
        }

        @Override
        public void responseReceived(SelectObjectContentResponse response) {}

        @Override
        public void exceptionOccurred(Throwable throwable) {}

        @Override
        public void complete() {}
    }

}
