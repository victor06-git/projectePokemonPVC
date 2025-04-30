package com.projecte;

public class createDatabase {


    public static void main(String[] args) {
        createTables();
    }

    public static void createTables() {

        AppData db = AppData.getInstance();
        db.connect("./data/pokemons.sqlite");
        
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

        // Luego creamos las tablas (en el orden correcto considerando las dependencias)
        String createTables = """
            CREATE TABLE Pokemon (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                type TEXT NOT NULL,
                image_path TEXT
            );

            CREATE TABLE PlayerPokemon (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                pokemon_id INTEGER NOT NULL,
                nickname TEXT,
                max_hp INTEGER NOT NULL,
                attack INTEGER NOT NULL,
                stamina INTEGER NOT NULL,
                unlocked BOOLEAN DEFAULT 0,
                FOREIGN KEY (pokemon_id) REFERENCES Pokemon(id)
            );

            CREATE TABLE Attack (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                type TEXT NOT NULL,
                damage INTEGER NOT NULL,
                stamina_cost INTEGER NOT NULL
            );

            CREATE TABLE PokemonAttack (
                pokemon_id INTEGER NOT NULL,
                attack_id INTEGER NOT NULL,
                PRIMARY KEY (pokemon_id, attack_id),
                FOREIGN KEY (pokemon_id) REFERENCES Pokemon(id),
                FOREIGN KEY (attack_id) REFERENCES Attack(id)
            );

            CREATE TABLE TypeEffectiveness (
                attack_type TEXT NOT NULL,
                target_type TEXT NOT NULL,
                multiplier REAL NOT NULL,
                PRIMARY KEY (attack_type, target_type)
            );

            CREATE TABLE Item (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                effect_type TEXT NOT NULL,
                effect_value INTEGER
            );

            CREATE TABLE ItemInventory (
                item_id INTEGER PRIMARY KEY,
                quantity INTEGER DEFAULT 0,
                FOREIGN KEY (item_id) REFERENCES Item(id)
            );

            CREATE TABLE GameStats (
                id INTEGER PRIMARY KEY CHECK (id = 1),
                total_experience INTEGER DEFAULT 0,
                battles_played INTEGER DEFAULT 0,
                max_win_streak INTEGER DEFAULT 0,
                current_win_streak INTEGER DEFAULT 0
            );
        """;
        db.update(dropTables);
        db.update(createTables);

    }

}