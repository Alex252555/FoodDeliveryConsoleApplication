public class Restaurant {

    private String name;
    private String address;
    private int rating;

    public String getName(){
        return this.name;
    }
    public String getAddress(){
        return this.address;
    }
    public int getRating(){
        return this.rating;
    }

    public Restaurant(String name, String address, int rating){
        this.name=name;
        this.address=address;
        this.rating=rating;
    }
}
