package com.mzfq.market.OnlineMarket.controllers;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mzfq.market.OnlineMarket.models.entities.SellersEntity;
import com.mzfq.market.OnlineMarket.services.SellersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class SellersControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SellersService sellersService;

    @InjectMocks
    private SellersController sellersController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sellersController).build();
    }

    @Test
    public void getSellerByCifOk() throws Exception {
        // setup of existing seller
        String cif = "B23456789";
        SellersEntity seller = new SellersEntity();
        seller.setCif(cif);
        seller.setName("FashionHub");

        // recover seller
        when(sellersService.getSellerByCif(cif)).thenReturn(seller);

        //check on controller
        mockMvc.perform(get("/sellers/{cif}", cif))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("seller"))
                .andExpect(model().attribute("seller", seller))
                .andExpect(view().name("index"));
    }

    @Test
    public void getSellerByCifNotFound() throws Exception {
        // create a non-existing CIF
        String cif = "NonExistentCIF";

        //expect to recover null
        when(sellersService.getSellerByCif(cif)).thenReturn(null);

        // check if that null seller returns a 404
        mockMvc.perform(get("/sellers/{cif}", cif))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateSellerOk() throws Exception {
        // simulate auth
        UserDetails userDetails = new User("B23456789", "password", new ArrayList<>());
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // set an existing seller
        SellersEntity existingSeller = new SellersEntity();
        existingSeller.setCif("B23456789");
        existingSeller.setName("FashionHub");
        existingSeller.setPlainPassword("oldPassword");
        existingSeller.setEmail("old@example.com");

        // expect to recover existing seller
        when(sellersService.getSellerByCif("B23456789")).thenReturn(existingSeller);

        // set the update to seller
        SellersEntity sellerToUpdate = new SellersEntity();
        sellerToUpdate.setCif("B23456789");
        sellerToUpdate.setName("FashionHub 2"); // we'll change the name
        sellerToUpdate.setPlainPassword("newPassword");
        sellerToUpdate.setEmail("test@example.com");

        // check if the validation to avoid if there was no changes returns false (meaning we can continue)
        when(sellersService.checkIfEqual(eq(existingSeller), any(SellersEntity.class))).thenReturn(false);

        // update the seller
        mockMvc.perform(post("/sellers/update")
                        .param("cif", sellerToUpdate.getCif())
                        .param("name", sellerToUpdate.getName())
                        .param("plainPassword", sellerToUpdate.getPlainPassword())
                        .param("email", sellerToUpdate.getEmail()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("okMessage", "Seller successfully updated! :)"));
    }

    @Test
    public void updateSellerInvalidData() throws Exception {
        // simulate auth
        UserDetails userDetails = new User("B23456789", "password", new ArrayList<>());
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // set a seller with invalid data
        SellersEntity sellerToUpdate = new SellersEntity();
        sellerToUpdate.setCif("B23456789");
        sellerToUpdate.setName(""); // empty name
        sellerToUpdate.setPlainPassword("short"); // short pass
        sellerToUpdate.setEmail("invalid-email"); // invalid mail

        // update
        mockMvc.perform(post("/sellers/update")
                        .param("cif", sellerToUpdate.getCif())
                        .param("name", sellerToUpdate.getName())
                        .param("plainPassword", sellerToUpdate.getPlainPassword())
                        .param("email", sellerToUpdate.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("koMessage")) // check there's a kowMessage
                .andExpect(view().name("index")); // check there would be a redirect to index
    }

    @Test
    public void updateSellerNotFound() throws Exception {
        // simulate auth
        UserDetails userDetails = new User("NonExistentCIF", "password", new ArrayList<>());
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // set a seller that does not exist
        SellersEntity sellerToUpdate = new SellersEntity();
        sellerToUpdate.setCif("NonExistentCIF");
        sellerToUpdate.setName("Unknown Seller");
        sellerToUpdate.setPlainPassword("password");
        sellerToUpdate.setEmail("unknown@example.com");

        // expect to recover a null checking by cif
        when(sellersService.getSellerByCif("NonExistentCIF")).thenReturn(null);

        // call update
        mockMvc.perform(post("/sellers/update")
                        .param("cif", sellerToUpdate.getCif())
                        .param("name", sellerToUpdate.getName())
                        .param("plainPassword", sellerToUpdate.getPlainPassword())
                        .param("email", sellerToUpdate.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("koMessage"))
                .andExpect(view().name("index"))
                .andExpect(model().attribute("koMessage", "Seller not found"));
    }

}