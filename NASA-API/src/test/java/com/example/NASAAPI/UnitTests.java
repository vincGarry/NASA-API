package com.example.NASAAPI;

import com.example.NASAAPI.services.GetAsteroids;
import com.example.NASAAPI.services.models.Asteroid;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class UnitTests {

    private GetAsteroids getAsteroids = new GetAsteroids();
    @Test
    void ParseNASADataCorrectly() throws Exception{
        JSONObject asteroid = new JSONObject("{\"links\":{\"self\":\"http://api.nasa.gov/neo/rest/v1/neo/2465633?api_key=DEMO_KEY\"},\"id\":\"2465633\",\"neo_reference_id\":\"2465633\",\"name\":\"465633 (2009 JR5)\",\"nasa_jpl_url\":\"https://ssd.jpl.nasa.gov/tools/sbdb_lookup.html#/?sstr=2465633\",\"absolute_magnitude_h\":20.44,\"estimated_diameter\":{\"kilometers\":{\"estimated_diameter_min\":0.2170475943,\"estimated_diameter_max\":0.4853331752},\"meters\":{\"estimated_diameter_min\":217.0475943071,\"estimated_diameter_max\":485.3331752235},\"miles\":{\"estimated_diameter_min\":0.1348670807,\"estimated_diameter_max\":0.3015719604},\"feet\":{\"estimated_diameter_min\":712.0984293066,\"estimated_diameter_max\":1592.3004946003}},\"is_potentially_hazardous_asteroid\":true,\"close_approach_data\":[{\"close_approach_date\":\"2015-09-08\",\"close_approach_date_full\":\"2015-Sep-08 20:28\",\"epoch_date_close_approach\":1441744080000,\"relative_velocity\":{\"kilometers_per_second\":\"18.1279360862\",\"kilometers_per_hour\":\"65260.5699103704\",\"miles_per_hour\":\"40550.3802312521\"},\"miss_distance\":{\"astronomical\":\"0.3027469457\",\"lunar\":\"117.7685618773\",\"kilometers\":\"45290298.225725659\",\"miles\":\"28142086.3515817342\"},\"orbiting_body\":\"Earth\"}],\"is_sentry_object\":false}");
        Asteroid result = getAsteroids.ParseAsteroidData(asteroid);
        assert(result.getId().equals("2465633"));
        assert(result.getName().equals("465633 (2009 JR5)"));
        assert(result.getAbsoluteMagnitudeH().doubleValue()==20.44);
        assert(result.getCloseApproachData().size()==1);
        assert(result.getDiameterMax().doubleValue()==485.3331752235);
        assert(result.getDiameterMin().doubleValue()==217.0475943071);
    }
}
