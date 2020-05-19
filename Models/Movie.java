package Models;

public class Movie
{
    private String id;
    private String name;
    private String genre;
    private String actor;
    private String director;
    private String producer;
    private String qualification;
    private String date;
    private String popularity;
    
    public Movie() {
        this.id = "";
        this.name = "";
        this.genre = "";
        this.actor = "";
        this.director = "";
        this.producer = "";
        this.qualification = "";
        this.date = "";
        this.popularity = "";
    }
    
    public Movie(final String id, final String name, final String genre, final String actor, final String director, final String producer, final String qualification, final String date, final String popularity) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.actor = actor;
        this.director = director;
        this.producer = producer;
        this.qualification = qualification;
        this.date = date;
        this.popularity = popularity;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getGenre() {
        return this.genre;
    }
    
    public void setGenre(final String genre) {
        this.genre = genre;
    }
    
    public String getActor() {
        return this.actor;
    }
    
    public void setActor(final String actor) {
        this.actor = actor;
    }
    
    public String getDirector() {
        return this.director;
    }
    
    public void setDirector(final String director) {
        this.director = director;
    }
    
    public String getProducer() {
        return this.producer;
    }
    
    public void setProducer(final String producer) {
        this.producer = producer;
    }
    
    public String getQualification() {
        return this.qualification;
    }
    
    public void setQualification(final String qualification) {
        this.qualification = qualification;
    }
    
    public String getDate() {
        return this.date;
    }
    
    public void setDate(final String date) {
        this.date = date;
    }
    
    public String getPopularity() {
        return this.popularity;
    }
    
    public void setPopularity(final String popularity) {
        this.popularity = popularity;
    }
    
    @Override
    public String toString() {
        return "Movie [id=" + this.id + ", name=" + this.name + ", genre=" + this.genre + ", actor=" + this.actor + ", director=" + this.director + ", producer=" + this.producer + ", qualification=" + this.qualification + ", date=" + this.date + ", popularity=" + this.popularity + "]";
    }
}
