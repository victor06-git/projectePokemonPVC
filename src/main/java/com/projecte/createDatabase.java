package com.projecte;

public class createDatabase {

    public static void main(String[] args) {
        setTables();
        insertAllPokemon();
    }

    public static void setTables() {
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

    public static void insertAllPokemon() {
        String[][] pokemons = {
            // Primera Generación (1-151)
            {"Bulbasaur", "Grass/Poison", "bulbasaur.gif"},
            {"Ivysaur", "Grass/Poison", "ivysaur.gif"},
            {"Venusaur", "Grass/Poison", "venusaur.gif"},
            {"Charmander", "Fire", "charmander.gif"},
            {"Charmeleon", "Fire", "charmeleon.gif"},
            {"Charizard", "Fire/Flying", "charizard.gif"},
            {"Squirtle", "Water", "squirtle.gif"},
            {"Wartortle", "Water", "wartortle.gif"},
            {"Blastoise", "Water", "blastoise.gif"},
            {"Caterpie", "Bug", "caterpie.gif"},
            {"Metapod", "Bug", "metapod.gif"},
            {"Butterfree", "Bug/Flying", "butterfree.gif"},
            {"Weedle", "Bug/Poison", "weedle.gif"},
            {"Kakuna", "Bug/Poison", "kakuna.gif"},
            {"Beedrill", "Bug/Poison", "beedrill.gif"},
            {"Pidgey", "Normal/Flying", "pidgey.gif"},
            {"Pidgeotto", "Normal/Flying", "pidgeotto.gif"},
            {"Pidgeot", "Normal/Flying", "pidgeot.gif"},
            {"Rattata", "Normal", "rattata.gif"},
            {"Raticate", "Normal", "raticate.gif"},
            {"Spearow", "Normal/Flying", "spearow.gif"},
            {"Fearow", "Normal/Flying", "fearow.gif"},
            {"Ekans", "Poison", "ekans.gif"},
            {"Arbok", "Poison", "arbok.gif"},
            {"Pikachu", "Electric", "pikachu.gif"},
            {"Raichu", "Electric", "raichu.gif"},
            {"Sandshrew", "Ground", "sandshrew.gif"},
            {"Sandslash", "Ground", "sandslash.gif"},
            {"Nidoran♀", "Poison", "nidoranf.gif"},
            {"Nidorina", "Poison", "nidorina.gif"},
            {"Nidoqueen", "Poison/Ground", "nidoqueen.gif"},
            {"Nidoran♂", "Poison", "nidoranm.gif"},
            {"Nidorino", "Poison", "nidorino.gif"},
            {"Nidoking", "Poison/Ground", "nidoking.gif"},
            {"Clefairy", "Fairy", "clefairy.gif"},
            {"Clefable", "Fairy", "clefable.gif"},
            {"Vulpix", "Fire", "vulpix.gif"},
            {"Ninetales", "Fire", "ninetales.gif"},
            {"Jigglypuff", "Normal/Fairy", "jigglypuff.gif"},
            {"Wigglytuff", "Normal/Fairy", "wigglytuff.gif"},
            {"Zubat", "Poison/Flying", "zubat.gif"},
            {"Golbat", "Poison/Flying", "golbat.gif"},
            {"Oddish", "Grass/Poison", "oddish.gif"},
            {"Gloom", "Grass/Poison", "gloom.gif"},
            {"Vileplume", "Grass/Poison", "vileplume.gif"},
            {"Paras", "Bug/Grass", "paras.gif"},
            {"Parasect", "Bug/Grass", "parasect.gif"},
            {"Venonat", "Bug/Poison", "venonat.gif"},
            {"Venomoth", "Bug/Poison", "venomoth.gif"},
            {"Diglett", "Ground", "diglett.gif"},
            {"Dugtrio", "Ground", "dugtrio.gif"},
            {"Meowth", "Normal", "meowth.gif"},
            {"Persian", "Normal", "persian.gif"},
            {"Psyduck", "Water", "psyduck.gif"},
            {"Golduck", "Water", "golduck.gif"},
            {"Mankey", "Fighting", "mankey.gif"},
            {"Primeape", "Fighting", "primeape.gif"},
            {"Growlithe", "Fire", "growlithe.gif"},
            {"Arcanine", "Fire", "arcanine.gif"},
            {"Poliwag", "Water", "poliwag.gif"},
            {"Poliwhirl", "Water", "poliwhirl.gif"},
            {"Poliwrath", "Water/Fighting", "poliwrath.gif"},
            {"Abra", "Psychic", "abra.gif"},
            {"Kadabra", "Psychic", "kadabra.gif"},
            {"Alakazam", "Psychic", "alakazam.gif"},
            {"Machop", "Fighting", "machop.gif"},
            {"Machoke", "Fighting", "machoke.gif"},
            {"Machamp", "Fighting", "machamp.gif"},
            {"Bellsprout", "Grass/Poison", "bellsprout.gif"},
            {"Weepinbell", "Grass/Poison", "weepinbell.gif"},
            {"Victreebel", "Grass/Poison", "victreebel.gif"},
            {"Tentacool", "Water/Poison", "tentacool.gif"},
            {"Tentacruel", "Water/Poison", "tentacruel.gif"},
            {"Geodude", "Rock/Ground", "geodude.gif"},
            {"Graveler", "Rock/Ground", "graveler.gif"},
            {"Golem", "Rock/Ground", "golem.gif"},
            {"Ponyta", "Fire", "ponyta.gif"},
            {"Rapidash", "Fire", "rapidash.gif"},
            {"Slowpoke", "Water/Psychic", "slowpoke.gif"},
            {"Slowbro", "Water/Psychic", "slowbro.gif"},
            {"Magnemite", "Electric/Steel", "magnemite.gif"},
            {"Magneton", "Electric/Steel", "magneton.gif"},
            {"Farfetch'd", "Normal/Flying", "farfetchd.gif"},
            {"Doduo", "Normal/Flying", "doduo.gif"},
            {"Dodrio", "Normal/Flying", "dodrio.gif"},
            {"Seel", "Water", "seel.gif"},
            {"Dewgong", "Water/Ice", "dewgong.gif"},
            {"Grimer", "Poison", "grimer.gif"},
            {"Muk", "Poison", "muk.gif"},
            {"Shellder", "Water", "shellder.gif"},
            {"Cloyster", "Water/Ice", "cloyster.gif"},
            {"Gastly", "Ghost/Poison", "gastly.gif"},
            {"Haunter", "Ghost/Poison", "haunter.gif"},
            {"Gengar", "Ghost/Poison", "gengar.gif"},
            {"Onix", "Rock/Ground", "onix.gif"},
            {"Drowzee", "Psychic", "drowzee.gif"},
            {"Hypno", "Psychic", "hypno.gif"},
            {"Krabby", "Water", "krabby.gif"},
            {"Kingler", "Water", "kingler.gif"},
            {"Voltorb", "Electric", "voltorb.gif"},
            {"Electrode", "Electric", "electrode.gif"},
            {"Exeggcute", "Grass/Psychic", "exeggcute.gif"},
            {"Exeggutor", "Grass/Psychic", "exeggutor.gif"},
            {"Cubone", "Ground", "cubone.gif"},
            {"Marowak", "Ground", "marowak.gif"},
            {"Hitmonlee", "Fighting", "hitmonlee.gif"},
            {"Hitmonchan", "Fighting", "hitmonchan.gif"},
            {"Lickitung", "Normal", "lickitung.gif"},
            {"Koffing", "Poison", "koffing.gif"},
            {"Weezing", "Poison", "weezing.gif"},
            {"Rhyhorn", "Ground/Rock", "rhyhorn.gif"},
            {"Rhydon", "Ground/Rock", "rhydon.gif"},
            {"Chansey", "Normal", "chansey.gif"},
            {"Tangela", "Grass", "tangela.gif"},
            {"Kangaskhan", "Normal", "kangaskhan.gif"},
            {"Horsea", "Water", "horsea.gif"},
            {"Seadra", "Water", "seadra.gif"},
            {"Goldeen", "Water", "goldeen.gif"},
            {"Seaking", "Water", "seaking.gif"},
            {"Staryu", "Water", "staryu.gif"},
            {"Starmie", "Water/Psychic", "starmie.gif"},
            {"Mr. Mime", "Psychic/Fairy", "mrmime.gif"},
            {"Scyther", "Bug/Flying", "scyther.gif"},
            {"Jynx", "Ice/Psychic", "jynx.gif"},
            {"Electabuzz", "Electric", "electabuzz.gif"},
            {"Magmar", "Fire", "magmar.gif"},
            {"Pinsir", "Bug", "pinsir.gif"},
            {"Tauros", "Normal", "tauros.gif"},
            {"Magikarp", "Water", "magikarp.gif"},
            {"Gyarados", "Water/Flying", "gyarados.gif"},
            {"Lapras", "Water/Ice", "lapras.gif"},
            {"Ditto", "Normal", "ditto.gif"},
            {"Eevee", "Normal", "eevee.gif"},
            {"Vaporeon", "Water", "vaporeon.gif"},
            {"Jolteon", "Electric", "jolteon.gif"},
            {"Flareon", "Fire", "flareon.gif"},
            {"Porygon", "Normal", "porygon.gif"},
            {"Omanyte", "Rock/Water", "omanyte.gif"},
            {"Omastar", "Rock/Water", "omastar.gif"},
            {"Kabuto", "Rock/Water", "kabuto.gif"},
            {"Kabutops", "Rock/Water", "kabutops.gif"},
            {"Aerodactyl", "Rock/Flying", "aerodactyl.gif"},
            {"Snorlax", "Normal", "snorlax.gif"},
            {"Articuno", "Ice/Flying", "articuno.gif"},
            {"Zapdos", "Electric/Flying", "zapdos.gif"},
            {"Moltres", "Fire/Flying", "moltres.gif"},
            {"Dratini", "Dragon", "dratini.gif"},
            {"Dragonair", "Dragon", "dragonair.gif"},
            {"Dragonite", "Dragon/Flying", "dragonite.gif"},
            {"Mewtwo", "Psychic", "mewtwo.gif"},
            {"Mew", "Psychic", "mew.gif"},
        
            // Segunda Generación (152-251)
            {"Chikorita", "Grass", "chikorita.gif"},
            {"Bayleef", "Grass", "bayleef.gif"},
            {"Meganium", "Grass", "meganium.gif"},
            {"Cyndaquil", "Fire", "cyndaquil.gif"},
            {"Quilava", "Fire", "quilava.gif"},
            {"Typhlosion", "Fire", "typhlosion.gif"},
            {"Totodile", "Water", "totodile.gif"},
            {"Croconaw", "Water", "croconaw.gif"},
            {"Feraligatr", "Water", "feraligatr.gif"},
            {"Sentret", "Normal", "sentret.gif"},
            {"Furret", "Normal", "furret.gif"},
            {"Hoothoot", "Normal/Flying", "hoothoot.gif"},
            {"Noctowl", "Normal/Flying", "noctowl.gif"},
            {"Ledyba", "Bug/Flying", "ledyba.gif"},
            {"Ledian", "Bug/Flying", "ledian.gif"},
            {"Spinarak", "Bug/Poison", "spinarak.gif"},
            {"Ariados", "Bug/Poison", "ariados.gif"},
            {"Crobat", "Poison/Flying", "crobat.gif"},
            {"Chinchou", "Water/Electric", "chinchou.gif"},
            {"Lanturn", "Water/Electric", "lanturn.gif"},
            {"Pichu", "Electric", "pichu.gif"},
            {"Cleffa", "Fairy", "cleffa.gif"},
            {"Igglybuff", "Normal/Fairy", "igglybuff.gif"},
            {"Togepi", "Fairy", "togepi.gif"},
            {"Togetic", "Fairy/Flying", "togetic.gif"},
            {"Natu", "Psychic/Flying", "natu.gif"},
            {"Xatu", "Psychic/Flying", "xatu.gif"},
            {"Sunkern", "Grass", "sunkern.gif"},
            {"Sunflora", "Grass", "sunflora.gif"},
            {"Yanma", "Bug/Flying", "yanma.gif"},
            {"Wooper", "Water/Ground", "wooper.gif"},
            {"Quagsire", "Water/Ground", "quagsire.gif"},
            {"Espeon", "Psychic", "espeon.gif"},
            {"Umbreon", "Dark", "umbreon.gif"},
            {"Murkrow", "Dark/Flying", "murkrow.gif"},
            {"Slowking", "Water/Psychic", "slowking.gif"},
            {"Misdreavus", "Ghost", "misdreavus.gif"},
            {"Unown", "Psychic", "unown.gif"},
            {"Wobbuffet", "Psychic", "wobbuffet.gif"},
            {"Gligar", "Ground/Flying", "gligar.gif"},
            {"Steelix", "Steel/Ground", "steelix.gif"},
            {"Snubbull", "Fairy", "snubbull.gif"},
            {"Granbull", "Fairy", "granbull.gif"},
            {"Qwilfish", "Water/Poison", "qwilfish.gif"},
            {"Corsola", "Water/Rock", "corsola.gif"},
            {"Remoraid", "Water", "remoraid.gif"},
            {"Octillery", "Water", "octillery.gif"},
            {"Delibird", "Ice/Flying", "delibird.gif"},
            {"Houndour", "Dark/Fire", "houndour.gif"},
            {"Houndoom", "Dark/Fire", "houndoom.gif"},
            {"Kingdra", "Water/Dragon", "kingdra.gif"},
            {"Pineco", "Bug", "pineco.gif"},
            {"Forretress", "Bug/Steel", "forretress.gif"},
            {"Dunsparce", "Normal", "dunsparce.gif"},
            {"Raikou", "Electric", "raikou.gif"},
            {"Entei", "Fire", "entei.gif"},
            {"Suicune", "Water", "suicune.gif"},
            {"Lugia", "Psychic/Flying", "lugia.gif"},
            {"Ho-oh", "Fire/Flying", "hooh.gif"},
            {"Celebi", "Psychic/Grass", "celebi.gif"}
        };
        

        AppData db = AppData.getInstance();
        db.connect("./data/pokemons.sqlite");

        // Iteramos sobre el arreglo de Pokémon para insertar uno por uno
        for (String[] pokemon : pokemons) {
            String name = pokemon[0];
            String type = pokemon[1];
            String imagePath = pokemon[2];

            // Ejecutamos la sentencia SQL para insertar el Pokémon
            db.update("INSERT INTO Pokemon (name, type, image_path) VALUES ('" +
                      name + "', '" + type + "', '" + imagePath + "');");
        }

        System.out.println("Pokémons insertados correctamente.");
    }
}
