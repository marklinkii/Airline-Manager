import java.util.HashMap;

public class Airport {
    private HashMap<String, Double> routes;
    private String name;
    private boolean isKnown;
    private String path;
    private double relativeCost;

    Airport(String name){
        this.name = name;
        this.routes = new HashMap<>();
        this.relativeCost = Integer.MAX_VALUE;
        path = "null";
    }

    public void addRoute(String destination, double cost){
        routes.put(destination, cost);
    }

    public void setCost(String destination, double cost){
        if(routes.containsKey(destination)) routes.replace(destination, cost);
        else routes.put(destination, cost);
    }

    public double getCost(String dest){
        return routes.get(dest);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isKnown() {
        return isKnown;
    }

    public void setKnown(boolean known) {
        isKnown = known;
    }

    public HashMap<String, Double> getRoutes() {
        return routes;
    }

    public void setRoutes(HashMap<String, Double> routes) {
        this.routes = routes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRelativeCost(double relativeCost) {
        this.relativeCost = relativeCost;
    }

    public double getRelativeCost() {
        return relativeCost;
    }

    @Override
    public String toString(){
        return "Name: " + name + ", Path: " + path + ", Relative cost: " + relativeCost;
    }

}
