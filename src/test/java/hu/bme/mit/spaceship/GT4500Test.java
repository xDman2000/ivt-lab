package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;

  private TorpedoStore primary;
  private TorpedoStore secondary;

  @BeforeEach
  public void init(){
    primary = mock(TorpedoStore.class);
    secondary = mock(TorpedoStore.class);

    this.ship = new GT4500(primary, secondary);
  }

  @Test
  public void fireTorpedo_Single_Success() {
    // Arrange
    when(primary.fire(1)).thenReturn(true);
    when(secondary.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primary, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_Success() {
    // Arrange
    when(primary.fire(1)).thenReturn(true);
    when(secondary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(primary, times(1)).fire(1);
    verify(secondary, times(1)).fire(1);
    assertEquals(true, result);
  }

  // Négy SINGLE esetén a váltakozást teszteljük 2 primary és 2 secondary.
  @Test
  public void fireFourSingle(){
    // Arrange
    when(primary.fire(1)).thenReturn(true);
    when(secondary.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primary, times(1)).fire(1);
    verify(secondary, times(0)).fire(1);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primary, times(1)).fire(1);
    verify(secondary, times(1)).fire(1);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primary, times(2)).fire(1);
    verify(secondary, times(1)).fire(1);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(secondary, times(2)).fire(1);
    verify(primary, times(2)).fire(1);
  }

  // Kettő egymás utáni ALL és egy SINGLE lövésnél megfelelően sülnek-e el a lövések a két Store-ból.
  @Test
  public void fireTwoAllOneSingle(){
    // Arrange
    when(primary.fire(1)).thenReturn(true);
    when(secondary.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(primary, times(1)).fire(1);
    verify(secondary, times(1)).fire(1);

    // Act
    ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(primary, times(2)).fire(1);
    verify(secondary, times(2)).fire(1);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primary, times(3)).fire(1);
    verify(secondary, times(2)).fire(1);
  }

  // Ha a secondary Store üres, akkor kétszer sül-e el a primary 2 SINGLE esetén.
  @Test
  public void fireTwoSingleWithSecondaryEmpty(){
    // Arrange
    when(primary.fire(1)).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primary, times(2)).fire(1);
    verify(secondary, times(0)).fire(1);
  }

  // Ha üres a primary akkor SINGLE esetén automatikusan a secondary-ből lő e.
  @Test
  public void fireSingleWithPrimaryEmpty(){
    // Arrange
    when(primary.isEmpty()).thenReturn(true);
    when(secondary.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primary, times(0)).fire(1);
    verify(secondary, times(1)).fire(1);
  }

  // ALL esetén ha csak a primaryben van töltény akkor csak a primary sül-e el.
  @Test
  public void fireAllWithOnlyPrimaryLoaded(){
    // Arrange
    when(secondary.isEmpty()).thenReturn(true);
    when(primary.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(primary, times(1)).fire(1);
    verify(secondary, times(0)).fire(1);
  }

  // ALL esetén ha csak a primaryben van töltény akkor csak az süljön el és utána ha kerül tölény a secondary-be akkor egy SINGLE esetén az süljön el
  @Test
  public void fireAllWithOnlyPrimaryLoadedAndSingleAfter(){
    // Arrange
    when(secondary.isEmpty()).thenReturn(true);
    when(primary.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(primary, times(1)).fire(1);
    verify(secondary, times(0)).fire(1);

    // Arrange
    when(secondary.isEmpty()).thenReturn(false);
    when(secondary.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primary, times(1)).fire(1);
    verify(secondary, times(1)).fire(1);
  }
}
