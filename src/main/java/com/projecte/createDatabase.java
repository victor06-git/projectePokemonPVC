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
                image_path TEXT,
                icon_path TEXT
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
            {"Bulbasaur", "Grass/Poison", "bulbasaur.gif", "001.png"},
            {"Ivysaur", "Grass/Poison", "ivysaur.gif", "002.png"},
            {"Venusaur", "Grass/Poison", "venusaur.gif", "003.png"},
            {"Charmander", "Fire", "charmander.gif", "004.png"},
            {"Charmeleon", "Fire", "charmeleon.gif", "005.png"},
            {"Charizard", "Fire/Flying", "charizard.gif", "006.png"},
            {"Squirtle", "Water", "squirtle.gif", "007.png"},
            {"Wartortle", "Water", "wartortle.gif", "008.png"},
            {"Blastoise", "Water", "blastoise.gif", "009.png"},
            {"Caterpie", "Bug", "caterpie.gif", "010.png"},
            {"Metapod", "Bug", "metapod.gif", "011.png"},
            {"Butterfree", "Bug/Flying", "butterfree.gif", "012.png"},
            {"Weedle", "Bug/Poison", "weedle.gif", "013.png"},
            {"Kakuna", "Bug/Poison", "kakuna.gif", "014.png"},
            {"Beedrill", "Bug/Poison", "beedrill.gif", "015.png"},
            {"Pidgey", "Normal/Flying", "pidgey.gif", "016.png"},
            {"Pidgeotto", "Normal/Flying", "pidgeotto.gif", "017.png"},
            {"Pidgeot", "Normal/Flying", "pidgeot.gif", "018.png"},
            {"Rattata", "Normal", "rattata.gif", "019.png"},
            {"Raticate", "Normal", "raticate.gif", "020.png"},
            {"Spearow", "Normal/Flying", "spearow.gif", "021.png"},
            {"Fearow", "Normal/Flying", "fearow.gif", "022.png"},
            {"Ekans", "Poison", "ekans.gif", "023.png"},
            {"Arbok", "Poison", "arbok.gif", "024.png"},
            {"Pikachu", "Electric", "pikachu.gif", "025.png"},
            {"Raichu", "Electric", "raichu.gif", "026.png"},
            {"Sandshrew", "Ground", "sandshrew.gif", "027.png"},
            {"Sandslash", "Ground", "sandslash.gif", "028.png"},
            {"Nidoran♀", "Poison", "nidoranf.gif", "029.png"},
            {"Nidorina", "Poison", "nidorina.gif", "030.png"},
            {"Nidoqueen", "Poison/Ground", "nidoqueen.gif", "031.png"},
            {"Nidoran♂", "Poison", "nidoranm.gif", "032.png"},
            {"Nidorino", "Poison", "nidorino.gif", "033.png"},
            {"Nidoking", "Poison/Ground", "nidoking.gif", "034.png"},
            {"Clefairy", "Fairy", "clefairy.gif", "035.png"},
            {"Clefable", "Fairy", "clefable.gif", "036.png"},
            {"Vulpix", "Fire", "vulpix.gif", "037.png"},
            {"Ninetales", "Fire", "ninetales.gif", "038.png"},
            {"Jigglypuff", "Normal/Fairy", "jigglypuff.gif", "039.png"},
            {"Wigglytuff", "Normal/Fairy", "wigglytuff.gif", "040.png"},
            {"Zubat", "Poison/Flying", "zubat.gif", "041.png"},
            {"Golbat", "Poison/Flying", "golbat.gif", "042.png"},
            {"Oddish", "Grass/Poison", "oddish.gif", "043.png"},
            {"Gloom", "Grass/Poison", "gloom.gif", "044.png"},
            {"Vileplume", "Grass/Poison", "vileplume.gif", "045.png"},
            {"Paras", "Bug/Grass", "paras.gif", "046.png"},
            {"Parasect", "Bug/Grass", "parasect.gif", "047.png"},
            {"Venonat", "Bug/Poison", "venonat.gif", "048.png"},
            {"Venomoth", "Bug/Poison", "venomoth.gif", "049.png"},
            {"Diglett", "Ground", "diglett.gif", "050.png"},
            {"Dugtrio", "Ground", "dugtrio.gif", "051.png"},
            {"Meowth", "Normal", "meowth.gif", "052.png"},
            {"Persian", "Normal", "persian.gif", "053.png"},
            {"Psyduck", "Water", "psyduck.gif", "054.png"},
            {"Golduck", "Water", "golduck.gif", "055.png"},
            {"Mankey", "Fighting", "mankey.gif", "056.png"},
            {"Primeape", "Fighting", "primeape.gif", "057.png"},
            {"Growlithe", "Fire", "growlithe.gif", "058.png"},
            {"Arcanine", "Fire", "arcanine.gif", "059.png"},
            {"Poliwag", "Water", "poliwag.gif", "060.png"},
            {"Poliwhirl", "Water", "poliwhirl.gif", "061.png"},
            {"Poliwrath", "Water/Fighting", "poliwrath.gif", "062.png"},
            {"Abra", "Psychic", "abra.gif", "063.png"},
            {"Kadabra", "Psychic", "kadabra.gif", "064.png"},
            {"Alakazam", "Psychic", "alakazam.gif", "065.png"},
            {"Machop", "Fighting", "machop.gif", "066.png"},
            {"Machoke", "Fighting", "machoke.gif", "067.png"},
            {"Machamp", "Fighting", "machamp.gif", "068.png"},
            {"Bellsprout", "Grass/Poison", "bellsprout.gif", "069.png"},
            {"Weepinbell", "Grass/Poison", "weepinbell.gif", "070.png"},
            {"Victreebel", "Grass/Poison", "victreebel.gif", "071.png"},
            {"Tentacool", "Water/Poison", "tentacool.gif", "072.png"},
            {"Tentacruel", "Water/Poison", "tentacruel.gif", "073.png"},
            {"Geodude", "Rock/Ground", "geodude.gif", "074.png"},
            {"Graveler", "Rock/Ground", "graveler.gif", "075.png"},
            {"Golem", "Rock/Ground", "golem.gif", "076.png"},
            {"Ponyta", "Fire", "ponyta.gif", "077.png"},
            {"Rapidash", "Fire", "rapidash.gif", "078.png"},
            {"Slowpoke", "Water/Psychic", "slowpoke.gif", "079.png"},
            {"Slowbro", "Water/Psychic", "slowbro.gif", "080.png"},
            {"Magnemite", "Electric/Steel", "magnemite.gif", "081.png"},
            {"Magneton", "Electric/Steel", "magneton.gif", "082.png"},
            {"Farfetch'd", "Normal/Flying", "farfetchd.gif", "083.png"},
            {"Doduo", "Normal/Flying", "doduo.gif", "084.png"},
            {"Dodrio", "Normal/Flying", "dodrio.gif", "085.png"},
            {"Seel", "Water", "seel.gif", "086.png"},
            {"Dewgong", "Water/Ice", "dewgong.gif", "087.png"},
            {"Grimer", "Poison", "grimer.gif", "088.png"},
            {"Muk", "Poison", "muk.gif", "089.png"},
            {"Shellder", "Water", "shellder.gif", "090.png"},
            {"Cloyster", "Water/Ice", "cloyster.gif", "091.png"},
            {"Gastly", "Ghost/Poison", "gastly.gif", "092.png"},
            {"Haunter", "Ghost/Poison", "haunter.gif", "093.png"},
            {"Gengar", "Ghost/Poison", "gengar.gif", "094.png"},
            {"Onix", "Rock/Ground", "onix.gif", "095.png"},
            {"Drowzee", "Psychic", "drowzee.gif", "096.png"},
            {"Hypno", "Psychic", "hypno.gif", "097.png"},
            {"Krabby", "Water", "krabby.gif", "098.png"},
            {"Kingler", "Water", "kingler.gif", "099.png"},
            {"Voltorb", "Electric", "voltorb.gif", "100.png"},
            {"Electrode", "Electric", "electrode.gif", "101.png"},
            {"Exeggcute", "Grass/Psychic", "exeggcute.gif", "102.png"},
            {"Exeggutor", "Grass/Psychic", "exeggutor.gif", "103.png"},
            {"Cubone", "Ground", "cubone.gif", "104.png"},
            {"Marowak", "Ground", "marowak.gif", "105.png"},
            {"Hitmonlee", "Fighting", "hitmonlee.gif", "106.png"},
            {"Hitmonchan", "Fighting", "hitmonchan.gif", "107.png"},
            {"Lickitung", "Normal", "lickitung.gif", "108.png"},
            {"Koffing", "Poison", "koffing.gif", "109.png"},
            {"Weezing", "Poison", "weezing.gif", "110.png"},
            {"Rhyhorn", "Ground/Rock", "rhyhorn.gif", "111.png"},
            {"Rhydon", "Ground/Rock", "rhydon.gif", "112.png"},
            {"Chansey", "Normal", "chansey.gif", "113.png"},
            {"Tangela", "Grass", "tangela.gif", "114.png"},
            {"Kangaskhan", "Normal", "kangaskhan.gif", "115.png"},
            {"Horsea", "Water", "horsea.gif", "116.png"},
            {"Seadra", "Water", "seadra.gif", "117.png"},
            {"Goldeen", "Water", "goldeen.gif", "118.png"},
            {"Seaking", "Water", "seaking.gif", "119.png"},
            {"Staryu", "Water", "staryu.gif", "120.png"},
            {"Starmie", "Water/Psychic", "starmie.gif", "121.png"},
            {"Mr. Mime", "Psychic/Fairy", "mrmime.gif", "122.png"},
            {"Scyther", "Bug/Flying", "scyther.gif", "123.png"},
            {"Jynx", "Ice/Psychic", "jynx.gif", "124.png"},
            {"Electabuzz", "Electric", "electabuzz.gif", "125.png"},
            {"Magmar", "Fire", "magmar.gif", "126.png"},
            {"Pinsir", "Bug", "pinsir.gif", "127.png"},
            {"Tauros", "Normal", "tauros.gif", "128.png"},
            {"Magikarp", "Water", "magikarp.gif", "129.png"},
            {"Gyarados", "Water/Flying", "gyarados.gif", "130.png"},
            {"Lapras", "Water/Ice", "lapras.gif", "131.png"},
            {"Ditto", "Normal", "ditto.gif", "132.png"},
            {"Eevee", "Normal", "eevee.gif", "133.png"},
            {"Vaporeon", "Water", "vaporeon.gif", "134.png"},
            {"Jolteon", "Electric", "jolteon.gif", "135.png"},
            {"Flareon", "Fire", "flareon.gif", "136.png"},
            {"Porygon", "Normal", "porygon.gif", "137.png"},
            {"Omanyte", "Rock/Water", "omanyte.gif", "138.png"},
            {"Omastar", "Rock/Water", "omastar.gif", "139.png"},
            {"Kabuto", "Rock/Water", "kabuto.gif", "140.png"},
            {"Kabutops", "Rock/Water", "kabutops.gif", "141.png"},
            {"Aerodactyl", "Rock/Flying", "aerodactyl.gif", "142.png"},
            {"Snorlax", "Normal", "snorlax.gif", "143.png"},
            {"Articuno", "Ice/Flying", "articuno.gif", "144.png"},
            {"Zapdos", "Electric/Flying", "zapdos.gif", "145.png"},
            {"Moltres", "Fire/Flying", "moltres.gif", "146.png"},
            {"Dratini", "Dragon", "dratini.gif", "147.png"},
            {"Dragonair", "Dragon", "dragonair.gif", "148.png"},
            {"Dragonite", "Dragon/Flying", "dragonite.gif", "149.png"},
            {"Mewtwo", "Psychic", "mewtwo.gif", "150.png"},
            {"Mew", "Psychic", "mew.gif", "151.png"},
            
            // Segunda Generación (152-251)
            {"Chikorita", "Grass", "chikorita.gif", "152.png"},
            {"Bayleef", "Grass", "bayleef.gif", "153.png"},
            {"Meganium", "Grass", "meganium.gif", "154.png"},
            {"Cyndaquil", "Fire", "cyndaquil.gif", "155.png"},
            {"Quilava", "Fire", "quilava.gif", "156.png"},
            {"Typhlosion", "Fire", "typhlosion.gif", "157.png"},
            {"Totodile", "Water", "totodile.gif", "158.png"},
            {"Croconaw", "Water", "croconaw.gif", "159.png"},
            {"Feraligatr", "Water", "feraligatr.gif", "160.png"},
            {"Sentret", "Normal", "sentret.gif", "161.png"},
            {"Furret", "Normal", "furret.gif", "162.png"},
            {"Hoothoot", "Normal/Flying", "hoothoot.gif", "163.png"},
            {"Noctowl", "Normal/Flying", "noctowl.gif", "164.png"},
            {"Ledyba", "Bug/Flying", "ledyba.gif", "165.png"},
            {"Ledian", "Bug/Flying", "ledian.gif", "166.png"},
            {"Spinarak", "Bug/Poison", "spinarak.gif", "167.png"},
            {"Ariados", "Bug/Poison", "ariados.gif", "168.png"},
            {"Crobat", "Poison/Flying", "crobat.gif", "169.png"},
            {"Chinchou", "Water/Electric", "chinchou.gif", "170.png"},
            {"Lanturn", "Water/Electric", "lanturn.gif", "171.png"},
            {"Pichu", "Electric", "pichu.gif", "172.png"},
            {"Cleffa", "Fairy", "cleffa.gif", "173.png"},
            {"Igglybuff", "Normal/Fairy", "igglybuff.gif", "174.png"},
            {"Togepi", "Fairy", "togepi.gif", "175.png"},
            {"Togetic", "Fairy/Flying", "togetic.gif", "176.png"},
            {"Natu", "Psychic/Flying", "natu.gif", "177.png"},
            {"Xatu", "Psychic/Flying", "xatu.gif", "178.png"},
            {"Mareep", "Electric", "mareep.gif", "179.png"},
            {"Flaaffy", "Electric", "flaaffy.gif", "180.png"},
            {"Ampharos", "Electric", "ampharos.gif", "181.png"},
            {"Bellossom", "Grass", "bellossom.gif", "182.png"},
            {"Marill", "Water/Fairy", "marill.gif", "183.png"},
            {"Azumarill", "Water/Fairy", "azumarill.gif", "184.png"},
            {"Sudowoodo", "Rock", "sudowoodo.gif", "185.png"},
            {"Politoed", "Water", "politoed.gif", "186.png"},
            {"Hoppip", "Grass/Flying", "hoppip.gif", "187.png"},
            {"Skiploom", "Grass/Flying", "skiploom.gif", "188.png"},
            {"Jumpluff", "Grass/Flying", "jumpluff.gif", "189.png"},
            {"Aipom", "Normal", "aipom.gif", "190.png"},
            {"Sunkern", "Grass", "sunkern.gif", "191.png"},
            {"Sunflora", "Grass", "sunflora.gif", "192.png"},
            {"Yanma", "Bug/Flying", "yanma.gif", "193.png"},
            {"Wooper", "Water/Ground", "wooper.gif", "194.png"},
            {"Quagsire", "Water/Ground", "quagsire.gif", "195.png"},
            {"Espeon", "Psychic", "espeon.gif", "196.png"},
            {"Umbreon", "Dark", "umbreon.gif", "197.png"},
            {"Murkrow", "Dark/Flying", "murkrow.gif", "198.png"},
            {"Slowking", "Water/Psychic", "slowking.gif", "199.png"},
            {"Misdreavus", "Ghost", "misdreavus.gif", "200.png"},
            {"Unown", "Psychic", "unown.gif", "201.png"},
            {"Wobbuffet", "Psychic", "wobbuffet.gif", "202.png"},
            {"Girafarig", "Normal/Psychic", "girafarig.gif", "203.png"},
            {"Pineco", "Bug", "pineco.gif", "204.png"},
            {"Forretress", "Bug/Steel", "forretress.gif", "205.png"},
            {"Dunsparce", "Normal", "dunsparce.gif", "206.png"},
            {"Gligar", "Ground/Flying", "gligar.gif", "207.png"},
            {"Steelix", "Steel/Ground", "steelix.gif", "208.png"},
            {"Snubbull", "Fairy", "snubbull.gif", "209.png"},
            {"Granbull", "Fairy", "granbull.gif", "210.png"},
            {"Qwilfish", "Water/Poison", "qwilfish.gif", "211.png"},
            {"Scizor", "Bug/Steel", "scizor.gif", "212.png"},
            {"Shuckle", "Bug/Rock", "shuckle.gif", "213.png"},
            {"Heracross", "Bug/Fighting", "heracross.gif", "214.png"},
            {"Sneasel", "Dark/Ice", "sneasel.gif", "215.png"},
            {"Teddiursa", "Normal", "teddiursa.gif", "216.png"},
            {"Ursaring", "Normal", "ursaring.gif", "217.png"},
            {"Slugma", "Fire", "slugma.gif", "218.png"},
            {"Magcargo", "Fire/Rock", "magcargo.gif", "219.png"},
            {"Swinub", "Ice/Ground", "swinub.gif", "220.png"},
            {"Piloswine", "Ice/Ground", "piloswine.gif", "221.png"},
            {"Corsola", "Water/Rock", "corsola.gif", "222.png"},
            {"Remoraid", "Water", "remoraid.gif", "223.png"},
            {"Octillery", "Water", "octillery.gif", "224.png"},
            {"Delibird", "Ice/Flying", "delibird.gif", "225.png"},
            {"Mantine", "Water/Flying", "mantine.gif", "226.png"},
            {"Skarmory", "Steel/Flying", "skarmory.gif", "227.png"},
            {"Houndour", "Dark/Fire", "houndour.gif", "228.png"},
            {"Houndoom", "Dark/Fire", "houndoom.gif", "229.png"},
            {"Kingdra", "Water/Dragon", "kingdra.gif", "230.png"},
            {"Phanpy", "Ground", "phanpy.gif", "231.png"},
            {"Donphan", "Ground", "donphan.gif", "232.png"},
            {"Porygon2", "Normal", "porygon2.gif", "233.png"},
            {"Stantler", "Normal", "stantler.gif", "234.png"},
            {"Smeargle", "Normal", "smeargle.gif", "235.png"},
            {"Tyrogue", "Fighting", "tyrogue.gif", "236.png"},
            {"Hitmontop", "Fighting", "hitmontop.gif", "237.png"},
            {"Smoochum", "Ice/Psychic", "smoochum.gif", "238.png"},
            {"Elekid", "Electric", "elekid.gif", "239.png"},
            {"Magby", "Fire", "magby.gif", "240.png"},
            {"Miltank", "Normal", "miltank.gif", "241.png"},
            {"Blissey", "Normal", "blissey.gif", "242.png"},
            {"Raikou", "Electric", "raikou.gif", "243.png"},
            {"Entei", "Fire", "entei.gif", "244.png"},
            {"Suicune", "Water", "suicune.gif", "245.png"},
            {"Larvitar", "Rock/Ground", "larvitar.gif", "246.png"},
            {"Pupitar", "Rock/Ground", "pupitar.gif", "247.png"},
            {"Tyranitar", "Rock/Dark", "tyranitar.gif", "248.png"},
            {"Lugia", "Psychic/Flying", "lugia.gif", "249.png"},
            {"Ho-Oh", "Fire/Flying", "hooh.gif", "250.png"},
            {"Celebi", "Psychic/Grass", "celebi.gif", "251.png"}
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
