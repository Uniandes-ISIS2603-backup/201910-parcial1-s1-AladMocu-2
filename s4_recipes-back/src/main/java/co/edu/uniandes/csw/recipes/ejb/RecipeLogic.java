/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.recipes.ejb;

import co.edu.uniandes.csw.recipes.entities.RecipeEntity;
import co.edu.uniandes.csw.recipes.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.recipes.persistence.RecipePersistence;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author CesarF
 */
@Stateless
public class RecipeLogic {
    @Inject
    private RecipePersistence persistence; // Variable para acceder a la persistencia de la aplicación. Es una inyección de dependencias.

    public RecipeEntity getRecipe(Long id) {
        return persistence.find(id);
    }

    
    //TODO crear el método createRecipe
    public RecipeEntity createRecipe(RecipeEntity entity) throws BusinessLogicException
    {
         if(entity.getName()==null)
        {
            throw new BusinessLogicException("name can't be null");
        }
         if(entity.getDescription()==null)
        {
            throw new BusinessLogicException("description can't be null");
        }
        if(entity.getName().equals(""))
        {
            throw new BusinessLogicException("name can't be empty");
        }
        if(entity.getName().length()>30)
        {
            throw new BusinessLogicException("name can't be longer than 30 characters");
        }
        if(entity.getDescription().equals(""))
        {
            throw new BusinessLogicException("description can't be empty");
        }
        
        if(entity.getDescription().length()>150)
        {
            throw new BusinessLogicException("description can't be longer than 150 characters");
        }
        if(persistence.findByName(entity.getName())!=null)
        {
            throw new BusinessLogicException("A element of that name already exist");
        }
        return persistence.createRecipe(entity);
    }


}
