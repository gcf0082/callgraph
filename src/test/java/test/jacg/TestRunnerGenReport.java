package test.jacg;

import com.contrastsecurity.sarif.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.StringWriter;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.Arrays;

public class TestRunnerGenReport {
    public static void main(String[] args) throws Exception{
        SarifSchema210 schema = new SarifSchema210();
        Location location = new Location();
        location.withPhysicalLocation(new PhysicalLocation().withArtifactLocation(new ArtifactLocation().withUri("Main.java")));
        Result result = new Result()
                .withMessage(new Message().withText("hi"))
                .withLevel(Result.Level.ERROR)
                .withLocations(Arrays.asList(location));
        Run run = new Run();
        run.withTool(new Tool());
        run.withResults(Arrays.asList(result));
        schema.withRuns(Arrays.asList(run));
        schema.withVersion(SarifSchema210.Version._2_1_0);


        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.writerWithDefaultPrettyPrinter().createGenerator(writer);

        generator.writeObject(schema);
        System.out.println(writer.toString());
    }
}
