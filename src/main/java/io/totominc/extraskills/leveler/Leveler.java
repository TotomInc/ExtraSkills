package io.totominc.extraskills.leveler;

import com.udojava.evalex.Expression;
import io.totominc.extraskills.ExtraSkills;
import io.totominc.extraskills.configuration.Option;
import io.totominc.extraskills.data.PlayerData;
import io.totominc.extraskills.skills.Skill;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public final class Leveler {
  private final ExtraSkills instance;

  public Leveler(ExtraSkills instance) {
    this.instance = instance;
  }

  /**
   * EvalEx expression representation from a string. This will return the experience expression
   * for this specific skill.
   *
   * @return Expression.
   * @see <a href="https://github.com/uklimaschewski/EvalEx">EvalEx</a>
   */
  public Expression getExperienceExpression(Skill skill) {
    return new Expression(this.instance.getSkillManager().getSkillOption(skill).experienceExpression());
  }

  /**
   * EvalEx expression representation from a string. This will return the global experience expression.
   *
   * @return Expression.
   * @see <a href="https://github.com/uklimaschewski/EvalEx">EvalEx</a>
   */
  public Expression getExperienceExpression() {
    return new Expression(this.instance.getOptionManager().getString(Option.GLOBAL_EXPERIENCE_EXPRESSION));
  }

  /**
   * EvalEx expression representation from a string. This will return the money expression used to gain
   * money on skill level-up.
   *
   * @return Expression.
   * @see <a href="https://github.com/uklimaschewski/EvalEx">EvalEx</a>
   */
  public Expression getMoneyExpression() {
    return new Expression(this.instance.getOptionManager().getString(Option.MONEY_SKILL_LEVELUP_EXPRESSION));
  }

  // TODO: Implement experience multiplier per skill with permissions.
  public double getMultiplier() {
    return 1d;
  }

  /**
   * Add experience to a specific skill, try to level-up the skill and send
   * action-bar to player with skill progression.
   */
  public void addExperience(Player player, Skill skill, double amount) {
    PlayerData playerData = this.instance.getPlayerDataManager().getPlayerData(player.getUniqueId());

    // Handle possible case if PlayerData is not set for current player.
    if (playerData == null) {
      return;
    }

    // Handle possible case if experience amount is set to 0. We want to allow possible
    // negative values to subtract experience from a player skill.
    if (amount == 0) {
      return;
    }

    boolean isMaxLevel = playerData.getSkillLevel(skill) >= this.instance.getSkillManager().getSkillOption(skill).maxLevel();
    double experience = amount * getMultiplier();

    // Add skill experience to the player's specified skill.
    playerData.addSkillExperience(skill, experience);

    // Keep in memory the current level of the player before doing level-up.
    double levelBeforeLevelup = playerData.getSkillLevel(skill);
    // Try to level-up and return the amount of levels gained during the process.
    double levelsGained = playerData.trySkillLevelup(skill);

    // If economy and money on skill level-up is enabled, use Vault to send money.
    if (
      levelsGained > 0 &&
      ExtraSkills.getIsVaultEnabled() &&
      ExtraSkills.getVaultEconomy() != null &&
      this.instance.getOptionManager().getBoolean(Option.ENABLE_GAIN_MONEY_SKILL_LEVELUP)
    ) {
      double i = levelsGained;

      while (i > 0) {
        double level = levelBeforeLevelup + levelsGained - (i - 1);
        Expression moneyExpression = this.getMoneyExpression();

        moneyExpression.setVariable("level", BigDecimal.valueOf(level));

        ExtraSkills.getVaultEconomy().depositPlayer(player, moneyExpression.eval().intValue());

        i -= 1;
      }
    }

    // If sounds are enabled, send sound on level-up.
    if (
      levelsGained > 0 &&
      this.instance.getOptionManager().getBoolean(Option.SOUND_ENABLED) &&
      this.instance.getOptionManager().getBoolean(Option.SOUND_ENABLE_SKILL_LEVELUP)
    ) {
      this.sendSound(player);
    }

    // If titles are enabled, send title on level-up.
    if (
      levelsGained > 0 &&
      this.instance.getOptionManager().getBoolean(Option.TITLE_ENABLED) &&
      this.instance.getOptionManager().getBoolean(Option.TITLE_ENABLE_SKILL_LEVELUP)
    ) {
      this.sendTitles(skill, player, playerData);
    }

    // If action-bars are enabled, send action-bar on experience gained.
    if (
      this.instance.getOptionManager().getBoolean(Option.ACTION_BAR_ENABLED) &&
      this.instance.getOptionManager().getBoolean(Option.ACTION_BAR_ENABLE_SKILL_EXPERIENCE)
    ) {
      this.sendActionBar(skill, player, playerData, experience, isMaxLevel);
    }

    // If boss-bars are enabled, send boss-bar on experience gained.
    if (
      this.instance.getOptionManager().getBoolean(Option.BOSS_BAR_ENABLED) &&
      this.instance.getOptionManager().getBoolean(Option.BOSS_BAR_ENABLE_SKILL_EXPERIENCE)
    ) {
      this.sendBossBar(skill, player, playerData, experience, isMaxLevel);
    }
  }

  private void sendSound(Player player) {
    @SuppressWarnings("PatternValidation") Sound sound = Sound.sound(
      Key.key(this.instance.getOptionManager().getString(Option.SOUND_SKILL_LEVELUP_SOUND)),
      Sound.Source.valueOf(this.instance.getOptionManager().getString(Option.SOUND_SKILL_LEVELUP_SOURCE)),
      (float) this.instance.getOptionManager().getDouble(Option.SOUND_SKILL_LEVELUP_VOLUME),
      (float) this.instance.getOptionManager().getDouble(Option.SOUND_SKILL_LEVELUP_PITCH)
    );

    ExtraSkills.getAdventure().player(player).playSound(sound);
  }

  private void sendTitles(Skill skill, Player player, PlayerData playerData) {
    ExtraSkills.getAdventure().player(player).sendTitlePart(
      TitlePart.TITLE,
      playerData.getTitleSkillLevelUpMessage(skill, Option.TITLE_SKILL_LEVELUP_TITLE)
    );

    ExtraSkills.getAdventure().player(player).sendTitlePart(
      TitlePart.SUBTITLE,
      playerData.getTitleSkillLevelUpMessage(skill, Option.TITLE_SKILL_LEVELUP_DESCRIPTION)
    );
  }

  private void sendActionBar(Skill skill, Player player, PlayerData playerData, double experience, boolean isMaxLevel) {
    if (!isMaxLevel) {
      ExtraSkills.getAdventure().player(player).sendActionBar(
        playerData.getActionBarSkillExperienceMessage(skill, experience)
      );
    }

    if (isMaxLevel && !this.instance.getOptionManager().getBoolean(Option.ACTION_BAR_DISABLE_MAX_LEVEL)) {
      ExtraSkills.getAdventure().player(player).sendActionBar(
        playerData.getActionBarMaxSkillLevelMessage(skill, experience)
      );
    }
  }

  private void sendBossBar(Skill skill, Player player, PlayerData playerData, double experience, boolean isMaxLevel) {
    BossBar.Color bossBarColor = BossBar.Color.valueOf(this.instance.getOptionManager().getString(Option.BOSS_BAR_COLOR));
    BossBar.Overlay bossBarOverlay = BossBar.Overlay.valueOf(this.instance.getOptionManager().getString(Option.BOSS_BAR_TYPE));

    if (!isMaxLevel) {
      float progression = (float) (playerData.getSkillExperience(skill) / playerData.getSkillExperienceRequired(skill));
      BossBar bossBar = BossBar.bossBar(
        playerData.getBossBarSkillExperienceMessage(skill, experience),
        progression,
        bossBarColor,
        bossBarOverlay
      );

      this.instance.getBossBarManager().sendBossBar(player, bossBar);
    }

    if (isMaxLevel && !this.instance.getOptionManager().getBoolean(Option.BOSS_BAR_DISABLE_MAX_LEVEL)) {
      BossBar bossBar = BossBar.bossBar(
        playerData.getBossBarMaxSkillLevelMessage(skill, experience),
        1,
        bossBarColor,
        bossBarOverlay
      );

      this.instance.getBossBarManager().sendBossBar(player, bossBar);
    }
  }
}
