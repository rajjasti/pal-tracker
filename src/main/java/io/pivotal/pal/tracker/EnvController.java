package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {

    private Map<String, String> envMap;

    public EnvController(@Value("${port:NOT SET}") String port,
                         @Value("${memory.limit:NOT SET}") String memoryLimit,
                         @Value("${cf.instance.index:NOT SET}") String cfInstanceIndex,
                         @Value("${cf.instance.addr:NOT SET}") String cfInstanceAddr){
        Map<String, String> tempEnv = new HashMap<>();
        tempEnv.put("PORT", port);
        tempEnv.put("MEMORY_LIMIT", memoryLimit);
        tempEnv.put("CF_INSTANCE_INDEX", cfInstanceIndex);
        tempEnv.put("CF_INSTANCE_ADDR", cfInstanceAddr);
        envMap = tempEnv;
    }

    @GetMapping("/env")
    public Map<String, String> getEnv(){

       return envMap;
    }
}
