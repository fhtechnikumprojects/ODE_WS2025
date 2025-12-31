package org.example.project_wobimich.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FavoriteService {

    private static final File FILE = new File("favorites.json");
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Set<String> loadFavorites() {
        try {
            if (!FILE.exists()) {
                return new HashSet<>();
            }
            return mapper.readValue(FILE, new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    public static void saveFavorites(Set<String> favorites) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(FILE, favorites);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addFavorite(String station) {
        Set<String> favorites = loadFavorites();
        favorites.add(station);
        saveFavorites(favorites);
    }

    public static void removeFavorite(String station) {
        Set<String> favorites = loadFavorites();
        favorites.remove(station);
        saveFavorites(favorites);
    }
}

