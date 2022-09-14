package io.totominc.extraskills.listeners;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.totominc.extraskills.ExtraSkills;
import io.totominc.extraskills.utils.BlockUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.junit.jupiter.api.*;

public class BlockListenersTest {
  private static ServerMock server;
  private static ExtraSkills instance;

  @BeforeAll
  public static void load() {
    server = MockBukkit.mock();
    instance = MockBukkit.load(ExtraSkills.class);
  }

  @AfterAll
  public static void unload() {
    MockBukkit.unmock();
  }

  @Test
  @DisplayName("Player place a block and it should be registered inside the chunk persistent data container.")
  void testPlayerPlaceBlock() {
    PlayerMock player = server.addPlayer();
    Location location = new Location(player.getWorld(), 10, 60, 1000);

    player.simulateBlockPlace(Material.COBBLESTONE, location);

    Block block = player.getWorld().getBlockAt(location);

    Assertions.assertTrue(BlockUtils.isPlayerPlaced(block));
  }
}
