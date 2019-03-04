/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.recipes.test.logic;

import co.edu.uniandes.csw.recipes.ejb.RecipeLogic;
import co.edu.uniandes.csw.recipes.entities.RecipeEntity;
import co.edu.uniandes.csw.recipes.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.recipes.persistence.RecipePersistence;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.junit.Arquillian;
import javax.transaction.UserTransaction;
import org.apache.commons.lang.RandomStringUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 *
 * @author estudiante
 */
@RunWith(Arquillian.class)
public class RecipeLogicTest {
    
    private PodamFactory factory = new PodamFactoryImpl();
    
    
    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;
    
    @Inject
    private RecipeLogic logic;
    
    private List<RecipeEntity> data = new ArrayList<>();
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(RecipeEntity.class.getPackage())
                .addPackage(RecipeLogic.class.getPackage())
                .addPackage(RecipePersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }
    @Before
    public void configTest() {
        try {
            utx.begin();
            clearData();
            insertData();
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    
    private void clearData() {
        em.createQuery("delete from RecipeEntity").executeUpdate();
    }

     private void insertData() {
        for (int i = 0; i < 5; i++) {
            RecipeEntity recipe = factory.manufacturePojo(RecipeEntity.class);
            em.persist(recipe);
            data.add(recipe);
        }
     }
     
     
     @Test
     public void testCreateOK()
     {
         try {
         RecipeEntity entity= factory.manufacturePojo(RecipeEntity.class);
         RecipeEntity result = logic.createRecipe(entity);
         Assert.assertNotNull(result);
         RecipeEntity created= logic.getRecipe(entity.getId());
             Assert.assertEquals(created.getDescription(),entity.getDescription());
             Assert.assertEquals(created.getName(), entity.getName());
         } catch (BusinessLogicException e) {
             Assert.fail("No deberia generar error");
         }
     }
     @Test(expected = BusinessLogicException.class)
     public void testCreateNameNull() throws BusinessLogicException
     {
     
         RecipeEntity entity= factory.manufacturePojo(RecipeEntity.class);
         entity.setName(null);
         RecipeEntity result = logic.createRecipe(entity);
     }
      @Test(expected = BusinessLogicException.class)
     public void testCreateNameEmpty() throws BusinessLogicException
     {
      
         RecipeEntity entity= factory.manufacturePojo(RecipeEntity.class);
         entity.setName("");
         RecipeEntity result = logic.createRecipe(entity);
     }
      @Test(expected = BusinessLogicException.class)
     public void testCreateNameLong() throws BusinessLogicException
     {
      
         RecipeEntity entity= factory.manufacturePojo(RecipeEntity.class);
         entity.setName("-----------------------------------");
         RecipeEntity result = logic.createRecipe(entity);
     }
     
      @Test(expected = BusinessLogicException.class)
     public void testCreateDescriptionNull() throws BusinessLogicException
     {
         RecipeEntity entity= factory.manufacturePojo(RecipeEntity.class);
         entity.setDescription(null);
         RecipeEntity result = logic.createRecipe(entity);
     }
       @Test(expected = BusinessLogicException.class)
     public void testCreateDescriptionEmpty() throws BusinessLogicException
     {
         RecipeEntity entity= factory.manufacturePojo(RecipeEntity.class);
         entity.setDescription("");
         RecipeEntity result = logic.createRecipe(entity);
     }
       @Test(expected = BusinessLogicException.class)
     public void testCreateDescriptionLong() throws BusinessLogicException
     {
         RecipeEntity entity= factory.manufacturePojo(RecipeEntity.class);
         entity.setDescription("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
         RecipeEntity result = logic.createRecipe(entity);
     }
     
      @Test(expected = BusinessLogicException.class)
     public void testCreateDescriptionDuplicated() throws BusinessLogicException
     {
         RecipeEntity entity= factory.manufacturePojo(RecipeEntity.class);
         try {
         logic.createRecipe(entity);
         } catch (Exception e) {
             Assert.fail("No deberia generar excepcion");
         }
         logic.createRecipe(entity);
     }
    
    
}
