package com.projecte;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javafx.application.Application;
import javafx.stage.Stage;

public class BuildDatabase extends Application {

    public static String selected_path;

    public static void main(String input_path) {
        AppData db = AppData.getInstance();
        selected_path = input_path;
        db.connect(selected_path);

        // Crear las tablas necesarias sin borrar las tablas del jugador
        cleanAllTables();
        createAllTables();

        // Insertar Pokémon, objetos y efectividad de tipos
        insertAllPokemon();
        insertAllItems();
        insertAllAttacks();
        insertAllTypeEffectiveness();

        // Verificar si existen registros en GameStats
        ArrayList<java.util.HashMap<String, Object>> gameStatsResult = db.query("SELECT COUNT(*) AS count FROM GameStats;");
        boolean hasGameStats = !gameStatsResult.isEmpty() && ((Number) gameStatsResult.get(0).get("count")).intValue() > 0;

        // Verificar si existen registros en ItemInventory
        ArrayList<java.util.HashMap<String, Object>> itemInventoryResult = db.query("SELECT COUNT(*) AS count FROM ItemInventory;");
        boolean hasItemInventory = !itemInventoryResult.isEmpty() && ((Number) itemInventoryResult.get(0).get("count")).intValue() > 0;

        // Verificar si PlayerPokemon tiene 3 o más con unlocked = 1
        ArrayList<java.util.HashMap<String, Object>> playerPokemonResult = db.query("SELECT COUNT(*) AS count FROM PlayerPokemon WHERE unlocked = 1;");
        int unlockedPokemonCount = playerPokemonResult.isEmpty() ? 0 : ((Number) playerPokemonResult.get(0).get("count")).intValue();

        // Insertar datos iniciales si no existen
        if (!hasGameStats) {
            db.update("INSERT INTO GameStats (id, total_experience, battles_played, max_win_streak, current_win_streak) VALUES (1, 0, 0, 0, 0);");
        }

        if (!hasItemInventory) {
            db.update("INSERT INTO ItemInventory (item_id, quantity) SELECT id, 0 FROM Item;");
        }

        if (unlockedPokemonCount < 3) {
            // Si no hay suficientes Pokémon desbloqueados, insertar 3 Pokémon desbloqueados
            Random random = new Random();
            HashSet<Integer> unlockedPokemonIds = new HashSet<>();
            while (unlockedPokemonIds.size() < 3) {
                int pokemonId = random.nextInt(251) + 1; // IDs del 1 al 251
                if (!unlockedPokemonIds.contains(pokemonId)) {
                    unlockedPokemonIds.add(pokemonId);
                    db.update("INSERT INTO PlayerPokemon (pokemon_id, max_hp, attack, stamina, unlocked) VALUES (" +
                          pokemonId + ", 100, 50, 30, 1);");

                    System.out.println("Pokémon desbloqueado: " + pokemonId );
                }
            }

            // Insertar el resto de los Pokémon con "unlocked" en false
            for (int i = 1; i <= 251; i++) {
                if (!unlockedPokemonIds.contains(i)) {
                    db.update("INSERT INTO PlayerPokemon (pokemon_id, max_hp, attack, stamina, unlocked) VALUES (" +
                          i + ", 100, 50, 30, 0);");
                }
            }
        }

        db.close();
    }

    

    public static void cleanAllTables() {
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        // Primero eliminamos las tablas si existen (en orden inverso por dependencias)
        String dropTables = """
            DROP TABLE IF EXISTS ItemInventory;
            DROP TABLE IF EXISTS Item;
            DROP TABLE IF EXISTS GameStats;
            DROP TABLE IF EXISTS PokemonAttack;
            DROP TABLE IF EXISTS TypeEffectiveness;
            DROP TABLE IF EXISTS PlayerPokemon;
            DROP TABLE IF EXISTS Attack;
            DROP TABLE IF EXISTS Pokemon;
        """;

       
        db.update(dropTables);
    }

    public static void cleanBaseTables() {
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        // Primero eliminamos las tablas si existen (en orden inverso por dependencias)
        String dropTables = """
            DROP TABLE IF EXISTS Item;
            DROP TABLE IF EXISTS Attack;
            DROP TABLE IF EXISTS TypeEffectiveness;
            DROP TABLE IF EXISTS Pokemon;
        """;

       
        db.update(dropTables);
    }

    public static void createAllTables() {
        AppData db = AppData.getInstance();
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
            """;
            
            db.update(createTables);
    }


    public static void insertAllPokemon() {
        String[][] pokemons = {
            // Primera Generación (1-151)
            {"Bulbasaur", "Grass/Poison", "001.gif", "001.png"},
            {"Ivysaur", "Grass/Poison", "002.gif", "002.png"},
            {"Venusaur", "Grass/Poison", "003.gif", "003.png"},
            {"Charmander", "Fire", "004.gif", "004.png"},
            {"Charmeleon", "Fire", "005.gif", "005.png"},
            {"Charizard", "Fire/Flying", "006.gif", "006.png"},
            {"Squirtle", "Water", "007.gif", "007.png"},
            {"Wartortle", "Water", "008.gif", "008.png"},
            {"Blastoise", "Water", "009.gif", "009.png"},
            {"Caterpie", "Bug", "010.gif", "010.png"},
            {"Metapod", "Bug", "011.gif", "011.png"},
            {"Butterfree", "Bug/Flying", "012.gif", "012.png"},
            {"Weedle", "Bug/Poison", "013.gif", "013.png"},
            {"Kakuna", "Bug/Poison", "014.gif", "014.png"},
            {"Beedrill", "Bug/Poison", "015.gif", "015.png"},
            {"Pidgey", "Normal/Flying", "016.gif", "016.png"},
            {"Pidgeotto", "Normal/Flying", "017.gif", "017.png"},
            {"Pidgeot", "Normal/Flying", "018.gif", "018.png"},
            {"Rattata", "Normal", "019.gif", "019.png"},
            {"Raticate", "Normal", "020.gif", "020.png"},
            {"Spearow", "Normal/Flying", "021.gif", "021.png"},
            {"Fearow", "Normal/Flying", "022.gif", "022.png"},
            {"Ekans", "Poison", "023.gif", "023.png"},
            {"Arbok", "Poison", "024.gif", "024.png"},
            {"Pikachu", "Electric", "025.gif", "025.png"},
            {"Raichu", "Electric", "026.gif", "026.png"},
            {"Sandshrew", "Ground", "027.gif", "027.png"},
            {"Sandslash", "Ground", "028.gif", "028.png"},
            {"Nidoran♀", "Poison", "029.gif", "029.png"},
            {"Nidorina", "Poison", "030.gif", "030.png"},
            {"Nidoqueen", "Poison/Ground", "031.gif", "031.png"},
            {"Nidoran♂", "Poison", "032.gif", "032.png"},
            {"Nidorino", "Poison", "033.gif", "033.png"},
            {"Nidoking", "Poison/Ground", "034.gif", "034.png"},
            {"Clefairy", "Fairy", "035.gif", "035.png"},
            {"Clefable", "Fairy", "036.gif", "036.png"},
            {"Vulpix", "Fire", "037.gif", "037.png"},
            {"Ninetales", "Fire", "038.gif", "038.png"},
            {"Jigglypuff", "Normal/Fairy", "039.gif", "039.png"},
            {"Wigglytuff", "Normal/Fairy", "040.gif", "040.png"},
            {"Zubat", "Poison/Flying", "041.gif", "041.png"},
            {"Golbat", "Poison/Flying", "042.gif", "042.png"},
            {"Oddish", "Grass/Poison", "043.gif", "043.png"},
            {"Gloom", "Grass/Poison", "044.gif", "044.png"},
            {"Vileplume", "Grass/Poison", "045.gif", "045.png"},
            {"Paras", "Bug/Grass", "046.gif", "046.png"},
            {"Parasect", "Bug/Grass", "047.gif", "047.png"},
            {"Venonat", "Bug/Poison", "048.gif", "048.png"},
            {"Venomoth", "Bug/Poison", "049.gif", "049.png"},
            {"Diglett", "Ground", "050.gif", "050.png"},
            {"Dugtrio", "Ground", "051.gif", "051.png"},
            {"Meowth", "Normal", "052.gif", "052.png"},
            {"Persian", "Normal", "053.gif", "053.png"},
            {"Psyduck", "Water", "054.gif", "054.png"},
            {"Golduck", "Water", "055.gif", "055.png"},
            {"Mankey", "Fighting", "056.gif", "056.png"},
            {"Primeape", "Fighting", "057.gif", "057.png"},
            {"Growlithe", "Fire", "058.gif", "058.png"},
            {"Arcanine", "Fire", "059.gif", "059.png"},
            {"Poliwag", "Water", "060.gif", "060.png"},
            {"Poliwhirl", "Water", "061.gif", "061.png"},
            {"Poliwrath", "Water/Fighting", "062.gif", "062.png"},
            {"Abra", "Psychic", "063.gif", "063.png"},
            {"Kadabra", "Psychic", "064.gif", "064.png"},
            {"Alakazam", "Psychic", "065.gif", "065.png"},
            {"Machop", "Fighting", "066.gif", "066.png"},
            {"Machoke", "Fighting", "067.gif", "067.png"},
            {"Machamp", "Fighting", "068.gif", "068.png"},
            {"Bellsprout", "Grass/Poison", "069.gif", "069.png"},
            {"Weepinbell", "Grass/Poison", "070.gif", "070.png"},
            {"Victreebel", "Grass/Poison", "071.gif", "071.png"},
            {"Tentacool", "Water/Poison", "072.gif", "072.png"},
            {"Tentacruel", "Water/Poison", "073.gif", "073.png"},
            {"Geodude", "Rock/Ground", "074.gif", "074.png"},
            {"Graveler", "Rock/Ground", "075.gif", "075.png"},
            {"Golem", "Rock/Ground", "076.gif", "076.png"},
            {"Ponyta", "Fire", "077.gif", "077.png"},
            {"Rapidash", "Fire", "078.gif", "078.png"},
            {"Slowpoke", "Water/Psychic", "079.gif", "079.png"},
            {"Slowbro", "Water/Psychic", "080.gif", "080.png"},
            {"Magnemite", "Electric", "081.gif", "081.png"},
            {"Magneton", "Electric", "082.gif", "082.png"},
            {"Farfetch", "Normal/Flying", "083.gif", "083.png"},
            {"Doduo", "Normal/Flying", "084.gif", "084.png"},
            {"Dodrio", "Normal/Flying", "085.gif", "085.png"},
            {"Seel", "Water", "086.gif", "086.png"},
            {"Dewgong", "Water/Ice", "087.gif", "087.png"},
            {"Grimer", "Poison", "088.gif", "088.png"},
            {"Muk", "Poison", "089.gif", "089.png"},
            {"Shellder", "Water", "090.gif", "090.png"},
            {"Cloyster", "Water/Ice", "091.gif", "091.png"},
            {"Gastly", "Ghost/Poison", "092.gif", "092.png"},
            {"Haunter", "Ghost/Poison", "093.gif", "093.png"},
            {"Gengar", "Ghost/Poison", "094.gif", "094.png"},
            {"Onix", "Rock/Ground", "095.gif", "095.png"},
            {"Drowzee", "Psychic", "096.gif", "096.png"},
            {"Hypno", "Psychic", "097.gif", "097.png"},
            {"Krabby", "Water", "098.gif", "098.png"},
            {"Kingler", "Water", "099.gif", "099.png"},
            {"Voltorb", "Electric", "100.gif", "100.png"},
            {"Electrode", "Electric", "101.gif", "101.png"},
            {"Exeggcute", "Grass/Psychic", "102.gif", "102.png"},
            {"Exeggutor", "Grass/Psychic", "103.gif", "103.png"},
            {"Cubone", "Ground", "104.gif", "104.png"},
            {"Marowak", "Ground", "105.gif", "105.png"},
            {"Hitmonlee", "Fighting", "106.gif", "106.png"},
            {"Hitmonchan", "Fighting", "107.gif", "107.png"},
            {"Lickitung", "Normal", "108.gif", "108.png"},
            {"Koffing", "Poison", "109.gif", "109.png"},
            {"Weezing", "Poison", "110.gif", "110.png"},
            {"Rhyhorn", "Ground/Rock", "111.gif", "111.png"},
            {"Rhydon", "Ground/Rock", "112.gif", "112.png"},
            {"Chansey", "Normal", "113.gif", "113.png"},
            {"Tangela", "Grass", "114.gif", "114.png"},
            {"Kangaskhan", "Normal", "115.gif", "115.png"},
            {"Horsea", "Water", "116.gif", "116.png"},
            {"Seadra", "Water", "117.gif", "117.png"},
            {"Goldeen", "Water", "118.gif", "118.png"},
            {"Seaking", "Water", "119.gif", "119.png"},
            {"Staryu", "Water", "120.gif", "120.png"},
            {"Starmie", "Water/Psychic", "121.gif", "121.png"},
            {"Mr. Mime", "Psychic/Fairy", "122.gif", "122.png"},
            {"Scyther", "Bug/Flying", "123.gif", "123.png"},
            {"Jynx", "Ice/Psychic", "124.gif", "124.png"},
            {"Electabuzz", "Electric", "125.gif", "125.png"},
            {"Magmar", "Fire", "126.gif", "126.png"},
            {"Pinsir", "Bug", "127.gif", "127.png"},
            {"Tauros", "Normal", "128.gif", "128.png"},
            {"Magikarp", "Water", "129.gif", "129.png"},
            {"Gyarados", "Water/Flying", "130.gif", "130.png"},
            {"Lapras", "Water/Ice", "131.gif", "131.png"},
            {"Ditto", "Normal", "132.gif", "132.png"},
            {"Eevee", "Normal", "133.gif", "133.png"},
            {"Vaporeon", "Water", "134.gif", "134.png"},
            {"Jolteon", "Electric", "135.gif", "135.png"},
            {"Flareon", "Fire", "136.gif", "136.png"},
            {"Porygon", "Normal", "137.gif", "137.png"},
            {"Omanyte", "Rock/Water", "138.gif", "138.png"},
            {"Omastar", "Rock/Water", "139.gif", "139.png"},
            {"Kabuto", "Rock/Water", "140.gif", "140.png"},
            {"Kabutops", "Rock/Water", "141.gif", "141.png"},
            {"Aerodactyl", "Rock/Flying", "142.gif", "142.png"},
            {"Snorlax", "Normal", "143.gif", "143.png"},
            {"Articuno", "Ice/Flying", "144.gif", "144.png"},
            {"Zapdos", "Electric/Flying", "145.gif", "145.png"},
            {"Moltres", "Fire/Flying", "146.gif", "146.png"},
            {"Dratini", "Dragon", "147.gif", "147.png"},
            {"Dragonair", "Dragon", "148.gif", "148.png"},
            {"Dragonite", "Dragon/Flying", "149.gif", "149.png"},
            {"Mewtwo", "Psychic", "150.gif", "150.png"},
            {"Mew", "Psychic", "151.gif", "151.png"},

            // Segunda Generación (152-251)// Segunda Generación (152-251)
            {"Chikorita", "Grass", "152.gif", "152.png"},
            {"Bayleef", "Grass", "153.gif", "153.png"},
            {"Meganium", "Grass", "154.gif", "154.png"},
            {"Cyndaquil", "Fire", "155.gif", "155.png"},
            {"Quilava", "Fire", "156.gif", "156.png"},
            {"Typhlosion", "Fire", "157.gif", "157.png"},
            {"Totodile", "Water", "158.gif", "158.png"},
            {"Croconaw", "Water", "159.gif", "159.png"},
            {"Feraligatr", "Water", "160.gif", "160.png"},
            {"Sentret", "Normal", "161.gif", "161.png"},
            {"Furret", "Normal", "162.gif", "162.png"},
            {"Hoothoot", "Normal/Flying", "163.gif", "163.png"},
            {"Noctowl", "Normal/Flying", "164.gif", "164.png"},
            {"Ledyba", "Bug/Flying", "165.gif", "165.png"},
            {"Ledian", "Bug/Flying", "166.gif", "166.png"},
            {"Spinarak", "Bug/Poison", "167.gif", "167.png"},
            {"Ariados", "Bug/Poison", "168.gif", "168.png"},
            {"Crobat", "Poison/Flying", "169.gif", "169.png"},
            {"Chinchou", "Water/Electric", "170.gif", "170.png"},
            {"Lanturn", "Water/Electric", "171.gif", "171.png"},
            {"Pichu", "Electric", "172.gif", "172.png"},
            {"Cleffa", "Fairy", "173.gif", "173.png"},
            {"Igglybuff", "Normal/Fairy", "174.gif", "174.png"},
            {"Togepi", "Fairy", "175.gif", "175.png"},
            {"Togetic", "Fairy/Flying", "176.gif", "176.png"},
            {"Natu", "Psychic/Flying", "177.gif", "177.png"},
            {"Xatu", "Psychic/Flying", "178.gif", "178.png"},
            {"Mareep", "Electric", "179.gif", "179.png"},
            {"Flaaffy", "Electric", "180.gif", "180.png"},
            {"Ampharos", "Electric", "181.gif", "181.png"},
            {"Bellossom", "Grass", "182.gif", "182.png"},
            {"Marill", "Water/Fairy", "183.gif", "183.png"},
            {"Azumarill", "Water/Fairy", "184.gif", "184.png"},
            {"Sudowoodo", "Rock", "185.gif", "185.png"},
            {"Politoed", "Water", "186.gif", "186.png"},
            {"Hoppip", "Grass/Flying", "187.gif", "187.png"},
            {"Skiploom", "Grass/Flying", "188.gif", "188.png"},
            {"Jumpluff", "Grass/Flying", "189.gif", "189.png"},
            {"Aipom", "Normal", "190.gif", "190.png"},
            {"Sunkern", "Grass", "191.gif", "191.png"},
            {"Sunflora", "Grass", "192.gif", "192.png"},
            {"Yanma", "Bug/Flying", "193.gif", "193.png"},
            {"Wooper", "Water/Ground", "194.gif", "194.png"},
            {"Quagsire", "Water/Ground", "195.gif", "195.png"},
            {"Espeon", "Psychic", "196.gif", "196.png"},
            {"Umbreon", "Dark", "197.gif", "197.png"},
            {"Murkrow", "Dark/Flying", "198.gif", "198.png"},
            {"Slowking", "Water/Psychic", "199.gif", "199.png"},
            {"Misdreavus", "Ghost", "200.gif", "200.png"},
            {"Unown", "Psychic", "201.gif", "201.png"},
            {"Wobbuffet", "Psychic", "202.gif", "202.png"},
            {"Girafarig", "Normal/Psychic", "203.gif", "203.png"},
            {"Pineco", "Bug", "204.gif", "204.png"},
            {"Forretress", "Bug/Steel", "205.gif", "205.png"},
            {"Dunsparce", "Normal", "206.gif", "206.png"},
            {"Gligar", "Ground/Flying", "207.gif", "207.png"},
            {"Steelix", "Steel/Ground", "208.gif", "208.png"},
            {"Snubbull", "Fairy", "209.gif", "209.png"},
            {"Granbull", "Fairy", "210.gif", "210.png"},
            {"Qwilfish", "Water/Poison", "211.gif", "211.png"},
            {"Scizor", "Bug/Steel", "212.gif", "212.png"},
            {"Shuckle", "Bug/Rock", "213.gif", "213.png"},
            {"Heracross", "Bug/Fighting", "214.gif", "214.png"},
            {"Sneasel", "Dark/Ice", "215.gif", "215.png"},
            {"Teddiursa", "Normal", "216.gif", "216.png"},
            {"Ursaring", "Normal", "217.gif", "217.png"},
            {"Slugma", "Fire", "218.gif", "218.png"},
            {"Magcargo", "Fire/Rock", "219.gif", "219.png"},
            {"Swinub", "Ice/Ground", "220.gif", "220.png"},
            {"Piloswine", "Ice/Ground", "221.gif", "221.png"},
            {"Corsola", "Water/Rock", "222.gif", "222.png"},
            {"Remoraid", "Water", "223.gif", "223.png"},
            {"Octillery", "Water", "224.gif", "224.png"},
            {"Delibird", "Ice/Flying", "225.gif", "225.png"},
            {"Mantine", "Water/Flying", "226.gif", "226.png"},
            {"Skarmory", "Steel/Flying", "227.gif", "227.png"},
            {"Houndour", "Dark/Fire", "228.gif", "228.png"},
            {"Houndoom", "Dark/Fire", "229.gif", "229.png"},
            {"Kingdra", "Water/Dragon", "230.gif", "230.png"},
            {"Phanpy", "Ground", "231.gif", "231.png"},
            {"Donphan", "Ground", "232.gif", "232.png"},
            {"Porygon2", "Normal", "233.gif", "233.png"},
            {"Stantler", "Normal", "234.gif", "234.png"},
            {"Smeargle", "Normal", "235.gif", "235.png"},
            {"Tyrogue", "Fighting", "236.gif", "236.png"},
            {"Hitmontop", "Fighting", "237.gif", "237.png"},
            {"Smoochum", "Ice/Psychic", "238.gif", "238.png"},
            {"Elekid", "Electric", "239.gif", "239.png"},
            {"Magby", "Fire", "240.gif", "240.png"},
            {"Miltank", "Normal", "241.gif", "241.png"},
            {"Blissey", "Normal", "242.gif", "242.png"},
            {"Raikou", "Electric", "243.gif", "243.png"},
            {"Entei", "Fire", "244.gif", "244.png"},
            {"Suicune", "Water", "245.gif", "245.png"},
            {"Larvitar", "Rock/Ground", "246.gif", "246.png"},
            {"Pupitar", "Rock/Ground", "247.gif", "247.png"},
            {"Tyranitar", "Rock/Dark", "248.gif", "248.png"},
            {"Lugia", "Psychic/Flying", "249.gif", "249.png"},
            {"Ho-Oh", "Fire/Flying", "250.gif", "250.png"},
            {"Celebi", "Psychic/Grass", "251.gif", "251.png"}
        };

        AppData db = AppData.getInstance();
        db.connect("./data/pokemons.sqlite");

        // Iteramos sobre el arreglo de Pokémon para insertar uno por uno
        for (String[] pokemon : pokemons) {
            String name = pokemon[0];
            String type = pokemon[1];
            String imagePath = pokemon[2];
            String iconPath = pokemon[3];

            // Ejecutamos la sentencia SQL para insertar el Pokémon
            db.update("INSERT INTO Pokemon (name, type, image_path, icon_path) VALUES ('" +
                      name + "', '" + type + "', '" + imagePath + "', '" + iconPath + "');");
        }

        System.out.println("Pokémons insertados correctamente.");
    }
    public static void insertAllItems() {
        String[][] items = {
            {"X_Attack", "attack", "10"},
            {"X_Defense", "Defense", "20"},
            {"Bottle_Cap", "All", "100"},
        };

        AppData db = AppData.getInstance();
        db.connect(selected_path);

        // Iteramos sobre el arreglo de objetos para insertar uno por uno
        for (String[] item : items) {
            String name = item[0];
            String effectType = item[1];
            int effectValue = Integer.parseInt(item[2]);

            // Ejecutamos la sentencia SQL para insertar el objeto
            db.update("INSERT INTO Item (name, effect_type, effect_value) VALUES ('" +
                      name + "', '" + effectType + "', '" + effectValue + "');");
        }

        System.out.println("Items insertados correctamente.");
    }

    public static void insertAllAttacks() {
        String[][] attacks = {
            {"Tackle", "Normal", "30", "5"},
            {"Growl", "Normal", "5", "5"},
            {"Ember", "Fire", "30", "5"},
            {"Water Gun", "Water", "30", "5"},
            {"Vine Whip", "Grass", "35", "5"},
            {"Thunder Shock", "Electric", "30", "5"},
            {"Quick Attack", "Normal", "30", "5"},
            {"Confusion", "Psychic", "40", "5"},
            {"Flamethrower", "Fire", "45", "45"},
            {"Hydro Pump", "Water", "45", "45"},
            {"Thunderbolt", "Electric", "45", "45"},
            {"Razor Leaf", "Grass", "45", "35"},
            {"Ice Beam", "Ice", "45", "45"},
            {"Psychic", "Psychic", "45", "45"},
            {"Shadow Ball", "Ghost", "45", "45"},
            {"Brick Break", "Fighting", "45", "40"},
            {"Earthquake", "Ground", "45", "45"},
            {"Air Slash", "Flying", "45", "40"},
            {"Sludge Bomb", "Poison", "45", "45"},
            {"Iron Tail", "Steel", "45", "45"},
            {"Rock Slide", "Rock", "45", "40"},
            {"Dragon Claw", "Dragon", "45", "45"},
            {"Dark Pulse", "Dark", "45", "45"}
        };

        AppData db = AppData.getInstance();
        db.connect("./data/pokemons.sqlite");

        // Iteramos sobre el arreglo de ataques para insertar uno por uno
        for (String[] attack : attacks) {
            String name = attack[0];
            String type = attack[1];
            int damage = Integer.parseInt(attack[2]);
            int staminaCost = Integer.parseInt(attack[3]);

            // Ejecutamos la sentencia SQL para insertar el ataque
            db.update("INSERT INTO Attack (name, type, damage, stamina_cost) VALUES ('" +
                      name + "', '" + type + "', " + damage + ", " + staminaCost + ");");
        }
        System.out.println("Attacks insertados correctamente.");        

    }
   

    public static void insertAllTypeEffectiveness() {
        String[][] typeEffectiveness = {
            // Fire
            {"Fire", "Grass", "2.0"},
            {"Fire", "Bug", "2.0"},
            {"Fire", "Ice", "2.0"},
            {"Fire", "Steel", "2.0"},
            {"Fire", "Fire", "0.5"},
            {"Fire", "Water", "0.5"},
            {"Fire", "Rock", "0.5"},
            {"Fire", "Dragon", "0.5"},

            // Water
            {"Water", "Fire", "2.0"},
            {"Water", "Ground", "2.0"},
            {"Water", "Rock", "2.0"},
            {"Water", "Water", "0.5"},
            {"Water", "Grass", "0.5"},
            {"Water", "Dragon", "0.5"},

            // Grass
            {"Grass", "Water", "2.0"},
            {"Grass", "Ground", "2.0"},
            {"Grass", "Rock", "2.0"},
            {"Grass", "Fire", "0.5"},
            {"Grass", "Grass", "0.5"},
            {"Grass", "Bug", "0.5"},
            {"Grass", "Flying", "0.5"},
            {"Grass", "Poison", "0.5"},
            {"Grass", "Dragon", "0.5"},
            {"Grass", "Steel", "0.5"},

            // Electric
            {"Electric", "Water", "2.0"},
            {"Electric", "Flying", "2.0"},
            {"Electric", "Electric", "0.5"},
            {"Electric", "Grass", "0.5"},
            {"Electric", "Dragon", "0.5"},
            {"Electric", "Ground", "0.0"},

            // Ground
            {"Ground", "Fire", "2.0"},
            {"Ground", "Electric", "2.0"},
            {"Ground", "Poison", "2.0"},
            {"Ground", "Rock", "2.0"},
            {"Ground", "Steel", "2.0"},
            {"Ground", "Grass", "0.5"},
            {"Ground", "Bug", "0.5"},
            {"Ground", "Flying", "0.0"},

            // Ice
            {"Ice", "Grass", "2.0"},
            {"Ice", "Ground", "2.0"},
            {"Ice", "Flying", "2.0"},
            {"Ice", "Dragon", "2.0"},
            {"Ice", "Fire", "0.5"},
            {"Ice", "Water", "0.5"},
            {"Ice", "Ice", "0.5"},
            {"Ice", "Steel", "0.5"},

            // Psychic
            {"Psychic", "Fighting", "2.0"},
            {"Psychic", "Poison", "2.0"},
            {"Psychic", "Psychic", "0.5"},
            {"Psychic", "Steel", "0.5"},
            {"Psychic", "Dark", "0.0"},

            // Dark
            {"Dark", "Psychic", "2.0"},
            {"Dark", "Ghost", "2.0"},
            {"Dark", "Fighting", "0.5"},
            {"Dark", "Dark", "0.5"},
            {"Dark", "Fairy", "0.5"},

            // Fairy
            {"Fairy", "Fighting", "2.0"},
            {"Fairy", "Dragon", "2.0"},
            {"Fairy", "Dark", "2.0"},
            {"Fairy", "Fire", "0.5"},
            {"Fairy", "Poison", "0.5"},
            {"Fairy", "Steel", "0.5"},

            // Ghost
            {"Ghost", "Psychic", "2.0"},
            {"Ghost", "Ghost", "2.0"},
            {"Ghost", "Dark", "0.5"},
            {"Ghost", "Normal", "0.0"},

            // Fighting
            {"Fighting", "Normal", "2.0"},
            {"Fighting", "Ice", "2.0"},
            {"Fighting", "Rock", "2.0"},
            {"Fighting", "Dark", "2.0"},
            {"Fighting", "Steel", "2.0"},
            {"Fighting", "Poison", "0.5"},
            {"Fighting", "Flying", "0.5"},
            {"Fighting", "Psychic", "0.5"},
            {"Fighting", "Fairy", "0.5"},
            {"Fighting", "Ghost", "0.0"}
        };
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        // Iteramos sobre el arreglo de efectividad de tipos para insertar uno por uno
        for (String[] effectiveness : typeEffectiveness) {
            String attackType = effectiveness[0];
            String targetType = effectiveness[1];
            double multiplier = Double.parseDouble(effectiveness[2]);

            // Ejecutamos la sentencia SQL para insertar la efectividad de tipo
            db.update("INSERT INTO TypeEffectiveness (attack_type, target_type, multiplier) VALUES ('" +
                      attackType + "', '" + targetType + "', '" + multiplier + "');");
        }
        System.out.println("Type effectiveness insertados correctamente.");
    }

    public static void buildJSON() {
        // TODO
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
