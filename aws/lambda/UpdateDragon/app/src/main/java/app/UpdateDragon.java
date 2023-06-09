package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import app.Dragon;

public class UpdateDragon implements RequestHandler<Map<String, String>, String> {

    private static final SsmClient ssmClient = SsmClient.builder().build();
    private static final S3Client s3Client = S3Client.builder().build();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String handleRequest(Map<String, String> input, Context context) {
        updateDragon(input);
        return "Dragon updated";
    }

    public void updateDragon(Map<String, String> request) {
        ResponseInputStream<GetObjectResponse> object = s3Client.getObject(
                GetObjectRequest
                        .builder()
                        .bucket(getBucket())
                        .key(getKey())
                        .build());

        String dragonDataString = textInputStreamToString(object);
        List<Dragon> dragonDataList = stringToList(dragonDataString);
        boolean updated = false;

        String oldName = request.get("oldName");
        String description = request.get("description");
        String name = request.get("name");
        String family = request.get("family");
        String spottedCity = request.get("spottedCity");
        String spottedNeighborhood = request.get("spottedNeighborhood");
        String spottedState = request.get("spottedState");

        for(Dragon dragon : dragonDataList) {
            if(dragon.getName().equals(oldName)) {

                if (!description.equals("")) {
                    dragon.setDescription(description);
                }
                if (!name.equals("")) {
                    dragon.setName(name);
                }
                if (!family.equals("")) {
                    dragon.setFamily(family);
                }
                if (!spottedCity.equals("")) {
                    dragon.setSpottedCity(spottedCity);
                }
                if (!spottedNeighborhood.equals("")) {
                    dragon.setSpottedNeighborhood(spottedNeighborhood);
                }
                if (!spottedState.equals("")) {
                    dragon.setSpottedState(spottedState);
                }

                updated = true;
                break;

            }
        }

        if (!updated) {
            throw new DragonUpdateException(
                "Dragon not found",
                new RuntimeException()
            );
        }

        uploadDragonData(getBucket(), getKey(), dragonDataList);
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

    private String textInputStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        String inputStreamString = "";

        try {
            while ((line = reader.readLine()) != null) {
                inputStreamString += line;
            }
        } catch (IOException err) {
            System.out.println(err.getMessage());
        }

        return inputStreamString;
    }

    private List<Dragon> stringToList(String string) {
        return gson.fromJson(
            string,
            new TypeToken<List<Dragon>>() {}.getType()
        );
    }

    private static void uploadDragonData(
        String bucket,
        String key,
        List<Dragon> dragonDataList
    ) {
        PutObjectRequest objectRequest = PutObjectRequest
            .builder()
            .bucket(bucket)
            .key(key)
            .build();

        s3Client.putObject(
            objectRequest,
            RequestBody.fromString(gson.toJson(dragonDataList))
        );
    }

}
