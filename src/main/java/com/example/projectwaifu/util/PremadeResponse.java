package com.example.projectwaifu.util;

import java.util.List;
import java.util.Random;

public class PremadeResponse {

    Random random = new Random();
    List<String> explicitResponse = List.of("Don't use that kind of language with me!", "Please don't talk to me like that ;-;", "Who taught you to talk like that? Be nice.");

    public String explicitWarning() {
        return explicitResponse.get(random.nextInt(explicitResponse.size()));
    }
}
