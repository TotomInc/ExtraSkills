package io.totominc.ExtraSkills.player;

public class PlayerSkill {
  public String id;
  public int level = 1;
  public double experience = 0;

  public PlayerSkill(String id) {
    this.id = id;
  }

  /**
   * Add experience and call levelup method.
   *
   * @param amount Amount of experience to gain.
   */
  public void gainExperience(Double amount) {
    this.experience += amount;
    this.levelup();
  }

  /**
   * Try to levelup as much as possible.
   */
  public void levelup() {
    while (this.experience >= this.getExperienceRequired()) {
      this.experience -= this.getExperienceRequired();
      this.level += 1;
    }
  }

  /**
   * Return the amount of experience required for the current level.
   *
   * @return Amount of experience required to levelup.
   */
  public double getExperienceRequired() {
    return getExperienceRequired(this.level);
  }

  /**
   * Return the amount of experience required for a specific level.
   *
   * @param level Level to check experience required for.
   * @return Amount of experience required for the specific level.
   */
  public double getExperienceRequired(int level) {
    return 100 * Math.pow(1.05, level - 1);
  }

  /**
   * Set the current level.
   *
   * @param level Level to set.
   */
  public void setLevel(int level) {
    this.level = level;
  }

  /**
   * Set the current amount of experience.
   *
   * @param experience Experience amount.
   */
  public void setExperience(double experience) {
    this.experience = experience;
  }
}
