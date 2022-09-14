package io.totominc.extraskills.leveler;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.totominc.extraskills.ExtraSkills;
import io.totominc.extraskills.skills.Skill;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;

public class LevelerTest {
  private static ServerMock server;
  private static ExtraSkills instance;

  @InjectMocks
  BukkitAudiences adventure;

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
  @DisplayName("It should add experience to a player skill.")
  public void testAddSkillExperience() {
    this.adventure = BukkitAudiences.create(instance);

    PlayerMock player = server.addPlayer();
    Skill skill = Skill.MINING;

    instance.getLeveler().addExperience(player, skill, 5);

    Assertions.assertEquals(
      instance.getPlayerDataManager().getPlayerData(player.getUniqueId()).getSkillExperience(skill),
      5
    );
  }
}
