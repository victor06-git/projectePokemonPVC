package com.projecte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class BuildDatabase {
    public static String selected_path;

    // Main method to initialize the database
    public static void main(String input_path) {
        AppData db = AppData.getInstance();
        selected_path = input_path;
        db.connect(selected_path);
        
        try {
            createOrReplaceAllTables(db);
            insertAllPokemon(db);
            insertAllItems(db);
            insertAllAttacks(db);
            insertAllTypeEffectiveness(db);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    // Initialize base game stats
    public static void insertBaseStats() {
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        db.update("INSERT INTO GameStats (id) VALUES (1);");
        db.close();
    }

    /**
     * Retrieves all game statistics including experience, battles played, win streaks, etc.
     * Combines data from GameStats and PlayerPokemon tables.
     * @return HashMap containing all game statistics with appropriate keys
     */
    public HashMap<String, Integer> getGameStats() {
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        // Get base game stats
        ArrayList<HashMap<String, Object>> result = db.query("SELECT * FROM GameStats;");
        // Get count of unlocked pokemon
        ArrayList<HashMap<String, Object>> result2 = db.query("SELECT count(*) as count FROM PlayerPokemon WHERE unlocked = 1;");

        if (result.isEmpty() || result2.isEmpty()) {
            System.out.println("No game statistics found.");
            return null;
        }

        // Combine both result sets
        HashMap<String, Object> stats = new HashMap<>(result.get(0));
        stats.putAll(result2.get(0));

        System.out.println("Objects: " + stats);
        
        // Extract and calculate statistics
        Integer totalExperience = (Integer) stats.get("total_experience");
        Integer level = totalExperience / 1000;
        Integer battlesPlayed = (Integer) stats.get("battles_played");
        Integer maxWinStreak = (Integer) stats.get("max_win_streak");
        Integer currentWinStreak = (Integer) stats.get("current_win_streak");
        Integer pokemonsCaught = (Integer) stats.get("count");
        

        HashMap<String, Integer> gameStats = new HashMap<>();

        // Populate the final stats map
        gameStats.put("total_experience", totalExperience);
        gameStats.put("battles_played", battlesPlayed);
        gameStats.put("max_win_streak", maxWinStreak);
        gameStats.put("current_win_streak", currentWinStreak);
        gameStats.put("level", level);
        gameStats.put("pokemons_caught", pokemonsCaught);
    
        db.close();

        return gameStats;
    }

    /**
     * Creates or replaces all database tables with their schema definitions.
     * This is the foundation of the database structure.
     * @param db The database connection instance
     */
    public static void createOrReplaceAllTables(AppData db) {
        String createTables = """
                CREATE TABLE IF NOT EXISTS Pokemon (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    type TEXT NOT NULL,
                    image_path TEXT,
                    icon_path TEXT
                );

                CREATE TABLE IF NOT EXISTS PlayerPokemon (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    pokemon_id INTEGER NOT NULL,
                    nickname TEXT,
                    max_hp INTEGER NOT NULL,
                    attack INTEGER NOT NULL,
                    stamina INTEGER NOT NULL,
                    unlocked BOOLEAN DEFAULT 0,
                    FOREIGN KEY (pokemon_id) REFERENCES Pokemon(id)
                );

                CREATE TABLE IF NOT EXISTS Attack (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    type TEXT NOT NULL,
                    damage INTEGER NOT NULL,
                    stamina_cost INTEGER NOT NULL
                );

                CREATE TABLE IF NOT EXISTS PokemonAttack (
                    pokemon_id INTEGER NOT NULL,
                    attack_id INTEGER NOT NULL,
                    PRIMARY KEY (pokemon_id, attack_id),
                    FOREIGN KEY (pokemon_id) REFERENCES Pokemon(id),
                    FOREIGN KEY (attack_id) REFERENCES Attack(id)
                );

                CREATE TABLE IF NOT EXISTS TypeEffectiveness (
                    attack_type TEXT NOT NULL,
                    target_type TEXT NOT NULL,
                    multiplier REAL NOT NULL,
                    PRIMARY KEY (attack_type, target_type)
                );

                CREATE TABLE IF NOT EXISTS Item (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE,
                    effect_type TEXT NOT NULL,
                    effect_value INTEGER
                );

                CREATE TABLE IF NOT EXISTS ItemInventory (
                    item_id INTEGER PRIMARY KEY,
                    quantity INTEGER DEFAULT 0,
                    FOREIGN KEY (item_id) REFERENCES Item(id)
                );

                CREATE TABLE IF NOT EXISTS GameStats (
                    id INTEGER PRIMARY KEY CHECK (id = 1),
                    total_experience INTEGER DEFAULT 0,
                    battles_played INTEGER DEFAULT 0,
                    max_win_streak INTEGER DEFAULT 0,
                    current_win_streak INTEGER DEFAULT 0
                );

                CREATE TABLE IF NOT EXISTS Battle (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,        
                    date TEXT NOT NULL,                          
                    map TEXT,                                  
                    winner TEXT CHECK(winner IN ('Player', 'Computer'))  
                );

                CREATE TABLE IF NOT EXISTS BattlePokemon (
                    battle_id INTEGER NOT NULL,                 
                    is_player BOOLEAN NOT NULL,                 
                    pokemon_id INTEGER NOT NULL,               
                    PRIMARY KEY (battle_id, is_player, pokemon_id),
                    FOREIGN KEY (battle_id) REFERENCES Battle(id),
                    FOREIGN KEY (pokemon_id) REFERENCES PlayerPokemon(id)
                );

                CREATE TABLE IF NOT EXISTS ItemEffect (
                    player_pokemon_id INTEGER NOT NULL,        
                    item_id INTEGER NOT NULL,                   
                    active BOOLEAN DEFAULT 1,                    
                    PRIMARY KEY (player_pokemon_id, item_id),
                    FOREIGN KEY (player_pokemon_id) REFERENCES PlayerPokemon(id),
                    FOREIGN KEY (item_id) REFERENCES Item(id)
                );

                CREATE TABLE IF NOT EXISTS ItemReward (
                    battle_id INTEGER NOT NULL,
                    item_id INTEGER NOT NULL,                   
                    quantity INTEGER DEFAULT 1,                
                    PRIMARY KEY (battle_id, item_id),
                    FOREIGN KEY (battle_id) REFERENCES Battle(id),
                    FOREIGN KEY (item_id) REFERENCES Item(id)
                );
            """;
            
        // Execute each table creation statement separately
        String[] tableStatements = createTables.split(";");
        for (String statement : tableStatements) {
            if (!statement.trim().isEmpty()) {
                db.update(statement.trim() + ";");
            }
        }
    }

    /**
     * Inserts all Pokémon data into the database.
     * Includes all Pokémon from generations 1 and 2 with their types and image paths.
     * @param db The database connection instance
     */
    public static void insertAllPokemon(AppData db) {
        cleanAndRecreateTable(db, "Pokemon");

        // Array containing all Pokémon data: name, type, gif path, png path
        String[][] pokemons = {
            // Generation 1 (1-151)
            {"Bulbasaur", "Grass/Poison", "001.gif", "001.png"},
            // ... (rest of the Pokémon data)
        };

        // Insert each Pokémon into the database
        for (String[] pokemon : pokemons) {
            db.update("INSERT INTO Pokemon (name, type, image_path, icon_path) VALUES ('" +
                    pokemon[0] + "', '" + pokemon[1] + "', '" + pokemon[2] + "', '" + pokemon[3] + "');");
        }
        System.out.println("Pokémon inserted successfully.");
    }

    /**
     * Inserts all items into the database and initializes inventory.
     * @param db The database connection instance
     */
    public static void insertAllItems(AppData db) {
        if (db == null) {
            System.out.println("Error: db is null");
            return;
        } else if (db.query("SELECT count(*) FROM Item;").isEmpty()) {
            cleanAndRecreateTable(db, "Item");
        }
        
        // Item data: name, effect type, effect value
        String[][] items = {
            {"X_Attack", "attack", "10"},
            {"X_Defense", "defense", "20"},
            {"Bottle_Cap", "all", "100"}
        };

        // Insert or replace items
        for (String[] item : items) {
            db.update("INSERT OR REPLACE INTO Item (name, effect_type, effect_value) VALUES ('" +
                    item[0] + "', '" + item[1] + "', '" + item[2] + "');");
        }

        // Initialize inventory with 0 quantity for all items
        ArrayList<HashMap<String, Object>> itemRows = db.query("SELECT id FROM Item");
        for (HashMap<String, Object> row : itemRows) {
            int itemId = (int) row.get("id");
            db.update("INSERT OR REPLACE INTO ItemInventory (item_id, quantity) VALUES (" + itemId + ", 0);");
        }

        System.out.println("Items inserted successfully.");
    }

    /**
     * Inserts all attack moves into the database and assigns them randomly to Pokémon.
     * @param db The database connection instance
     */
    public static void insertAllAttacks(AppData db) {
        cleanAndRecreateTable(db, "Attack");

        // Attack data: name, type, damage, stamina cost
        String[][] attacks = {
            {"Tackle", "Normal", "30", "5"},
            // ... (rest of the attack data)
        };

        // Insert all attacks into the database
        for (String[] attack : attacks) {
            db.update("INSERT OR REPLACE INTO Attack (name, type, damage, stamina_cost) VALUES ('" +
                attack[0] + "', '" + attack[1] + "', " + attack[2] + ", " + attack[3] + ");");
        }
        System.out.println("Attacks inserted successfully.");      
        
        // Assign attacks to Pokémon based on type compatibility
        assignRandomAttacksToPokemon(db);
    }

    /**
     * Complex function that assigns random attacks to each Pokémon based on their type(s).
     * For single-type Pokémon: assigns 4 attacks of their type.
     * For dual-type Pokémon: assigns 2 attacks of each type.
     * @param db The database connection instance
     */
    private static void assignRandomAttacksToPokemon(AppData db) {
        // Clear existing attack assignments
        db.update("DELETE FROM PokemonAttack;");
        
        // Get all Pokémon and their types
        ArrayList<HashMap<String, Object>> pokeTypes = db.query("SELECT id, type FROM Pokemon");
        // Get all attacks and their types
        ArrayList<HashMap<String, Object>> attackTypes = db.query("SELECT id, type FROM Attack");
        Random rand = new Random();
        
        // Process each Pokémon
        for (HashMap<String, Object> pokeType : pokeTypes) {
            int pokeId = (int) pokeType.get("id");
            String pokeTypeStr = (String) pokeType.get("type");
            // Split dual types
            String[] types = pokeTypeStr.split("/");

            ArrayList<Integer> selectedAttackIds = new ArrayList<>();
            int maxAttacks = 4;  // Maximum attacks per Pokémon
            int attacksPerType = types.length == 2 ? 2 : 4;  // 2 per type if dual-type

            // For each type the Pokémon has
            for (String type : types) {
                // Get attacks that match this type
                ArrayList<Integer> matchingAttacks = getMatchingAttacks(attackTypes, type);
                // Randomly select attacks for this type
                HashSet<Integer> chosen = selectRandomAttacks(matchingAttacks, attacksPerType, rand);
                selectedAttackIds.addAll(chosen);
            }

            // Ensure we don't exceed max attacks
            limitAttacks(selectedAttackIds, maxAttacks, rand);
            // Save the selected attacks for this Pokémon
            insertSelectedAttacks(db, pokeId, selectedAttackIds);
        }
        System.out.println("Attacks assigned to Pokémon successfully.");
    }

    /**
     * Helper function to find all attacks of a specific type.
     * @param attackTypes List of all attacks with their types
     * @param type The type to filter by
     * @return List of attack IDs that match the type
     */
    private static ArrayList<Integer> getMatchingAttacks(ArrayList<HashMap<String, Object>> attackTypes, String type) {
        ArrayList<Integer> matchingAttacks = new ArrayList<>();
        for (HashMap<String, Object> attackType : attackTypes) {
            if (((String) attackType.get("type")).equals(type)) {
                matchingAttacks.add((int) attackType.get("id"));
            }
        }
        return matchingAttacks;
    }

    /**
     * Randomly selects attacks from the available pool.
     * @param matchingAttacks List of eligible attack IDs
     * @param attacksPerType Number of attacks to select
     * @param rand Random number generator
     * @return Set of selected attack IDs
     */
    private static HashSet<Integer> selectRandomAttacks(ArrayList<Integer> matchingAttacks, int attacksPerType, Random rand) {
        HashSet<Integer> chosen = new HashSet<>();
        while (chosen.size() < Math.min(attacksPerType, matchingAttacks.size())) {
            chosen.add(matchingAttacks.get(rand.nextInt(matchingAttacks.size())));
        }
        return chosen;
    }

    /**
     * Reduces the number of attacks if exceeding the maximum allowed.
     * @param selectedAttackIds List of currently selected attack IDs
     * @param maxAttacks Maximum number of attacks allowed
     * @param rand Random number generator
     */
    private static void limitAttacks(ArrayList<Integer> selectedAttackIds, int maxAttacks, Random rand) {
        while (selectedAttackIds.size() > maxAttacks) {
            selectedAttackIds.remove(rand.nextInt(selectedAttackIds.size()));
        }
    }

    /**
     * Inserts the selected attacks for a Pokémon into the database.
     * @param db Database connection instance
     * @param pokeId Pokémon ID
     * @param selectedAttackIds List of attack IDs to assign
     */
    private static void insertSelectedAttacks(AppData db, int pokeId, ArrayList<Integer> selectedAttackIds) {
        for (int attackId : selectedAttackIds) {
            db.update("INSERT OR REPLACE INTO PokemonAttack (pokemon_id, attack_id) VALUES (" +
                pokeId + ", " + attackId + ");");
        }
    }

    /**
     * Inserts type effectiveness multipliers into the database.
     * Defines how effective each attack type is against each defending type.
     * @param db The database connection instance
     */
    public static void insertAllTypeEffectiveness(AppData db) {
        // Effectiveness data: attack type, target type, multiplier
        String[][] typeEffectiveness = {
            // Fire
            {"Fire", "Grass", "2.0"},
            // ... (rest of type effectiveness data)
        };

        // Insert all type effectiveness rules
        for (String[] effectiveness : typeEffectiveness) {
            db.update("INSERT OR REPLACE INTO TypeEffectiveness (attack_type, target_type, multiplier) VALUES ('" +
                    effectiveness[0] + "', '" + effectiveness[1] + "', '" + effectiveness[2] + "');");
        }
        System.out.println("Type effectiveness inserted successfully.");
    }

    /**
     * Cleans and recreates a specific table.
     * Special handling for join tables like PokemonAttack.
     * @param db Database connection instance
     * @param tableName Name of table to clean/recreate
     */
    private static void cleanAndRecreateTable(AppData db, String tableName) {
        // For join tables, just clear the data
        if(tableName.equals("PokemonAttack")) {
            db.update("DELETE FROM PokemonAttack;");
        } else {
            // For regular tables, drop and recreate
            db.update("DROP TABLE IF EXISTS " + tableName + ";");
            createOrReplaceAllTables(db);
        }
    }
}