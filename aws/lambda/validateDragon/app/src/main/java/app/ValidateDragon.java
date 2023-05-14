package app;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

public class ValidateDragon implements RequestHandler<Map<String, String>, String> {
    
    private static final SsmClient ssmClient = SsmClient.builder().build();
    private static final S3AsyncClient s3 = S3AsyncClient.builder().build();
    
    @Override
    public String handleRequest(Map<String, String> input, Context context) {
        readDragonData(input);
        return "Dragon validated";
    }
    
    protected static void readDragonData(Map<String, String> request) {
        String bucket = getBucket();
        String key = getKey();
        String query = getQuery(request);
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
                DefaultRecords defaultRecords =(DefaultRecords) events;
                String payload = defaultRecords
                    .payload()
                    .asString(StandardCharsets.UTF_8);
                
                if (payload != null && !(payload.equals(""))) {
                    throw new DragonValidationException(
                        "Duplicate dragon found",
                        new RuntimeException()
                    );
                }
                
            }
        }
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
    
    private static String getQuery(Map<String, String> request) {
        return "select * from S3Object[*][*] s where s.name = '" 
            + request.get("dragonName") + "'";
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
