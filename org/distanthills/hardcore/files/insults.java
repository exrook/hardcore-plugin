package org.distanthills.hardcore.files;

import org.distanthills.hardcore.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.bukkit.Bukkit.getLogger;

public class insults {
    File insultsFile = new File(Main.get().getDataFolder(), "insults.txt");
    List<String> insults = new ArrayList();

    public insults() {
        if(!insultsFile.exists()) {
            Main.get().saveResource("insults.txt", false);
        }
        try {
            Scanner myReader = new Scanner(insultsFile);
            while(myReader.hasNextLine()) {
                insults.add(myReader.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        getLogger().info("Loaded " + insults.size() + " insults");
    }

    public String getInsult() {
        Random rand = new Random();
        return insults.get(rand.nextInt(insults.size()));
    }
}
