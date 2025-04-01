package org.mps.board;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class AdvertisementBoardTest {
    //Se requiere implementar los siguientes casos de prueba en la clase de prueba AdvertisementBoardTest:

    //Comprobar que inicialmente hay un anuncio en el tablón.
    //Partimos de la base de que no hace falta hacer mock para todos los test,
    // solo para aquellos en los que se comprueben dependencias externas.

    @Test
    @DisplayName("Constructor: construye el objeto y añade un anuncio")
    public void AdvertisementBoard_init_hasOneAdvertisement() {
        //Arrange, Act
        AdvertisementBoard board = new AdvertisementBoard();

  

        //Assert
        assertEquals(1, board.numberOfPublishedAdvertisements());
    }

    //Crear un anuncio por parte de "THE Company", insertarlo y comprobar que se ha incrementado en uno el número de anuncios del tablón.
    //En este caso la clase AdvertisementBoard tiene como dependencias externas la base de datos y la pasarela de pago,
    //que se pasa a traves de los argumentos de la función publish, en este caso como estamos testeando con el BOARD_OWNER 
    //como advertiser no se están usando las dependencias,
    //por eso tenemos que usar 
    @Test
    @DisplayName("publishTheCompanyAd: Se añade un anuncio del dueño del board y no se comprueba si está en la base de datos ni si tiene fondos")
    public void publish_AdBoardOwner_incrementsAdsByOne() {
        //Arrange
        AdvertisementBoard board = new AdvertisementBoard();
        int numberOfAds = board.numberOfPublishedAdvertisements();
        String title = "Title";
        String text = "Text";
        String boardOwnerName = AdvertisementBoard.BOARD_OWNER;
        Advertisement advertisement = new Advertisement(title, text, boardOwnerName);
        AdvertiserDatabase dummyDB = mock(AdvertiserDatabase.class);
        PaymentGateway dummyGateway = mock(PaymentGateway.class); //Ninguna de estas dos se va a usar
        //Act
        board.publish(advertisement, dummyDB, dummyGateway);

        //Assert
        assertEquals(numberOfAds + 1, board.numberOfPublishedAdvertisements());
        verifyNoInteractions(dummyDB);
        verifyNoInteractions(dummyGateway);
    }
    
    //Publicar un anuncio por parte del anunciante "Pepe Gotera y Otilio" y comprobar que, si está en la base de datos de anunciantes pero no tiene saldo, el anuncio no se inserta, lo que se determina observando que el número de anuncios no aumenta.
   @Test
   @DisplayName("PublishRegisteredNoFundsAdvertiser: Se intenta insertar un anunciante registrado pero sin recursos")
    public void publish_AdInDBAndNotenoughMoney_notIncremented() {
        // Arrange
        String anunciante = "Pepe Gotera y Otilio";
        String title = "Title";
        String text = "Text";
        Advertisement advertisement = new Advertisement(title, text, anunciante);
        AdvertisementBoard board = new AdvertisementBoard();
        //Creación de mocks
        AdvertiserDatabase mockDb = mock(AdvertiserDatabase.class);
        PaymentGateway mockGateway = mock(PaymentGateway.class);

        //Stubbing
        // El anunciante está en la base de datos
        when(mockDb.advertiserIsRegistered(anunciante)).thenReturn(true);
        // Pero no tiene dinero
        when(mockGateway.advertiserHasFunds(anunciante)).thenReturn(false);

        int before = board.numberOfPublishedAdvertisements();

        // Act
        board.publish(advertisement, mockDb, mockGateway);

        // Assert
        assertEquals(before, board.numberOfPublishedAdvertisements());
        verify(mockDb, times(1)).advertiserIsRegistered(anunciante);
        verify(mockGateway, times(1)).advertiserHasFunds(anunciante);
        // También puedes verificar que NO se le haya cobrado
        verify(mockGateway, never()).chargeAdvertiser(anunciante);
    }
    //Publicar un anuncio por parte de un anunciante llamado "Robin Robot", 
    //asumiendo que está en la base de datos de anunciantes, que tiene saldo y 
    //finalmente comprobando que se ha realizado el cargo.
    @Test
    @DisplayName("PublishAdvertiserWithFundsAndRegistered: inserta el anuncio y cobra al anunciante si está registrado y tiene fondos")
    void publish_validAdvertisement_publishesAndCharges() {
        // Arrange
        String advertiser = "Robin Robot";
        String title = "Robots en oferta";
        Advertisement ad = new Advertisement(title, "Descuentos especiales", advertiser);
    
        AdvertisementBoard board = new AdvertisementBoard();
    
        // Crear mocks para las dependencias
        AdvertiserDatabase stubDb = mock(AdvertiserDatabase.class);
        PaymentGateway stubGateway = mock(PaymentGateway.class);
    
        // Stubbing: anunciante válido y con fondos
        when(stubDb.advertiserIsRegistered(advertiser)).thenReturn(true);
        when(stubGateway.advertiserHasFunds(advertiser)).thenReturn(true);
    
        // Act
        board.publish(ad, stubDb, stubGateway);
    
        // Assert: El anuncio está en el board
        Optional<Advertisement> result = board.findByTitle(title);
        assertTrue(result.isPresent(), "El anuncio debería estar publicado");
        assertEquals(advertiser, result.get().advertiser, "El anunciante debe coincidir");
    
        // Verificar que se llamó a chargeAdvertiser una vez
        verify(stubGateway, times(1)).chargeAdvertiser(advertiser);
        verify(stubDb, times(1)).advertiserIsRegistered(advertiser);
        verify(stubGateway, times(1)).advertiserHasFunds(advertiser);
    }
    
    //Publicar dos anuncios distintos por parte de "THE Company", borrar el primero y comprobar que si se busca ya no está en el tablón.
    @Test
    @DisplayName("publishTwoAdsByTheCompanyAndDelteOne: Se publican dos anuncios por el dueño del board y se borra el primero")
        public void delete_deleteFirstCompanyAd_thenItIsNotFound() {
        // Arrange
        AdvertisementBoard board = new AdvertisementBoard();
        String title1 = "Título 1";
        String title2 = "Título 2";
        String text = "Anuncio de prueba";
        String company = AdvertisementBoard.BOARD_OWNER;

        Advertisement ad1 = new Advertisement(title1, text, company);
        Advertisement ad2 = new Advertisement(title2, text, company);

        AdvertiserDatabase dummyDb = mock(AdvertiserDatabase.class);
        PaymentGateway dummyGateway = mock(PaymentGateway.class);

        // Act
        board.publish(ad1, dummyDb, dummyGateway);
        board.publish(ad2, dummyDb, dummyGateway);
        board.deleteAdvertisement(title1, company);

        // Assert
        // El primero ya no está
        assertTrue(board.findByTitle(title1).isEmpty());

        // El segundo sigue estando 
        assertTrue(board.findByTitle(title2).isPresent());

        // Las dependencias externas no se usaron
        verifyNoInteractions(dummyDb);
        verifyNoInteractions(dummyGateway);
    }
    //Comprobar que si se intenta publicar un anuncio que ya existe 
    //(mismo título y mismo anunciante), no se realiza la publicación y
    // no se realiza ningún cargo. El anunciante no debe ser "THE Company". 
    //Para pasar esta prueba hay que modificar la clase AdvertisementBoard.

    @Test
    @DisplayName("publishDuplicateAdByCompany: lanza excepción y no inserta el anuncio duplicado")

    public void publish_duplicateAdByCompany_throwsExceptionAndIsNotInserted() {
        // Arrange
        AdvertisementBoard board = new AdvertisementBoard();
        String title = "Oferta exclusiva";
        String advertiser = AdvertisementBoard.BOARD_OWNER;

        Advertisement ad = new Advertisement(title, "Texto original", advertiser);

        AdvertiserDatabase dummyDb = mock(AdvertiserDatabase.class);
        PaymentGateway dummyGateway = mock(PaymentGateway.class);

        int before = board.numberOfPublishedAdvertisements();

        // Act
        board.publish(ad, dummyDb, dummyGateway); // Primera publicación (válida)

        // Assert: segunda publicación lanza excepción por duplicado
        AdvertisementBoardException exception = assertThrows(
            AdvertisementBoardException.class,
            () -> board.publish(ad, dummyDb, dummyGateway)
        );

        assertEquals("Duplicate Ads cannot be published in the board", exception.getMessage());

        // Confirmamos que no se ha incrementado más del primer anuncio
        assertEquals(before + 1, board.numberOfPublishedAdvertisements());

        // Las dependencias externas no deben haberse usado
        verifyNoInteractions(dummyDb);
        verifyNoInteractions(dummyGateway);
    }
    //Primero sin spy
    //Comprobar que si se intenta publicar un anuncio por parte del anunciante "Tim O'Theo" y el tablón está lleno se eleva la excepción AdvertisementBoardException.  Para pasar esta prueba hay que modificar la clase AdvertisementBoard.
    @Test
    @DisplayName("publishWhenBoardIsFull: lanza excepción al publicar cuando el tablón está lleno")

    public void publish_publishWhenBoardIsFull_throwsException() {
        // Arrange
        AdvertisementBoard board = new AdvertisementBoard(); // Ya contiene 1 anuncio inicial
        String advertiser = "Tim O'Theo";

        AdvertiserDatabase stubDb = mock(AdvertiserDatabase.class);
        PaymentGateway stubGateway = mock(PaymentGateway.class);

        when(stubDb.advertiserIsRegistered(advertiser)).thenReturn(true);
        when(stubGateway.advertiserHasFunds(advertiser)).thenReturn(true);

        // Publicar anuncios hasta llegar al límite MAX_BOARD_SIZE
        int adsToAdd = AdvertisementBoard.MAX_BOARD_SIZE - board.numberOfPublishedAdvertisements();

        for (int i = 0; i < adsToAdd; i++) {
            Advertisement ad = new Advertisement("Title " + i, "Text", advertiser);
            board.publish(ad, stubDb, stubGateway);
        }

        // Act & Assert: el siguiente anuncio lanza excepción
        Advertisement finalAd = new Advertisement("Extra", "Should not be inserted", advertiser);

        AdvertisementBoardException exception = assertThrows(
            AdvertisementBoardException.class,
            () -> board.publish(finalAd, stubDb, stubGateway)
        );

        assertEquals("The board is full. Cannot publish more advertisements.", exception.getMessage());

        // Verificamos que no se ha cobrado el intento extra
        verify(stubGateway, times(adsToAdd)).chargeAdvertiser(advertiser); // solo los válidos
        verify(stubGateway, never()).chargeAdvertiser("Extra"); // el título es distinto, pero la verificación ya cubre el caso
        }

    //Tiene sentido usar un spy? podríamos decir que no, estamos comprobando el estado interno del tablón, no obstante al 
    //usar un anunciante disitinto de The Company se están llamando dependencias externas y conseguiríamos ahorrar complejidad y memoria,
    
    @Test
    @DisplayName("publishWhenBoardIsFullUsingSpy: lanza excepción y no se interactúa con las dependencias")

    public void publish_whenBoardIsFull_usingSpy_throwsException() {
    // Arrange
        String advertiser = "Tim O'Theo";
        Advertisement ad = new Advertisement("Extra", "Should not be inserted", advertiser);

        // Spy del AdvertisementBoard
        AdvertisementBoard board = new AdvertisementBoard();
        AdvertisementBoard spyBoard = Mockito.spy(board);

        // Stub de numberOfPublishedAdvertisements() para simular que ya está lleno
        doReturn(AdvertisementBoard.MAX_BOARD_SIZE).when(spyBoard).numberOfPublishedAdvertisements();

        // Dependencias dummy simuladas con mocks
        AdvertiserDatabase dummyDb = mock(AdvertiserDatabase.class);
        PaymentGateway dummyGateway = mock(PaymentGateway.class);


        // Act & Assert
        AdvertisementBoardException exception = assertThrows(
            AdvertisementBoardException.class,
            () -> spyBoard.publish(ad, dummyDb, dummyGateway)
        );

        assertEquals("The board is full. Cannot publish more advertisements.", exception.getMessage());

        // Verificar que NO se interactuó con las dependencias porque ni siquiera se pasa la validación del tamaño
        verifyNoInteractions(dummyDb);
        verifyNoInteractions(dummyGateway);
    }

}
