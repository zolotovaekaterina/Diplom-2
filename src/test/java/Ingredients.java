import java.util.List;

public class Ingredients {
    public List<String> getingredients() {
        return ingredients;
    }

    public void set_id(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    private List<String> ingredients;

    public Ingredients(List<String> ingredients) {
        this.ingredients = ingredients;

    }

    public Ingredients() {

    }


}
