package org.mps.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    //Partimos de la base de que no hace falta hacer mock para todos los test, solo para aquellos en los que se comprueben dependencias externas.

    @Test
    public void AdvertisementBoard_init_hasOneAdvertisement() {
        //Arrange, Act
        AdvertisementBoard board = new AdvertisementBoard();

  

        //Assert
        assertEquals(1, board.numberOfPublishedAdvertisements());
    }

    //Crear un anuncio por parte de "THE Company", insertarlo y comprobar que se ha incrementado en uno el número de anuncios del tablón.
    //En este caso la clase AdvertisementBoard tiene como dependencias externas la base de datos y la pasarela de pago,
    //que se pasa a traves de los argumentos de la función publish, en este caso como estamos testeando con el BOARD_OWNER como advertiser no se están usando las dependencias,
    //por eso tenemos que usar 
    @Test
    public void publish_AdBoardOwner_incrementsAdsByOne() {
        //Arrange
        AdvertisementBoard board = new AdvertisementBoard();
        int numberOfAds = board.numberOfPublishedAdvertisements();
        Advertisement advertisement = new Advertisement("Title", "Text", AdvertisementBoard.BOARD_OWNER);
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
    public void publish_AdInDBAndNotenoughMoney_notIncremented() {
        // Arrange
        String anunciante = "Pepe Gotera y Otilio";
        Advertisement advertisement = new Advertisement("Title", "Text", anunciante);
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

    //Publicar dos anuncios distintos por parte de "THE Company", borrar el primero y comprobar que si se busca ya no está en el tablón.
    @Test
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

        // El segundo sigue estando (opcional pero útil)
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
    //Comprobar que si se intenta publicar un anuncio por parte del anunciante "Tim O'Theo" y el tablón está lleno se eleva la excepción AdvertisementBoardException.  Para pasar esta prueba hay que modificar la clase AdvertisementBoard.
    @Test
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

    //Tiene sentido usar un spy? considero que no, estamos comprobando el estado interno del tablón,
    //por lo que siempre es mejor testear la implementación real, no obstante es posible hacerlo y conseguiríamos ahorrar complejidad y memoria,
    
    @Test
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
        AdvertiserDatabase stubDb = mock(AdvertiserDatabase.class);
        PaymentGateway stubGateway = mock(PaymentGateway.class);

        when(stubDb.advertiserIsRegistered(advertiser)).thenReturn(true);
        when(stubGateway.advertiserHasFunds(advertiser)).thenReturn(true);

        // Act & Assert
        AdvertisementBoardException exception = assertThrows(
            AdvertisementBoardException.class,
            () -> spyBoard.publish(ad, stubDb, stubGateway)
        );

        assertEquals("The board is full. Cannot publish more advertisements.", exception.getMessage());

        // Verificar que NO se interactuó con las dependencias porque ni siquiera se pasa la validación del tamaño
        verifyNoInteractions(stubDb);
        verifyNoInteractions(stubGateway);
    }

}
