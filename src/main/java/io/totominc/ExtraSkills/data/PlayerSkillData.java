package io.totominc.ExtraSkills.data;

public final class PlayerSkillData {
  private double experienceRequired;
  private double experience = 0;
  private double level = 1;

  public PlayerSkillData(double initialExperienceRequired) {
    this.experienceRequired = initialExperienceRequired;
  }

  public void setExperienceRequired(double amount) {
    this.experienceRequired = amount;
  }

  public double getExperienceRequired() {
    return this.experienceRequired;
  }

  public void addExperience(double amount) {
    this.experience += amount;
  }

  public void removeExperience(double amount) {
    this.experience -= amount;
  }

  public double getExperience() {
    return this.experience;
  }

  public void addLevels(double level) {
    this.level += level;
  }

  public double getLevel() {
    return this.level;
  }
}
