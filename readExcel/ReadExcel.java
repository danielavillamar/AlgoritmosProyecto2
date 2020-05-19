package readExcel;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Label;
import java.util.Date;
import java.util.Arrays;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import Models.Movie;
import java.util.ArrayList;
import java.io.File;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.GraphDatabaseService;

public class ReadExcel
{
    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }
    
    public static void main(final String[] args) {
        final GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("moviesDb/"));
        registerShutdownHook(db);
        final ArrayList<Movie> movies = new ArrayList<Movie>();
        final String nombreArchivo = "tmdb_5000_credits.csv";
        final String rutaArchivo = ".\\Exceldb\\" + nombreArchivo;
        final String nombreArchivo2 = "tmdb_5000_movies.csv";
        final String rutaArchivo2 = ".\\Exceldb\\" + nombreArchivo2;
        final List<List<String>> records = new ArrayList<List<String>>();
        final List<List<String>> records2 = new ArrayList<List<String>>();
        try {
            Throwable t = null;
            try {
                final BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
                try {
                    Throwable t2 = null;
                    try {
                        final BufferedReader br2 = new BufferedReader(new FileReader(rutaArchivo2));
                        try {
                            String line;
                            while ((line = br.readLine()) != null) {
                                final String[] values = line.split("\\s*,\\s*");
                                records.add(Arrays.asList(values));
                            }
                            int counter = 0;
                            for (int i = 1; i < records.size(); ++i) {
                                final String id = records.get(i).get(0);
                                String name = records.get(i).get(1);
                                if (name.contains("\"")) {
                                    name = name.substring(1);
                                }
                                String actor = "";
                                String director = "";
                                String producer = "";
                                if (name.contains("\"")) {
                                    name = name.substring(1);
                                }
                                for (int j = 2; j < records.get(i).size(); ++j) {
                                    if (records.get(i).get(j).contains("character") && records.get(i).size() > j + 4 && records.get(i).get(j + 4).contains("name") && actor == "") {
                                        actor = records.get(i).get(j + 4);
                                    }
                                    if (records.get(i).get(j).contains("\"Director\"") && records.get(i).size() > j + 1 && records.get(i).get(j + 1).contains("name") && director == "") {
                                        final String[] str = records.get(i).get(j + 1).split("\"\"");
                                        director = str[3];
                                    }
                                    if (records.get(i).get(j).contains("\"Producer\"") && records.get(i).size() > j + 1 && records.get(i).get(j + 1).contains("name") && producer == "") {
                                        final String[] str = records.get(i).get(j + 1).split("\"\"");
                                        producer = str[3];
                                    }
                                }
                                final String[] actors = actor.split("\"\"");
                                if (actors.length >= 3) {
                                    actor = actors[3];
                                }
                                else {
                                    actor = "";
                                }
                                if (actor != "" && director != "" && producer != "") {
                                    ++counter;
                                    final Movie currentMovie = new Movie(id, name, "", actor, director, producer, "", "", "");
                                    movies.add(currentMovie);
                                }
                            }
                            System.out.println(counter);
                            String line2;
                            while ((line2 = br2.readLine()) != null) {
                                final String[] values2 = line2.split("\\s*,\\s*");
                                records2.add(Arrays.asList(values2));
                            }
                            for (int k = 1; k < records2.size(); ++k) {
                                int l = 1;
                                String currentId = "";
                                final String qualification = records2.get(k).get(records2.get(k).size() - 2);
                                String date = "";
                                String popularity = "";
                                String genre = records2.get(k).get(2);
                                if (genre.contains("name")) {
                                    final String[] genres = genre.split("\"");
                                    if (genres.length >= 3) {
                                        genre = genres[6];
                                    }
                                    else {
                                        genre = "";
                                    }
                                }
                                else {
                                    genre = "";
                                }
                                while ((currentId == "" || date == "" || popularity == "") && l <= records2.get(k).size()) {
                                    try {
                                        if (currentId == "") {
                                            final int cid = Integer.parseInt(records2.get(k).get(l));
                                            currentId = records2.get(k).get(l);
                                        }
                                        else if (popularity == "") {
                                            final double cpopularity = Double.parseDouble(records2.get(k).get(l));
                                            popularity = records2.get(k).get(l);
                                        }
                                        final long cdate = Date.parse(records2.get(k).get(l));
                                        date = records2.get(k).get(l);
                                    }
                                    catch (Exception e3) {
                                        ++l;
                                    }
                                }
                                if (currentId != "" && qualification != "" && date != "" && popularity != "" && genre != "") {
                                    for (int m = 0; m < movies.size(); ++m) {
                                        if (movies.get(m).getId().equals(currentId)) {
                                            final Movie currentMovie2 = movies.get(m);
                                            currentMovie2.setQualification(qualification);
                                            currentMovie2.setGenre(genre);
                                            currentMovie2.setDate(date);
                                            currentMovie2.setPopularity(popularity);
                                            movies.set(m, currentMovie2);
                                        }
                                    }
                                }
                            }
                            for (int k = 0; k < movies.size(); ++k) {
                                if (movies.get(k).getQualification().equals("")) {
                                    movies.remove(k);
                                    --counter;
                                }
                            }
                            for (int k = 0; k < movies.size(); ++k) {
                                System.out.println(movies.get(k).toString());
                                final Transaction tx = db.beginTx();
                                try {
                                    final Node node = db.createNode(new Label[] { Label.label("Movie") });
                                    node.setProperty("id", (Object)movies.get(k).getId());
                                    node.setProperty("name", (Object)movies.get(k).getName());
                                    node.setProperty("genre", (Object)movies.get(k).getGenre());
                                    node.setProperty("actor", (Object)movies.get(k).getActor());
                                    node.setProperty("director", (Object)movies.get(k).getDirector());
                                    node.setProperty("producer", (Object)movies.get(k).getProducer());
                                    node.setProperty("date", (Object)movies.get(k).getDate());
                                    node.setProperty("popularity", (Object)movies.get(k).getPopularity());
                                    node.setProperty("qualification", (Object)movies.get(k).getQualification());
                                    tx.success();
                                }
                                finally {
                                    tx.close();
                                }
                                tx.close();
                            }
                            System.out.println(counter);
                        }
                        finally {
                            if (br2 != null) {
                                br2.close();
                            }
                        }
                    }
                    finally {
                        if (t2 == null) {
                            final Throwable exception;
                            t2 = exception;
                        }
                        else {
                            final Throwable exception;
                            if (t2 != exception) {
                                t2.addSuppressed(exception);
                            }
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (br != null) {
                        br.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable exception2;
                    t = exception2;
                }
                else {
                    final Throwable exception2;
                    if (t != exception2) {
                        t.addSuppressed(exception2);
                    }
                }
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
