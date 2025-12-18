package org.example.project_wobimich;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.project_wobimich.model.FunFact;

import java.io.File;
import java.util.List;
import java.util.Random;

public class FunFactUtils {

    /**
     * This function return a fact of the given id
     *
     * @return fact as string
     */
    public static String getFact(int id) {
        FunFact funFact = new FunFact();
        File pathToJsonFile = new File("src/main/resources/org/example/project_wobimich/data/wl-fun-facts.json");

        try {
            ObjectMapper mapper = new ObjectMapper();

            List<FunFact> funFactJson = mapper.readValue(
                    pathToJsonFile,
                    new TypeReference<List<FunFact>>() {}
            );


            for (FunFact f : funFactJson) {
                if (f.getId() == id) {
                    funFact = f;
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return funFact.getFact();
    }

    /**
     *This function return a random fact of wl-fun-facts.json as String
     * @return random fact of wl-fun-facts.json
     */
    public static String getRandomFact() {
        Random random = new Random();
        int numbOfFacts = 46;
        int randNumb = random.nextInt(numbOfFacts) + 1;

        return getFact(randNumb);
    }

}
