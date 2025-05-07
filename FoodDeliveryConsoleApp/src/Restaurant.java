public class Restaurant {

    private String name;
    private String address;
    private int rating;

    private double generatedBudget=0;

    public String getName(){
        return this.name;
    }
    public String getAddress(){
        return this.address;
    }
    public int getRating(){
        return this.rating;
    }

    public double getBudget(){
        return generatedBudget;
    }

    public void addToBudget(double amount){
        this.generatedBudget+=amount;
    }

    public Restaurant(String name, String address, int rating, double budget){
        this.name=name;
        this.address=address;
        this.rating=rating;
        this.generatedBudget=budget;
    }

    public Restaurant(String name, String address, int rating){
        this.name=name;
        this.address=address;
        this.rating=rating;
        this.generatedBudget=0;
    }
}
