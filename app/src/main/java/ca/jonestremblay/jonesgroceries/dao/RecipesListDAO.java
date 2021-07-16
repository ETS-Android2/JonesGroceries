package ca.jonestremblay.jonesgroceries.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ca.jonestremblay.jonesgroceries.entities.Category;
import ca.jonestremblay.jonesgroceries.entities.Recipe;

@Dao
public interface RecipesListDAO {

    @Query("SELECT * FROM recipes")
    List<Recipe> getAllRecipes();

    @Insert
    void insertRecipe(Recipe...recipe);

    @Update
    void updateRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);

}
