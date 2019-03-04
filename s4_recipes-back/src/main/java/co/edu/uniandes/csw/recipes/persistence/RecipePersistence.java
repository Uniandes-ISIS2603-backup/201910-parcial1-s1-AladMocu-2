/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.recipes.persistence;

import co.edu.uniandes.csw.recipes.entities.RecipeEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author CesarF
 */

@Stateless
public class RecipePersistence {
    private static final Logger LOGGER = Logger.getLogger(RecipePersistence.class.getName());
    
    @PersistenceContext(unitName = "recipesPU")
    protected EntityManager em;
    
  
    public RecipeEntity find(Long id) {
        return em.find(RecipeEntity.class, id);
    }
    
    //TODO método crear de recipe
    public RecipeEntity createRecipe(RecipeEntity entity)
    {
        LOGGER.log(Level.INFO,"Creando una nueva receta-Inicio");
        em.persist(entity);
        LOGGER.log(Level.INFO,"Creando una nueva receta-Final");
        return entity;
    }
    
   public RecipeEntity findByName(String name)
   {
      LOGGER.log(Level.INFO,"Buscando una receta por nombre Inicio");
      TypedQuery query = em.createQuery("Select e From RecipeEntity e where e.name = :name", RecipeEntity.class);
      query = query.setParameter("name", name);
      List<RecipeEntity> sameName = query.getResultList();
      RecipeEntity result;
       if (sameName == null) {
            result = null;
        } else if (sameName.isEmpty()) {
            result = null;
        } else {
            result = sameName.get(0);
        }
        LOGGER.log(Level.INFO, "Saliendo de consultar  receta por nombre");
        return result;
      
   }

}
